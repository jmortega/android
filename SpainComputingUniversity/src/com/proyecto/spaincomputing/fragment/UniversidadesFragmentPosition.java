package com.proyecto.spaincomputing.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.proyecto.spaincomputing.PanoramioActivity;
import com.proyecto.spaincomputing.R;

import com.proyecto.spaincomputing.adapter.ImagePagerAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.listener.UniversidadListener;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;

public class UniversidadesFragmentPosition extends Fragment implements LocationListener{
	
  static private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
  private UniversidadListener listener;
  
  private ListView lstListado;
 
  private ArrayList<UniversidadBean> listadoAux=new ArrayList<UniversidadBean>();
  
  public static ArrayList<UniversidadBean> getListado() {
	return listado;
}

public static void setListado(ArrayList<UniversidadBean> listado) {
	UniversidadesFragmentPosition.listado = listado;
}

private Button btnSearch;
  private EditText mtxt;
	
  //dialogo compartir
  private int selected = 0;
  private int buffKey  = 0;
  
  private double distanceDouble=0.0;
  private TextView latitudText;
  private TextView longitudText;

  private Location myLocation;
  
  private LocationManager locationManager;
  
  private Boolean inicializarDatosASC=false,inicializarDatosDESC=false;
  
  private UniversidadAdapter adapter; 
  
  protected int currentColor = 0xFF666666;
  
  @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.lista_posicion, container, false);
	}
  
  @Override
  public void onActivityCreated(Bundle state) {
    super.onActivityCreated(state);

	latitudText = (TextView)getView().findViewById(R.id.latitudText);
	longitudText = (TextView)getView().findViewById(R.id.longitudText);

    lstListado = (ListView)getView().findViewById(R.id.LstListado);
    btnSearch = (Button) getView().findViewById(R.id.btnSearch);
	mtxt = (EditText) getView().findViewById(R.id.edSearch);
	mtxt.clearFocus();
	
	if(!inicializarDatosASC && !inicializarDatosDESC){
		  
		listado=inicializarDatos();
		  
	}
	
    if(listado.size()>0){
    	
    lstListado.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
			if (listener!=null) {
				listener.onUniversidadSelected((UniversidadBean)lstListado.getAdapter().getItem(pos));
			}
		}
	});
    
    }
   
    mtxt = (EditText) getView().findViewById(R.id.edSearch);
	mtxt.addTextChangedListener(new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			if (0 != mtxt.getText().length()) {
				String spnId = mtxt.getText().toString();
				setSearchResult(spnId);
			} else {
				listado=inicializarDatos();
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			if (0 != mtxt.getText().length()) {
				String spnId = mtxt.getText().toString();
				setSearchResult(spnId);
			} else {
				listado=inicializarDatos();
			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (0 != mtxt.getText().length()) {
				String spnId = mtxt.getText().toString();
				setSearchResult(spnId);
			} else {
				listado=inicializarDatos();
			}
		}
	});
	
	btnSearch.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {

			mtxt.setText("");
			setSearchResult("");
			mtxt.clearFocus();
		}
	});
	
	comenzarLocalizacion();
  }

  public void setUniversidadListener(UniversidadListener listener) {
    this.listener=listener;
  }
  
  
  class UniversidadAdapter extends ArrayAdapter<UniversidadBean> {
	  
	  Activity context;
	  
	  UniversidadAdapter(Fragment context) {
		  
		  super(context.getActivity(), R.layout.row, listado);
		  
		  this.context = context.getActivity();
      
	  }
    
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
    	
      LayoutInflater inflater = context.getLayoutInflater();	
      View item = inflater.inflate(R.layout.row_position, null);
      
      TextView nombre=(TextView)item.findViewById(R.id.textView_superior);
      TextView descripcion=(TextView)item.findViewById(R.id.textView_inferior);
      TextView distancia=(TextView)item.findViewById(R.id.textView_distancia);
      ImageView imagen=(ImageView)item.findViewById(R.id.imageView_imagen);
      
      String distance=getText(R.string.sin_datos).toString();
      
     
      
      if(listado.size()>0){
    	  
    	  nombre.setText(listado.get(position).getNombre());
    	  descripcion.setText(listado.get(position).getDescripcion());
    	  imagen.setImageResource(listado.get(position).getIdImagen());
    	  
    	  
    	  
    	  if(myLocation!=null){
    	   distanceDouble=CalculateDistance(myLocation.getLatitude(),myLocation.getLongitude(),listado.get(position).getLatitud(),listado.get(position).getLongitud());

    	  }

    	  if(distanceDouble>=1){
    		  distance=distanceDouble+" "+getText(R.string.kilometros).toString();
    	  }else{
    		  distance=(distanceDouble)+" "+getText(R.string.metros).toString();
    	  }
    	  
    	  UniversidadBean ub=new UniversidadBean(listado.get(position).getId(),listado.get(position).getIdImagen(),listado.get(position).getNombre(), listado.get(position).getDescripcion(), listado.get(position).getEnlace(), listado.get(position).getTipo(), listado.get(position).getGrado(), listado.get(position).getLatitud(), listado.get(position).getLongitud(),distanceDouble);
    	  
    	  listado.set(position,ub);
    	  
    	  distancia.setText(getText(R.string.distancia).toString()+": "+distance);
    	  
      }else{
    	  
    	  nombre.setText(getText(R.string.no_datos));
      }
      
      return(item);
    }
    
  }

  static class UniversidadWrapper {
    private TextView nombre=null;
    private TextView descripcion=null;
    private ImageView imagen=null;
    
    UniversidadWrapper(View row) {
    	nombre=(TextView)row.findViewById(R.id.textView_superior);
    	descripcion=(TextView)row.findViewById(R.id.textView_inferior);
    	imagen=(ImageView)row.findViewById(R.id.imageView_imagen);
    }
    
    TextView getNombre() {
      return(nombre);
    }
    
    TextView getDescripcion() {
        return(descripcion);
      }
    
    ImageView getImagen() {
      return(imagen);
    }
    
    void populateFrom(UniversidadBean uw) {
    	getNombre().setText(uw.getNombre());
    	getImagen().setImageResource(uw.getIdImagen());
    	getDescripcion().setText(uw.getDescripcion());
    }
  }
  
  public  ArrayList<UniversidadBean> inicializarDatos(){
	  
	  if(inicializarDatosASC){
		  
		  		  
	  }else if(inicializarDatosDESC){
		  

	  }else{
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
	  
	  }
	  
	  ArrayList<UniversidadBean> listadoAux=new ArrayList<UniversidadBean>();
	  
	  for(int i=0;i<listado.size();i++){
		  
	
		  if(listado.get(i)!=null){
			  listadoAux.add(listado.get(i));
		  }
		  
		  
	  }
	  
	  lstListado.setAdapter(new UniversidadAdapter(this));
	  
	  registerForContextMenu(lstListado);
	  
	  return listadoAux;
  }
  
  public  ArrayList<UniversidadBean> inicializarDatosASC(){
	  
	  inicializarDatosASC=true;
	  inicializarDatosDESC=false;
	  
	  for (int i = 0; i < listado.size(); i++) {
		  for (int j = i + 1; j < listado.size(); j++) {

		  if (listado.get(i).getDistancia() > listado.get(j).getDistancia()) {
			  
			  UniversidadBean aux = listado.get(i);
			  listado.set(i,listado.get(j));
			  listado.set(j,aux);

		  }
		 }
	}
	  return listado;

  }
  
  public  ArrayList<UniversidadBean> inicializarDatosDESC(){
	  
	  inicializarDatosASC=false;
	  inicializarDatosDESC=true;
	  
	  for (int i = 0; i < listado.size(); i++) {
		  for (int j = i + 1; j < listado.size(); j++) {

		  if (listado.get(i).getDistancia() < listado.get(j).getDistancia()) {
			  
			  UniversidadBean aux = listado.get(i);
			  listado.set(i,listado.get(j));
			  listado.set(j,aux);

		  }
		 }
	}
	  return listado;
	  
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

	  if(listado.size()>0){
		  
          menu.setHeaderTitle(R.string.accion);

          menu.add(0, Constants.MENU_MAPA_DISTANCIA, 2, getResources().getText(R.string.distanciaMapa));

          menu.add(0, Constants.MENU_COMPARTIR, 3, getResources().getText(R.string.compartir));
          
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

          case Constants.MENU_MAPA_DISTANCIA:

        	   listener.onUniversidadMapa(ub.getLatitud(),ub.getLongitud(),ub.getNombre(),ub.getDescripcion(),ub.getIdImagen(),"distancia");

                return true;
                
          case Constants.MENU_COMPARTIR:

        	  showDialogShareButtonClick(ub);

           return true;
           

          default:
                  return super.onContextItemSelected(item);
          }
       
  }
  
  
  private void showDialogShareButtonClick(final UniversidadBean ub) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      
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
            	listener.onUniversidadCompartir(ub);
              }
              if(selected==1){
            	listener.onUniversidadContactos(ub);
              }
              if(selected==2){
              	listener.onUniversidadContactosSearch(ub);
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
  
  @SuppressLint("DefaultLocale")
  public void setSearchResult(String str) {
	  
	  	listadoAux=MySingleton.getInstance().getUniversitiesList();
	  	
	  	listado=new ArrayList<UniversidadBean>();

		for (UniversidadBean temp : listadoAux) {
			if(temp!=null && temp.getNombre()!=null && str!=null){
				if (temp.getNombre().toString().toLowerCase().contains(str.toLowerCase()) || temp.getDescripcion().toString().toLowerCase().contains(str.toLowerCase())) {
					listado.add(temp);
				}
			}
		}
		
		/*if(inicializarDatosASC){
			  
			  for (int i = 0; i < listado.size(); i++) {
				  for (int j = i + 1; j < listado.size(); j++) {

				  if (listado.get(i).getDistancia() > listado.get(j).getDistancia()) {
					  
					  UniversidadBean aux = listado.get(i);
					  listado.set(i,listado.get(j));
					  listado.set(j,aux);

				  }
				 }
			}
			  
		  }else if(inicializarDatosDESC){
			  
			 
			  for (int i = 0; i < listado.size(); i++) {
				  for (int j = i + 1; j < listado.size(); j++) {

				  if (listado.get(i).getDistancia() < listado.get(j).getDistancia()) {
					  
					  UniversidadBean aux = listado.get(i);
					  listado.set(i,listado.get(j));
					  listado.set(j,aux);

				  }
				 }
			}
			  
		  }*/
		
		if(str!=null && str.equals("")){
			listado=listadoAux;
		}
		
		
		adapter=new  UniversidadAdapter(this);
		adapter.notifyDataSetChanged();
		
		lstListado.setAdapter(adapter);
	    registerForContextMenu(lstListado);
	    
	}
  
  public void panoramio(Double latitud,Double longitud){
	    
	  Intent intent = new Intent();
      intent.setClass(getActivity(), PanoramioActivity.class);
      String url="http://www.panoramio.com/map/#lt="+latitud.toString()+"&ln="+longitud.toString()+"&z=1";
      intent.putExtra(Constants.EXTRA_URL,url);
      intent.putExtra(Constants.EXTRA_COLOR, currentColor);
      startActivity(intent);
  }


  @Override
	public void onLocationChanged(Location location) {
	
	if(location!=null){
		
		mostrarPosicion(location);
  	
		myLocation=new Location(location);
	}
  }



	 private void mostrarPosicion(Location loc) {
		 
	    	if(loc != null)
	    	{
	    		myLocation=new Location(loc);
	    		latitudText.setText(getActivity().getText(R.string.latitud).toString()+": " + String.valueOf(loc.getLatitude()));
	    		longitudText.setText(getActivity().getText(R.string.longitud).toString()+": "  + String.valueOf(loc.getLongitude()));
	    	}
	    	else
	    	{
	    		latitudText.setText(getActivity().getText(R.string.latitud).toString()+": "+getText(R.string.sin_datos).toString());
	    		longitudText.setText(getActivity().getText(R.string.longitud).toString()+": "+getText(R.string.sin_datos).toString());
	    	}
	    	
	}
	
	 
	public void comenzarLocalizacion(){
		
		 // Getting LocationManager object from System Service LOCATION_SERVICE
      locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

      mtxt.setText("");
      
      if(locationManager!=null){
      	
      	// Creating a criteria object to retrieve provider
      	Criteria criteria = new Criteria();
      	criteria.setAccuracy(Criteria.ACCURACY_FINE); 
      	criteria.setAltitudeRequired(false); 
      	criteria.setBearingRequired(false); 
      	criteria.setCostAllowed(true); 
      	criteria.setPowerRequirement(Criteria.POWER_LOW); 
      	
      	// Getting the name of the best provider
      	String provider = locationManager.getBestProvider(criteria, true);

      	if(provider!=null){
      		
      		//Getting Current Location
      		locationManager.requestLocationUpdates(provider, 1000, 0, this);
      		
      		Location location = locationManager.getLastKnownLocation(provider);
      	
      		
      		if(location!=null){
      	
      			//mostrarToast("Location: "+location);
      			
      		}else{
      		
      			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

      		}
            
      		mostrarPosicion(location);
      		
      		if(location!=null){
              	onLocationChanged(location);
      		}

      		locationManager.requestLocationUpdates(provider, 20000, 0, this);
      		
      	}else{
      		
      		//Obtenemos la ultima posicion conocida
          	Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          	
          	mostrarPosicion(loc);
      	}
      
      }
      
      if(myLocation==null){
    	  
    	  showDialogShareButtonClick2();
          
      }
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	
	}
  
	
	public static double CalculateDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double t = lon1 - lon2;
		double distance = Math.sin(Degree2Radius(lat1)) * Math.sin(Degree2Radius(lat2)) + Math.cos(Degree2Radius(lat1)) * Math.cos(Degree2Radius(lat2)) * Math.cos(Degree2Radius(t));
		distance = Math.acos(distance);
		distance = Radius2Degree(distance);
		distance = distance * 60 * 1.1515; //millas

		
		float milla = 1.609F;
		distance = distance * milla; //kilometros
		
		return Redondear(distance);

	}

	private static double Degree2Radius(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	private static double Radius2Degree(double rad)
	{
		return rad / Math.PI * 180.0;
	}
	
	public static double Redondear(double numero)
	{
	       return Math.rint(numero*100)/100;
	}
	
	
	private void showDialogShareButtonClick2() {
		 	Dialog newDialog = null;
		 	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	      

              builder.setMessage(R.string.confirmar_enable_provider);
              builder.setCancelable(false);
              builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int id) {
                       

                  	Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);        				
                  }
              });

              builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
                  public void onClick(DialogInterface dialog, int id) {
                      dialog.cancel();
                  }
              });
              newDialog = builder.create();
              newDialog.show();


      }
	
	public void setColor(int color){
		  
		  this.currentColor=color;
	}
}