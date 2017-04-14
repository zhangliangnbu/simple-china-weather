package com.tech.heathcilff.simplechinaweather;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * Created by zhangliang on 07/03/2017.
 */

public class SerializableTest {

	private String path = "SerializableTest.txt";// /SimpleChinaWeather/SerializableTest.txt

	@Test
	public void testSerAndDeser() throws Exception{
		testSerialization();
		testDeserialization();
	}

	@Test
	public void testSerialization() throws Exception{
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		clear(file);

		SerializableData data = new SerializableData(123);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(data);
		objectOutputStream.close();
	}

	@Test
	public void testDeserialization() throws Exception{
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		Object o = objectInputStream.readObject();
		if(o instanceof SerializableData) {
			print(o.toString());
		}
	}

	private void print(String s) {
		System.out.println("test==" + s);
	}

	private void clear (File file) throws Exception {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("");
		fileWriter.close();
	}

}
