package com.proyecto.spaincomputing;

import com.proyecto.spaincomputing.utils.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Actividad que muestra una lista con los contactos del telefono
 */

@SuppressLint("NewApi")
public class MostrarContactosActivity extends ListActivity {
    
        
    private Cursor mCursor;
    
    private ListAdapter adapter;
    
    /** Called when the activity is first created. */
		@SuppressWarnings("deprecation")
		@Override
          public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.lista_contactos);
              
              String contactos="";
              
              //obtenemos datos para mostrar en el mail
              Bundle extras = getIntent().getExtras();
              if(extras != null) {
                  contactos=extras.getString("contactos");
              }
              
              if(contactos!=null && contactos.equals("mail")){
                  
                  setTitle(getText(R.string.seleccione_email).toString());
                  

                  if(android.os.Build.VERSION.SDK_INT<11){
                	  
                	  /// Consulta: contactos con email ordenados por nombre
                	  
                	  //before honeycomb
                      mCursor = getContentResolver().query(
                              Data.CONTENT_URI,
                              new String[] { Data._ID, Data.DISPLAY_NAME, Email.DATA1,Phone.TYPE },
                              Data.MIMETYPE + "='" + Email.CONTENT_ITEM_TYPE + "' AND "+ Email.DATA1 + " IS NOT NULL", null,
                              Data.DISPLAY_NAME + " ASC");

                	  
                  }else{
                  
                	  //after honeycomb
                	 CursorLoader cursorLoader=new CursorLoader(this,
                			 Data.CONTENT_URI,
                             new String[] { Data._ID, Data.DISPLAY_NAME, Email.DATA1,Phone.TYPE },
                             Data.MIMETYPE + "='" + Email.CONTENT_ITEM_TYPE + "' AND "+ Email.DATA1 + " IS NOT NULL", null,
                             Data.DISPLAY_NAME + " ASC");
                	 
                	 mCursor=cursorLoader.loadInBackground();
                	 
                  }
                  
           
                  //Lista de contactos
                  if(android.os.Build.VERSION.SDK_INT<11){
                	  
                	  //before honeycomb
                	  adapter = new SimpleCursorAdapter(this, // context
                              android.R.layout.simple_list_item_2, // Layout para las filas
                              mCursor, // cursor
                              new String[] { Data.DISPLAY_NAME, Phone.NUMBER }, //COLUMNS
                              new int[] { android.R.id.text1, android.R.id.text2 } //views
                              ); 
                	  
                  }else{
                  
                	  //after honeycomb
                	  adapter = new SimpleCursorAdapter(this, // context
                          android.R.layout.simple_list_item_2, // Layout para las filas
                          mCursor, // cursor
                          new String[] { Data.DISPLAY_NAME, Phone.NUMBER }, //COLUMNS
                          new int[] { android.R.id.text1, android.R.id.text2 }, //views
                          CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER); //flag 
                  }
                  
                  
                  if(mCursor!=null && mCursor.getCount()>0){
                      setTitle(mCursor.getCount()+" "+getText(R.string.contactos_encontrados).toString()+"\n"+getText(R.string.seleccione_email).toString());

                      mostrarToast(getText(R.string.seleccione_email).toString());
                  }
                  
                  if(adapter!=null){
                	  setListAdapter(adapter);
                  }
                  
                
                  
              }

         }
      
          @Override
          protected void onListItemClick(ListView l, View v, int position, long id) {
              Intent result = new Intent();
      
              
              // Obtiene los datos de la posicion seleccionada
              Cursor c = (Cursor) getListAdapter().getItem(position);
              int colIdx = c.getColumnIndex(Phone.NUMBER);
              String phone = c.getString(colIdx);
      
             // Guardamos el telefono en un extra para tenerlo disponible en la actividad llamante
              result.putExtra(Constants.EXTRA_EMAIL, phone);
              setResult(Activity.RESULT_OK, result);
       
              // Cierra la actividad y devuelve el control al llamador
              finish();
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

}
