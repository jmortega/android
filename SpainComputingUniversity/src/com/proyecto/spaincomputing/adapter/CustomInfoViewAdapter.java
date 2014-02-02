package com.proyecto.spaincomputing.adapter;


import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.entity.PlaceBean;
import com.proyecto.spaincomputing.singleton.MySingleton;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomInfoViewAdapter implements InfoWindowAdapter {


	private Context mContext;
	
	private HashMap<Marker, PlaceBean> markerPlacesMap = new HashMap<Marker, PlaceBean>();
	
	 public CustomInfoViewAdapter(Context context,HashMap<Marker, PlaceBean> markerPlacesMap) {
		 
	    //inicializar contexto
	     mContext = context;
	     this.markerPlacesMap=markerPlacesMap;
	}
	 
	@Override
	public View getInfoContents(Marker marker) {
		PlaceBean currentPlace = markerPlacesMap.get(marker);
		View window = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window, null);
        
        TextView txt_title = (TextView)window.findViewById(R.id.txt_title);
        TextView txt_description = (TextView)window.findViewById(R.id.txt_description);
        TextView txt_snippet = (TextView)window.findViewById(R.id.txt_snippet);
        ImageView img_thumbnail = (ImageView)window.findViewById(R.id.img_thumbnail);
        txt_title.setText(marker.getTitle());
        
        String snippet = marker.getSnippet();
        
        if(currentPlace!=null && currentPlace.getAuthor()!=null){
        String author = currentPlace.getAuthor();


        if (author != null && !author.equals("")) {
        	snippet += "\n" + author;
        }
       
        }
        
        if(currentPlace!=null && currentPlace.getDescription()!=null){

            String description = currentPlace.getDescription();
            
            txt_description.setText(description);
           
         }
        
        
        txt_snippet.setText(snippet);
        
        if(currentPlace!=null && currentPlace.getThumbnailURL()!=null){
        	Log.i("currentplace Thumbnail ",currentPlace.getThumbnailURL());
        }
        
        LruCache<PlaceBean, Bitmap> thumbnails = ((MySingleton)mContext.getApplicationContext()).getThumbnails();

        if(currentPlace!=null){
        	Bitmap bitmap = thumbnails.get(currentPlace);
        	if (bitmap != null) {
        		img_thumbnail.setImageBitmap(bitmap);
        	}
        }
        
        return window;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
