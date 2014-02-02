package com.proyecto.spaincomputing.adapter;

import java.util.ArrayList;


import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
    private Integer[] IdsImagen;
    
    private Resources resources;
	private LayoutInflater inflater;
	
    public GridViewAdapter(Context context) {

    	//inicializar contexto
        mContext = context;
        
    	this.resources = context.getResources();
        this.inflater = LayoutInflater.from(mContext);
        
        //inicializar listado imágenes
        listado=MySingleton.getInstance().getUniversitiesList();
        IdsImagen=new Integer[listado.size()];
        
        for(int i=0;i<listado.size();i++){
        	
        	IdsImagen[i]=listado.get(i).getIdImagen();
        }
    }

    public int getCount() {
        return IdsImagen.length;
    }

    public Object getItem(int position) {
        return IdsImagen[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
       
    	//ImageView imageView;
        
    	Log.i("GridViewAdapter-position:",String.valueOf(position));
    	
        ViewHolder holder;
        if (convertView == null) {
        	
        	Log.i("GridViewAdapter", "New");
	        convertView = inflater.inflate(R.layout.grid_image, null);

	        holder = new ViewHolder();
	        holder.imagen = (ImageView) convertView.findViewById(R.id.imgFlag);

	        convertView.setTag(holder);	        
	    } else {
	    	
	    	Log.i("GridViewAdapter", "Recicling");
	    	
	        holder = (ViewHolder) convertView.getTag();
	    }
        
        /*if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(225, 225));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(IdsImagen[position]);
        
        return imageView;*/
        
        holder.imagen.setImageBitmap(
        	    decodeSampledBitmapFromResource(resources, IdsImagen[position], 225, 225));
        
        return convertView;
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
	
	
    static class ViewHolder {
		public ImageView imagen;
	}
}
