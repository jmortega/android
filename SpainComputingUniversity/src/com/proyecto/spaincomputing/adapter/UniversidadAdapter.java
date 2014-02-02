package com.proyecto.spaincomputing.adapter;

import java.util.ArrayList;


import com.proyecto.spaincomputing.ListViewActivity;
import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UniversidadAdapter extends ArrayAdapter<UniversidadBean> {
	  
	private final Activity context;
	  
	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	  
	public UniversidadAdapter(Fragment context,ArrayList<UniversidadBean> listado) {
		  
		  super(context.getActivity(), R.layout.row, listado);
		  
		  this.context = context.getActivity();
		  
		  this.listado=listado;
    
	  }
  
	public UniversidadAdapter(ListViewActivity listViewActivity,ArrayList<UniversidadBean> listado) {

		  super(listViewActivity, R.layout.row, listado);
		  
		  this.context = listViewActivity;
		  
		  this.listado=listado;
	}

	static class ViewContainer{
		
	 public ImageView imagen;
	 public TextView nombre;
	 public TextView descripcion;
	 public ViewContainer(){ 
	 }
	 public ViewContainer(View row){
	   //obtiene una referencia a todas las vistas de la fila
	    nombre=(TextView)row.findViewById(R.id.textView_superior);
	    descripcion=(TextView)row.findViewById(R.id.textView_inferior);
	    imagen=(ImageView)row.findViewById(R.id.imageView_imagen);
	 
	 }
	}
	
   @Override
   public View getView(int position, View convertView,ViewGroup parent) {
  	
	ViewContainer viewContainer;

	Log.i("UniversidadAdapter-position:",String.valueOf(position));
	
	//si es la primera vez que se imprime la fila
	if(convertView==null){
		
		Log.i("UniversidadAdapter", "New");
		LayoutInflater inflater = context.getLayoutInflater();	
		convertView = inflater.inflate(R.layout.row, null,true);
	    
	    //crea una vista para el objeto contenedor
	    viewContainer=new ViewContainer(convertView);
	    
	    //obtiene una referencia a todas las vistas de la fila
	    //viewContainer.nombre=(TextView)convertView.findViewById(R.id.textView_superior);
	    //viewContainer.descripcion=(TextView)convertView.findViewById(R.id.textView_inferior);
	    //viewContainer.imagen=(ImageView)convertView.findViewById(R.id.imageView_imagen);
	    
	    //asigna el contenedor de la vista a rowView
	    convertView.setTag(viewContainer);
	    
	}else{
	
		Log.i("UniversidadAdapter", "Recicling");
	
		viewContainer=(ViewContainer) convertView.getTag();
		
		
	}
	    
    //personaliza el contenido de cada fila basándone en su posición
	viewContainer.nombre.setText(listado.get(position).getNombre());
	viewContainer.descripcion.setText(listado.get(position).getDescripcion());
	viewContainer.imagen.setImageResource(listado.get(position).getIdImagen());
  
    
    return(convertView);
   }
  
}