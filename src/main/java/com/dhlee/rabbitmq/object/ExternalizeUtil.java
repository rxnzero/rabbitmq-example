package com.dhlee.rabbitmq.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ExternalizeUtil {
	
	public static byte[] toBytes(Object object) throws IOException {
		if (object instanceof Externalizable) {
			return toExternalizedBytes(object);
    	}
    	else {
    		return toSerialzeBytes(object);
    	}	    		
	}
	
	public static byte[] toExternalizedBytes(Object object) throws IOException {
		ObjectOutputStream boos = null;
		byte[] objectBytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	boos = new ObjectOutputStream(bos);
	    	
	    	((Externalizable) object).writeExternal(boos);
	    	boos.flush();
	    	objectBytes = bos.toByteArray();
		}
		finally {
			if(boos != null)
				try {
					boos.close();
				} catch (IOException e) { }
		}
		return objectBytes;
	}
	
	public static byte[] toSerialzeBytes(Object object) throws IOException {
		ObjectOutputStream boos = null;
		byte[] objectBytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	boos = new ObjectOutputStream(bos);
	    	boos.writeObject(object);
	    	boos.flush();
	    	objectBytes = bos.toByteArray();
		}
		finally {
			if(boos != null)
				try {
					boos.close();
				} catch (IOException e) { }
		}
		return objectBytes;
	}
	
	public static Object fromBytes(Object object, byte[] objectBytes) throws IOException, ClassNotFoundException {
		ObjectInputStream bois = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
			bois = new ObjectInputStream(bis);

			if (object instanceof Externalizable) {
	    		((Externalizable) object).readExternal(bois);
	    	}
	    	else {
	    		object = bois.readObject();
	    	}
		}		
		finally {
			if(bois != null)
				try {
					bois.close();
				} catch (IOException e) { }
		}
		return object;
	}
}
