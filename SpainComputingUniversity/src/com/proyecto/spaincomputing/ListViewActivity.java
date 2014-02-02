package com.proyecto.spaincomputing;


import java.util.ArrayList;
import java.util.StringTokenizer;

import com.proyecto.spaincomputing.actionbar.FadingActionBarHelper;
import com.proyecto.spaincomputing.adapter.UniversidadAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class ListViewActivity extends ActionMainActivity {

	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	
	//dialogo compartir
	private int selected = 0;
	private int buffKey  = 0;
	  
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Drawable colorDrawable = new ColorDrawable(currentColor);
		Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });
		
        FadingActionBarHelper helper = new FadingActionBarHelper()
        	.actionBarBackground(layerDrawable)
        	.headerLayout(R.layout.header)
            .contentLayout(R.layout.activity_listview);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        ListView listView = (ListView) findViewById(android.R.id.list);
        
        inicializarDatos();

        listView.setAdapter(new UniversidadAdapter(this,listado));
		  
		registerForContextMenu(listView);

    }

    
    public  ArrayList<UniversidadBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  
		  return listado;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

  	  if(listado.size()>0){
  		  
            menu.setHeaderTitle(R.string.accion);

            menu.add(0, Constants.MENU_ENLACE, 0, getResources().getText(R.string.link));

            menu.add(0, Constants.MENU_RUTA, 1, getResources().getText(R.string.ruta_google));

            menu.add(0, Constants.MENU_MAPA, 2, getResources().getText(R.string.mapa));

            menu.add(0, Constants.MENU_FAVORITO, 3, getResources().getText(R.string.anyadir_favorito));

            menu.add(0, Constants.MENU_COMPARTIR, 4, getResources().getText(R.string.compartir));
            
            menu.add(0, Constants.MENU_PANORAMIO, 5, getResources().getText(R.string.panoramio));
  	  }
    }
    
    /**
     * Gestionamos la pulsacion de un menu contextual
     */
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

            UniversidadBean ub = (UniversidadBean) getUniversidad((int) info.id);
            
            switch (item.getItemId()) {
            
            case Constants.MENU_ENLACE:
                
            	if(ub.getEnlace()!=null){
          			onUniversidadLink(ub.getEnlace());
          	
            	}

                return true;

            case Constants.MENU_RUTA:


            	if(Double.valueOf(ub.getLatitud())!=null && Double.valueOf(ub.getLongitud())!=null){
            		rutaGoogleNavigation(ub.getLatitud(),ub.getLongitud());
            	}

                return true;
                
            case Constants.MENU_MAPA:

          	   onUniversidadMapa(ub.getLatitud(),ub.getLongitud(),ub.getNombre(),ub.getDescripcion(),ub.getIdImagen(),"");

                  return true;
                  
            case Constants.MENU_COMPARTIR:

          	  showDialogShareButtonClick(ub);

             return true;
             
            case Constants.MENU_FAVORITO:

            	   onUniversidadFavorito(ub);

                return true;
                
            case Constants.MENU_PANORAMIO:

          	    
            	panoramio(ub.getLatitud(),ub.getLongitud());
            	
            	return true;

            default:
                    return super.onContextItemSelected(item);
            }
         
    }
    
    
    private void showDialogShareButtonClick(final UniversidadBean ub) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle(getText(R.string.compartir).toString());
        
        final CharSequence[] choiceList = { getText(R.string.aplicacion).toString(),getText(R.string.contactos).toString(),getText(R.string.contactosSearch).toString() };

        builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                buffKey = which;
            }
        }).setCancelable(false).setPositiveButton(getText(R.string.Aceptar).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // set buff to selected
                selected = buffKey;
                if(selected==0){
                	onUniversidadCompartir(ub);
                }
                if(selected==1){
                	onUniversidadContactos(ub);
                }
                if(selected==2){
                	onUniversidadContactosSearch(ub);
                }
            }
        }).setNegativeButton(getText(R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public void rutaGoogleNavigation(Double latitud,Double longitud){
        
        //lanzar la aplicacion "navigation" pasando como parametros las coordenadas del punto al que deseas ir

        Intent intentNavigate = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +latitud+","+longitud));

        intentNavigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        startActivity(intentNavigate);

    }
    
    public UniversidadBean getUniversidad(int position){
      	UniversidadBean universidad=new UniversidadBean();
          
          if(listado!=null && listado.size()>0){
          	universidad=listado.get(position);
          }
          
          return universidad;
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
  
  public void panoramio(Double latitud,Double longitud){
	    
	  Intent intent = new Intent();
      intent.setClass(getApplicationContext(), PanoramioActivity.class);
      String url="http://www.panoramio.com/map/#lt="+latitud.toString()+"&ln="+longitud.toString()+"&z=1";
      intent.putExtra(Constants.EXTRA_URL,url);
      intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
      startActivity(intent);
  }
  
}
