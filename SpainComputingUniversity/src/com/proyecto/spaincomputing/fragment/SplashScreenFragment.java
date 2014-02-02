package com.proyecto.spaincomputing.fragment;


import com.proyecto.spaincomputing.R;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
*
* Fragment que define el comportamiento del splash screen
*
* @author jmortega
*
*/
public class SplashScreenFragment extends Fragment {
	
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 
	        	return inflater.inflate(R.layout.splash, container,false);
	    }
}