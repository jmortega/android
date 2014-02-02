package com.proyecto.spaincomputing.fragment;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.spaincomputing.adapter.ImagePagerAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.R;

@SuppressLint("DefaultLocale")
public class UniversidadesImagesFragment extends Fragment{
	
	private ViewPager viewPager;
	
	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	
	private ArrayList<UniversidadBean> listadoAux=new ArrayList<UniversidadBean>();
	
	private ImagePagerAdapter adapter; 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listado=inicializarDatos();
		
		if (viewPager.getAdapter() == null) {
			adapter= new ImagePagerAdapter(getFragmentManager(),listado);
			adapter.setListado(listado);
			viewPager.setAdapter(adapter);
		} else {
			    ((ImagePagerAdapter)viewPager.getAdapter()).refill(listado);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_universidades_images, container, false);
		viewPager = (ViewPager) view.findViewById(R.id.pager);
		return view;
	}
	
	public void searchData(String query) {
		  
	  	listadoAux=MySingleton.getInstance().getUniversitiesList();
	  	
	  	listado=new ArrayList<UniversidadBean>();

		for (UniversidadBean temp : listadoAux) {
			if(temp!=null && temp.getNombre()!=null && query!=null){
				if (temp.getNombre().toString().toLowerCase(Locale.getDefault()).contains(query.toLowerCase()) || temp.getDescripcion().toString().toLowerCase().contains(query.toLowerCase())) {
					listado.add(temp);
				}
			}
		}
	
		if(query!=null && query.equals("")){
			listado=listadoAux;
		}
		
		
		if (viewPager.getAdapter() == null) {
			adapter= new ImagePagerAdapter(getFragmentManager(),listado);
			adapter.setListado(listado);
			viewPager.setAdapter(adapter);
		} else {
			    ((ImagePagerAdapter)viewPager.getAdapter()).refill(listado);
		}

		adapter.notifyDataSetChanged();

	}
	
	public  ArrayList<UniversidadBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  
		  return listado;
	}
}
