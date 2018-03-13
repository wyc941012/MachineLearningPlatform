package com.hhu.machinelearningplatformserver.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteObjectUtil {

	public static Object ByteToObject(byte[] bytes) {
		Object object=null;
		try {
			ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
			ObjectInputStream objectInputStream=new ObjectInputStream(byteArrayInputStream);
			object=objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
	public static byte[] ObjectToByte(Object object) {
		byte[] bytes=null;
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			//objectOutputStream.flush();
			bytes=byteArrayOutputStream.toByteArray();
			objectOutputStream.close();
			byteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
}
