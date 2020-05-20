package com.pool.Weride.Model;

public class Contact {
	private int imageSource;
	private String name;
	private String contact;
	private int idtext;
	
	public Contact(int imagesource, String nameText, String contactText,int idtext) {
		this.imageSource = imagesource;
		this.name = nameText;
		this.contact = contactText;
		this.idtext = idtext;
	}
	
	
	public String getContact() {
		return contact;
	}
	
	public int getImageresource() {
		return imageSource;
	}
	
	public String getName() {
		return name;
	}
	
	public void setImageresource(int imagesource) {
		this.imageSource = imagesource;
	}
	
	public void setName(String nameText) {
		this.name = nameText;
	}
	
	public void setContact(String contactText) {
		this.contact = contactText;
	}
	
	public int getIdtext() {
		return idtext;
	}
	
	public void setIdtext(int idtext) {
		this.idtext = idtext;
	}
	
	@Override
	public boolean equals( Object obj) {
		return this.contact.equalsIgnoreCase(((Contact) obj).contact);
	}
	
}
