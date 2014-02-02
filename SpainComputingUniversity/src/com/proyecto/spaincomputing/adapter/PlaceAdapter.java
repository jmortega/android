package com.proyecto.spaincomputing.adapter;

import java.util.ArrayList;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.entity.PlaceBean;
import com.proyecto.spaincomputing.utils.BitmapLRUCache;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.TextView;


	
public class PlaceAdapter extends ArrayAdapter<PlaceBean> {
	  
	private final Activity context;
	  
	private ArrayList<PlaceBean> listado=new ArrayList<PlaceBean>();

	private ImageLoader imageLoader;
	
	public static RequestQueue requestQueue;
	
	private Resources resources;
    
	public PlaceAdapter(Fragment context,ArrayList<PlaceBean> listado) {
		  
		  super(context.getActivity(), R.layout.row_places, listado);
		  
		  this.resources = context.getResources();
		  
		  this.context = context.getActivity();
		  
		  this.listado=listado;

		  requestQueue = Volley.newRequestQueue(context.getActivity());
		  
		  this.imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());   

	        
	  }
  
	static class ViewContainer{
		
	 public NetworkImageView imagen;
	 public ImageView  imagen2;
	 public TextView nombre;
	 public TextView descripcion;
	 public ViewContainer(){ 
	 }
	 public ViewContainer(final View row){
	   //obtiene una referencia a todas las vistas de la fila
	    nombre=(TextView)row.findViewById(R.id.textView_superior);
	    descripcion=(TextView)row.findViewById(R.id.textView_inferior);
	    imagen=(NetworkImageView)row.findViewById(R.id.imageView_imagen);
	    imagen2=(ImageView)row.findViewById(R.id.imageView_imagen2);
	  	
	}
	 
	}
	
   @Override
   public View getView(int position, View convertView,ViewGroup parent) {
  	
	ViewContainer viewContainer;

	Log.i("PlaceAdapter-position:",String.valueOf(position));
	
	//si es la primera vez que se imprime la fila
	if(convertView==null){
		
		Log.i("PlaceAdapter", "New");
		LayoutInflater inflater = context.getLayoutInflater();	
		convertView = inflater.inflate(R.layout.row_places, null,true);
	    
	    //crea una vista para el objeto contenedor
	    viewContainer=new ViewContainer(convertView);
	    
	    //asigna el contenedor de la vista a rowView
	    convertView.setTag(viewContainer);
	    
	}else{
	
		Log.i("UniversidadAdapter", "Recicling");
	
		viewContainer=(ViewContainer) convertView.getTag();
		
		
	}
	    
    //personaliza el contenido de cada fila basandone en su posicion
	viewContainer.nombre.setText(listado.get(position).getDescription());
	String author="";
	if(listado.get(position).getAuthor()!=null){
		author=listado.get(position).getAuthor();
	}
	viewContainer.descripcion.setText(listado.get(position).getDate()+ " / "+listado.get(position).getTime()+ " / "+author);

	if ((listado.get(position).getThumbnailURL() != null) && (!listado.get(position).getThumbnailURL().equals("")))  {
	
		viewContainer.imagen2.setVisibility(View.GONE);
		viewContainer.imagen.setVisibility(View.VISIBLE);
		
		viewContainer.imagen.setImageUrl(listado.get(position).getThumbnailURL(),imageLoader);

	}else{
		
		viewContainer.imagen.setVisibility(View.GONE);
		viewContainer.imagen2.setVisibility(View.VISIBLE);
		
		viewContainer.imagen2.setImageBitmap(
        	    decodeSampledBitmapFromResource(resources, R.drawable.icon, 225, 225));

	}
	
    
    return(convertView);
   }
   
   
   public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {

       // First decode with inJustDecodeBounds=true to check dimensions
       final BitmapFactory.Options options = new BitmapFactory.Options();
       options.inJustDecodeBounds = true;
       BitmapFactory.decodeResource(res, resId, options);

       // Calculate inSampleSize
       options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

       // Decode bitmap with inSampleSize set
       options.inJustDecodeBounds = false;
       return BitmapFactory.decodeResource(res, resId, options);
   }
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
   // Raw height and width of image
   final int height = options.outHeight;
   final int width = options.outWidth;
   int inSampleSize = 1;

   if (height > reqHeight || width > reqWidth) {

       final int halfHeight = height / 2;
       final int halfWidth = width / 2;

       // Calculate the largest inSampleSize value that is a power of 2 and keeps both
       // height and width larger than the requested height and width.
       while ((halfHeight / inSampleSize) > reqHeight
               && (halfWidth / inSampleSize) > reqWidth) {
           inSampleSize *= 2;
       }
   }

   return inSampleSize;
}
   
}