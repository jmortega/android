package com.proyecto.spaincomputing.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Clase que permite geolocalizar un lugar a partir de un punto obteniendo informacion adicional sobre este punto
 */
public class LocationGeoCoder {

    private static LocationGeoCoder instance  = new LocationGeoCoder();
    static Context               context;
    ConnectivityManager          connectivityManager;
    NetworkInfo                  wifiInfo, mobileInfo;
    boolean                      connected = false;
    

    public static LocationGeoCoder getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    /**
     * Obtiene la direccion de un lugar a partir de latidud y longitud 
     * 
     * @param gp GeoPoint Objeto que contiene latitud y longitud
     * @return String 
     */
    public String obtenerLocationGeoCoder(LatLng gp){

        boolean redOnline=false;
        
        String addressText = "";
        
        if (NetWorkStatus.getInstance(context).isOnline()) {
            
            redOnline=true;
        }

        //si hay red y esta Online se intenta obtener la direccion del lugar a partir de latitud y longitud
        
        if(redOnline){
            
            Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
         // Create a list to contain the result address
            List<Address> addresses = null;
            try {
            	/*
                 * Return 1 address.
                 */
                addresses = geoCoder.getFromLocation(gp.latitude , gp.longitude , 1);
                
               //If the reverse geocode returned an address
                if (addresses!=null && addresses.size() > 0) {
                	
                	// Get the first address
                    Address address = addresses.get(0);
                    
                    /*
                     * Format the first line of address (if available),
                     * city, and country name.
                     */
                    
                    if(address.getMaxAddressLineIndex() > 0 ){
                    	addressText =address.getAddressLine(0);
                    }
                    
                    if(address.getLocality()!=null){
                    	addressText = addressText+" / "+address.getLocality();
                    }
                    
                    if(address.getCountryName()!=null){
                    	addressText = addressText+" / "+address.getCountryName();
                    }
                    
                }
            
            } catch (IOException e) {
                
            	Log.e("LocationSampleActivity","IO Exception in getFromLocation()");
             	e.printStackTrace();
                return addressText;
            }
            catch (IllegalArgumentException e2) {
            	// Error message to post in the log
            	String errorString = "Illegal arguments " +Double.toString(gp.latitude) +" , " +Double.toString(gp.longitude) +" passed to address service";
            	Log.e("LocationSampleActivity", errorString);
            	e2.printStackTrace();
            	return "";
            }
        }
        
        return addressText;
    }

   
}
