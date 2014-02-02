package com.proyecto.spaincomputing.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import com.proyecto.spaincomputing.InstagramActivity;
import com.proyecto.spaincomputing.PanoramioActivity;
import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.listener.UniversidadListener;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;


public class UniversidadesFragmentFavorito extends Fragment {
	
  static private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
  private UniversidadListener listener;
  private boolean favoritos=false;
  
  private ListView lstListado;
  private TextView favorito;
 
  private Button btnSearch;
  private EditText mtxt;
	
  //dialogo compartir
  private int selected = 0;
  private int buffKey  = 0;
  
  protected int currentColor = 0xFFC74B46;
  
  @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.lista_favoritos, container, false);
	}
  
  @Override
  public void onActivityCreated(Bundle state) {
    super.onActivityCreated(state);

    lstListado = (ListView)getView().findViewById(R.id.LstListado);
    favorito = (TextView)getView().findViewById(R.id.no_favoritos);
    btnSearch = (Button) getView().findViewById(R.id.btnSearch);
	mtxt = (EditText) getView().findViewById(R.id.edSearch);

	mtxt.setText("");
	mtxt.clearFocus();
	
	favorito.setVisibility(View.VISIBLE);
	
    listado=inicializarDatos();
    
    if(listado.size()>0){
    favorito.setVisibility(View.GONE);
    lstListado.setVisibility(View.VISIBLE);
    
    lstListado.setOnItemClickListener(new OnItemClickListener() {
    	
    	
		@Override
		public void onItemClick(AdapterView<?> list, View view, int pos, long id) {

			if (listener!=null) {
				listener.onUniversidadSelected((UniversidadBean)lstListado.getAdapter().getItem(pos));
			}
		}
	});
    
    }else{
    	favorito.setVisibility(View.VISIBLE);
    	lstListado.setVisibility(View.GONE);
    }
   
    mtxt = (EditText) getView().findViewById(R.id.edSearch);
	mtxt.addTextChangedListener(new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			if (0 != mtxt.getText().length()) {
				String spnId = mtxt.getText().toString();
				setSearchResult(spnId);
			} else {
				listado=inicializarDatos();
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			if (0 != mtxt.getText().length()) {
				String spnId = mtxt.getText().toString();
				setSearchResult(spnId);
			} else {
				listado=inicializarDatos();
			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (0 != mtxt.getText().length()) {
				String spnId = mtxt.getText().toString();
				setSearchResult(spnId);
			} else {
				listado=inicializarDatos();
			}
		}
	});
	
	btnSearch.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {

			mtxt.setText("");
			setSearchResult("");
			mtxt.clearFocus();
		}
	});
  }

  public void setUniversidadListener(UniversidadListener listener) {
    this.listener=listener;
  }
  
  public void setUniversidadFavoritos(boolean favoritos) {
	    this.favoritos=favoritos;
  }
  
  class UniversidadAdapter extends ArrayAdapter<UniversidadBean> {
	  
	  Activity context;
	  
	  UniversidadAdapter(Fragment context) {
		  
		  super(context.getActivity(), R.layout.row, listado);
		  
		  this.context = context.getActivity();
      
	  }
    
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
    	
      LayoutInflater inflater = context.getLayoutInflater();	
      View item = inflater.inflate(R.layout.row, null);
      
      TextView nombre=(TextView)item.findViewById(R.id.textView_superior);
      TextView descripcion=(TextView)item.findViewById(R.id.textView_inferior);
      ImageView imagen=(ImageView)item.findViewById(R.id.imageView_imagen);
      

      if(listado!=null && listado.size()>0){
    	  nombre.setText(listado.get(position).getNombre());
    	  descripcion.setText(listado.get(position).getDescripcion());
    	  imagen.setImageResource(listado.get(position).getIdImagen());
      }
      else{
    	  
    	  favorito.setText(getText(R.string.no_hay_favoritos));
      }
      
      return(item);
    }
    
  }

  static class UniversidadWrapper {
    private TextView nombre=null;
    private TextView descripcion=null;
    private ImageView imagen=null;
    
    UniversidadWrapper(View row) {
    	nombre=(TextView)row.findViewById(R.id.textView_superior);
    	descripcion=(TextView)row.findViewById(R.id.textView_inferior);
    	imagen=(ImageView)row.findViewById(R.id.imageView_imagen);
    }
    
    TextView getNombre() {
      return(nombre);
    }
    
    TextView getDescripcion() {
        return(descripcion);
      }
    
    ImageView getImagen() {
      return(imagen);
    }
    
    void populateFrom(UniversidadBean uw) {
    	getNombre().setText(uw.getNombre());
    	getImagen().setImageResource(uw.getIdImagen());
    	getDescripcion().setText(uw.getDescripcion());
    }
  }
  
  public  ArrayList<UniversidadBean> inicializarDatos(){
	  
	  ArrayList<UniversidadBean> listadoAux=new ArrayList<UniversidadBean>();

	  listadoAux=MySingleton.getInstance().getUniversitiesList();
	  
	  listado.clear();
	  
	  for(int i=0;i<listadoAux.size();i++){
		  
		  if(listadoAux.get(i)!=null){
			  
			  if(isFavorito(listadoAux.get(i).getId())){
				  
				  listado.add(listadoAux.get(i));
			  }
		  }
		  
	  }
	  
	  lstListado.setAdapter(new UniversidadAdapter(this));
	  
	  registerForContextMenu(lstListado);
	  
	  


	  return listado;
  }
  
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater=getActivity().getMenuInflater();
	  if(listado.size()>0){
		  inflater.inflate(R.menu.menu_mostrar_favoritos, menu);
	  }
  }
 
  
  /**
   * Gestionamos la pulsacion de un menu contextual
   */
  @Override
  public boolean onContextItemSelected(android.view.MenuItem item) {
          AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

          UniversidadBean ub = (UniversidadBean) getUniversidad((int) info.id);
          
          switch (item.getItemId()) {
          
          case R.id.link:
              
          	if(ub.getEnlace()!=null){
        			listener.onUniversidadLink(ub.getEnlace());
        	
          	}

              return true;

          case R.id.rutaGoogle:


          	if(Double.valueOf(ub.getLatitud())!=null && Double.valueOf(ub.getLongitud())!=null){
          		rutaGoogleNavigation(ub.getLatitud(),ub.getLongitud());
          	}

              return true;
              
          case R.id.mapa:

        	   listener.onUniversidadMapa(ub.getLatitud(),ub.getLongitud(),ub.getNombre(),ub.getDescripcion(),ub.getIdImagen(),"");

                return true;
                
          case R.id.share:

        	  showDialogShareButtonClick(ub);

           return true;
           
          case R.id.favorito:

          	   listener.onUniversidadFavorito(ub);

              return true;
              
          case R.id.panoramio:

        	    
          	panoramio(ub.getLatitud(),ub.getLongitud());
          	
          	return true;
          	
          case R.id.instagram:
        	
          	instagram(ub.getNombre());
          	
          	return true;

          default:
                  return super.onContextItemSelected(item);
          }
       
  }
  
  
  private void showDialogShareButtonClick(final UniversidadBean ub) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      
      builder.setTitle(getText(R.string.compartir).toString());
      
      final CharSequence[] choiceList = { getText(R.string.aplicacion).toString(),getText(R.string.contactos).toString(),getText(R.string.contactosSearch).toString() };

      builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
              buffKey = which;
          }
      }).setCancelable(false).setPositiveButton(getText(R.string.Aceptar).toString(), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              // set buff to selected
              selected = buffKey;
              if(selected==0){
            	listener.onUniversidadCompartir(ub);
              }
              if(selected==1){
            	listener.onUniversidadContactos(ub);
              }
              if(selected==2){
              	listener.onUniversidadContactosSearch(ub);
              }
          }
      }).setNegativeButton(getText(R.string.cancel).toString(), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.cancel();
          }
      });

      AlertDialog alert = builder.create();
      alert.show();
  }
  public void rutaGoogleNavigation(Double latitud,Double longitud){
      
      //lanzar la aplicacion "navigation" pasando como parametros las coordenadas del punto al que deseas ir

      Intent intentNavigate = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +latitud+","+longitud));

      intentNavigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      
      startActivity(intentNavigate);

  }
  
  public UniversidadBean getUniversidad(int position){
  	UniversidadBean universidad=new UniversidadBean();
      
      if(listado!=null && listado.size()>0){
      	universidad=listado.get(position);
      }
      
      return universidad;
  }
  
  

@SuppressLint("DefaultLocale")
public void setSearchResult(String str) {
	  
	  	ArrayList<UniversidadBean> listadoAux=new ArrayList<UniversidadBean>();
	  	
	  	listadoAux=listado;
	  	listado=new ArrayList<UniversidadBean>();

		for (UniversidadBean temp : listadoAux) {
			if(temp!=null && temp.getNombre()!=null && str!=null){
				if (temp.getNombre().toString().toLowerCase(Locale.getDefault()).contains(str.toLowerCase(Locale.getDefault())) || temp.getDescripcion().toString().toLowerCase(Locale.getDefault()).contains(str.toLowerCase())) {
					listado.add(temp);
				}
			}
		}
		
		if(favoritos &&  listado.size()==0){
			
			favorito.setVisibility(View.VISIBLE);
			lstListado.setVisibility(View.GONE);
		}
		
		if(favoritos &&  listado.size()>0){
			
			favorito.setVisibility(View.GONE);
			lstListado.setVisibility(View.VISIBLE);
		}
		
		lstListado.setAdapter(new UniversidadAdapter(this));
	    registerForContextMenu(lstListado);
	    
	}
  
  	public boolean isFavorito(Integer id){
	    
		boolean isFavorito=false;
	
		//Obtiene el objeto de preferencias de la aplicacion llamado favoritos
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("favoritos", 0);
		   
		//Obtiene un string almacenado en las preferencias de nombre favoritos.
		//El segundo parametro indica el valor a devolver si no lo encuentra, en este caso, ""
		String favoritos = sharedPreferences.getString("favoritos", "");

		StringTokenizer tokens=new StringTokenizer(favoritos,"/");

		while(tokens.hasMoreTokens()){
			
					if(tokens.nextToken().toString().equals(id.toString())){
						isFavorito=true;
						break;
					}
		}
		
		return isFavorito;
 }
  
  public void panoramio(Double latitud,Double longitud){
	    
	  Intent intent = new Intent();
      intent.setClass(getActivity(), PanoramioActivity.class);
      String url="http://www.panoramio.com/map/#lt="+latitud.toString()+"&ln="+longitud.toString()+"&z=1";
      intent.putExtra(Constants.EXTRA_URL,url);
      intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
      startActivity(intent);
  }
  
  public void instagram(String tag){
	   
	 //comprobar conexion a internet
	  if(NetWorkStatus.getInstance(getActivity()).isOnline()){
  		
  	  Intent intent = new Intent();
	      intent.setClass(getActivity(), InstagramActivity.class);
	      intent.putExtra(Constants.EXTRA_TAG,tag);
	      intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	      startActivity(intent);
	      	
  	}else{

          String soporte=getText(R.string.sin_conexion).toString();
          AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
          dialog.setTitle(getText(R.string.sin_conexion).toString());
          dialog.setMessage(soporte);
          dialog.show();
          
  	}
		
  }
  
  public void setColor(int color){
	  
	  this.currentColor=color;
  }

  
}