package com.minggo.pluto.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件操作类
 * @author minggo
 * @time 2014-12-2下午2:16:19
 */
public class FileUtils {

	/**
	 * 写文件
	 * @param filePath
	 * @param fileName
	 * @param content
	 * @param append 是否添加在原内容的后边
	 * @return
	 */
	public static boolean WriterTxtFile(String filePath, String fileName,
			String content,boolean append) {
		String strFile = filePath + "/" + fileName;
		File file = new File(strFile);

		// 判断目录是否存在。如不存在则创建一个目录
		file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(strFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(strFile, append);
			out.write(content.getBytes("UTF-8"));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}// true表示在文件末尾添加
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}
	
	/**
	 * 写文件
	 * 若文件存在则返回
	 * @param filePath
	 * @param fileName
	 * @param content
	 * @param append 是否添加在原内容的后边
	 * @return
	 */
	public static boolean WriterTxtFileReturn(String filePath, String fileName,
			String content,boolean append) {
		String strFile = filePath + "/" + fileName;
		File file = new File(strFile);

		// 判断目录是否存在。如不存在则创建一个目录
		file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(strFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return true;
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(strFile, append);
			out.write(content.getBytes("UTF-8"));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}// true表示在文件末尾添加
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	/**
	 * 读取文本文件中的内容
	 * @param strFilePath 文件详细路径
	 * @return
	 */
	public static String ReadTxtFile(String strFilePath) {
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(strFilePath);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (!file.isDirectory()&&file.exists()) {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						if(strFilePath.contains("ggid")){
							content += line;
						}else{
							content += line + "\n";
						}
					}
					instream.close();
				}
			} catch (FileNotFoundException e) {
				// Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				// Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}

	/**
	 * 读取文本文件中的内容
	 * @param strFilePath 文件详细路径
	 * @return
	 */
	public static String RemoveTxtFile(String strFilePath) {
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(strFilePath);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (file.exists()){
			file.delete();
		}
		return content;
	}

	/**
	 * 读取文本文件中的内容
	 * @param strFilePath 文件详细路径
	 * @return
	 */
	public static String ReadTxtFileForBookids(String strFilePath) {
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(strFilePath);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (!file.isDirectory()) {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						content += line;
					}
					instream.close();
				}
			} catch (FileNotFoundException e) {
				// Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				// Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}

	/**
	 * 读取文件
	 * @param inputStream
	 * @return
	 */
	public static String ReadTxtFile(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
            e.printStackTrace();
		}
		return outputStream.toString();
	}
	
	
	/**
	 * 根据文件绝对路径获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName( String filePath )
	{
		if( StringUtils.isEmpty(filePath) )	return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat( String filePath){
		if(StringUtils.isEmpty(filePath)){
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator)+1,point);
	}
	
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat( String fileName )
	{
		if( StringUtils.isEmpty(fileName) )	return "";
		
		int point = fileName.lastIndexOf( '.' );
		return fileName.substring( point+1 );
	}
	
	/**
	 * 获取文件大小
	 * @param filePath
	 * @return
	 */
	public static long getFileSize( String filePath )
	{
		long size = 0;
		
		File file = new File( filePath );
		if(file!=null && file.exists())
		{
			size = file.length();
		} 
		return size;
	}
	
	/**
	 * 获取文件大小
	 * @param size 字节
	 * @return
	 */
	public static String getFileSize(long size) 
	{
		if (size <= 0)	return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float)size / 1024;
		if (temp >= 1024) 
		{
			return df.format(temp / 1024) + "M";
		}
		else 
		{
			return df.format(temp) + "K";
		}
	}

	/**
	 * 转换文件大小
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

	/**
	 * 获取目录文件大小
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
	    if (!dir.isDirectory()) {
	    	return 0;
	    }
	    long dirSize = 0;
	    File[] files = dir.listFiles();
	    for (File file : files) {
	    	if (file.isFile()) {
	    		dirSize += file.length();
	    	} else if (file.isDirectory()) {
	    		dirSize += file.length();
	    		dirSize += getDirSize(file); //递归调用继续统计
	    	}
	    }
	    return dirSize;
	}
	
	/**
	 * 获取目录文件个数
	 * @param dir
	 * @return
	 */
	public long getFileList(File dir){
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
            	count = count + getFileList(file);//递归
            	count--;
            }
        }
        return count;  
    }
	
	public static byte[] toBytes(InputStream in) throws IOException 
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    int ch;
	    while ((ch = in.read()) != -1)
	    {
	    	out.write(ch);
	    }
	    byte buffer[]=out.toByteArray();
	    out.close();
	    return buffer;
	}
	
	/**
	 * 检查文件是否存在
	 * @param filepath
	 * @return
	 */
	public static boolean checkFileExists(String filepath) {
		boolean status;
		if (!filepath.equals("")) {			
			File newPath = new File(filepath);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;

	}
	
	
	/**
	 * 计算SD卡的剩余空间
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 新建目录
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装SD卡
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
                    LogUtils.info("DirectoryManager deleteDirectory", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除文件
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}
	
	/**
	 * 删除SD卡中的文件
	 * @param strFilePath
	 * @return
	 */
	public static boolean deleteSDFile(String strFilePath){
		if (strFilePath.equals("")) {
			return false;
		}
		File file = new File(strFilePath);
			return file.exists()&&file.delete();
	}

	/**
	 * 获取文件修改时间，失败为0
	 */
	public static long getLastModified(String filePath) {
		File file = new File(filePath);
		return file.lastModified();
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		} finally {
			if (inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


	/**
	 * 获取path目录下所有文件名
	 * @param path 文件夹目录
	 * @return 文件名列表
	 */
	public static List<String> getDirsFileNames(String path){
		List<String> fileNames = new ArrayList<String>();
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}

	/**
	 * 外部存储files子目录
	 * <br>/storage/emulated/0/Android/data/com.mengmengda.reader/files/dirType</br>
	 *
	 * @param dirType files子目录，null则为files目录
	 */
	public static File getExternalFilesDir(Context context, String dirType) {
		return context.getExternalFilesDir(dirType);
	}

	/**
	 * 外部存储cache目录
	 * <br>/storage/emulated/0/Android/data/com.mengmengda.reader/cache</br>
	 */
	public static File getExternalCacheDir(Context context) {
		return context.getExternalCacheDir();
	}
}
