package com.proyecto.spaincomputing;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;

public class SpinnerViewActivity extends ActionMainActivity {

	// Declare Variables
	UniversidadBean ubBean;
	
	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	 
	private String[] datos;
	
	private Spinner spinner1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.spinnerView));
			 }
			 
			//Inflate the custom view
		     View customNav = LayoutInflater.from(this).inflate(R.layout.custom_spinnerview, null);

		     RadioGroup radioGroup= ((RadioGroup)customNav.findViewById(R.id.radio_nav));
		     
		     radioGroup.check(R.id.radio_nav_2);
		     
		      //Bind to its state change
		        ((RadioGroup)customNav.findViewById(R.id.radio_nav)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
		            @Override
		            public void onCheckedChanged(RadioGroup group, int checkedId) {

		                switch (checkedId) {
		                
		                
		                case R.id.radio_nav_1:
		
		                	Intent intent = new Intent();
		                    intent.setClass(getApplicationContext(), ListViewActivity.class);
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
		 setContentView(R.layout.spinner_main);

		datos=inicializarDatos();


		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos));

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
	    	@Override
	    	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	            
	    		if(spinner1.getSelectedItemPosition()>0){
	    			ubBean=listado.get(spinner1.getSelectedItemPosition());

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
	  	      		startActivity(intent);
	    		}
	        }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}


	    });
	}

	// Not using options menu in this tutorial
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
	  		
	  		NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
        
        break;
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }
	
	public String[] inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  datos=new String[listado.size()];
		  
		  datos[0]=getText(R.string.seleccionar_item_lista).toString();
		  
		  for(int i=1;i<listado.size();i++){
			  
			  datos[i]=listado.get(i).getNombre()+"["+listado.get(i).getGrado()+"]";
		  }
		  
		  return datos;
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
        	
        	Toast toast = Toast.makeText(SpinnerViewActivity.this, getText(R.string.seleccionar_universidad), Toast.LENGTH_LONG);
            toast.show();
        	
        }
    }
	
}