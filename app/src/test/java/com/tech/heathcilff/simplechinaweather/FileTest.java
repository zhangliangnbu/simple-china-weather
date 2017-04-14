package com.tech.heathcilff.simplechinaweather;

import org.junit.Test;

import java.io.File;

/**
 * Created by zhangliang on 07/03/2017.
 */

public class FileTest {

	@Test
	public void testSeparator() {
		System.out.println(File.pathSeparator);// :
		System.out.println(File.separator);// /
	}
}
