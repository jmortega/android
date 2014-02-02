package com.proyecto.spaincomputing;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.FragmentDetalle;
import com.proyecto.spaincomputing.fragment.UniversidadesFragmentPosition;
import com.proyecto.spaincomputing.listener.UniversidadListener;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class PosicionActivity extends ActionMainActivity implements UniversidadListener{

	
	private GoogleMap mapa = null;

    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
 
        ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.posicion));
			 }
			 
		 }
		 
		//Activamos el modo fullscreen
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    
	    setContentView(R.layout.listado_activity_position);
		
		UniversidadesFragmentPosition listado=(UniversidadesFragmentPosition)getSupportFragmentManager().findFragmentById(R.id.FrgListadoPosition);
		
		listado.setColor(this.currentColor);
		listado.setUniversidadListener(this);
			 
    }
  

    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_posicion, menu);
		
		return super.onCreateOptionsMenu(menu);

	}
    
    @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  		NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
        
        break;
        
	  	case  R.id.actualizar:
	  		
	  		UniversidadesFragmentPosition listado=(UniversidadesFragmentPosition)getSupportFragmentManager().findFragmentById(R.id.FrgListadoPosition);
	  		
	  		listado.comenzarLocalizacion();
	  		
	  		listado.inicializarDatos();
	  		
	  		break;
	  		
	  	case  R.id.btnVerMapa:
	  		
	  		verMiPosicionMapa();
	  		
	  		break;
	  		
	  	case  R.id.btnPositionASC:
	  		
	  		UniversidadesFragmentPosition listadoASC=(UniversidadesFragmentPosition)getSupportFragmentManager().findFragmentById(R.id.FrgListadoPosition);
	  		
	  		listadoASC.comenzarLocalizacion();
	  		
	  		listadoASC.inicializarDatosASC();
	  		
	  		
	  		
	  		break;
	  		
	  	case  R.id.btnPositionDESC:
	  		
	  		UniversidadesFragmentPosition listadoDESC=(UniversidadesFragmentPosition)getSupportFragmentManager().findFragmentById(R.id.FrgListadoPosition);
	  		
	  		listadoDESC.comenzarLocalizacion();
	  		
	  		listadoDESC.inicializarDatosDESC();
	  		
	  		
	  		
	  		break;
	
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }



	@Override
	public void onUniversidadSelected(UniversidadBean ub) {
		
		boolean hayDetalle = (getSupportFragmentManager().findFragmentById(R.id.FrgDetalle) != null) && (getSupportFragmentManager().findFragmentById(R.id.FrgDetalle).isInLayout());
		
		
	    if (hayDetalle) {
	    	
	      mapa  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.vistaMapa)).getMap();
	    
	      ((FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle)).mostrarDetalle(ub,mapa,currentColor);                          
	    }
	    else {
	      Intent intent=new Intent(this, DetalleUniversidadActivity.class);

	      intent.putExtra(Constants.EXTRA_ID, ub.getId());
	      intent.putExtra(Constants.EXTRA_ID_IMAGEN, ub.getIdImagen());
  		  intent.putExtra(Constants.EXTRA_NOMBRE, ub.getNombre());
  		  intent.putExtra(Constants.EXTRA_DESCRIPCION, ub.getDescripcion());
  		  intent.putExtra(Constants.EXTRA_ENLACE, ub.getEnlace());
  		  intent.putExtra(Constants.EXTRA_GRADO, ub.getGrado());
  		  intent.putExtra(Constants.EXTRA_TIPO, ub.getTipo());
  		  intent.putExtra(Constants.EXTRA_LATITUD, ub.getLatitud());
  		  intent.putExtra(Constants.EXTRA_LONGITUD, ub.getLongitud());
  		  intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	      startActivity(intent);
	    }
		
	}



	@Override
	public void onUniversidadLink(String link) {
		
		if( link==null || (link!=null && link.equals(""))){
    		
            mostrarToast(getText(R.string.enlace_no_encontrado).toString());
    	    
    	}
		//compborar conexion a internet
		else if(link!=null && !link.equals("") && NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
    		
			String content = link.toString();
    	    Intent intent = new Intent();
  	      	intent.setClass(getApplicationContext(), LinkWebActivity.class);
  	      	intent.putExtra(Constants.EXTRA_URL,content);
  	      	intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
  	      	startActivity(intent);
    	    
    	}
		else{

            mostrarToast(getText(R.string.sin_conexion).toString());
    	}
		
	}



	@Override
	public void onUniversidadCompartir(UniversidadBean ub) {
		
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/html");

        List<ResolveInfo> resInfo = getApplicationContext().getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                targetedShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                targetedShareIntent.setType("text/html");
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Spain COmputing UNiversity");

                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(ub.getNombre()+"\n"+ub.getDescripcion()+"\n"+ub.getGrado()+"\n"+ub.getEnlace()));
                
                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);


            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size()-1), getText(R.string.compartir));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

            startActivity(chooserIntent);
        }
		
	}



	@Override
	public void onUniversidadFavorito(UniversidadBean ub) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onUniversidadContactos(UniversidadBean ub) {


		Intent intent = new Intent();
	     String datos=
	                  ub.getNombre()+"\n"+
	                  ub.getDescripcion()+"\n"+
	                  ub.getGrado()+"\n"+
	                  ub.getEnlace()+"\n"+
	                  getText(R.string.latitud)+":"+ub.getLatitud()+"\n"+
	                  getText(R.string.longitud)+":"+ub.getLongitud();
	     
	     intent.putExtra(Constants.EXTRA_DATOS,datos);
	     intent.putExtra(Constants.EXTRA_ID_IMAGEN,ub.getIdImagen());
	     intent.putExtra(Constants.EXTRA_CONTACT_SEARCH,"CONTACT");
	     intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	     intent.setClass(getApplicationContext(), SendMAILActivity.class);
	     startActivity(intent);
		
	}



	@Override
	public void onUniversidadMapa(double latitud, double longitud,
			String nombre, String descripcion,int idImagen,String flag) {

		Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        if(Double.valueOf(latitud)!=null && Double.valueOf(longitud)!=null){
        	intent.putExtra(Constants.EXTRA_LATITUD, latitud);
        	intent.putExtra(Constants.EXTRA_LONGITUD, longitud);
        	intent.putExtra(Constants.EXTRA_NOMBRE, nombre);
        	intent.putExtra(Constants.EXTRA_DESCRIPCION, descripcion);
        	intent.putExtra(Constants.EXTRA_ID_IMAGEN, idImagen);
        	intent.putExtra(Constants.EXTRA_FLAG, flag);
        	 intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        	if(flag!=null && flag.equals("distancia")){
        		intent.putExtra(Constants.EXTRA_MI_POSICION, true);
        	}
        	
        	startActivity(intent);
        }else{
        	            
            mostrarToast(getText(R.string.seleccionar_universidad).toString());
        	
        }
		
	}


	public void verMiPosicionMapa() {
	       
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);

        intent.putExtra(Constants.EXTRA_MI_POSICION, Boolean.TRUE);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
 
    }
	
	public void mostrarToast(String text){
		
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
	  public void onUniversidadContactosSearch(UniversidadBean ub) {

		  Intent intent = new Intent();
		     String datos=
		                  ub.getNombre()+"\n"+
		                  ub.getDescripcion()+"\n"+
		                  ub.getGrado()+"\n"+
		                  ub.getEnlace()+"\n"+
		                  getText(R.string.latitud)+":"+ub.getLatitud()+"\n"+
		                  getText(R.string.longitud)+":"+ub.getLongitud();
		     
		     intent.putExtra(Constants.EXTRA_DATOS,datos);
		     intent.putExtra(Constants.EXTRA_ID_IMAGEN,ub.getIdImagen());
		     intent.putExtra(Constants.EXTRA_CONTACT_SEARCH,"CONTACT_SEARCH");
		     intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
		     intent.setClass(getApplicationContext(), SendMAILActivity.class);
		     startActivity(intent);
	  }

}

