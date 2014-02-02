package com.proyecto.spaincomputing.adapter;

import java.util.ArrayList;

import com.proyecto.spaincomputing.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	private Context context;
	private ArrayList<String> nombre=new ArrayList<String>();
	private ArrayList<String> descripcion=new ArrayList<String>();
	private ArrayList<String> titulo=new ArrayList<String>();
	private ArrayList<Integer> imagen=new ArrayList<Integer>();
	private LayoutInflater inflater;
	private int count=0;
	private int position=0;

	public ViewPagerAdapter(Context context, ArrayList<String> name, ArrayList<String> description,ArrayList<String> titulo, ArrayList<Integer> image) {
		this.context = context;
		this.nombre = name;
		this.descripcion = description;
		this.titulo = titulo;
		this.imagen = image;
		if(name!=null){
			this.count = name.size();
		}
	}

	@Override
	public int getCount() {
		return nombre.size();
	}
	
	public int getPosition() {
		return position;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
    public CharSequence getPageTitle(int position) {
        return "Page #" + ( position + 1 );
    }
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		this.position=position;
		
		// Declare Variables
		TextView txtNombre;
		TextView txtDescription;
		TextView txtTitulo;
		ImageView image;
		TextView posicion;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,false);

		// Locate the TextViews in viewpager_item.xml
		txtNombre = (TextView) itemView.findViewById(R.id.nombre);
		txtDescription = (TextView) itemView.findViewById(R.id.descripcion);
		txtTitulo = (TextView) itemView.findViewById(R.id.titulo);
		posicion = (TextView) itemView.findViewById(R.id.posicion);
		
		// Capture position and set to the TextViews
		txtNombre.setText(nombre.get(position));
		txtDescription.setText(descripcion.get(position));
		txtTitulo.setText(titulo.get(position));
		String posicionAux=position+1+"/"+count;
		posicion.setText(posicionAux);
		// Locate the ImageView in viewpager_item.xml
		image = (ImageView) itemView.findViewById(R.id.flag);
		// Capture position and set to the ImageView
		image.setImageResource(imagen.get(position));
		
		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}
