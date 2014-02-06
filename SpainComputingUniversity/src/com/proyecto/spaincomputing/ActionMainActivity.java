package com.proyecto.spaincomputing;



import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.FragmentDetalle;
import com.proyecto.spaincomputing.listener.UniversidadListener;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class ActionMainActivity extends ActionBarActivity  implements UniversidadListener{
	
	private GoogleMap mapa = null; 
	private UniversidadBean ubBean;
	
	private Drawable oldBackground = null;
    protected int currentColor = 0xFFC74B46;
    
    private final Handler handler = new Handler();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		 
		ActionBar ab = getSupportActionBar();
		 
		 //Activamos el modo fullscreen
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 
		 
		 if(ab!=null){
			 
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					
					ab.setDisplayShowHomeEnabled(true);
					ab.setDisplayShowTitleEnabled(true);
					ab.setDisplayShowCustomEnabled(true);
					ab.setHomeButtonEnabled(true);
					
					// Show the Up button in the action bar.
					new ActionBarHelper().setDisplayHomeAsUpEnabled(true);
			 }
			 
			//Recogemos el color para la action bar
		      Bundle extras = getIntent().getExtras();
		      if(extras != null) {
		        	currentColor = extras.getInt(Constants.EXTRA_COLOR);
		      }
		        
		     changeColor(currentColor);

		 }
	        
	}

	@Override
	public void onUniversidadSelected(UniversidadBean ub) {
		
	
		
		boolean hayDetalle = (getSupportFragmentManager().findFragmentById(R.id.FrgDetalle) != null) && (getSupportFragmentManager().findFragmentById(R.id.FrgDetalle).isInLayout());
		
		ubBean=ub;
		
		//si el fragmento de detalle se encuentra en la actividad
	    if (hayDetalle) {
	    	
	      mapa  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.vistaMapa)).getMap();
	    
	      ((FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle)).mostrarDetalle(ub,mapa,currentColor);                          
	    }
	    else {
	      Intent intent=new Intent(this, DetalleUniversidadActivity.class);
	      intent.putExtra(Constants.EXTRA_COLOR, currentColor);
	      intent.putExtra(Constants.EXTRA_ID, ub.getId());
	      intent.putExtra(Constants.EXTRA_ID_IMAGEN, ub.getIdImagen());
  		  intent.putExtra(Constants.EXTRA_NOMBRE, ub.getNombre());
  		  intent.putExtra(Constants.EXTRA_DESCRIPCION, ub.getDescripcion());
  		  intent.putExtra(Constants.EXTRA_ENLACE, ub.getEnlace());
  		  intent.putExtra(Constants.EXTRA_GRADO, ub.getGrado());
  		  intent.putExtra(Constants.EXTRA_TIPO, ub.getTipo());
  		  intent.putExtra(Constants.EXTRA_LATITUD, ub.getLatitud());
  		  intent.putExtra(Constants.EXTRA_LONGITUD, ub.getLongitud());
  		  
	      startActivity(intent);
	    }
	  }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_listado, menu);
		return true;

	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  	//NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
         
	  	Intent intent = new Intent(this, PrincipalActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	    startActivity(intent);
	    
         break;
         
	  }
	  
	  return super.onOptionsItemSelected(item);
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
  	      	intent.putExtra(Constants.EXTRA_COLOR,currentColor);
  	      	startActivity(intent);
    	    
    	}
		else{

            mostrarToast(getText(R.string.sin_conexion).toString());
    	}
		
	}
	
	@Override
	public void onUniversidadMapa(double latitud,double longitud,String nombre,String descripcion,int idImagen,String flag) {
	       
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        if(Double.valueOf(latitud)!=null && Double.valueOf(longitud)!=null){
        	intent.putExtra(Constants.EXTRA_LATITUD, latitud);
        	intent.putExtra(Constants.EXTRA_LONGITUD, longitud);
        	intent.putExtra(Constants.EXTRA_NOMBRE, nombre);
        	intent.putExtra(Constants.EXTRA_DESCRIPCION, descripcion);
        	intent.putExtra(Constants.EXTRA_ID_IMAGEN,idImagen);
        	intent.putExtra(Constants.EXTRA_COLOR,currentColor);
        	startActivity(intent);
        }else{
        	
            mostrarToast(getText(R.string.seleccionar_universidad).toString());
        	
        }
    }
	
	@Override
	public void onUniversidadCompartir(UniversidadBean ubBean) {
	    
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

                  targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(ubBean.getNombre()+"\n"+ubBean.getDescripcion()+"\n"+ubBean.getGrado()+"\n"+ubBean.getEnlace()));
                  
                  targetedShareIntent.setPackage(packageName);
                  targetedShareIntents.add(targetedShareIntent);


              }
              Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size()-1), getText(R.string.compartir));
              chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

              startActivity(chooserIntent);
          }
          
	}
	
	public void verMapa() {
       
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        if(Double.valueOf(ubBean.getLatitud())!=null && Double.valueOf(ubBean.getLongitud())!=null){
        	intent.putExtra(Constants.EXTRA_LATITUD, ubBean.getLatitud());
        	intent.putExtra(Constants.EXTRA_LONGITUD, ubBean.getLongitud());
        	intent.putExtra(Constants.EXTRA_NOMBRE, ubBean.getNombre());
        	intent.putExtra(Constants.EXTRA_DESCRIPCION, ubBean.getDescripcion());
        	intent.putExtra(Constants.EXTRA_ID_IMAGEN, ubBean.getIdImagen());
        	intent.putExtra(Constants.EXTRA_COLOR,currentColor);
        	startActivity(intent);
        }else{
        	
            mostrarToast(getText(R.string.seleccionar_universidad).toString());
            
        }
    }
	
	public void verMapaViewPager(UniversidadBean ubBean) {
	    
		this.ubBean=ubBean;
		
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        if(Double.valueOf(ubBean.getLatitud())!=null && Double.valueOf(ubBean.getLongitud())!=null){
        	intent.putExtra(Constants.EXTRA_LATITUD, ubBean.getLatitud());
        	intent.putExtra(Constants.EXTRA_LONGITUD, ubBean.getLongitud());
        	intent.putExtra(Constants.EXTRA_NOMBRE, ubBean.getNombre());
        	intent.putExtra(Constants.EXTRA_DESCRIPCION, ubBean.getDescripcion());
        	intent.putExtra(Constants.EXTRA_ID_IMAGEN, ubBean.getIdImagen());
        	intent.putExtra(Constants.EXTRA_COLOR,currentColor);
        	startActivity(intent);
        }else{
        	
            mostrarToast(getText(R.string.seleccionar_universidad).toString());
            
        }
    }
	
	public boolean isFavorito(Integer id){
	    
		boolean isFavorito=false;
	
		//Obtiene el objeto de preferencias de la aplicacion llamado favoritos
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("favoritos", 0);
		   
		//Obtiene un string almacenado en las preferencias de nombre favoritos.
		//El segundo parametro indica el valor a devolver si no lo encuentra, en este caso, ""
		String favoritos = sharedPreferences.getString("favoritos", "");

		StringTokenizer tokens=new StringTokenizer(favoritos,"/");

		while(tokens.hasMoreTokens()){
			
					if(tokens.nextToken().toString().equals(id.toString())){
						isFavorito=true;
						break;
					}
		}
		
		return isFavorito;
   }
  
  public void favorito(Integer id,String nombre,String descripcion){

	  if(isFavorito(id)){

		  mostrarToast(getText(R.string.favorito_existe).toString());
            
	  }else{
		 
	  String cadena="";

	  //Obtiene el objeto de preferencias de la aplicacion llamado favoritos
	   SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("favoritos", 0);
	   
	  //Obtiene un string almacenado en las preferencias de nombre favoritos.
	  //El segundo parametro indica el valor a devolver si no lo encuentra, en este caso, ""
	  String favoritos = sharedPreferences.getString("favoritos", "");

	  //Obtenemos el editor de las preferencias.
	  SharedPreferences.Editor editor = sharedPreferences.edit();

	  cadena=favoritos;
	  
	  //concatenamos id de universidad
	  if(id!=null){
		  cadena+="/"+id.toString();
	  }
	  
	  //Le indicamos que queremos que almacene un string de nombre favoritos con valor cadena
	  editor.putString("favoritos", cadena);
	   
	  //Tras haber indicado todos los cambios a realizar, le indicamos al editor que los almacene en las preferencias.
	  editor.commit();
	  
  
      mostrarToast(getText(R.string.favorito_ok).toString());
      
      SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("appPreferences", MODE_PRIVATE);
      
      //Lanzamos notificacion favoritos dependiendo del valor de preferencias
      if(sharedPreferences2.getBoolean("pref_notificaciones_favoritos",true)){
    	  com.proyecto.spaincomputing.utils.StatusBarNotify.getInstance(getApplicationContext()).statusBarNotify("nuevo_favorito",nombre,descripcion);
  	  }

	}
	    
		
  }

  @Override
  public void onUniversidadFavorito(UniversidadBean ub) {

	  favorito(ub.getId(),ub.getNombre(),ub.getDescripcion());
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
     intent.putExtra(Constants.EXTRA_COLOR,currentColor);
     intent.setClass(getApplicationContext(), SendMAILActivity.class);
     startActivity(intent);
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
	     intent.putExtra(Constants.EXTRA_COLOR,currentColor);
	     intent.setClass(getApplicationContext(), SendMAILActivity.class);
	     startActivity(intent);
  }
  
  
  public void onColorClicked(View v) {

		int color = Color.parseColor(v.getTag().toString());
		changeColor(color);
		Log.i("COLOR",color+"");

	}
   
   @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}
	
	private void changeColor(int newColor) {

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getSupportActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getSupportActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(true);

		}

		currentColor = newColor;

	}
	
	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getSupportActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	@SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
	
	
	/**
     * Helper for fix issue VerifyError on Android 1.6. On Android 1.6 virtual machine
     * tries to resolve (verify) getActionBar function, and since there is no such function,
     * Dalvik throws VerifyError.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class ActionBarHelper {

        /**
         * Set whether home should be displayed as an "up" affordance.
         * Set this to true if selecting "home" returns up by a single level in your UI
         * rather than back to the top level or front page.
         *
         * @param showHomeAsUp true to show the user that selecting home will return one
         *                     level up rather than to the top level of the app.
         */
        private void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
            }
        }
    }

	
}
