package com.proyecto.spaincomputing.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Clase que permite obtener el estado de la conexion
 */
public class NetWorkStatus {

    private static NetWorkStatus instance  = new NetWorkStatus();
    static Context               context;
    ConnectivityManager          connectivityManager;
    NetworkInfo                  wifiInfo, mobileInfo;
    boolean                      connected = false;
    

    public static NetWorkStatus getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            wifiInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            
            mobileInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            
            if(wifiInfo.isConnected()){
            	Toast.makeText(context, "Wifi is connected", Toast.LENGTH_LONG).show();
            }else{
            	Toast.makeText(context, "Wifi is not connected", Toast.LENGTH_LONG).show();
            }
            
            if(mobileInfo.isConnected()){
            	Toast.makeText(context, "3G/4G is connected", Toast.LENGTH_LONG).show();
            }else{
            	Toast.makeText(context, "3G/4G is not connected", Toast.LENGTH_LONG).show();
            }
            
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
    
    public String getConnectionType() {
    	
    	String connectionType = "";
    	
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            wifiInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            
            mobileInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            
            if(wifiInfo.isConnected()){
            	connectionType= "WIFI";
            }
            
            if(mobileInfo.isConnected()){
            	connectionType= "3G/4G";
            }


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        
        return connectionType;
    }
    
	public String getLocalIpv4Address() {
		try {
			for (Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnum.hasMoreElements();) {
				NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
				for (Enumeration<InetAddress> ipAddressEnum = networkInterface.getInetAddresses(); ipAddressEnum.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) ipAddressEnum.nextElement();
					// ---check that it is not a loopback address and
					// it is IPv4---
					if (!inetAddress.isLoopbackAddress()&& InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("getLocalIpv4Address", ex.toString());
		}
		return null;
	}

	public String getLocalIpv6Address() {
		try {
			for (Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnum.hasMoreElements();) {
				NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
				for (Enumeration<InetAddress> ipAddressEnum = networkInterface.getInetAddresses(); ipAddressEnum.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) ipAddressEnum.nextElement();
					// ---check that it is not a loopback address and
					// it is IPv6---
					if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv6Address(inetAddress.getHostAddress())) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("getLocalIpv6Address", ex.toString());
		}
		return null;
	}

	public void activarWIFI(){
		
		WifiManager wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
	}
    
    
}
