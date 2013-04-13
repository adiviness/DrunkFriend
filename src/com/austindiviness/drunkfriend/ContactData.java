package com.austindiviness.drunkfriend;

import java.io.Serializable;

public class ContactData implements Serializable {
	String name;
	String number;
	
	public ContactData(String name, String number){
		this.name = name;
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
}
