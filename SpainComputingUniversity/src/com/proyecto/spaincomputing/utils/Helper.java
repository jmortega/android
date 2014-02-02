package com.proyecto.spaincomputing.utils;

public class Helper {
	public final static String INSTAGRAM_API_KEY = "2b751d04969b47468e410f6a0a4388c0";
	public final static String BASE_API_URL = "https://api.instagram.com/v1/";
	
	public final static String FLICKR_API_URL = "http://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

	public static String getRecentUrl(String tag){
		return BASE_API_URL + "tags/" + tag + "/media/recent?client_id=" + INSTAGRAM_API_KEY; 
	}
}
