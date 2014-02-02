package com.proyecto.spaincomputing.entity;

import com.google.android.gms.maps.model.LatLng;

public class PlaceBean {
	private int id;
	private String date;
	private String time;
	private String author;
	private LatLng location;
	private String thumbnailURL;
	private String description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public LatLng getLocation() {
		return location;
	}
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
