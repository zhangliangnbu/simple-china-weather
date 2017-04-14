package com.tech.heathcilff.simplechinaweather;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * Created by zhangliang on 07/03/2017.
 */

public class SerializableData implements Serializable {
	private static final long serialVersionUID = -8965789465661191484L;
	private int data; // Stores session data
	private transient long activationTime;

	public SerializableData(int data) {
		this.data = data;
		this.activationTime = System.currentTimeMillis();
	}

	private void writeObject(ObjectOutputStream outputStream) throws IOException {
		outputStream.defaultWriteObject();
		outputStream.writeLong(activationTime);
		System.out.println("writeObject");
	}

	private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
		objectInputStream.defaultReadObject();
		activationTime = objectInputStream.readLong();
		System.out.println("readObject");
	}

	private Object writeReplace() {
		System.out.println("writeReplace");
		return this;
	}

	private Object readResolve() {
		System.out.println("readResolve");
		return this;
	}


	@Override
	public String toString() {
		return "SerializableData{" +
				"data=" + data +
				", activationTime=" + activationTime +
				'}';
	}
}
