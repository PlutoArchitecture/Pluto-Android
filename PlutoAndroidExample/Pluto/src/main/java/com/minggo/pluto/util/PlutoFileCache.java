package com.minggo.pluto.util;

import com.minggo.pluto.Pluto;
import com.minggo.pluto.db.manager.DataManager;
import com.minggo.pluto.db.manager.DataManagerStub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存处理工具类
 * 
 * @author minggo
 * @time 2014-12-2下午1:58:22
 */
public class PlutoFileCache extends DataManagerStub{
	static PlutoFileCache cacheUtils;

	private PlutoFileCache() {

	}

	/**
	 * 初始化或者获取本地缓存
	 * 
	 * @return
	 */
	public static PlutoFileCache getInstance() {
		if (null == cacheUtils) {
			cacheUtils = new PlutoFileCache();
		}
		return cacheUtils;
	}

	/**
	 * 判断缓存是否失效
	 * 
	 * @param key
	 *            主键
	 * @param cache_time_min
	 *            分钟
	 * @return
	 */
	public boolean isCacheDataFailure(String key, int cache_time_min) {
		cache_time_min = cache_time_min * 60000; // 把分钟转换为毫秒
		boolean failure = false;
		File data = new File(Pluto.SDPATH + "cache/" + "cache_" + key
				+ ".data");

		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > cache_time_min) {
			failure = true;
		} else if (!data.exists()) {
			failure = true;
		}
        LogUtils.info("reader", data.getPath() + "文件是否失效====>" + failure);
		return failure;
	}

	/**
	 * 判断缓存是否失效
	 * 
	 * @param path
	 *            主键
	 * @param cache_time_min
	 *            分钟
	 * @return
	 */
	public boolean isCacheDataFailurePath(String path, int cache_time_min) {
		cache_time_min = cache_time_min * 60000; // 把分钟转换为毫秒
		boolean failure = false;
		File data = new File(path);

		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > cache_time_min) {
			failure = true;
		} else if (!data.exists()) {
			failure = true;
		}
        LogUtils.info("reader", path + "文件是否失效====>" + failure);
		return failure;
	}
	
	/**
	 * 保存磁盘缓存
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileUtils.WriterTxtFile(Pluto.SDPATH + "cache/", "cache_" + key
				+ ".data", value, false);
	}

	/**
	 * 获取磁盘缓存数据
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getDiskCache(String key) {
		String content = null;
		try {
			content = FileUtils.ReadTxtFile(Pluto.SDPATH + "cache/"
					+ "cache_" + key + ".data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 删除磁盘缓存数据
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String removeDiskCache(String key) {
		String content = null;
		try {
			content = FileUtils.RemoveTxtFile(Pluto.SDPATH + "cache/"
					+ "cache_" + key + ".data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 获取所有缓存文件，判断依据：文件名开头cache_并且文件名结尾.data
	 *
	 * @return 返回列表，长度0则为空
	 */
	public List<File> getAllDiskCacheFile() {
		List<File> allFiles = new ArrayList<>();
		File cacheDir = new File(Pluto.SDPATH + "cache/");

		if (cacheDir.exists()) {
			File[] files = cacheDir.listFiles();
			for (File file : files) {
				String fileName = file.getName();
				if (fileName.startsWith("cache_") && fileName.endsWith(".data")) {
					allFiles.add(file);
				}
			}
		}

		return allFiles;
	}

	public File getDiskCacheFile(String key) {
		return new File(Pluto.SDPATH + "cache/" + "cache_" + key + ".data");
	}

	@Override
	public void saveData(Object key, Object object) {
		super.saveData(key, object);
		try {
			setDiskCache(key.toString(),object.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> T queryData(Object key, Class<T> clazz) {
		 return (T)getDiskCache(key.toString());
	}

	@Override
	public <T> void deleteData(Object key, Class<T> clazz) {
		super.deleteData(key, clazz);
		removeDiskCache(key.toString());
	}

	@Override
	public void updateData(Object key, Object object) {
		super.updateData(key, object);
		saveData(key,object);
	}
}