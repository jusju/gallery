package com.gallery.gallery.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Picture {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long pictureId;
	private String name;
	private String type;
	
	
	public Picture() {}
	
	public Picture( String name) {
		this.name = name;
	}
	
	
	public Picture(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public Long getPictureId() {
		return pictureId;
	}

	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Picture [pictureId=" + pictureId + ", name=" + name + ", type=" + type + "]";
	}

	

	

	
}