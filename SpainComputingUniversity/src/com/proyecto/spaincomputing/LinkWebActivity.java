package com.proyecto.spaincomputing;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LinkWebActivity extends ActionMainActivity
{

    private String url="";
    private TextView text_source_code;
    private TextView text_loading_source_code;
    private ProgressBar progressBar;
    
    private Activity activity = this;
    
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        
       
        ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.link));
			 }
		 }	 
			 
		 
		 
        //Recogemos la url que se nos proporcionar a traves del intent de llamada
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            url = extras.getString(Constants.EXTRA_URL);
  
        }
        
        
        LayoutInflater factory2 = LayoutInflater.from(this);
        final View textEntryView2 = factory2.inflate(R.layout.custom_dialog, null);
        
        TextView text1 = (TextView) textEntryView2.findViewById (R.id.dialog_text); 
        text_source_code = (TextView) textEntryView2.findViewById (R.id.source_code_text);
        text_loading_source_code = (TextView) textEntryView2.findViewById (R.id.source_code_loading);    
        Button btn = (Button) textEntryView2.findViewById (R.id.button_source_code);
        
        progressBar = (ProgressBar)textEntryView2.findViewById(R.id.progressBarSourceCode);
        
        //btn.setOnClickListener(this);
        
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mostrarToast(url);
				
				new RequestTask().execute(url);

			}
		});
        
        text1.setText(url);
        
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
        alertDialog.setTitle(getText(R.string.link).toString());
        alertDialog.setIcon(R.drawable.link);
        alertDialog.setView(textEntryView2) ;
        alertDialog.setPositiveButton(getText(R.string.ir_al_sitio).toString(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            	if(url!=null){
                    
            		//compborar conexion a internet
            		if(url!=null && NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
                		
                		String content = url.toString();
                		Intent showContent = new Intent(getApplicationContext(),VisorWebActivity.class);
                		showContent.putExtra(Constants.EXTRA_COLOR, currentColor);
                	    showContent.setData(Uri.parse(content));
                	    startActivity(showContent);
                	    
                	}else{

                        mostrarToast(getText(R.string.sin_conexion).toString());
                	}
                }

            	finish();
            	
            }
        });
        alertDialog.setNegativeButton(getText(R.string.cancel).toString(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            	finish();


            }
        });
        alertDialog.show();
    }
  

    private void mostrarToast(String text){
		
		Toast toast=new Toast(getApplicationContext());
        LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.layout_toast,(ViewGroup)findViewById(R.id.lytLayout));
        TextView texto=(TextView)layout.findViewById(R.id.txtMensaje);
        texto.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
	}

    @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 
	  		
	  	NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
        
        break;
	
	  }
	  
	  return super.onOptionsItemSelected(item);
	 }
    
    class RequestTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {			
			
			progressBar.setVisibility(View.VISIBLE);
			text_loading_source_code.setVisibility(View.VISIBLE);
			text_source_code.setVisibility(View.INVISIBLE);
			
			activity.setProgressBarIndeterminateVisibility(true);
		}
		
		@Override
        protected String doInBackground(String... uri) {
        	// Create HTTP Client
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,3000); // 3s max for connection
			HttpConnectionParams.setSoTimeout(httpParameters, 4000); // 4s max to get data
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			
            HttpResponse response;
            String responseString = null;
            
            /*runOnUiThread(new Runnable() {

            	public void run() {

            	  Toast.makeText(getApplicationContext(), "Example for Toast", Toast.LENGTH_SHORT).show();

            	   }
            	});*/

            
            try {
            	//Execute uri
            	
            	progressBar.setVisibility(View.VISIBLE);
    			text_loading_source_code.setVisibility(View.VISIBLE);
    			
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                
                Log.i("status",String.valueOf(statusLine.getStatusCode()));
                
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                	
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            
            return responseString;
            
            
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            progressBar.setVisibility(View.GONE);
            
            text_loading_source_code.setVisibility(View.GONE);
            
            text_source_code.setVisibility(View.VISIBLE);
            
            //mostrarToast(result);
            
            text_source_code.setText(result);
            
            activity.setProgressBarIndeterminateVisibility(false);
            
        }
    }
    
    
}

