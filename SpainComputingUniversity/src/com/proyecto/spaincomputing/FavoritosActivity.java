package com.proyecto.spaincomputing;


import java.util.StringTokenizer;

import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.UniversidadesFragmentFavorito;
import com.proyecto.spaincomputing.utils.Constants;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.widget.Toast;


public class FavoritosActivity extends ActionMainActivity{
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		
		if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				 ab.setTitle(getString(R.string.favoritos));
			 }
		 
		 }
		setContentView(R.layout.listado_activity_favorito);
		
		UniversidadesFragmentFavorito listado=(UniversidadesFragmentFavorito)getSupportFragmentManager().findFragmentById(R.id.FrgListadoFavorito);

		listado.setColor(currentColor);
		
		listado.setUniversidadFavoritos(true);
		
		listado.setUniversidadListener(this);
	}
	
	@Override
	public void onUniversidadFavorito(final UniversidadBean ub) {
		// TODO Auto-generated method stub
		
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.eliminar_favorito)
        .setMessage(R.string.confirmar_eliminar_favorito)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            	eliminarFavorito(ub.getId(),ub.getNombre(),ub.getDescripcion());
            }

        })
        .setNegativeButton(R.string.cancel, null)
        .show();
		
		
	}
	
	
	public void eliminarFavorito(Integer id,String nombre,String descripcion){

		   String cadena="";
		  
		  //Obtiene el objeto de preferencias de la aplicacion llamado favoritos
		   SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("favoritos", 0);
		   
		  //Obtiene un string almacenado en las preferencias de nombre favoritos.
		  //El segundo parametro indica el valor a devolver si no lo encuentra, en este caso, ""
		  String favoritos = sharedPreferences.getString("favoritos", "");

		  //Obtenemos el editor de las preferencias.
		  SharedPreferences.Editor editor = sharedPreferences.edit();

		  StringTokenizer tokens=new StringTokenizer(favoritos,"/");

		  while(tokens.hasMoreTokens()){
				
			  			String token=tokens.nextToken().toString();
			  			
						if(token.equals(id.toString())){

							
						}else{
						
							cadena+="/"+token;
							
						}
						
			}
		  

		  //Le indicamos que queremos que almacene un string de nombre favoritos con valor cadena
		  editor.putString("favoritos", cadena);
		   
		  //Tras haber indicado todos los cambios a realizar, le indicamos al editor que los almacene en las preferencias.
		  editor.commit();
		  
		  
		  Toast toast = Toast.makeText(FavoritosActivity.this, getText(R.string.eliminada_favoritos), Toast.LENGTH_LONG);
	      toast.show();
	      
	      SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("appPreferences", MODE_PRIVATE);
	     
	      /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

	      Boolean valor = prefs.getBoolean("pref_notificaciones_favoritos",true);
	      if(valor){
	    	  com.proyecto.spaincomputing.utils.StatusBarNotify.getInstance(getApplicationContext()).statusBarNotify("eliminado_favorito",nombre,descripcion);
	      }*/
	      
	      //Lanzamos notificacion favoritos dependiendo del valor de preferencias
	      if(sharedPreferences2.getBoolean("pref_notificaciones_favoritos",true)){
	    	  com.proyecto.spaincomputing.utils.StatusBarNotify.getInstance(getApplicationContext()).statusBarNotify("eliminado_favorito",nombre,descripcion);
	  	  }
	      
	      
	      Intent intent = new Intent();
	      intent.setClass(getApplicationContext(), FavoritosActivity.class);
	      intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	      startActivity(intent);
	      
	      finish();
	      
		}

}
