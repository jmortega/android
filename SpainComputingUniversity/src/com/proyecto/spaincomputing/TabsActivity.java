package com.proyecto.spaincomputing;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.proyecto.spaincomputing.fragment.AboutFragment;
import com.proyecto.spaincomputing.fragment.PlacesListFragment;
import com.proyecto.spaincomputing.fragment.UniversidadesImagesFragment;
import com.proyecto.spaincomputing.fragment.UniversityListFragment;
import com.proyecto.spaincomputing.utils.Constants;

public class TabsActivity extends ActionMainActivity implements TabListener,android.support.v7.widget.SearchView.OnQueryTextListener {
	
	Fragment[] fragments = new Fragment[]{new UniversityListFragment(),new UniversidadesImagesFragment(),new PlacesListFragment(),new AboutFragment()};
	
	private SearchView mSearchView;

	private ArrayList<String> fragment_tags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){
			 ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
		 }
	        
		 //Activamos el modo fullscreen
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 
		 fragment_tags = new ArrayList<String>();
		 fragment_tags.add("list_fragment");
		 fragment_tags.add("images_fragment");
		 fragment_tags.add("places_fragment");
		 fragment_tags.add("about_fragment");
		 
		setContentView(R.layout.tabs_activity_main);

		fragments[0].setHasOptionsMenu(true);
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.addTab(
                actionBar.newTab()
                        .setText(getResources().getString(R.string.listado))
                        .setTabListener(this));
		
		actionBar.addTab(
                actionBar.newTab()
                        .setText(getResources().getString(R.string.imagenes))
                        .setTabListener(this));
		
		actionBar.addTab(
                actionBar.newTab()
                        .setText(getResources().getString(R.string.places))
                        .setTabListener(this));
		
		actionBar.addTab(
                actionBar.newTab()
                        .setText(getResources().getString(R.string.acercaDe))
                        .setTabListener(this));

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.mainContent, fragments[0],fragment_tags.get(0))
        		.add(R.id.mainContent, fragments[1],fragment_tags.get(1))
        		.add(R.id.mainContent, fragments[2],fragment_tags.get(2))
        		.add(R.id.mainContent, fragments[3],fragment_tags.get(3))
        	    .commit();
        
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {	
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		setContent(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
	
	public void setContent(int tab) {		
		Fragment toHide = null;
		Fragment toHide2 = null;
		Fragment toHide3 = null;
		Fragment toShow = null;
		switch (tab) {
			case 0:
				toHide = fragments[1];
				toHide2 = fragments[2];
				toHide3 = fragments[3];
				toShow = fragments[0];
				break;
			case 1:
				toHide = fragments[0];
				toHide2 = fragments[2];
				toHide3 = fragments[3];
				toShow = fragments[1];
				break;
			case 2:
				toHide = fragments[0];
				toHide2 = fragments[1];
				toHide3 = fragments[3];
				toShow = fragments[2];
				break;
			case 3:
				toHide = fragments[0];
				toHide2 = fragments[1];
				toHide3 = fragments[2];
				toShow = fragments[3];
				break;

		}
		
		FragmentManager manager = getSupportFragmentManager();
		
		manager.beginTransaction()
				.hide(toHide)
				.hide(toHide2)
				.hide(toHide3)
				.show(toShow)
				.commit();
	}
	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_listado_tabs, menu);
	

		SearchManager SManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        mSearchView.setSearchableInfo(SManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("Search...");
        mSearchView.setOnQueryTextListener(this);
		
        return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  		NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
         
         finish();
         
         break;
         
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }

	@Override
	public boolean onQueryTextSubmit(String query) {

		searchData(query);

	    return true;
	}

	@Override
	public boolean onQueryTextChange(String query) {

		searchData(query);
		
		return true;
	}
	
	public void searchData(String query){
		
		UniversityListFragment fragmentList = (UniversityListFragment)getSupportFragmentManager().findFragmentByTag("list_fragment");
		
		UniversidadesImagesFragment fragmentListImages = (UniversidadesImagesFragment)getSupportFragmentManager().findFragmentByTag("images_fragment");
		
		PlacesListFragment fragmentListPlaces = (PlacesListFragment)getSupportFragmentManager().findFragmentByTag("places_fragment");
		
		fragmentList.searchData(query);
		fragmentListImages.searchData(query);
		fragmentListPlaces.searchData(query);
		
	}
}
