package com.proyecto.spaincomputing.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.UniversityDetailActivity;
import com.proyecto.spaincomputing.adapter.UniversidadAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class UniversityListFragment extends Fragment implements OnItemClickListener {
	String universidad = "";
	ListView list;
	String url="";
	
	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	
	private ArrayList<UniversidadBean> listadoAux=new ArrayList<UniversidadBean>();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_universidades_list, container, false);
		list = (ListView)view.findViewById(R.id.auxlistView);
		
		listado=inicializarDatos();
		
		String[] array_universities = new String[listado.size()];
		
		int i=0;
		for(UniversidadBean ub:listado){
			
			array_universities[i]=ub.getNombre();
			i++;
		}
		
		ArrayList<String> universities = new ArrayList<String>(Arrays.asList(array_universities));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, universities);
		
		list.setAdapter(adapter);		
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
		
		return view;
	}

	@Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_compartir, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu){
		boolean landscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		MenuItem share = menu.getItem(menu.size()-1);
		share.setVisible(landscape);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	
			case R.id.action_share:
				if (!url.equals("")) {

					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SEND);					
					intent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.msg_share) + " " + universidad + " " + url);
					intent.setType("text/plain");
					startActivity(Intent.createChooser(intent, getResources().getText(R.string.action_share)));
				}
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterView.AdapterContextMenuInfo info =
	            (AdapterView.AdapterContextMenuInfo) menuInfo;
	    
	    
	    UniversidadBean ub=listado.get((int) info.id);
	    
	    if(ub!=null){
	    	url=ub.getEnlace();
	    	Log.i("Universidad url", ub.getEnlace());
	    	universidad = ub.getNombre();  
	    	Log.i("Universidad name", ub.getNombre());
	    }
	    
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.menu_compartir, menu);
	}	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {

		UniversidadBean ub=listado.get(position);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			FragmentManager manager = getActivity().getSupportFragmentManager();
			UniversidadInfoFragment fragment = (UniversidadInfoFragment) manager.findFragmentById(R.id.fragmentUniversidadInfo);
			fragment.loadWebViewContent(ub.getEnlace());
			//getActivity().invalidateOptionsMenu();
		} else {
			Intent intent = new Intent(getActivity().getApplicationContext(), UniversityDetailActivity.class);
			intent.putExtra(UniversityDetailActivity.URL, ub.getEnlace());
			url=ub.getEnlace();
			intent.putExtra(UniversityDetailActivity.UNIVERSIDAD, ub.getNombre());
			startActivity(intent);			
		}
	}
	
	public  ArrayList<UniversidadBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  
		  return listado;
	}
	
	
	@SuppressLint("DefaultLocale")
	public void searchData(String query) {
		Log.i("Search", query);
		
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
		
		list.setAdapter(new UniversidadAdapter(this,listado));
	    registerForContextMenu(list);
	    
	}
	
}
