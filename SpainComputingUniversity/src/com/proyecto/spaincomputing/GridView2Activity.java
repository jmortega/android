package com.proyecto.spaincomputing;

import java.util.ArrayList;

import com.proyecto.spaincomputing.adapter.GridViewImageAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;

public class GridView2Activity extends ActionMainActivity {

	private ArrayList<UniversidadBean> imagePaths = new ArrayList<UniversidadBean>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private int columnWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){
			 
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.gridImageView));
			 }
		 }
		 
		setContentView(R.layout.activity_grid_view);

		gridView = (GridView) findViewById(R.id.grid_view);

		// Initilizing Grid View
		InitilizeGridLayout();

		// loading all image paths from SD card
		imagePaths = MySingleton.getInstance().getUniversitiesList();

		// Gridview adapter
		adapter = new GridViewImageAdapter(GridView2Activity.this, imagePaths,columnWidth);

		// setting grid view adapter
		gridView.setAdapter(adapter);
	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,Constants.GRID_PADDING, r.getDisplayMetrics());

		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("appPreferences", MODE_PRIVATE);
	     
		//Obtenemos columnas del grid view a partir de las preferencias
	    int columns=Integer.valueOf(sharedPreferences.getString("pref_grid_columns","3"));

		columnWidth = (int) ((getScreenWidth() - ((columns + 1) * padding)) / columns);

		gridView.setNumColumns(columns);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

	/*
	 * getting screen width
	 */
	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  		Intent intent = new Intent(this, PrincipalActivity.class);
		    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
		    startActivity(intent);
       
       break;
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }
}