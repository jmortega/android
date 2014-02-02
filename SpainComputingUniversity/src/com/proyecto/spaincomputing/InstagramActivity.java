package com.proyecto.spaincomputing;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.proyecto.spaincomputing.adapter.InstagramImageAdapter;
import com.proyecto.spaincomputing.entity.InstagramImage;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.Helper;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

public class InstagramActivity extends ActionMainActivity  {

	public static RequestQueue requestQueue;
	  
	private GridView gridview;
	private ImageView imageview;
	
	private String tag="";
	private String url="";
	
	private InstagramImageAdapter adapter;
	private ArrayList<InstagramImage> imagesArray;
	
	AlertDialog.Builder dialog;
	//dialogo compartir
	private int selected = 0;
	private int buffKey  = 0;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_instagram);
		
		requestQueue = Volley.newRequestQueue(this);
		
		dialog = new AlertDialog.Builder(this);
		 
		ActionBar ab = getSupportActionBar();
		 
		
		//Recogemos la url que se nos proporcionar a traves del intent de llamada
	       Bundle extras = getIntent().getExtras();
	       if(extras != null) {
	           tag = extras.getString(Constants.EXTRA_TAG);
	       }
	       
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

					ab.setTitle(getString(R.string.instagram)+" "+tag);
			 }
		 }	 
			 
        //Inicializar componentes interfaz
       
        imagesArray=new ArrayList<InstagramImage>();
        adapter=new InstagramImageAdapter(this,imagesArray);
        
	    gridview = (GridView) findViewById(R.id.gridInstagram);
	    imageview = (ImageView) findViewById(R.id.instagramImageView);
	    imageview.setVisibility(View.GONE);
	    gridview.setAdapter(adapter);	

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

	            InstagramImage instragamImage=imagesArray.get(position);
	            
	            if(instragamImage!=null){
	            	
	            	mostrarLink(instragamImage.getLink());
	            	
	            	mostrarToast(instragamImage.getLink());
	            }

	        }


	    });
	    
	    
	    tag=normalizeTag(tag);
	    
	    APICallInstagram(tag);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_compartir_json, menu);
		
		return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  	NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
       
       break;
       
	  	case R.id.action_share_json:


          	share(url);
          	

           break;
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }
	


	public void APICallInstagram(String tag) {
		url = Helper.getRecentUrl(tag);
		findViewById(R.id.progressBarInstagram).setVisibility(View.VISIBLE);
		
		//Callback que se ejecuta una vez ha terminado la peticion
	    Response.Listener<JSONObject> successListener = 
	    		new Response.Listener<JSONObject>() {
		            @Override
		            public void onResponse(JSONObject response) {
		            	findViewById(R.id.progressBarInstagram).setVisibility(View.GONE);
						findViewById(R.id.gridInstagram).setVisibility(View.VISIBLE);
						findViewById(R.id.url_instagram).setVisibility(View.VISIBLE);
						Log.e("RESPONSE",response.toString());
						
						JSONArray data;
						try {
							data = response.getJSONArray("data");
			            	for (int i = 0; i < data.length(); i++) {
			            		JSONObject currentElement = data.getJSONObject(i);
			            		String type = currentElement.getString("type");
			            		if (type.equals("image")) {
			            			JSONObject user = currentElement.getJSONObject("user");
			            			JSONObject images = currentElement.getJSONObject("images");
			            			JSONObject standardResolution = images.getJSONObject("standard_resolution");
			            			String link = currentElement.getString("link");
			            			
			            			String imgUrl = standardResolution.getString("url");
			            			String userName = user.getString("username");
			            			
			            			InstagramImage image = new InstagramImage();
			            			if(imgUrl!=null){
			            				image.setImgUrl(imgUrl);
			            			}
			            			
			            			if(userName!=null){
			            				image.setUserName(userName);
			            			}
			            			
			            			if(link!=null){
			            				image.setLink(link);
			            			}
			            			
			            			imagesArray.add(image);
			            		}
			            	}	
			            	adapter.notifyDataSetChanged();

			            	//notificaciÛn carga completada
			            	showNotificationLoadComplete();
			            	
						} catch (JSONException e) {
							Log.e("ERROR",Log.getStackTraceString(e));
							
							dialog.setTitle(getText(R.string.cargando_imagenes).toString());
							String soporte=getText(R.string.error_cargando_imagenes_JSON).toString();
							dialog.setMessage(imagesArray.size()+" "+soporte);
							dialog.show();
							
							InstagramImage image = new InstagramImage();
	            			image.setImgUrl("http://vaho.ws/blog/wp-content/uploads/2012/12/Atencion.jpg");

							imagesArray.add(image);
						}
						
						
				       
						if(imagesArray.size()>0){
							String soporte="";
							dialog.setTitle(getText(R.string.cargando_imagenes).toString());
							
							if(imagesArray.size()==1){
								soporte=getText(R.string.image).toString();
								
							}else{
								soporte=getText(R.string.imagenes).toString();
							}
							dialog.setMessage(imagesArray.size()+" "+soporte);
							dialog.show();
						}else{
							
							String soporte="";
							dialog.setTitle(getText(R.string.cargando_imagenes).toString());
			
							soporte=getText(R.string.no_datos).toString();
					
							dialog.setMessage(soporte);
							dialog.show();
							
							imageview.setVisibility(View.VISIBLE);
						}

		            }
	    };
	
	    //Petcion GET mediate Volley a la url de instagram
	    Log.i("URL",url);
	    
	    TextView texto=(TextView)findViewById(R.id.url_instagram);
        texto.setText(url);
        
        ErrorListener errorListener =new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
				Log.e("ERROR IN VOLLEY REQUEST ",error.getMessage());
			}
        	
		};
		
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, successListener,errorListener);
		
		requestQueue.add(jsObjRequest);	
		
		
	}
	
	
	public void showNotificationLoadComplete() {

	     com.proyecto.spaincomputing.utils.StatusBarNotify.getInstance(getApplicationContext()).
	     statusBarNotify(getString(R.string.txt_notification_title),getString(R.string.txt_notification_title),getString(R.string.txt_notification_title2));
	  	  
	}
	
	/**
	 * FunciÛn que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public static String normalizeTag(String tag) {
	    // Cadena de caracteres original a sustituir.
	    String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
	    // Cadena de caracteres ASCII que reemplazar·n los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = tag;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	        output = output.replace(" ","");
	        output = output.replace("-","");
	    }
	    return output;
	}
	
	public void share(String link){
	    
		  showDialogShareButtonClick(link);
	}
	
	public void showDialogShareButtonClick(final String url) {
	      AlertDialog.Builder builder = new AlertDialog.Builder(this);
	      
	      builder.setTitle(getText(R.string.compartir_url_json).toString());
	      
	      final CharSequence[] choiceList = { getText(R.string.aplicacion).toString(),getText(R.string.contactos).toString() };

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
	            	  mostrarShare(url);
	              }
	              if(selected==1){
	            	enviarEmailContactos(url);
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
	
	public void mostrarShare(String url){
	    
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        List<ResolveInfo> resInfo = this.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                targetedShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                targetedShareIntent.setType("text/html");
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Spain COmputing UNiversity");

                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(url));
                
                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);


            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size()-1), getText(R.string.compartir));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

            startActivity(chooserIntent);
        }
        
	}
	
	public void enviarEmailContactos(String url) {
		
		 Intent intent = new Intent();
	     intent.putExtra(Constants.EXTRA_DATOS,url);
	     intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	     intent.setClass(this, SendMAILActivity.class);
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
	
	public void mostrarLink(String enlace){
	    
		//comprobar conexion a internet
		if(enlace!=null && NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
    		
    		String content = enlace.toString();
    	    Intent intent = new Intent();
  	      	intent.setClass(getApplicationContext(), LinkWebActivity.class);
  	      	intent.putExtra(Constants.EXTRA_URL,content);
  	      	intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
  	      	startActivity(intent);
    	    
  	      	
    	}else{

            String soporte=getText(R.string.sin_conexion).toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
            dialog.setTitle(getText(R.string.sin_conexion).toString());
            dialog.setMessage(soporte);
            dialog.show();
            
    	}
  }
	
}
