package com.tech.heathcilff.androidlib.cache;

import java.io.File;

/**
 *
 * Created by zhangliang on 07/03/2017.
 */

public class CacheManager {

	private static CacheManager instance = new CacheManager();
	private File cacheDirectory;
	private CacheManager() {}

	public void initCacheDirectory(String path) {
		cacheDirectory = new File(path);
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
	}

	public File getCacheDirectory(){
		return cacheDirectory;
	}

	public static CacheManager getInstance() {
		return instance;
	}
}
