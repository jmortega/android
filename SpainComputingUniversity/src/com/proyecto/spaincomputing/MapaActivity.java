package com.proyecto.spaincomputing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.proyecto.spaincomputing.adapter.CustomInfoViewAdapter;
import com.proyecto.spaincomputing.adapter.DBAdapter;
import com.proyecto.spaincomputing.entity.PlaceBean;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.ErrorDialogFragment;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.Helper;
import com.proyecto.spaincomputing.utils.LocationGeoCoder;


public class MapaActivity extends ActionMainActivity implements OnConnectionFailedListener,
ConnectionCallbacks, com.google.android.gms.location.LocationListener,OnMapLongClickListener{

	static private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();



	private final int RQS_GooglePlayServices = 1;
	private String GlEsVersion="";

	private GoogleMap mapa=null;
	private int vista = 0;

	private String nombre, descripcion;
	private Integer idImage;
	private Double latitud, longitud;
	private Boolean localizar=false;
	private Context context=this;
	private String bestProvider="";
	private boolean miPosition=false;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private Location locationAux;

	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * 5;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * 1;	
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationClient locationClient;
	private LocationRequest locationRequest;

	private ArrayList<PlaceBean> places;
	private Bundle savedInstanceState;
	private HashMap<Marker, PlaceBean> markerPlacesMap = new HashMap<Marker, PlaceBean>();
	private DBAdapter dataBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.savedInstanceState = savedInstanceState;

		dataBase = ((MySingleton)getApplicationContext()).getDataBase();

		places = ((MySingleton)getApplicationContext()).getPlaces();

		getPlaces();


		ActionBar ab = getSupportActionBar();

		if(ab!=null){

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ab.setTitle(getString(R.string.mapa));
			}
		}

		//Activamos el modo fullscreen
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.fragment_mapa);

		locationClient = new LocationClient(this, this, this);
		locationRequest = LocationRequest.create();

		locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		locationListener=new MyLocationListener();

		// Getting LocationManager object from System Service LOCATION_SERVICE

		//utilizamos la clase LocationManager para obtener los datos sobre la ubicacion
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

	}

	public void drawMarkers() {
		for (PlaceBean p : places) {

			String title = getApplicationContext().getString(R.string.txt_marker_title,p.getDate());
			String snippet = getApplicationContext().getString(R.string.txt_marker_snippet,p.getTime());

			MarkerOptions options = new MarkerOptions()
			.position(p.getLocation())
			.title(title)
			.snippet(snippet);

			Marker marker = mapa.addMarker(options);
			markerPlacesMap.put(marker, p);
			grabThumbnailImage(marker);
		}
	}

	public void removeAllMarkers() {
		places.clear();
		markerPlacesMap.clear();
		mapa.clear();
	}

	public void setupMap() {

		mapa  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		//Recogemos los datos que se nos proporciona a traves del intent de llamada
		Bundle extras = getIntent().getExtras();

		if(mapa!=null){


			inicializarDatos();
			marcadores();


			mapa.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng point) {
					Projection proj = mapa.getProjection();
					Point coord = proj.toScreenLocation(point);

					/*mostrarToast(
				            "Lat: " + point.latitude + "\n" +
				            "Lng: " + point.longitude + "\n" +
				            "X: " + coord.x + " - Y: " + coord.y);*/
				}



			});

			mapa.setOnMapLongClickListener(new OnMapLongClickListener() {

				public void onMapLongClick(LatLng point) {
					Projection proj = mapa.getProjection();
					Point coord = proj.toScreenLocation(point);

					/*mostrarToast(
			            "Lat: " + point.latitude + "\n" +
			            "Lng: " + point.longitude + "\n" +
			            "X: " + coord.x + " - Y: " + coord.y);*/
				}
			});


			mapa.setOnCameraChangeListener(new OnCameraChangeListener() {
				public void onCameraChange(CameraPosition position) {


					Log.i("CAMBARA","Cambio Camara\n" +
							"Lat: " + position.target.latitude + "\n" +
							"Lng: " + position.target.longitude + "\n" +
							"Zoom: " + position.zoom + "\n" +
							"Orientacion: " + position.bearing + "\n" +
							"angulo: " + position.tilt);

					/*mostrarToast(
			        		"Cambio Camara\n" +
						            "Lat: " + position.target.latitude + "\n" +
						            "Lng: " + position.target.longitude + "\n" +
						            "Zoom: " + position.zoom + "\n" +
						            "Orientacion: " + position.bearing + "\n" +
						            "angulo: " + position.tilt);*/
				}
			});


			// Enabling MyLocation Layer of Google Map
			//define la posicion actual y muestra un punto azul

			if (savedInstanceState == null) {
				mapa.setMyLocationEnabled(true);
				mapa.setTrafficEnabled(true);
				mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				mapa.setOnMapLongClickListener(this);
				mapa.setInfoWindowAdapter(new CustomInfoViewAdapter(getApplicationContext(),markerPlacesMap));
				mapa.getUiSettings().setZoomControlsEnabled(true);

			}

			drawMarkers();

			if(locationManager!=null){

				List<String> locationProviders=locationManager.getAllProviders();
				for(String provider:locationProviders){
					Log.d("Location providers",provider);
				}

				// Creating a criteria object to retrieve provider
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE); 
				criteria.setAltitudeRequired(false); 
				criteria.setBearingRequired(false); 
				criteria.setCostAllowed(true); 
				criteria.setPowerRequirement(Criteria.POWER_LOW); 

				// Getting the name of the best provider
				//aqui es el sistema operativo el que decide cual es el mejor proveedor para obtener la localizacion en funcion de criteria
				bestProvider = locationManager.getBestProvider(criteria, true);
				Log.d("Location providers", "Best provider is "+bestProvider);

				if(bestProvider!=null){

					locationManager.requestLocationUpdates(bestProvider, 1000, 0, locationListener);

					//Getting Current Location
					Location location = locationManager.getLastKnownLocation(bestProvider);	


					if(location==null){

						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					}

					if(location==null){

						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

					}

					if(location!=null){

						locationListener.onLocationChanged(location);

						//Creating a LatLng object for the current location
						if((Double.valueOf(location.getLatitude())!=null) && (Double.valueOf(location.getLongitude())!=null)){
							LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

							//Showing the current location in Google Map
							mapa.moveCamera(CameraUpdateFactory.newLatLng(latLng));

							// Zoom in the Google Map
							mapa.animateCamera(CameraUpdateFactory.zoomTo(6));
						}
					}

					if(location!=null && extras != null && extras.getBoolean(Constants.EXTRA_MI_POSICION)) {

						latitud= location.getLatitude();
						longitud= location.getLongitude();

						localizar=true;

						miPosition=true;

						//posicion en el mapa
						mostrarMiPosicion(latitud, longitud,getText(R.string.posicion).toString());


					}

					if(extras != null && extras.getBoolean(Constants.EXTRA_MI_POSICION) && extras.getString(Constants.EXTRA_FLAG)!=null && extras.getString(Constants.EXTRA_FLAG).equals("distancia")) {

						latitud= extras.getDouble(Constants.EXTRA_LATITUD);
						longitud= extras.getDouble(Constants.EXTRA_LONGITUD);
						nombre= extras.getString(Constants.EXTRA_NOMBRE);
						descripcion= extras.getString(Constants.EXTRA_DESCRIPCION);
						idImage= extras.getInt(Constants.EXTRA_ID_IMAGEN);
						localizar=true;

						if(latitud!=null && longitud!=null){
							//Marcador para el mapa
							mostrarMarcador(latitud,longitud,nombre,descripcion,idImage);
						}

						mapa.addMarker(new MarkerOptions()
						.position(new LatLng(latitud, longitud))
						.title(nombre)
						.snippet(descripcion)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

						if(location!=null){

							mapa.addPolyline(new PolylineOptions()
							.add(new LatLng(latitud, longitud),new LatLng(location.getLatitude(), location.getLongitude()))
							.width(5)
							.color(Color.GREEN));

							//Creating a LatLng object for the current location
							LatLng latLng2 = new LatLng(location.getLatitude(), location.getLongitude());

							//Showing the current location in Google Map
							mapa.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

							// Zoom in the Google Map
							mapa.animateCamera(CameraUpdateFactory.zoomTo(10));

						}


					}

					locationManager.requestLocationUpdates(bestProvider, 20000, 0, locationListener);

					locationAux=location;
				}

			}

		}



		if(extras != null && !extras.getBoolean(Constants.EXTRA_MI_POSICION)) {

			latitud= extras.getDouble(Constants.EXTRA_LATITUD);
			longitud= extras.getDouble(Constants.EXTRA_LONGITUD);
			nombre= extras.getString(Constants.EXTRA_NOMBRE);
			descripcion= extras.getString(Constants.EXTRA_DESCRIPCION);
			idImage= extras.getInt(Constants.EXTRA_ID_IMAGEN);
			localizar=true;

			if(latitud!=null && longitud!=null && idImage!=null && nombre!=null){
				//Marcador para el mapa
				mostrarMarcador(latitud,longitud,nombre,descripcion,idImage);
			}else{
				mostrarMarcador2(latitud,longitud,nombre,descripcion,idImage);
			}

		}

		GlEsVersion=((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();

		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

		logGooglePlayServices(result);

	}

	public void soporteOPENGL() {

		String soporte_opengl="";

		// Check if the system supports OpenGL ES 2.0.
		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if(supportsEs2){
			soporte_opengl = getText(R.string.version_opengl) +" "+ GlEsVersion + "\n" +
					getText(R.string.version_ok_opengl);
		}else{
			soporte_opengl = getText(R.string.version_opengl) + " "+ GlEsVersion + "\n" +
					getText(R.string.version_error_opengl);
		}

		AlertDialog.Builder soporte = new AlertDialog.Builder(context);
		soporte.setTitle(getText(R.string.version_opengl).toString());
		soporte.setMessage(soporte_opengl);
		soporte.show();

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_mapa, menu);

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home: 

			Intent intent = new Intent(this, PrincipalActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			break;

		case R.id.opengl:

			soporteOPENGL();

			break;

		case R.id.menu_vista:

			alternarVista();
			break;

		case R.id.menu_mover:

			if(localizar){

				//Centramos el mapa en latitud/longitud
				CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(new LatLng(latitud, longitud));
				mapa.moveCamera(camUpd1);
			}else{

				//Centramos el mapa en Espa�a
				CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(new LatLng(40.41, -3.69));
				mapa.moveCamera(camUpd1);
			}

			break;

		case R.id.menu_animar:

			if(localizar){

				//Centramos el mapa en latitud/longitud y con nivel de zoom 5
				CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud),15F);
				mapa.moveCamera(camUpd1);

			}else{

				//Centramos el mapa en Espa�a y con nivel de zoom 5
				CameraUpdate camUpd2 = CameraUpdateFactory.newLatLngZoom(new LatLng(42.41, -8.69), 6F);
				mapa.animateCamera(camUpd2);
			}

			break;

		case R.id.menu_3d:

			LatLng pos = new LatLng(40.497325, -5.583081);

			CameraPosition camPos = new CameraPosition.Builder()
			.target(pos)   //Centramos el mapa en centro espanya
			.zoom(7)         //Establecemos el zoom en 7
			.bearing(45)      //Establecemos la orientacion con el noreste arriba
			.tilt(70)         //Bajamos el punto de vista de la camara 70 grados
			.build();


			if(localizar){

				pos = new LatLng(latitud, longitud);

				camPos = new CameraPosition.Builder()
				.target(pos)   //Centramos el mapa en centro espanya
				.zoom(17)         //Establecemos el zoom en 17
				.bearing(45)      //Establecemos la orientacion con el noreste arriba
				.tilt(70)         //Bajamos el punto de vista de la camara 70 grados
				.build();
			}

			CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

			mapa.animateCamera(camUpd3);
			break;

		case R.id.menu_posicion:

			if(locationAux!=null){
				if(Double.valueOf(locationAux.getLatitude())!=null && Double.valueOf(locationAux.getLongitude())!=null)
					mostrarMiPosicion(locationAux.getLatitude(), locationAux.getLongitude(),getText(R.string.posicion).toString());
			}

			break;

		case R.id.menu_providers:

			String soporte="";
			List<String> locationProviders=locationManager.getAllProviders();
			for(String provider:locationProviders){
				soporte=soporte+provider+"\n";
			}
			soporte=soporte+"\n\nBest Provider "+bestProvider;

			mostrarToast(soporte);

			break;

		case R.id.menu_delete_places:

			Dialog dialogo = onCreateDialog(Constants.DIALOGO_BORRADO_LUGARES);
			dialogo.show();

			break;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onDestroy() {

		super.onDestroy();

		//---remove the location listener---
		locationManager.removeUpdates(locationListener);

	}


	@Override
	public void onResume() {

		super.onResume();

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS){

			mostrarToast("Google Play Services Available SUCCESS");
		}
		else if (resultCode == ConnectionResult.DEVELOPER_ERROR){

			mostrarToast("Google Play Service DEVELOPER_ERROR");
		}
		else if (resultCode == ConnectionResult.INTERNAL_ERROR){

			mostrarToast("Google Play Service INTERNAL_ERROR");
		}
		else if (resultCode == ConnectionResult.INVALID_ACCOUNT){

			mostrarToast("Google Play Service INVALID_ACCOUNT");
		}
		else if (resultCode == ConnectionResult.NETWORK_ERROR){

			mostrarToast("Google Play Service NETWORK_ERROR");
		}
		else if (resultCode == ConnectionResult.RESOLUTION_REQUIRED){

			mostrarToast("Google Play Service RESOLUTION_REQUIRED");
		}
		else if (resultCode == ConnectionResult.SERVICE_DISABLED){

			mostrarToast("Google Play Service SERVICE_DISABLED");
		}
		else if (resultCode == ConnectionResult.SERVICE_INVALID){

			mostrarToast("Google Play Service SERVICE_INVALID");
		}
		else if (resultCode == ConnectionResult.SERVICE_MISSING){

			mostrarToast("Google Play Service SERVICE_MISSING");
		}
		else if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){

			mostrarToast("Google Play Service SERVICE_VERSION_UPDATE_REQUIRED");
		}
		else if (resultCode == ConnectionResult.SIGN_IN_REQUIRED){
			mostrarToast("Google Play Service SIGN_IN_REQUIRED");
		}
		else{
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}

		//actualizar posicion mediante GPS PROVIDER
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationListener);

		//si el servicio de mapas no esta disponible ocultamos el fragmento del mapa 
		if (!servicesConnected()){

			FragmentManager manager = getSupportFragmentManager();

			SupportMapFragment mapa_fragment  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

			manager.beginTransaction().hide(mapa_fragment).commit();

		}

		setupMap();

	}

	private void alternarVista()
	{
		vista = (vista + 1) % 4;

		switch(vista)
		{
		case 0:
			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case 1:
			mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case 2:
			mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case 3:
			mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		}
	}


	public  ArrayList<UniversidadBean> inicializarDatos(){

		listado=MySingleton.getInstance().getUniversitiesList();

		return listado;
	}


	private void marcadores()
	{

		for(int i=0;i<listado.size();i++){

			double lat=0.0;
			double lng=0.0;
			String nombre="";
			String descripcion="";

			if(listado.get(i)!=null){

				if(Double.valueOf(listado.get(i).getLatitud())!=null){
					lat=listado.get(i).getLatitud();
				}
				if(Double.valueOf(listado.get(i).getLongitud())!=null){
					lng=listado.get(i).getLongitud();
				}
				if(listado.get(i).getNombre()!=null){
					nombre=listado.get(i).getNombre();
				}
				if(listado.get(i).getDescripcion()!=null){
					descripcion=listado.get(i).getDescripcion();
				}

				mapa.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lng))
				.title(nombre)
				.snippet(descripcion)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}

		}

		CameraUpdate camUpd2 = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 6F);
		mapa.animateCamera(camUpd2);
	}

	private void mostrarMarcador(double lat, double lng,String nombre,String descripcion,int idImagen)
	{

		MarkerOptions marker=new MarkerOptions().position(new LatLng(lat, lng))
				.title(nombre)
				.snippet(descripcion)
				.icon(BitmapDescriptorFactory.fromResource(idImagen));


		if(mapa!=null){
			mapa.addMarker(marker);

			//Centramos el mapa en las coordenadas latitud/longitud y con nivel de zoom 16
			CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16F);

			mapa.animateCamera(camera);
		}
	}

	private void mostrarMarcador2(double lat, double lng,String nombre,String descripcion,int idImagen)
	{

		//Centramos el mapa en las coordenadas latitud/longitud y con nivel de zoom 6
		CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 6F);

		mapa.animateCamera(camera);

	}

	private void mostrarMiPosicion(double lat, double lng,String nombre)
	{

		mapa.addMarker(new MarkerOptions()
		.position(new LatLng(lat, lng))
		.title(nombre)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.posicion)));;

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

	private class MyLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				Toast.makeText(getBaseContext(),
						"Location changed : Lat: " + loc.getLatitude() +
						" Lng: " + loc.getLongitude(),
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getBaseContext(),
					"Provider: " + provider + " disabled",
					Toast.LENGTH_SHORT).show();

			if(miPosition==true){
				Dialog dialogo = onCreateDialog(Constants.DIALOGO_ENABLE_PROVIDER);
				dialogo.show();
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getBaseContext(),
					"Provider: " + provider + " enabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			String statusString = "";            
			switch (status) {
			case android.location.LocationProvider.AVAILABLE:
				statusString = "available";
			case android.location.LocationProvider.OUT_OF_SERVICE:
				statusString = "out of service";
			case 
			android.location.LocationProvider.TEMPORARILY_UNAVAILABLE:
				statusString = "temporarily unavailable";
			}            
			Toast.makeText(getBaseContext(),
					provider + " " + statusString,
					Toast.LENGTH_SHORT).show();          
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(
						this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				Log.e("ERROR",Log.getStackTraceString(e));
			}
		} else {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					connectionResult.getErrorCode(),
					this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "dialog");
			}
		}

	}

	private boolean servicesConnected() {
		int resultCode =
				GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(), "errorDialog");
			}
			return false;
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		updateLocation(locationClient.getLastLocation());
		locationClient.requestLocationUpdates(locationRequest, this);	

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		super.onStart();
		locationClient.connect();
	}	    

	@Override
	public void onStop() {

		if (locationClient.isConnected()) {
			locationClient.removeLocationUpdates(this);
		}

		locationClient.disconnect();
		super.onStop();
	}

	public void updateLocation(Location location) {
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.e("TAG","Ubicacion nueva Lat:" + location.getLatitude() + " Lon:" + location.getLongitude());
		updateLocation(location);
	}


	@Override
	public void onMapLongClick(LatLng location) {

		String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

		String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());

		String title = getApplicationContext().getString(R.string.txt_marker_title,date);
		String snippet = getApplicationContext().getString(R.string.txt_marker_snippet,time);

		MarkerOptions options = new MarkerOptions()
		.position(location)
		.title(title)
		.snippet(snippet);

		Marker marker = mapa.addMarker(options);
		PlaceBean place = createNewPlace(date, time,location);
		markerPlacesMap.put(marker, place);

		imageFromFlickr(marker, place); //obtener imagen de flickr
	}

	public PlaceBean createNewPlace(String date, String time,LatLng location) {

		ArrayList<PlaceBean> placesList=new ArrayList<PlaceBean>();

		PlaceBean newPlace = new PlaceBean();
		int Random = (int)(Math.random()*1000);

		newPlace.setId(Random);
		newPlace.setDate(date);
		newPlace.setTime(time);
		newPlace.setLocation(location);
		newPlace.setDescription(LocationGeoCoder.getInstance(context).obtenerLocationGeoCoder(new LatLng(location.latitude,location.longitude )));

		places.add(newPlace);

		dataBase.insertPlace(newPlace);

		Log.i("NEW PLACE",dataBase.getTotalPlacesinDatabase()+"");

		/*placesList=dataBase.getPlaces();

		Log.i("GET PLACES 2","---------inicio-----------------");
		if(placesList!=null && placesList.size()>0){
			for(int i=0;i<placesList.size();i++){

				Log.i("GET PLACES 2",placesList.get(i).getId()+" "+placesList.get(i).getAuthor()+" "+placesList.get(i).getDate()+" "+placesList.get(i).getTime()+" "+placesList.get(i).getThumbnailURL()+" "+placesList.get(i).getDescription()+" "+placesList.get(i).getLocation().latitude+" "+placesList.get(i).getLocation().longitude);

			}
		}
		Log.i("GET PLACES 2","---------fin-----------------");
		 */

		getPlaces();

		return newPlace;
	}

	public void getPlaces() {
		if(places!=null && places.size()>0){
			for(int i=0;i<places.size();i++){
				Log.i("GET PLACES",places.get(i).getId()+" "+places.get(i).getAuthor()+" "+places.get(i).getDate()+" "+places.get(i).getTime()+" "+places.get(i).getThumbnailURL()+" "+places.get(i).getDescription()+" "+places.get(i).getLocation().latitude+" "+places.get(i).getLocation().longitude);
			}
		}
		Log.i("TOTAL GET PLACES",places.size()+"");

	}

	public void imageFromFlickr(final Marker m, final PlaceBean p){		
		String url = Helper.FLICKR_API_URL;
		final RequestQueue queue = ((MySingleton)getApplicationContext()).getRequestQueue();
		Response.Listener<JSONObject> successListener = 
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {			            	
				try {
					JSONArray items = response.getJSONArray("items");			            	
					JSONObject media = items.getJSONObject(0).getJSONObject("media");
					String url = media.getString("m");
					String author = items.getJSONObject(0).getString("author");			            	
					p.setAuthor(author);
					p.setThumbnailURL(url);
					dataBase.updatePlace(p);
					markerPlacesMap.put(m, p);
					grabThumbnailImage(m);		            	

				} catch (JSONException e) {
					Log.e("ERROR",Log.getStackTraceString(e));
				}
			}
		};

		ErrorListener errorListener =new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				Log.e("ERROR IN VOLLEY REQUEST ",error.getMessage());
			}

		};

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, successListener,errorListener);

		queue.add(jsObjRequest);

	}

	public void grabThumbnailImage(final Marker marker) {
		final PlaceBean currentPlace = markerPlacesMap.get(marker);
		final LruCache<PlaceBean, Bitmap> thumbnails = ((MySingleton)getApplicationContext()).getThumbnails();
		final RequestQueue queue = ((MySingleton)getApplicationContext()).getRequestQueue();
		if (thumbnails.get(currentPlace) == null) {
			queue.add(
					new ImageRequest(currentPlace.getThumbnailURL(), new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap bitmap) {
							Log.i("Thumbnail ",currentPlace.getThumbnailURL());
							thumbnails.put(currentPlace, bitmap);
							if (marker.isInfoWindowShown()) {
								marker.showInfoWindow();
							};
						}
					}, 256, 256, Config.ARGB_4444, null));
		}			            	

	}

	public void logGooglePlayServices(int result){
		switch (result) {
		case ConnectionResult.SUCCESS:
			Log.e("Maps", "RESULT:: SUCCESS");			
			break;
		case ConnectionResult.DEVELOPER_ERROR:
			Log.e("Maps", "RESULT:: DEVELOPER_ERROR");			
			break;

		case ConnectionResult.INTERNAL_ERROR:
			Log.e("Maps", "RESULT:: INTERNAL_ERROR");			
			break;

		case ConnectionResult.INVALID_ACCOUNT:
			Log.e("Maps", "RESULT:: INVALID_ACCOUNT");			
			break;

		case ConnectionResult.NETWORK_ERROR:
			Log.e("Maps", "RESULT:: NETWORK_ERROR");			
			break;

		case ConnectionResult.RESOLUTION_REQUIRED:
			Log.e("Maps", "RESULT:: RESOLUTION_REQUIRED");			
			break;

		case ConnectionResult.SERVICE_DISABLED:
			Log.e("Maps", "RESULT:: SERVICE_DISABLED");			
			break;

		case ConnectionResult.SERVICE_INVALID:
			Log.e("Maps", "RESULT:: SERVICE_INVALID");			
			break;

		case ConnectionResult.SERVICE_MISSING:
			Log.e("Maps", "RESULT:: SERVICE_MISSING");			
			break;

		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Log.e("Maps", "RESULT:: SERVICE_VERSION_UPDATE_REQUIRED");			
			break;

		case ConnectionResult.SIGN_IN_REQUIRED:
			Log.e("Maps", "RESULT:: SIGN_IN_REQUIRED");			
			break;		

		default:
			break;
		}

	}


	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog newDialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);

		switch (id) {
		case Constants.DIALOGO_BORRADO_LUGARES:
			builder.setMessage(R.string.confirmar_delete_places);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int id) {

					ArrayList<PlaceBean> places = ((MySingleton)getApplicationContext()).getPlaces();
					places.clear();

					DBAdapter db = ((MySingleton)getApplicationContext()).getDataBase();
					db.deleteAllPlaces();

					removeAllMarkers();

					marcadores();

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

		case Constants.DIALOGO_ENABLE_PROVIDER:
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
			break;

		}

		return newDialog;

	}

}
