package com.proyecto.spaincomputing.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.fragment.UniversidadImageFragment;
public class ImagePagerAdapter extends FragmentPagerAdapter {

	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	
	public ImagePagerAdapter(FragmentManager fm, ArrayList<UniversidadBean>listado) {
		
		super(fm);
		
		this.listado=listado;
		
	}

	@Override
	public Fragment getItem(int item) {

        Fragment fragment = new UniversidadImageFragment();
        Bundle args = new Bundle();
        args.putInt(UniversidadImageFragment.RESOURCE, listado.get(item).getIdImagen());
        fragment.setArguments(args);
        return fragment;		
		
	}

	@Override
	public int getCount() {
		return listado.size();
	}
	
	public void setListado(ArrayList<UniversidadBean>listado){
		this.listado=listado;
	}
	public ArrayList<UniversidadBean> getListado(){
		return listado;
	}
	
	public void refill(ArrayList<UniversidadBean> events) {
	    listado.clear();
	    listado.addAll(events);
	    notifyDataSetChanged();
	}


}
