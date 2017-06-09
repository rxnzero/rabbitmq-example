package com.dhlee.rabbitmq.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User 
implements Serializable
//implements Externalizable  
{
	private long id;
    private String name;
    private int age;
    private List<String> phoneNumbers;
    
    public User() {
    	// Def Con.
    	phoneNumbers = new ArrayList<String>();
    }
    
	public User(long id, String name, int age, List<String> phoneNumbers) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.phoneNumbers = phoneNumbers;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}


	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(id);
		out.writeObject(name);
		out.writeInt(age);
		
// Case 1. 
/*
		Externalize : 117
		Serialze : 165
*/		
//		out.writeObject(phoneNumbers);
		
//	Case 2. reduce length
/*
		Externalize : 67
		Serialze : 115
 */
		int phoneNumberSize = 0;
		if(phoneNumbers != null) {
			phoneNumberSize = phoneNumbers.size();
			out.writeInt(phoneNumberSize);
			for (String e : phoneNumbers) {
				out.writeObject(e);
			}
		}
		else {
			out.writeInt(phoneNumberSize);
		}
		
	}

	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {		
		this.id = in.readLong();
		this.name = (String) in.readObject();
		this.age = in.readInt();
		
//		this.phoneNumbers =(List<String>) in.readObject();
		
		int phoneNumberSize = in.readInt();
		for (int i = 0; i < phoneNumberSize; i++) {
			phoneNumbers.add((String)in.readObject());
		}
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (age != other.age)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumbers == null) {
			if (other.phoneNumbers != null)
				return false;
		} else if (!phoneNumbers.equals(other.phoneNumbers))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", phoneNumbers=" + phoneNumbers + "]";
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
    	List<String> phoneNumbers = new ArrayList<String>();
    	phoneNumbers.add("02-1234-1234");
    	phoneNumbers.add("010-1212-3434");
    	
    	User userWrite = new User(1, "홍길동", 10, phoneNumbers);
    	
if(false) {
    	//-------------------------------------------------------------------------
    	// File Test
    	FileOutputStream fos = new FileOutputStream("testfile");
    	ObjectOutputStream oos = new ObjectOutputStream(fos);
    	userWrite.writeExternal(oos);
    	oos.flush();
    	oos.close();
    	
    	User userRead = new User();
    	FileInputStream fis = new FileInputStream("testfile");
    	ObjectInputStream ois = new ObjectInputStream(fis);
    	userRead.readExternal(ois);
    	ois.close();
    	
    	System.out.println("equals : " + userRead.equals(userWrite));
    	System.out.println("userWrite : " + userWrite.toString());
    	System.out.println("userRead : " + userRead.toString());
    	
    	//---------------------------------------------------------------------
    	// Bytes Test
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	ObjectOutputStream boos = new ObjectOutputStream(bos);
    	userWrite.writeExternal(boos);
    	boos.flush();
    	byte[] objectBytes = bos.toByteArray();
    	boos.close();
    	
    	ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
    	ObjectInputStream bois = new ObjectInputStream(bis);
    			
    	User userBytesRead = new User();		
    	userBytesRead.readExternal(bois);
    	bois.close();
    	
    	System.out.println("equals : " + userBytesRead.equals(userWrite));
    	System.out.println("userWrite : " + userWrite.toString());
    	System.out.println("userBytesRead : " + userBytesRead.toString());
}
    	//---------------------------------------------------------------------
    	// Util Test

    	byte[] extBytes = ExternalizeUtil.toBytes(userWrite);
    	
    	System.out.println("Externalize : " + extBytes.length);
    	System.out.println("Serialze : " + ExternalizeUtil.toSerialzeBytes(userWrite).length);
    	
    	User userExt = new User();
    	try {
    		userExt = (User)ExternalizeUtil.fromBytes(userExt,  extBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("equals : " + userExt.equals(userWrite));
    	System.out.println("userWrite : " + userWrite.toString());
    	System.out.println("userExt : " + userExt.toString());
    	
    }

}
