package com.proyecto.spaincomputing.adapter;


import java.util.ArrayList;

import com.proyecto.spaincomputing.FullScreenViewActivity;
import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.utils.TouchImageView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity _activity;
	private ArrayList<UniversidadBean> _imagePaths;
	private LayoutInflater inflater;
	private Resources resources;
	
	// constructor
	public FullScreenImageAdapter(FullScreenViewActivity activity,
			ArrayList<UniversidadBean> universitiesList) {
		// TODO Auto-generated constructor stub
		this._activity = activity;
		this._imagePaths = universitiesList;
		this.resources = activity.getResources();
	}

	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;
 
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
 
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        
        Bitmap image = decodeSampledBitmapFromResource(resources, _imagePaths.get(position).getIdImagen());
        imgDisplay.setImageBitmap(image);
        
        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				_activity.finish();
			}
		}); 

        ((ViewPager) container).addView(viewLayout);
 
        return viewLayout;
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
 
    }
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {

	       // First decode with inJustDecodeBounds=true to check dimensions
	       final BitmapFactory.Options options = new BitmapFactory.Options();
	       options.inJustDecodeBounds = true;
	       options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	       BitmapFactory.decodeResource(res, resId, options);

	       // Decode bitmap with inSampleSize set
	       options.inJustDecodeBounds = false;
	       return BitmapFactory.decodeResource(res, resId, options);
	   }
		
		

}
