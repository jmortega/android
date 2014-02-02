package com.proyecto.spaincomputing;

import com.proyecto.spaincomputing.fragment.ContactsListFragment;
import com.proyecto.spaincomputing.utils.Constants;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;

/**
 * FragmentActivity to hold the main {@link ContactsListFragment}. On larger screen devices which
 * can fit two panes also load {@link ContactDetailFragment}.
 */
public class ContactsListActivity extends FragmentActivity implements ContactsListFragment.OnContactsInteractionListener {


    // True if this activity instance is a search result view (used on pre-HC devices that load
    // search results in a separate instance of the activity rather than loading results in-line
    // as the query is typed.
    private boolean isSearchResultView = false;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
  
        super.onCreate(savedInstanceState);

        // Set main content view. On smaller screen devices this is a single pane view with one
        // fragment. One larger screen devices this is a two pane view with two fragments.
        setContentView(R.layout.contacts_activity_main);
        
        // Check if this activity instance has been triggered as a result of a search query. This
        // will only happen on pre-HC OS versions as from HC onward search is carried out using
        // an ActionBar SearchView which carries out the search in-line without loading a new
        // Activity.
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

            // Fetch query from intent and notify the fragment that it should display search
            // results instead of all contacts.
            String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            ContactsListFragment mContactsListFragment = (ContactsListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.contact_list);

            // This flag notes that the Activity is doing a search, and so the result will be
            // search results rather than all contacts. This prevents the Activity and Fragment
            // from trying to a search on search results.
            isSearchResultView = true;
            mContactsListFragment.setSearchQuery(searchQuery);

            // Set special title for search results
            String title = getString(R.string.contacts_list_search_results_title, searchQuery);
            setTitle(title);
        }

    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact has been selected.
     *
     * @param contactUri The contact Uri to the selected contact.
     */
    @Override
    public void onContactSelected(Uri contactUri) {
        
    	Intent result = new Intent();
        
    	String name = "";
    	int idx;
    	Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
    	if (cursor.moveToFirst()) {

    	    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    	    name = cursor.getString(idx);

    	}
    	
       // Guardamos el email en un extra para tenerlo disponible en la actividad llamante
        result.putExtra(Constants.EXTRA_EMAIL, name);
        setResult(Activity.RESULT_OK, result);
 
        // Cierra la actividad y devuelve el control al llamador
        finish();
    }


    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }


	@Override
	public void onSelectionCleared() {
		// TODO Auto-generated method stub
		
	}


}
