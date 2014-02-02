package com.proyecto.spaincomputing.adapter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.proyecto.spaincomputing.FullScreenViewActivity;
import com.proyecto.spaincomputing.GridView2Activity;
import com.proyecto.spaincomputing.entity.UniversidadBean;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewImageAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<UniversidadBean> _filePaths = new ArrayList<UniversidadBean>();
	private Resources resources;
	
	public GridViewImageAdapter(GridView2Activity activity, ArrayList<UniversidadBean> filePaths,int imageWidth) {
		this._activity = activity;
		this._filePaths = filePaths;
		this.resources = activity.getResources();
	}

	@Override
	public int getCount() {
		return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(_activity);
		} else {
			imageView = (ImageView) convertView;
		}

		// get screen dimensions
		Bitmap image = decodeSampledBitmapFromResource(resources, _filePaths.get(position).getIdImagen(), 225, 225);

		//imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,imageWidth));
		imageView.setImageBitmap(image);

		// image view click listener
		imageView.setOnClickListener(new OnImageClickListener(position));

		return imageView;
	}

	class OnImageClickListener implements OnClickListener {

		int _postion;

		// constructor
		public OnImageClickListener(int position) {
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			// on selecting grid view image
			// launch full screen activity
			Intent i = new Intent(_activity, FullScreenViewActivity.class);
			i.putExtra("position", _postion);
			_activity.startActivity(i);
		}

	}

	/*
	 * Resizing image size
	 */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
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
