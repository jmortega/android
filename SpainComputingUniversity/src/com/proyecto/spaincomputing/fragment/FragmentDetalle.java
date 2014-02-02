package com.proyecto.spaincomputing.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proyecto.spaincomputing.LinkWebActivity;
import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.SendMAILActivity;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
*
* Fragment que define el comportamiento del detalle de la lista una vez se selecciona un elemento 
*
* @author jmortega
*
*/
public class FragmentDetalle extends Fragment {
	
	
		//Elementos Layout para mostrar los datos
		private TextView nombreTextView;
		private TextView descripcionTextView;
		private TextView gradoTextView;
		private TextView tipoTextView;
		private TextView latitudTextView;
		private TextView longitudTextView;
		
		private TextView latitudTextView2;
		private TextView longitudTextView2;
		
		
		private TextView seleccioneTextView;
		private ImageView imagen;
		private ImageView favorito;
		private Button botonLink;
		private Button botonfavorito;
		private Button botonShare;
		private LinearLayout linear;
		
		private UniversidadBean ubBean;
		
		//dialogo compartir
		private int selected = 0;
		private int buffKey  = 0;
		
		protected int currentColor = 0xFF666666;
		
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			
			super.onActivityCreated(savedInstanceState);
			
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		  
			View view= inflater.inflate(R.layout.mostrar_universidad, container, false);

			return view;
			
			
		}
	  
	  
	  
		public void mostrarDetalle(UniversidadBean ub,GoogleMap mapa,int color) {
			
			ubBean=ub;
			
			currentColor=color;
			
			//Recogemos los elementos de la interfaz que nos hacen falta para cargar los datos
	        nombreTextView = (TextView) getView().findViewById(R.id.nombre);
	        descripcionTextView = (TextView) getView().findViewById(R.id.descripcion);
	        gradoTextView = (TextView) getView().findViewById(R.id.grado);
	        tipoTextView = (TextView) getView().findViewById(R.id.tipo);
	        latitudTextView = (TextView) getView().findViewById(R.id.latitud);
	        longitudTextView = (TextView) getView().findViewById(R.id.longitud);
	        
	        latitudTextView2 = (TextView) getView().findViewById(R.id.latitudText);
	        longitudTextView2 = (TextView) getView().findViewById(R.id.longitudText);
	        
	        linear = (LinearLayout) getView().findViewById(R.id.linear2);
	        
	        imagen = (ImageView) getView().findViewById(R.id.imageView_imagen);
	        favorito = (ImageView) getView().findViewById(R.id.image_favorito);
	        
	        seleccioneTextView = (TextView) getView().findViewById(R.id.seleccione);
	        
	        botonLink = (Button) getView().findViewById(R.id.botonLink);
	        botonfavorito = (Button) getView().findViewById(R.id.botonFavorito);
	        botonShare = (Button) getView().findViewById(R.id.botonShare);
	        
	        seleccioneTextView.setVisibility(View.VISIBLE);
	        
	        botonLink.setVisibility(View.GONE);
	        botonShare.setVisibility(View.GONE);
	        nombreTextView.setVisibility(View.GONE);
	        descripcionTextView.setVisibility(View.GONE);
	        tipoTextView.setVisibility(View.GONE);
	        gradoTextView.setVisibility(View.GONE);
	        latitudTextView.setVisibility(View.GONE);
	        longitudTextView.setVisibility(View.GONE);
	        latitudTextView2.setVisibility(View.GONE);
	        longitudTextView2.setVisibility(View.GONE);
	        imagen.setVisibility(View.GONE);

	        linear.setVisibility(View.GONE);
	        
	        //Listeners botones
	        botonLink.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	mostrarLink(ubBean.getEnlace());
	            }
	        });
	        
	        botonfavorito.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	favorito(ubBean.getId(),ubBean.getNombre(),ubBean.getDescripcion());
	            }
	        });
	        
	        botonShare.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	showDialogShareButtonClick(ubBean);
	            }
	        });
	        
	        if(ub!=null){
	        	
			//Cargamos en pantalla los datos
		    if(ub.getNombre()!=null){
		        nombreTextView.setText(ub.getNombre());
		        seleccioneTextView.setVisibility(View.GONE);
		        
		        botonLink.setVisibility(View.VISIBLE);
		        botonShare.setVisibility(View.VISIBLE);
		        nombreTextView.setVisibility(View.VISIBLE);
		        descripcionTextView.setVisibility(View.VISIBLE);
		        gradoTextView.setVisibility(View.VISIBLE);
		        tipoTextView.setVisibility(View.VISIBLE);
		        latitudTextView.setVisibility(View.VISIBLE);
		        longitudTextView.setVisibility(View.VISIBLE);
		        latitudTextView2.setVisibility(View.VISIBLE);
		        longitudTextView2.setVisibility(View.VISIBLE);
		        linear.setVisibility(View.VISIBLE);
		        imagen.setVisibility(View.VISIBLE);
		    }
		    if(ub.getDescripcion()!=null){
		        descripcionTextView.setText(ub.getDescripcion());
		    }
		    if(ub.getTipo()!=null){
		        tipoTextView.setText(ub.getTipo());
		    }
		    if(ub.getGrado()!=null){
		        gradoTextView.setText(ub.getGrado());
		    }
		    if(String.valueOf(ub.getLatitud())!=null){
		        latitudTextView.setText(String.valueOf(ub.getLatitud()));
		    }
		    if(String.valueOf(ub.getLongitud())!=null){
		        longitudTextView.setText(String.valueOf(ub.getLongitud()));
		    }
		    if(Integer.valueOf(ub.getIdImagen())!=null){
		    	imagen.setImageResource(ub.getIdImagen());
		    }
		    
		    //comprobar favorito
		    if(isFavorito(ub.getId())){
		    	
		    	botonfavorito.setVisibility(View.GONE);
		    	
		    	favorito.setVisibility(View.VISIBLE);
		    	
		    }else{
		    	
		    	botonfavorito.setVisibility(View.VISIBLE);
		    	//GONE se muestra invisible y se gana el espacio ocupado por el elemento
		    	favorito.setVisibility(View.GONE);
		    }
		    
		   //Marcador para el mapa
	        mostrarMarcador(ub.getLatitud(),ub.getLongitud(),ub.getNombre(),ub.getDescripcion(),mapa);
	        
	        }
			
		}
		
		private void mostrarMarcador(double lat, double lng,String nombre,String descripcion,GoogleMap mapa)
		{

		    mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nombre));
		    
		    //Centramos el mapa en las coordenadas latitud/longitud y con nivel de zoom 12
			CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12F);
			
			mapa.animateCamera(camera);
		}
		
		public void mostrarLink(String enlace){
		    
				//comprobar conexion a internet
				if(enlace!=null && NetWorkStatus.getInstance(getActivity()).isOnline()){
		    		
		    		String content = enlace.toString();
		    	    Intent intent = new Intent();
		  	      	intent.setClass(getActivity(), LinkWebActivity.class);
		  	      	intent.putExtra(Constants.EXTRA_URL,content);
		  	      	intent.putExtra(Constants.EXTRA_COLOR, currentColor);
		  	      	startActivity(intent);
		    	    
		  	      	
		    	}else{

		            String soporte=getText(R.string.sin_conexion).toString();
		            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		            dialog.setTitle(getText(R.string.sin_conexion).toString());
		            dialog.setMessage(soporte);
		            dialog.show();
		            
		    	}
		  }
		

		public void showDialogShareButtonClick(final UniversidadBean ub) {
		      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		      
		      builder.setTitle(getText(R.string.compartir).toString());
		      
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
		            	  mostrarShare(ub);
		              }
		              if(selected==1){
		            	enviarEmailContactos(ub);
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
		
		public void mostrarShare(UniversidadBean ubBean){
		    
	          List<Intent> targetedShareIntents = new ArrayList<Intent>();
	          Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
	          shareIntent.setType("text/html");
	          List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(shareIntent, 0);
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
		
		 public boolean isFavorito(Integer id){
			    
				boolean isFavorito=false;
			
				//Obtiene el objeto de preferencias de la aplicacion llamado favoritos
				SharedPreferences sharedPreferences = getActivity().getSharedPreferences("favoritos", 0);
				   
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
				  
				  String soporte=getText(R.string.favorito_existe).toString();
		            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		            dialog.setTitle(getText(R.string.favoritos).toString());
		            dialog.setMessage(soporte);
		            dialog.show();
		            
			  }else{
				 
			  String cadena="";

			  //Obtiene el objeto de preferencias de la aplicacion llamado favoritos
			   SharedPreferences sharedPreferences = getActivity().getSharedPreferences("favoritos", 0);
			   
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
			  
			  
			  String soporte=getText(R.string.favorito_ok).toString();
	          AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
	          dialog.setTitle(getText(R.string.favoritos).toString());
	          dialog.setMessage(soporte);
	          dialog.show();
	          
	          SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("appPreferences", 0);
	          
	          //Lanzamos notificacion favoritos dependiendo del valor de preferencias
	          if(sharedPreferences2.getBoolean("pref_notificaciones_favoritos",true)){
	        	  com.proyecto.spaincomputing.utils.StatusBarNotify.getInstance(getActivity()).statusBarNotify("nuevo_favorito",nombre,descripcion);
	      	  }
	          
	          
			}
			    
				
		  }
		  
		  public void enviarEmailContactos(UniversidadBean ub) {
			
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
		     intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
		     intent.setClass(getActivity(), SendMAILActivity.class);
		     startActivity(intent);
		  }
		  
		  public void setColor(int color){
			  
			  this.currentColor=color;
		  }

}