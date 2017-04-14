package com.tech.heathcilff.androidlib.utils;

import java.io.File;

/**
 * Created by zhangliang on 07/03/2017.
 */

public class FileUtils {

	public static void clearFiles(File rootFile) {
		File[] files = rootFile.listFiles();
		if (files == null || files.length == 0) {
			return;
		}

		for (File file : files) {
			if (file != null) {
				file.delete();
			}
		}
	}
}
