package com.proyecto.spaincomputing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class UniversityDetailActivity extends FragmentActivity {
	private String url = "";
	private String universidad = "";
	public final static String URL = "url";
	public final static String UNIVERSIDAD = "universidad";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_university_detail);		
		Intent intent = getIntent();
		url = intent.getStringExtra(URL);
		universidad=intent.getStringExtra(UNIVERSIDAD);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_compartir, menu);		
		return true;
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
	
	
	public String getURLSelected(){
		return url;
	}
}
