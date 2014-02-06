package com.proyecto.spaincomputing;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.proyecto.spaincomputing.fragment.AboutFragment;
import com.proyecto.spaincomputing.fragment.PlacesListFragment;
import com.proyecto.spaincomputing.fragment.PortadaFragment;
import com.proyecto.spaincomputing.fragment.UniversidadesImagesFragment;
import com.proyecto.spaincomputing.fragment.UniversityListFragment;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Actividad principal que muestra la pantalla de inicio
 */
public class PrincipalActivity extends ActionMainActivity{
    
    private Dialog dialogo;
    
    private ListView drawerList;
    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    
    
    
    //new UniversityListFragment()
    Fragment[] fragments = new Fragment[]{new PortadaFragment(),new UniversityListFragment(),new UniversidadesImagesFragment(),new PlacesListFragment(),new AboutFragment()};
    
    FragmentManager manager;
    
    /**
     * Crea la vista y los componentes
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Activamos el modo fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
        
        setContentView(R.layout.main);
       
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        
       // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                                       R.layout.drawer_list_item, 
                                                       drawerOptions));
        
        drawerList.setItemChecked(0, true);
        
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open, //textos para accesibilidad
                R.string.drawer_close
                ) {

            public void onDrawerClosed(View view) {
            	ActivityCompat.invalidateOptionsMenu(PrincipalActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
            	ActivityCompat.invalidateOptionsMenu(PrincipalActivity.this);
            }
        };
        
        //listener
        drawerLayout.setDrawerListener(drawerToggle);   
        drawerToggle.setDrawerIndicatorEnabled(true);


        manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.contentFrame, fragments[0])
        		.add(R.id.contentFrame, fragments[1])
        		.add(R.id.contentFrame, fragments[2])
        		.add(R.id.contentFrame, fragments[3])
        		.add(R.id.contentFrame, fragments[4])
        	    .commit();
        
        manager.beginTransaction().show(fragments[0]).commit();
        manager.beginTransaction().hide(fragments[1]).commit();
        manager.beginTransaction().hide(fragments[2]).commit();
        manager.beginTransaction().hide(fragments[3]).commit();	
        manager.beginTransaction().hide(fragments[4]).commit();	
    }
    
    
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        manager = getSupportFragmentManager();
        manager.beginTransaction().show(fragments[0]).commit();
        manager.beginTransaction().hide(fragments[1]).commit();
        manager.beginTransaction().hide(fragments[2]).commit();
        manager.beginTransaction().hide(fragments[3]).commit();	
        manager.beginTransaction().hide(fragments[4]).commit();	
    
    }
    
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        manager = getSupportFragmentManager();
        manager.beginTransaction().show(fragments[0]).commit();
        manager.beginTransaction().hide(fragments[1]).commit();
        manager.beginTransaction().hide(fragments[2]).commit();
        manager.beginTransaction().hide(fragments[3]).commit();	
        manager.beginTransaction().hide(fragments[4]).commit();	
    
    }
    
    /**
     * Se muestran las opciones de menu de la pantalla principal
     * @param menu Objeto menu 
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        	setContent(position);
        }
    }
   
    public void setContent(int index) {

		final ActionBar actionBar = getSupportActionBar();

		 // Insert the fragment by replacing any existing fragment
		FragmentManager manager = getSupportFragmentManager();
		
		switch (index) {
			case 0:

				manager.beginTransaction().show(fragments[1]).commit();
				manager.beginTransaction().hide(fragments[0]).commit();
		        manager.beginTransaction().hide(fragments[2]).commit();
		        manager.beginTransaction().hide(fragments[3]).commit();
		        manager.beginTransaction().hide(fragments[4]).commit();
		        
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				break;
			case 1:
	
				manager.beginTransaction().show(fragments[2]).commit();
				manager.beginTransaction().hide(fragments[0]).commit();
		        manager.beginTransaction().hide(fragments[1]).commit();
		        manager.beginTransaction().hide(fragments[3]).commit();
		        manager.beginTransaction().hide(fragments[4]).commit();
		        
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);				
				break;
				
			case 2:
				manager.beginTransaction().show(fragments[3]).commit();
				manager.beginTransaction().hide(fragments[0]).commit();
		        manager.beginTransaction().hide(fragments[1]).commit();
		        manager.beginTransaction().hide(fragments[2]).commit();
		        manager.beginTransaction().hide(fragments[4]).commit();
		        
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);				
				break;
				
			case 3:
				manager.beginTransaction().show(fragments[4]).commit();
				manager.beginTransaction().hide(fragments[2]).commit();
				manager.beginTransaction().hide(fragments[0]).commit();
		        manager.beginTransaction().hide(fragments[1]).commit();
		        manager.beginTransaction().hide(fragments[3]).commit();
		        
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);				
				break;
		}
		
		

		// Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(index, true);
        setTitle(drawerOptions[index]);
        drawerLayout.closeDrawer(drawerList);
	}
    

    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	
        drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		
		//sincronizar estado del drawer toogle
		drawerToggle.syncState();
	}

	/**
     * Definimos las acciones correspondientes con cada opci�n de men�
     * @param item MenuItem 
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (item.getItemId() == android.R.id.home) {
         	 
            if (drawerLayout.isDrawerOpen(drawerList)) {
            	drawerLayout.closeDrawer(drawerList);
            } else {
            	drawerLayout.openDrawer(drawerList);
            }
        
            return true;
        }
        
		if (itemId == R.id.acerca) {
			dialogo = onCreateDialog(Constants.DIALOGO_INFO);
			return true;
		} else if (itemId == R.id.salir) {
			dialogo = onCreateDialog(Constants.DIALOGO_SALIR);
			dialogo.show();
			return true;
		} else if (itemId == R.id.cambiarIdioma) {
			cambiarIdioma();
			return true;
		} else if (itemId == R.id.btnVerMapa) {
			verMapa();
			return true;
		}
		else if (itemId == R.id.btnListado) {
			verListado();
			return true;
		}
		else if (itemId == R.id.miPosicion) {
			miPosicion();
			return true;
		}
		else if (itemId == R.id.btnFavoritos) {
			verFavoritos();
			return true;
		}
		else if (itemId == R.id.conexion) {
			checkConexion();
			return true;
		}
		else if (itemId == R.id.listadoGridView) {
			listadoGridView();
			return true;
		}
		else if (itemId == R.id.listadoGridView2) {
			listadoGridView2();
			return true;
		}
		else if (itemId == R.id.coverFlowView) {
			coverFlowView();
			return true;
		}
		else if (itemId == R.id.preferencias) {
			preferenciasView();
			return true;
		}
		else if (itemId == R.id.SpinnerView) {
			spinnerView();
			return true;
		}
		else if (itemId == R.id.tabsView) {
			tabsView();
			return true;
		}
		else if (itemId == R.id.exportImportView) {
			ExportImportView();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
    }
    

    /**
     * Comportamiento de la tecla ATRAS del terminal
     * @keyCode int Codigo de la tecla pulsada
     * @event KeyEvent Evento que ha provocado la llamada al metodo
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialogo = onCreateDialog(Constants.DIALOGO_SALIR);
            dialogo.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog newDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
        Animation animShow = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        final Animation animHide = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        switch (id) {
            case Constants.DIALOGO_SALIR:
                builder.setMessage(R.string.salir);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                         finish();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                newDialog = builder.create();
                break;

            case Constants.DIALOGO_INFO:
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.info,null);
       
                layout.startAnimation(animShow);

                builder.setNeutralButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        layout.startAnimation(animHide);
                    }
                });
                
                
                builder.setView(layout);
                newDialog = builder.create();
                
                newDialog.show();
                break;
        }

        return newDialog;

    }
    
    
    public void miPosicion() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), PosicionActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }
    
    public void listadoGridView2() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), GridView2Activity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }

    public void listadoGridView() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), GridViewActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }


    public void verMapa() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapaActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }
    
    public void verListado() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ListadoActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }
    
    public void verListado2() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ListViewActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }
    
    public void verFavoritos() {
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), FavoritosActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
        
    }
    
    public void preferenciasView(){
    	
    	Intent intent = new Intent();
        intent.setClass(getApplicationContext(), PreferenciasActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
    }
    
    public void spinnerView(){
    	
    	Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SpinnerViewActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
    }
    
    public void tabsView(){
    	
    	Intent intent = new Intent();
        intent.setClass(getApplicationContext(), TabsActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
    }
    
    public void ExportImportView(){
    	
    	Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ExportImportActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
    }
    
    public void coverFlowView(){
    	
    	Intent intent = new Intent();
        intent.setClass(getApplicationContext(), CoverFlowActivity.class);
        intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
        startActivity(intent);
    }

    public void cambiarIdioma() {
        
   
        Toast toast = Toast.makeText(PrincipalActivity.this, getText(R.string.idiomas_soportados).toString(), Toast.LENGTH_LONG);
        toast.show();
        
        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS),Constants.ACTIVIDAD_IDIOMA);

    }
    
    public void checkConexion() {
        
        
    	if(NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
    		
            String soporte=getText(R.string.conexion_internet_ok).toString();
            String ipv4=NetWorkStatus.getInstance(getApplicationContext()).getLocalIpv4Address();
            String ipv6=NetWorkStatus.getInstance(getApplicationContext()).getLocalIpv6Address();
            String conecctionType=NetWorkStatus.getInstance(getApplicationContext()).getConnectionType();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getText(R.string.conexion).toString());
            if(ipv4==null && ipv6==null){
            	dialog.setMessage(soporte+"\n\n"+conecctionType);
            }
            else if(ipv4!=null && ipv6!=null){
            	dialog.setMessage(soporte+"\n\n"+"IPV4 "+ipv4+"\nIPV6 "+ipv6+"\n\n"+conecctionType);
            }
            else if(ipv4!=null && ipv6==null){
            	dialog.setMessage(soporte+"\n\n"+"IPV4 "+ipv4+"\n\n"+conecctionType);
            }
            else if(ipv4==null && ipv6!=null){
            	dialog.setMessage(soporte+"\n\n"+"IPV6 "+ipv6+"\n\n"+conecctionType);
            }
            dialog.show();
    	    
    	}
		else{
			
			String soporte=getText(R.string.sin_conexion).toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getText(R.string.conexion).toString());
            dialog.setMessage(soporte);
            dialog.show();
            
    	}
        
    }
    
	
    /**
     * Metodo que gestiona las respuestas de las actividades
     */
     @Override 
     public void onActivityResult(int requestCode, int resultCode, Intent data) {     
       super.onActivityResult(requestCode, resultCode, data);
       
       switch(requestCode) { 
         case Constants.ACTIVIDAD_IDIOMA: 

                 Intent intent = new Intent();
                 intent.setClass(getApplicationContext(), PrincipalActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
                 startActivity(intent);
             
             break;
       }
     }

     @Override
     public boolean dispatchKeyEvent(KeyEvent event) {
             return super.dispatchKeyEvent(event);
     }
     
     
}