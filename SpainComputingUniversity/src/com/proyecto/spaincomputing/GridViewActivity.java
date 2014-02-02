package com.proyecto.spaincomputing;

import java.util.ArrayList;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.proyecto.spaincomputing.adapter.GridViewAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;

public class GridViewActivity extends ActionMainActivity {

	// Declare Variables
	private UniversidadBean ubBean;
	
	private GridView gridView;
	private int columnWidth;
	
	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){
			 
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.gridView));
			 }
			 
			//Inflate the custom view
		     View customNav = LayoutInflater.from(this).inflate(R.layout.custom_gridview, null);

		     RadioGroup radioGroup= ((RadioGroup)customNav.findViewById(R.id.radio_nav));
		     
		     radioGroup.check(R.id.radio_nav_2);
		     
		      //Bind to its state change
		        ((RadioGroup)customNav.findViewById(R.id.radio_nav)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
		            @Override
		            public void onCheckedChanged(RadioGroup group, int checkedId) {
	
		                switch (checkedId) {
		                
		                
		                case R.id.radio_nav_1:
		
		                	Intent intent = new Intent();
		                    intent.setClass(getApplicationContext(), ListadoActivity.class);
		                    intent.putExtra(Constants.EXTRA_COLOR, currentColor);
		                    startActivity(intent);
		                    finish();
		                    

		                 break;
		                 
		                case R.id.radio_nav_2:
		                	
		                
		                    
		                 break;
		                 
		                }
		               
		            }
		        });

		        //Attach to the action bar
		       ab.setCustomView(customNav);
		       ab.setDisplayShowCustomEnabled(true);
		 }
		 
		// Get the view from listado_grid.xml
		 setContentView(R.layout.listado_grid);

		listado=inicializarDatos();


		gridView = (GridView) findViewById(R.id.gridview);
		
		// Initilizing Grid View
		InitilizeGridLayout();
				
		gridView.setAdapter(new GridViewAdapter(this));
	    
		gridView.setVisibility(View.INVISIBLE);
		
		new APITask().execute();

		gridView.setOnItemClickListener(new OnItemClickListener() {
	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

	            ubBean=listado.get(position);
	            
	            Intent intent=new Intent();
	            intent.setClass(getApplicationContext(), DetalleUniversidadActivity.class);
	            intent.putExtra(Constants.EXTRA_ID, ubBean.getId());
	  	      	intent.putExtra(Constants.EXTRA_ID_IMAGEN, ubBean.getIdImagen());
	    		intent.putExtra(Constants.EXTRA_NOMBRE, ubBean.getNombre());
	    		intent.putExtra(Constants.EXTRA_DESCRIPCION, ubBean.getDescripcion());
	    		intent.putExtra(Constants.EXTRA_ENLACE, ubBean.getEnlace());
	    		intent.putExtra(Constants.EXTRA_GRADO, ubBean.getGrado());
	    		intent.putExtra(Constants.EXTRA_TIPO, ubBean.getTipo());
	    		intent.putExtra(Constants.EXTRA_LATITUD, ubBean.getLatitud());
	    		intent.putExtra(Constants.EXTRA_LONGITUD, ubBean.getLongitud());
	    		intent.putExtra(Constants.EXTRA_COLOR, currentColor);
	    		mostrarToast(ubBean.getNombre().toString());
	    		
	  	      	startActivity(intent);
	        }


	    });
	    
	    FrameLayout item = (FrameLayout) findViewById(R.id.selectableItem);
	    	        item.setOnClickListener(new View.OnClickListener() {
	    	            @Override
	    	            public void onClick(View v) {
	    	            }
	    	        });

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
	
	private void mostrarToast(String text){
		
		Toast toast=new Toast(getApplicationContext());
        LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.layout_toast,(ViewGroup)findViewById(R.id.lytLayout));
        TextView texto=(TextView)layout.findViewById(R.id.txtMensaje);
        texto.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_listado, menu);
        
        return super.onCreateOptionsMenu(menu);
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
	
	public  ArrayList<UniversidadBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  
		  return listado;
	}
	
	
	public void verMapa(double latitud,double longitud,String nombre,String descripcion) {
	       
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        if(Double.valueOf(latitud)!=null && Double.valueOf(longitud)!=null){
        	intent.putExtra(Constants.EXTRA_LATITUD, latitud);
        	intent.putExtra(Constants.EXTRA_LONGITUD, longitud);
        	intent.putExtra(Constants.EXTRA_NOMBRE, nombre);
        	intent.putExtra(Constants.EXTRA_DESCRIPCION,descripcion);
        	intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        	startActivity(intent);
        }else{
        	
  
            mostrarToast(getText(R.string.seleccionar_universidad).toString());
        	
        }
    }
	
	class APITask extends AsyncTask<Void, Void, Void> {
		ProgressBar progressBar;

		@Override
		protected void onPreExecute() {			
			progressBar = (ProgressBar)findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
		}		
		
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
	
}