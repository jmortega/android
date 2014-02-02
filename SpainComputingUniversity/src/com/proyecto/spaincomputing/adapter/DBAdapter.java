package com.proyecto.spaincomputing.adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.proyecto.spaincomputing.entity.PlaceBean;
import com.proyecto.spaincomputing.fragment.PlacesListFragment;
import com.proyecto.spaincomputing.utils.DBHelper;

public class DBAdapter {
	 private DBHelper dbHelper;
	 private static final String DATABASE_NAME = "places.db";
	 private static final int DATABASE_VERSION = 3;

	 //constructor
     public DBAdapter (Context context){
    	 dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
     }
     
     public void insertPlace(PlaceBean p){    	 
    	 ContentValues values = buildContentValuesFromPlace(p);
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         try {
        	 db.insertWithOnConflict(DBHelper.PLACES_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);                 
         } finally {
        	 db.close();
         }
     }
          
     public void updatePlace(PlaceBean p) {
    	 ContentValues values = buildContentValuesFromPlace(p);
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         try {
        	 db.updateWithOnConflict(DBHelper.PLACES_TABLE, values, 
        			 		         DBHelper.KEY_ID+"=?", new String[]{p.getId()+""}, SQLiteDatabase.CONFLICT_IGNORE);                 
         } finally {
        	 db.close();
         }
    	 
     }    
     
     public void deleteAllPlaces(){
    	 SQLiteDatabase db = dbHelper.getWritableDatabase();
    	 try {
    		 db.delete(DBHelper.PLACES_TABLE,null, null);
    	 } finally {
    		 db.close();
    	 }
     }
     
     public void deletePlace(long id,PlacesListFragment plf){
    	 SQLiteDatabase db = dbHelper.getWritableDatabase();
    	 
    	 String whereclause = DBHelper.KEY_ID+ " = "+id;
    	 
    	 try {
    		 Log.i("DELETE PLACE", whereclause);
    		 db.delete(DBHelper.PLACES_TABLE,whereclause, null);
    	 } finally {
    		 db.close();
    	 }
    	 
    	 plf.loadplaces();
     }
     
     public int getTotalPlacesinDatabase() {
    	 SQLiteDatabase db = dbHelper.getReadableDatabase();
    	 Cursor cursor = db.query(DBHelper.PLACES_TABLE, null, null, null, null, null, null);
    	 int total = cursor.getCount();
    	 cursor.close();
    	 return total;
     }
     
     public ArrayList<PlaceBean> getPlaces(){
    	 SQLiteDatabase db = dbHelper.getReadableDatabase();
    	 Cursor cursor = db.query(DBHelper.PLACES_TABLE, null, null, null, null, null, null);
    	 ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
    	 
    	 while (cursor.moveToNext()) {
    		 PlaceBean p = new PlaceBean();
    		 p.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
    		 p.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE)));
    		 p.setTime(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME)));
    		 p.setAuthor(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AUTHOR)));
    		 p.setLocation(new LatLng(
    				 	cursor.getDouble(cursor.getColumnIndex(DBHelper.KEY_LATITUDE)),
    				 	cursor.getDouble(cursor.getColumnIndex(DBHelper.KEY_LONGITUDE))
    				 	));
    		 p.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION)));
    		 p.setThumbnailURL(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_THUMBNAILURL)));
    		 places.add(p);
    	 }
    	 cursor.close();
    	 return places;
     }
     
     public ContentValues buildContentValuesFromPlace (PlaceBean p) {
    	 ContentValues values = new ContentValues();
    	 values.put(DBHelper.KEY_ID, p.getId());
    	 values.put(DBHelper.KEY_DATE, p.getDate());
    	 values.put(DBHelper.KEY_TIME, p.getTime());
    	 values.put(DBHelper.KEY_AUTHOR, p.getAuthor());
    	 values.put(DBHelper.KEY_LATITUDE, p.getLocation().latitude);
    	 values.put(DBHelper.KEY_LONGITUDE, p.getLocation().longitude);
    	 values.put(DBHelper.KEY_THUMBNAILURL, p.getThumbnailURL());
    	 values.put(DBHelper.KEY_DESCRIPTION, p.getDescription());
    	 return values;
     }
}
