package com.minggo.pluto.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Display;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具
 * @author minggo
 * @time 2014-12-2下午2:18:56
 */
@SuppressLint("NewApi")
public class PhotoUtil {
	public static final int IMAGE_MAX_WIDTH = 300;
	public static final int IMAGE_MAX_HEIGHT = 300;
	
	/**
	 * sdcard 是否存在
	 * 
	 * @return
	 */
	public static boolean isSDCardReady() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String getFilePath(Activity activity, Uri uri) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		// 将光标移至开头 ，这个很重要，不小心很容易引起越界

		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		return path;
	}
	
	public static Bitmap getBitpMap(Context context, Uri uri, Display display) {
		ParcelFileDescriptor pfd;
		try {
			pfd = context.getContentResolver().openFileDescriptor(uri, "r");
		} catch (IOException ex) {
			return null;
		}
		java.io.FileDescriptor fd = pfd.getFileDescriptor();
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 先指定原始大小
		options.inSampleSize = 1;
		// 只进行大小判断
		options.inJustDecodeBounds = true;
		// 调用此方法得到options得到图片的大小
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		// 我们的目标是在800pixel的画面上显示。
		// 所以需要调用computeSampleSize得到图片缩放的比例
		options.inSampleSize = computeSampleSize(options, display.getWidth(),
				display.getHeight());
		// OK,我们得到了缩放的比例，现在开始正式读入BitMap数据
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		// 根据options参数，减少所需要的内存
		Bitmap sourceBitmap = BitmapFactory.decodeFileDescriptor(fd, null,
				options);
		return sourceBitmap;
	}

	// 这个函数会对图片的大小进行判断，并得到合适的缩放比例，比如2即1/2,3即1/3
	static int computeSampleSize(BitmapFactory.Options options,
			int targetWindth, int targetHieght) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidate = 0;

		int candidateW = w / targetWindth;
		int candidateH = h / targetHieght;
		candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > targetWindth) && (w / candidate) < targetWindth)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > targetHieght) && (h / candidate) < targetHieght)
				candidate -= 1;
		}

		return candidate;
	}

	/**
	 * Compress and resize the Image
	 * 
	 * <br />
	 * 因为不论图片大小和尺寸如何, 都会对图片进行一次有损压缩, 所以本地压缩应该 考虑图片将会被二次压缩所造成的图片质量损耗
	 * 
	 * @param quality
	 *            0~100,
	 * @return
	 * @throws IOException
	 */
	public static void compressImage(String filepath, int quality,
			Display display) {
		// 1. Calculate scale
		int scale = 1;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		if (options.outWidth > IMAGE_MAX_WIDTH
				|| options.outHeight > IMAGE_MAX_HEIGHT) {
			scale = calculateInSampleSize(options, display.getWidth(),
					display.getHeight());
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		Matrix matrix = new Matrix();
		matrix.setRotate(readPictureDegree(filepath));
		Bitmap saveBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		writeToFile(filepath, saveBitmap, quality);
	}

	public static void compressImage(String savePath, String filepath,
			int quality, Display display) {
		int scale = 1;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		if (options.outWidth > IMAGE_MAX_WIDTH
				|| options.outHeight > IMAGE_MAX_HEIGHT) {
			scale = calculateInSampleSize(options, display.getWidth(),
					display.getHeight());
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		Matrix matrix = new Matrix();
		matrix.setRotate(readPictureDegree(filepath));
		Bitmap saveBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		writeToFile(savePath, saveBitmap, quality);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqWidth);
			} else {
				inSampleSize = Math.round((float) width / (float) reqHeight);
			}
		}
		return inSampleSize + 1;
	}

	public static void writeToFile(String file, Bitmap bitmap, int quality) {
		if (bitmap == null) {
			return;
		}

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos); // PNG
		} catch (IOException ioe) {
		} finally {

			try {
				if (bos != null) {
					bitmap.recycle();
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {
                e.printStackTrace();
			}
		}
	}

	

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
    /**
     * 转换图片成圆形
     *
     * @param bitmap
     *            传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
    	
    	//setup(bitmap);
    	//return bitmap;
    	
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        return output;
    }
    
    public static String getDataColumn(Context context, Uri uri, String selection,  
            String[] selectionArgs) {  
      
        Cursor cursor = null;  
        final String column = "_data";  
        final String[] projection = {  
                column  
        };  
      
        try {  
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
                    null);  
            if (cursor != null && cursor.moveToFirst()) {  
                final int index = cursor.getColumnIndexOrThrow(column);  
                return cursor.getString(index);  
            }  
        } finally {  
            if (cursor != null)  
                cursor.close();  
        }  
        return null;  
    }  
      
      
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is ExternalStorageProvider. 
     */  
    public static boolean isExternalStorageDocument(Uri uri) {  
        return "com.android.externalstorage.documents".equals(uri.getAuthority());  
    }  
      
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is DownloadsProvider. 
     */  
    public static boolean isDownloadsDocument(Uri uri) {  
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
    }  
      
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is MediaProvider. 
     */  
    public static boolean isMediaDocument(Uri uri) {  
        return "com.android.providers.media.documents".equals(uri.getAuthority());  
    }  
      
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is Google Photos. 
     */  
    public static boolean isGooglePhotosUri(Uri uri) {  
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
    }  
}
