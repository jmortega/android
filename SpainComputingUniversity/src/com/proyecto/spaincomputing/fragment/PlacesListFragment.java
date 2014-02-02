package com.proyecto.spaincomputing.fragment;

import java.util.ArrayList;
import java.util.Locale;

import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.adapter.DBAdapter;
import com.proyecto.spaincomputing.adapter.PlaceAdapter;
import com.proyecto.spaincomputing.entity.PlaceBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PlacesListFragment extends Fragment implements OnItemClickListener {
	String description = "";
	ListView list;
	String url="";
	
	private ArrayList<PlaceBean> listado=new ArrayList<PlaceBean>();
	
	private ArrayList<PlaceBean> listadoAux=new ArrayList<PlaceBean>();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_places_list, container, false);
		
		View viewEmpty = inflater.inflate(R.layout.empty, container, false);
		
		list = (ListView)view.findViewById(android.R.id.list);
		list.setEmptyView(viewEmpty);
		
		listado=inicializarDatos();
		
		if(listado!=null && listado.size()>0){
			list.setAdapter(new PlaceAdapter(this,listado));
		}
		
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
		
		return view;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_place, menu);
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
					intent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.msg_share) + " " + description + " " + url);
					intent.setType("text/plain");
					startActivity(Intent.createChooser(intent, getResources().getText(R.string.action_share)));
				}
				return true;
				
			case R.id.action_delete_place:	
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		        PlaceBean pb = (PlaceBean) getPlace((int) info.id);
		        Log.i("PlaceBean ID",pb.getId()+"");
				Dialog dialogo = onCreateDialog(Constants.DIALOGO_BORRADO_LUGAR,pb.getId(),this);
				dialogo.show();
				
				
				
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	public PlaceBean getPlace(int position){
		PlaceBean place=new PlaceBean();
	      
	      if(listado!=null && listado.size()>0){
	      	place=listado.get(position);
	      }
	      
	      return place;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterView.AdapterContextMenuInfo info =
	            (AdapterView.AdapterContextMenuInfo) menuInfo;
	    
	    
	    PlaceBean pb=listado.get((int) info.id);
	    
	    if(pb!=null){
	    	if(pb.getThumbnailURL()!=null){
	    		url=pb.getThumbnailURL();
	    		Log.i("PlaceBean thumbnail url", url);
	    	}
	    	if(pb.getDescription()!=null){
	    		description = pb.getDescription();  
	    		Log.i("Place description name", description);
	    	}
	    }
	    
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.menu_place, menu);
	}	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {

	}
	
	public  ArrayList<PlaceBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getPlacesDataBase();
		  
		  return listado;
	}
	
	
	@SuppressLint("DefaultLocale")
	public void searchData(String query) {
		Log.i("Search", query);
		
	  	listadoAux=MySingleton.getInstance().getPlaces();
	  	
	  	listado=new ArrayList<PlaceBean>();

		for (PlaceBean temp : listadoAux) {
			if(temp!=null && temp.getDescription()!=null && query!=null){
				if (temp.getDescription().toString().toLowerCase(Locale.getDefault()).contains(query.toLowerCase()) || temp.getDescription().toString().toLowerCase().contains(query.toLowerCase())) {
					listado.add(temp);
				}
			}
		}
		

		if(query!=null && query.equals("")){
			listado=listadoAux;
		}
		
		list.setAdapter(new PlaceAdapter(this,listado));
	    registerForContextMenu(list);
	    
	}
	
    protected Dialog onCreateDialog(int id,final int id2,final PlacesListFragment plf) {
        Dialog newDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Log.i("PlaceBean ID22",id2+"");
        switch (id) {
            case Constants.DIALOGO_BORRADO_LUGAR:
                builder.setMessage(R.string.confirmar_delete_places);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int arg) {
                    	Log.i("PlaceBean ID2",id2+"");
        				DBAdapter db = ((MySingleton)getActivity().getApplicationContext()).getDataBase();
        				db.deletePlace(id2,plf);

        				loadplaces();

        				
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                newDialog = builder.create();
                break;
 
        }

        return newDialog;

    }
	
    public void loadplaces(){
    	
    	listado=inicializarDatos();
		
		if(listado!=null && listado.size()>0){
			list.setAdapter(new PlaceAdapter(this,listado));
		}
		
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
		
    }
}
