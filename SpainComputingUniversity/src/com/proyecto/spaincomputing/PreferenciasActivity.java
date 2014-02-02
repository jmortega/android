package com.proyecto.spaincomputing;





import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PreferenciasActivity extends PreferenceActivity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            onCreatePreferenceActivity();
        } else {
            onCreatePreferenceFragment();
        }
		
	}
	
	/**
     * Wraps legacy {@link #onCreate(Bundle)} code for Android < 3 (i.e. API lvl
     * < 11).
     */
    @SuppressWarnings("deprecation")
    private void onCreatePreferenceActivity() {
    	
    	PreferenceManager prefManager=getPreferenceManager();
    	prefManager.setSharedPreferencesName("appPreferences");
    	
        addPreferencesFromResource(R.xml.preferencias);
    }

    /**
     * Wraps {@link #onCreate(Bundle)} code for Android >= 3 (i.e. API lvl >=
     * 11).
     */
    @SuppressLint("NewApi")
    private void onCreatePreferenceFragment() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment ())
                .commit();
    }
    
    @SuppressLint("NewApi")
	public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            
            PreferenceManager prefManager=getPreferenceManager();
        	prefManager.setSharedPreferencesName("appPreferences");
        	
            addPreferencesFromResource(R.xml.preferencias);
            
        }
    }

	// Not using options menu in this tutorial
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_listado, menu);
        return true;
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  		NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class));
        
        break;
			
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }

}