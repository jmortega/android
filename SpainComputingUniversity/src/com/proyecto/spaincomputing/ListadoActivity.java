package com.proyecto.spaincomputing;




import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.FragmentDetalle;
import com.proyecto.spaincomputing.fragment.UniversidadesFragment;
import com.proyecto.spaincomputing.utils.Constants;

import android.content.Intent;
import android.content.res.Configuration;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
public class ListadoActivity extends ActionMainActivity{
	
	private GoogleMap mapa = null;
	private UniversidadBean ubBean;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		
		if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			    ab.setTitle(getString(R.string.listado));
			 }
		 
		 }
		
		//Inflate the custom view
	     View customNav = LayoutInflater.from(this).inflate(R.layout.custom_view, null);

	     RadioGroup radioGroup= ((RadioGroup)customNav.findViewById(R.id.radio_nav));
	     
	     radioGroup.check(R.id.radio_nav_1);
	     
	     ((RadioGroup)customNav.findViewById(R.id.radio_nav)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
	            @Override
	            public void onCheckedChanged(RadioGroup group, int checkedId) {
	
	                switch (checkedId) {
	                
	                
	                case R.id.radio_nav_1:
	

	                 break;
	                 
	                case R.id.radio_nav_2:
	                	
	                	Intent intent = new Intent();
	                    intent.setClass(getApplicationContext(), ViewPagerActivity.class);
	                    intent.putExtra(Constants.EXTRA_COLOR, currentColor);
	                    startActivity(intent);
	                    finish();

	                    break;
	                 
	                }
	               
	            }
	        });

	        //Attach to the action bar
	       ab.setCustomView(customNav);
	       
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
			 
			setContentView(R.layout.listado_landspace_activity);
			
			mapa  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.vistaMapa)).getMap();
		    
			((FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle)).mostrarDetalle(ubBean,mapa,currentColor);
			   

			//Centramos el mapa en las coordenadas latitud/longitud y con nivel de zoom 5.5
			CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5.5F);
				
			mapa.animateCamera(camera);
			
		}else{
			
			setContentView(R.layout.listado_activity);

		}
		
		UniversidadesFragment listado=(UniversidadesFragment)getSupportFragmentManager().findFragmentById(R.id.FrgListado);

		listado.setColor(currentColor);
		
		listado.setUniversidadFavoritos(false);
		
		listado.setUniversidadListener(this);

	}


}
