package com.proyecto.spaincomputing;



import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.FragmentDetalle;
import com.proyecto.spaincomputing.fragment.LinkDialogFragment;
import com.proyecto.spaincomputing.fragment.LinkDialogFragment.NoticeDialogListener;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
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


/**
*
* Actividad que muestra la vista de detalle en modo portrait
*
* @author jmortega
*/

public class DetalleUniversidadActivity extends ActionMainActivity implements NoticeDialogListener {

	//datos de una universidad
	private Integer id,idImagen;
	private String nombre, descripcion, enlace, grado , tipo;
	private Double latitud, longitud;

	private GoogleMap mapa = null;
	 
	private FragmentDetalle detalle;
	
	private ShareActionProvider mShareActionProvider;
	
	static final String STATE_ID = "ID";
	static final String STATE_IDIMAGEN = "IDIMAGEN";

	private Bundle savedInstanceState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.detalle));
			 }
		 }
		 
		//Activamos el modo fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
     // Debe validarse si el dispositivo se ha cambiado para el modo landscape
     // Si sí, se finaliza la actividad y se recarga la actividad inicial
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	finish();
        	return;
        }
        
		setContentView(R.layout.detalle_activity);

		detalle = (FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle);

		//Recogemos los datos que se nos proporciona a traves del intent de llamada
      	Bundle extras = getIntent().getExtras();
      	if(extras != null) {

      			id = extras.getInt(Constants.EXTRA_ID);
      			idImagen = extras.getInt(Constants.EXTRA_ID_IMAGEN);
      			nombre= extras.getString(Constants.EXTRA_NOMBRE);
      			descripcion= extras.getString(Constants.EXTRA_DESCRIPCION);
      			enlace= extras.getString(Constants.EXTRA_ENLACE);
      			tipo= extras.getString(Constants.EXTRA_TIPO);
      			grado= extras.getString(Constants.EXTRA_GRADO);
      			latitud= extras.getDouble(Constants.EXTRA_LATITUD);
      			longitud= extras.getDouble(Constants.EXTRA_LONGITUD);
      			
      	}
      	
      	if (savedInstanceState != null) {
            // Restore value of members from saved state
            id = savedInstanceState.getInt(STATE_ID);
            idImagen = savedInstanceState.getInt(STATE_IDIMAGEN);
        } 

		UniversidadBean ub=new UniversidadBean(id,idImagen,nombre,descripcion,enlace,grado,tipo,latitud,longitud);
		
		//Marcador para el mapa
        mostrarMarcador(latitud,longitud,nombre,descripcion);
        
        detalle.setColor(this.currentColor);
		detalle.mostrarDetalle(ub,mapa,currentColor);
		
		
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putInt(STATE_ID, id);
	    savedInstanceState.putInt(STATE_IDIMAGEN, idImagen);
	    
	    this.savedInstanceState=savedInstanceState;
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    id = savedInstanceState.getInt(STATE_ID);
	    idImagen = savedInstanceState.getInt(STATE_IDIMAGEN);
	}

	
	private void mostrarMarcador(double lat, double lng,String nombre,String descripcion)
	{

		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.vistaMapa)).getMap();
		
		Toast.makeText(DetalleUniversidadActivity.this, "Lat: " + lat + " - Lng: " + lng,Toast.LENGTH_LONG).show();
		
		mostrarToast("Lat: " + lat + " - Lng: " + lng);
         
	    mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nombre));
	    
	    //Centramos el mapa en las coordenadas latitud/longitud y con nivel de zoom 12
		CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12F);
		
		mapa.animateCamera(camera);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_mostrar, menu);
		
		MenuItem shareItem = menu.findItem(R.id.action_share);
		mShareActionProvider = (ShareActionProvider)
	    MenuItemCompat.getActionProvider(shareItem);
	    mShareActionProvider.setShareIntent(createShareIntent());
		
	    return super.onCreateOptionsMenu(menu);

	}
	
    private Intent createShareIntent() {  
        Intent shareIntent = new Intent(Intent.ACTION_SEND); 
        shareIntent.setType("text/html");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Spain COmputing UNiversity");

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            id = savedInstanceState.getInt(STATE_ID);
            idImagen = savedInstanceState.getInt(STATE_IDIMAGEN);
        } 
        
        if(id!=null){
        	
        	UniversidadBean ub=new UniversidadBean(id, idImagen, nombre, descripcion, enlace, tipo, grado, latitud, longitud);
        
        	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(ub.getNombre()+"\n"+ub.getDescripcion()+"\n"+ub.getGrado()+"\n\n"+ub.getEnlace()+"\n\n"+ub.getLatitud()+"\n"+ub.getLongitud()));
        }
        
        return shareIntent;  
   }  

	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  	/*Intent intent = NavUtils.getParentActivityIntent(this); 
	  	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 

	  	NavUtils.navigateUpTo(this, intent);
	  	*/
        
        NavUtils.navigateUpFromSameTask(this);
        
        break;
        
	  	case  R.id.mapa:
	  		
	  		if(id!=null){
	  			verMapa();
	  			
	  		}else{
	  			
	  			String soporte=getText(R.string.seleccionar_universidad).toString();
	            AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
	            dialog.setTitle(getText(R.string.seleccionar_item_lista).toString());
	            dialog.setMessage(soporte);
	            dialog.show();
	            
	  		}
	  		
	  		break;
	  		
	  	case R.id.link:
            
          	if(enlace!=null){
          		link(enlace);
          	}

           break;

          case R.id.rutaGoogle:


          	if(Double.valueOf(latitud)!=null && Double.valueOf(longitud)!=null){
          		rutaGoogleNavigation(latitud,longitud);
          	}

             break;
             
          case R.id.favorito:

            		
            favorito();
            	

             break;
             
          case R.id.share:

          	UniversidadBean ub=new UniversidadBean(id, idImagen, nombre, descripcion, enlace, tipo, grado, latitud, longitud);
          	
          	share(ub);
          	

           break;
           
          case R.id.panoramio:

    
            	panoramio(latitud,longitud);
            	

             break;
             
          case R.id.instagram:

        	UniversidadBean ub2=new UniversidadBean(id, idImagen, nombre, descripcion, enlace, tipo, grado, latitud, longitud);
        	
          	instagram(ub2.getNombre());
          	

           break;
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }
	
	public void verMapa() {
	       
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        if(Double.valueOf(latitud)!=null && Double.valueOf(longitud)!=null){
        	intent.putExtra(Constants.EXTRA_LATITUD, latitud);
        	intent.putExtra(Constants.EXTRA_LONGITUD, longitud);
        	intent.putExtra(Constants.EXTRA_NOMBRE, nombre);
        	intent.putExtra(Constants.EXTRA_DESCRIPCION, descripcion);
        	intent.putExtra(Constants.EXTRA_ID_IMAGEN, idImagen);
        	intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        	startActivity(intent);
        }else{
        	
        	String soporte=getText(R.string.seleccionar_universidad).toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getText(R.string.seleccionar_item_lista).toString());
            dialog.setMessage(soporte);
            dialog.show();
        }
    }
	
	public void rutaGoogleNavigation(Double latitud,Double longitud){
	      
	      //lanzar la aplicacion "navigation" pasando como parametros las coordenadas del punto al que deseas ir

	      Intent intentNavigate = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +latitud+","+longitud));

	      intentNavigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	      
	      startActivity(intentNavigate);

	  }
	  
	  public void link(String enlace){
	    
		  //detalle.mostrarLink(enlace);
		  
		//Construyo objeto LinkDialogFragment
		 LinkDialogFragment linkDialogFragment=new LinkDialogFragment();
		    
		  //Pasar parámetros al fragment
	     Bundle args = new Bundle();
	     args.putString("ENLACE", enlace);
	     linkDialogFragment.setArguments(args);
	     linkDialogFragment.setColor(this.currentColor);
		 linkDialogFragment.show(getSupportFragmentManager(), "");

	  }
	  
	  public boolean isFavorito(){
		    
		  	return detalle.isFavorito(id);
		 
	  }
	  
	  public void favorito(){

		  detalle.favorito(id,nombre,descripcion);
		    
			
	  }
	  
	  public void share(UniversidadBean ub){
		    
		  detalle.showDialogShareButtonClick(ub);
	  }
	  
	  public void panoramio(Double latitud,Double longitud){
		    
	   //comprobar conexion a internet
	  if(enlace!=null && NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
				
		  Intent intent = new Intent();
	      intent.setClass(getApplicationContext(), PanoramioActivity.class);
	      String url="http://www.panoramio.com/map/#lt="+latitud.toString()+"&ln="+longitud.toString()+"&z=1";
	      intent.putExtra(Constants.EXTRA_URL,url);
	      intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	      startActivity(intent);
	      
	  }else{

          String soporte=getText(R.string.sin_conexion).toString();
          AlertDialog.Builder dialog = new AlertDialog.Builder(this);
          dialog.setTitle(getText(R.string.sin_conexion).toString());
          dialog.setMessage(soporte);
          dialog.show();
          
  	   }
	  
	  }
	  
	  public void instagram(String tag){
		   
		 	//comprobar conexion a internet
			if(NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
	    		
	    	  Intent intent = new Intent();
	  	      intent.setClass(getApplicationContext(), InstagramActivity.class);
		      intent.putExtra(Constants.EXTRA_TAG,tag);
		      intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
		      startActivity(intent);
	  	      	
	    	}else{

	            String soporte=getText(R.string.sin_conexion).toString();
	            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	            dialog.setTitle(getText(R.string.sin_conexion).toString());
	            dialog.setMessage(soporte);
	            dialog.show();
	            
	    	}
			
	  }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		//Toast.makeText(this, getResources().getString(R.string.ir_al_sitio), Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		//Toast.makeText(this, getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
		
	}
}
