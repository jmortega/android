package com.proyecto.spaincomputing;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.proyecto.spaincomputing.utils.ImportExportAsyncTask;
import com.proyecto.spaincomputing.utils.ImportExportAsyncTask.ImportExportAsyncTaskResponder;
import com.proyecto.spaincomputing.utils.MessageBar;


/**
 * Actividad que muestra un combo con la lista de lugares registrados
 */
public class ExportImportActivity extends ActionMainActivity implements MessageBar.OnMessageClickListener{

    //Contexto
    Context context = this;

    private ProgressDialog dialog;

    private MessageBar mMessageBar;
    
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){
			 
		 
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			    ab.setTitle(getString(R.string.exportar_importar));
			 }
		 
		 }
		 
        
        //Activamos el modo fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.export_import);

        mMessageBar = new MessageBar(this);
        mMessageBar.setOnClickListener(this);

        findViewById(R.id.botonExportar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageBar.show("Ha seleccionado la opcion de exportar");
            	exportar();
            }
        });
        
        findViewById(R.id.botonImportar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageBar.show("Ha seleccionado la opcion de importar");
            	importar();
            }
        });
        
    }
   
 
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            return super.dispatchKeyEvent(event);
    }
    
    /**
     * Comportamiento de la tecla ATRAS del terminal
     * 
     * @keyCode int Codigo de la tecla pulsada
     * @event KeyEvent Evento que ha provocado la llamada al metodo
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
     
    /**
     * Se muestran las opciones de menú de la lista de lugares
     * 
     * @param menu
     *            Objeto menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_export_import, menu);
        return true;
    }
    
    /**
     * Definimos las acciones correspondientes con cada opción de menú
     * 
     * @param item
     *            MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
        switch (item.getItemId()) {
        
        	case android.R.id.home: 
	  		
        		Intent intent2 = new Intent(this, PrincipalActivity.class);
        		intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		startActivity(intent2);

        		
            break;
               
            case R.id.btnExportar:
                
                exportar();
                break;

            case R.id.btnImportar:
                
                importar();
                break;

                
            case R.id.btnInfo:

                AlertDialog.Builder ab=new AlertDialog.Builder(ExportImportActivity.this);
                ab.setTitle(R.string.acerca_de_export_import);
                ab.setIcon(getResources().getDrawable(R.drawable.icon));
                ab.setMessage(R.string.exportar_importar_info);
                ab.setPositiveButton(R.string.Aceptar,null);
                ab.show();
                
                break;

                
            case R.id.btnRecargarLugares:
    
                
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MapaActivity.class);
                startActivity(intent);
                finish();
                break;
               
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Metodo que exporta la BD
     */
     public void exportarDB(View v) {
             exportar();
             
     }
     
     /**
      * Metodo que exporta la BD
      */
      public void importarDB(View v) {
              importar();
              
      }
      
      /**
       * Metodo que recarga los lugares
       */
       public void recargarLugares(View v) {
           
           Intent intent = new Intent();
           intent.setClass(getApplicationContext(), MapaActivity.class);
           startActivity(intent);
           finish();
               
       }
     
      /**
       * Metodo que muestra la info sobre export/import
       */
       public void info(View v) {
           
           AlertDialog.Builder ab=new AlertDialog.Builder(ExportImportActivity.this);
           ab.setTitle(R.string.acerca_de_export_import);
           ab.setIcon(getResources().getDrawable(R.drawable.icon));
           ab.setMessage(R.string.exportar_importar_info);
           ab.setPositiveButton(R.string.Aceptar,null);
           ab.show();
       }
       
    /**
     * Exportar la base de datos
     */
    private void exportar() {

            dialog = ProgressDialog.show(ExportImportActivity.this, "", getString(R.string.procesando), true);

            ImportExportAsyncTaskResponder imporExportAsyncTaskResponder = new ImportExportAsyncTaskResponder() {
                    public void backupLoaded(Boolean resultado) {

                            if (resultado != null && resultado) {

                                    dialog.dismiss();
 
                                    AlertDialog.Builder ab=new AlertDialog.Builder(ExportImportActivity.this);
                                    ab.setTitle(R.string.exportar);
                                    ab.setIcon(getResources().getDrawable(R.drawable.icon));
                                    ab.setMessage(R.string.exportar_ok);
                                    ab.setPositiveButton(R.string.Aceptar,null);
                                    ab.show();

                            } else {
                                    dialog.dismiss();
  
                                    AlertDialog.Builder ab=new AlertDialog.Builder(ExportImportActivity.this);
                                    ab.setTitle(R.string.exportar);
                                    ab.setIcon(getResources().getDrawable(R.drawable.icon));
                                    ab.setMessage(R.string.exportar_error);
                                    ab.setPositiveButton(R.string.Aceptar,null);
                                    ab.show();
                            }
                    }
            };

            new ImportExportAsyncTask(imporExportAsyncTaskResponder).execute("exportar");

    }

    /**
     * Importar la base de datos
     */
    private void importar() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.importar_pregunta)).setCancelable(false).setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                            procederImportacion();

                    }
            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                    }
            });
            AlertDialog alert = builder.create();

            alert.show();

    }
    
    /**
     * Recuperar
     */
    private void procederImportacion() {

            dialog = ProgressDialog.show(ExportImportActivity.this, "", getString(R.string.procesando), true);

            ImportExportAsyncTaskResponder importExportAsyncTaskResponder = new ImportExportAsyncTaskResponder() {
                    public void backupLoaded(Boolean resultado) {

                            if (resultado != null && resultado) {

                                    dialog.dismiss();
                                    
                                    AlertDialog.Builder ab=new AlertDialog.Builder(ExportImportActivity.this);
                                    ab.setTitle(R.string.importar);
                                    ab.setIcon(getResources().getDrawable(R.drawable.icon));
                                    ab.setMessage(R.string.importar_ok);
                                    ab.setPositiveButton(R.string.Aceptar,null);
                                    ab.show();
                                    
                                    

                            } else {
                                    // Error al recuperar datos

                                    dialog.dismiss();

                                    AlertDialog.Builder ab=new AlertDialog.Builder(ExportImportActivity.this);
                                    ab.setTitle(R.string.importar);
                                    ab.setIcon(getResources().getDrawable(R.drawable.icon));
                                    ab.setMessage(R.string.importar_error);
                                    ab.setPositiveButton(R.string.Aceptar,null);
                                    ab.show();

                            }
                    }
            };

            new ImportExportAsyncTask(importExportAsyncTaskResponder).execute("importar");

    }
    
	@Override
	public void onMessageClick(Parcelable token) {
		// TODO Auto-generated method stub
		
	}


    
    
}
