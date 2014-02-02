package com.proyecto.spaincomputing;

import java.util.ArrayList;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.proyecto.spaincomputing.adapter.ViewPagerAdapter;
import com.proyecto.spaincomputing.entity.UniversidadBean;
import com.proyecto.spaincomputing.singleton.MySingleton;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.DepthPageTransformer;
//import com.proyecto.spaincomputing.utils.ZoomOutPageTransformer;

public class ViewPagerActivity extends ActionMainActivity{

	// Declare Variables
	private ViewPager viewPager;
	private ViewPagerAdapter adapter;
	
	private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
	 
	private ArrayList<String> nombre=new ArrayList<String>();
	private ArrayList<String> descripcion=new ArrayList<String>();
	private ArrayList<String> titulo=new ArrayList<String>();
	private ArrayList<Integer> imagen=new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){
			 
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.page_view));
			 }
			 
			//Inflate the custom view
		     View customNav = LayoutInflater.from(this).inflate(R.layout.custom_view, null);

		     RadioGroup radioGroup= ((RadioGroup)customNav.findViewById(R.id.radio_nav));
		     
		     radioGroup.check(R.id.radio_nav_2);
		     
		      //Bind to its state change
		        ((RadioGroup)customNav.findViewById(R.id.radio_nav)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
		            @Override
		            public void onCheckedChanged(RadioGroup group, int checkedId) {

		                switch (checkedId) {
		                
		                
		                case R.id.radio_nav_1:
		
		                	Intent intent = new Intent();
		                    intent.setClass(getApplicationContext(), ListViewActivity.class);
		                    intent.putExtra(Constants.EXTRA_COLOR, currentColor);
		                    startActivity(intent);
		                    finish();
		                    

		                 break;
		                 
		                case R.id.radio_nav_2:
		                	
		                
		                    
		                 break;
		                 
		                }
		               
		            }
		        });

		        //Attach to the action bar
		       ab.setCustomView(customNav);
		 }
		 
		// Get the view from viewpager_main.xml
		setContentView(R.layout.viewpager_main);

		listado=inicializarDatos();
		
		for(int i=0;i<listado.size();i++){
			nombre.add(listado.get(i).getNombre());
			descripcion.add(listado.get(i).getDescripcion());
			titulo.add(listado.get(i).getGrado());
			imagen.add(listado.get(i).getIdImagen());
		}

		// Locate the ViewPager in viewpager_main.xml
		viewPager = (ViewPager) findViewById(R.id.pager);
		// Pass results to ViewPagerAdapter Class
		adapter = new ViewPagerAdapter(this, nombre, descripcion,titulo, imagen);

		// Binds the Adapter to the ViewPager
		viewPager.setAdapter(adapter);
		
		//efecto al pasar entre páginas
		viewPager.setPageTransformer(true, new DepthPageTransformer());
		//viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @SuppressLint("NewApi")
			@Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
               
            }
        });

	}

	// Not using options menu in this tutorial
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_view_pager, menu);

        menu.findItem(R.id.action_previous).setEnabled(viewPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE,Constants.MENU_PAGER, Menu.NONE,
                (viewPager.getCurrentItem() == adapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
        // Navigate "up" the demo structure to the launchpad activity.
        // See http://developer.android.com/design/patterns/navigation.html for more.
        NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class));
        
        break;

	  	case R.id.action_previous:
            // Go to the previous step in the wizard. If there is no previous step,
            // setCurrentItem will do nothing.
	  		viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return true;
            
	  	case R.id.action_last:
            // Go to the previous step in the wizard. If there is no previous step,
            // setCurrentItem will do nothing.
	  		viewPager.setCurrentItem(adapter.getCount() - 1);
            return true;
            
	  	case R.id.action_start:
            // Go to the previous step in the wizard. If there is no previous step,
            // setCurrentItem will do nothing.
	  		viewPager.setCurrentItem(0);
            return true;

        case Constants.MENU_PAGER:
            // Advance to the next step in the wizard. If there is no next step, setCurrentItem
            // will do nothing.
        	viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            return true;
            
        case  R.id.mapa:
	  		
        	UniversidadBean ub=listado.get(viewPager.getCurrentItem());
	  		verMapaViewPager(ub);
	  		
	  		break; 
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }
	
	public  ArrayList<UniversidadBean> inicializarDatos(){
		  
		  listado=MySingleton.getInstance().getUniversitiesList();
		  
		  return listado;
	}
	
	
}