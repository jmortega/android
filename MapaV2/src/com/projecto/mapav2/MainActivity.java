package com.projecto.mapav2;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private final LatLng LOCATION_BURNABY = new LatLng(49.27645, -122.917587);
	private final LatLng LOCATION_SURRREY = new LatLng(49.187500, -122.849000);
	private final int RQS_GooglePlayServices = 1;
	String pm="";
	int versionCode=0;
	int versionCode2=0;
	boolean supportsEs2=false;
	String GlEsVersion="";
	int gps=0;
	
	private GoogleMap mapa;
	
	private Context context=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try{
			

		setContentView(R.layout.activity_main);

		
		mapa  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		if(mapa!=null){
			
			mapa.addMarker(new MarkerOptions().position(LOCATION_SURRREY).title("Find me here!"));
		
			mapa.setMyLocationEnabled(true);
			
			mapa.setTrafficEnabled(true);
			
			mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
		   
		gps=GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		
		GlEsVersion=((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
		
		
		
		try {
			versionCode=context.getPackageManager().getPackageInfo("com.android.vending", 0).versionCode;
			versionCode2=context.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			pm=context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("com.google.android.maps.v2.API_KEY");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		
		Toast.makeText(
				MainActivity.this,
	            "isGooglePlayServicesAvailable" + gps + "\n" +
	            "GlEsVersion " + GlEsVersion + "\n" +
	            "versionCode Vending" + versionCode + "\nversionCode GMS " + versionCode2+"\n"+
	            "getPackageManager " + pm + "\n" +
	            "supportsEs2 " + supportsEs2 + "\n"+
	            "getVersionFromPackageManager " + getVersionFromPackageManager(this) + "\n",
	            Toast.LENGTH_LONG).show();
		}catch(Exception e){
		
			e.printStackTrace();
			Log.d(INPUT_SERVICE,e.toString() );
		}
		
		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		Log.e("Maps", "Result int value::" + result);
		switch (result) {
		case ConnectionResult.SUCCESS:
			Log.e("Maps", "RESULT:: SUCCESS");			
			break;
			
		case ConnectionResult.DEVELOPER_ERROR:
			Log.e("Maps", "RESULT:: DE");			
			break;
			
		case ConnectionResult.INTERNAL_ERROR:
			Log.e("Maps", "RESULT:: IE");			
			break;
			
		case ConnectionResult.INVALID_ACCOUNT:
			Log.e("Maps", "RESULT:: IA");			
			break;
			
		case ConnectionResult.NETWORK_ERROR:
			Log.e("Maps", "RESULT:: NE");			
			break;
			
		case ConnectionResult.RESOLUTION_REQUIRED:
			Log.e("Maps", "RESULT:: RR");			
			break;
			
		case ConnectionResult.SERVICE_DISABLED:
			Log.e("Maps", "RESULT:: SD");			
			break;
			
		case ConnectionResult.SERVICE_INVALID:
			Log.e("Maps", "RESULT:: SI");			
			break;
			
		case ConnectionResult.SERVICE_MISSING:
			Log.e("Maps", "RESULT:: SM");			
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Log.e("Maps", "RESULT:: SVUR");			
			break;
		case ConnectionResult.SIGN_IN_REQUIRED:
			Log.e("Maps", "RESULT:: SIR");			
			break;		

		default:
			break;
		}
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.setMyLocationEnabled(true);
		Log.e("Maps", "------EOC-------");
		
	}

	private static int getVersionFromPackageManager(Context context) {
	    PackageManager packageManager = context.getPackageManager();
	    FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
	    if (featureInfos != null && featureInfos.length > 0) {
	        for (FeatureInfo featureInfo : featureInfos) {
	            // Null feature name means this feature is the open gl es version feature.
	            if (featureInfo.name == null) {
	                if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
	                    return getMajorVersion(featureInfo.reqGlEsVersion);
	                } else {
	                    return 1; // Lack of property means OpenGL ES version 1
	                }
	            }
	        }
	    }
	    return 1;
	}

	/** @see FeatureInfo#getGlEsVersion() */
	private static int getMajorVersion(int glEsVersion) {
	    return ((glEsVersion & 0xffff0000) >> 16);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick_Location(View v) {
		mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BURNABY, 9);
		mapa.animateCamera(update);
	}

	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	  switch (item.getItemId()) {
	     case R.id.action_settings:
	      String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
	        getApplicationContext());
	      AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
	      LicenseDialog.setTitle("Legal Notices");
	      LicenseDialog.setMessage(LicenseInfo);
	      LicenseDialog.show();
	         return true;
	     
	     case R.id.action_settings2:
	      String LicenseInfo2 = "isGooglePlayServicesAvailable" + gps + "\n" +
		            "GlEsVersion " + GlEsVersion + "\n" +
		            "versionCode Vending" + versionCode + "\nversionCode GMS " + versionCode2+"\n"+
		            "getPackageManager " + pm + "\n" +
		            "supportsEs2 " + supportsEs2 + "\n"+
		            "getVersionFromPackageManager " + getVersionFromPackageManager(this) + "\n";
	      
	      AlertDialog.Builder LicenseDialog2 = new AlertDialog.Builder(MainActivity.this);
	      LicenseDialog2.setTitle("Legal Notices");
	      LicenseDialog2.setMessage(LicenseInfo2);
	      LicenseDialog2.show();
	         return true;
	     }
	  return super.onOptionsItemSelected(item);
	 }

	 @Override
	 protected void onResume() {
	  // TODO Auto-generated method stub
	  super.onResume();

	  int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	  
	  if (resultCode == ConnectionResult.SUCCESS){
	   Toast.makeText(getApplicationContext(), 
	     "isGooglePlayServicesAvailable SUCCESS", 
	     Toast.LENGTH_LONG).show();
	  }else{
	   GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
	  }
	  
	 }

}
