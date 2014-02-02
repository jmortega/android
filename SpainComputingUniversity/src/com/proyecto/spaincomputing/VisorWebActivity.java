package com.proyecto.spaincomputing;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.proyecto.spaincomputing.utils.Constants;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class VisorWebActivity extends ActionMainActivity {
	 
	private WebView viewer;
    private ProgressBar progressBar;
    private TextView cargandoTextView;
    private TextView urlTextView;
    
    private ProgressBar pbarProgreso;
    
    private ProgressDialog pDialog;
    private Boolean completado=false;
    
    private final static String KEY_HIGH_SCORE = "KEY_HIGH_SCORE";

    private Activity activity = this;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	 
		super.onCreate(savedInstanceState);
				 
		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){
			 
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.www));
			 }
		 }
	        
		 //Activamos el modo fullscreen
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 
        setContentView(R.layout.visor_web);
        
       
        
        Intent launchingIntent = getIntent();
        String content = launchingIntent.getData().toString();
        viewer = (WebView) findViewById(R.id.visorWebView);
        
        viewer.setWebViewClient(new myWebClient());
        viewer.getSettings().setJavaScriptEnabled(true);
        viewer.getSettings().setBuiltInZoomControls(true);
        viewer.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        viewer.addJavascriptInterface(new JavaScriptInterface(), "android");
        
        viewer.loadUrl(content);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pbarProgreso = (ProgressBar)findViewById(R.id.pbarProgreso);
        
        cargandoTextView = (TextView) findViewById(R.id.cargandoProgress);
        urlTextView = (TextView) findViewById(R.id.url_text);
        urlTextView.setText(content);
        
        enableHttpResponseCache();
        
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        //si hay conexion ejecutar la asynctask pasándole por parámetro la url
        if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask().execute(content);
        } 

       
		
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
        	
        	NavUtils.navigateUpTo(this, new Intent(this, ListadoActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
    		
            return true;
        }
        return true;
    }
	
	
	public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            
            activity.setProgressBarIndeterminateVisibility(true);
        }
 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            
            return true;
 
        }
 
        @Override
        public void onPageFinished(WebView view, String url) {
        	
            super.onPageFinished(view, url);
 
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            
            progressBar.setVisibility(View.GONE);
            
            cargandoTextView.setText(R.string.carga_ok);
            
            completado=true;
            
            activity.setProgressBarIndeterminateVisibility(false);
            
        }
    }
	
	// To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && viewer.canGoBack()) {
        	viewer.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    


    private class DownloadWebpageTask  extends AsyncTask<String, Integer, Boolean> {
    	

    	private int count=0;

    	@SuppressLint("SdCardPath")
		@Override
    	protected Boolean doInBackground(String... params) {
 
    		
    		// params comes from the execute() call: params[0] is the url.
      
             try {
            	 
        		URL url=new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000 /* milliseconds */);
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                

        		// getting file length
        		int lengthOfFile2=connection.getContentLength();
        		// input stream to read file - with 8k buffer
        		InputStream input =new BufferedInputStream(url.openStream(),8192);
        		// Output stream to write file
                OutputStream output = new FileOutputStream("/data/data/com.proyecto.spaincomputing/temp.xml");
        		byte data[]= new byte[1024];
        		long total=0;
        		
        		System.out.println("lengthOfFile2---"+lengthOfFile2);
        		
        		while((count = input .read(data))!= -1 && !completado){
        			
        			total+=count;
        			int progress = (int)((total*100)/lengthOfFile2) ;
        			publishProgress(progress);
        			System.out.println("update progress---"+progress);
        			System.out.println("update total---"+total);
        			output.write(data, 0, count);
        			
        			if(isCancelled())
        				break;
        		}
        		
        		// flushing output
              output.flush();
              // closing streams
              output.close();
              input.close();
        		
        		
        	} catch (MalformedURLException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        		cancel(true);
        		return false;
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        		cancel(true);
        		return false;
        	}
            	
             if(completado){
        			publishProgress(100);
             }
             
             return true;
   
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		int progreso = values[0].intValue();
    		
    		pDialog.setProgress(progreso);
    		
    		pbarProgreso.setProgress(progreso);
    	}
    	
    	@Override
    	protected void onPreExecute() {
    		
            pDialog = new ProgressDialog(VisorWebActivity.this);
    		//pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		
    		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		
    		
    		pDialog.setMessage("Procesando...");
    		pDialog.setCancelable(true);
    		pDialog.setMax(100);
    		
    		pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					DownloadWebpageTask .this.cancel(true);
				}
			});
    		
    		pDialog.setProgress(0);
    		pDialog.show();
    		
    		pbarProgreso.setMax(100);
    		pbarProgreso.setProgress(0);

    	}
    	
    	@SuppressLint("SdCardPath")
		@SuppressWarnings("resource")
		@Override
    	protected void onPostExecute(Boolean result) {
    		
    		pDialog.setProgress(100);
     		
     		pbarProgreso.setProgress(100);

     		File yourFile = new File("/data/data/com.proyecto.spaincomputing/temp.xml");
     	    try {
				@SuppressWarnings("unused")
				InputStream input =  new BufferedInputStream(new FileInputStream(yourFile), 8086);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
     	     
    		if(result)
    		{
    			try { 
    				Thread.sleep(1000); 
    			} catch(InterruptedException e) {}
    			
    			pDialog.dismiss();
    			mostrarToast("Carga finalizada");
    		}
    		
    		
    	}
    	
    	@Override
    	protected void onCancelled() {
    		Toast.makeText(VisorWebActivity.this, "Carga cancelada", Toast.LENGTH_SHORT).show();
    		mostrarToast("Carga cancelada");
    	}
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
    
    final class JavaScriptInterface {
		JavaScriptInterface() {
		}

		public int getScreenWidth() {
			return viewer.getWidth();
		}

		public int getScreenHeight() {
			// Removing 5 pixels to prevent vertical scrolling.
			return viewer.getHeight() - 5;
		}

		public int getHighScore() {
			@SuppressWarnings("deprecation")
			SharedPreferences preferences = getPreferences(MODE_WORLD_WRITEABLE);
			return preferences.getInt(KEY_HIGH_SCORE, 0);
		}

		public void setHighScore(int value) {
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putInt(KEY_HIGH_SCORE, value);
			editor.commit();
		}

	}
    
    private void enableHttpResponseCache() {
    	  try {
    	    long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
    	    File httpCacheDir = new File(getCacheDir(), "http");
    	    Class.forName("android.net.http.HttpResponseCache")
    	         .getMethod("install", File.class, long.class)
    	         .invoke(null, httpCacheDir, httpCacheSize);
    	  } catch (Exception httpResponseCacheNotAvailable) {
    	    Log.d("TAG", "HTTP response cache is unavailable.");
    	  }
    }
}
