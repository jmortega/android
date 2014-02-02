package com.proyecto.spaincomputing.fragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.UniversityDetailActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class UniversidadInfoFragment extends Fragment {
	
	private WebView webView;
	private ProgressDialog pDialog;
	private MiTareaAsincronaDialog tarea2;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_universidad_info, container, false);
		webView = (WebView)view.findViewById(R.id.fragmento_web_wiew);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity() instanceof UniversityDetailActivity) {
			String url = ((UniversityDetailActivity)getActivity()).getURLSelected();
			loadWebViewContent(url);
		}
	}
	
	public void loadWebViewContent(String url) {
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
			@Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return true;
		    }			
		});
		
		
		pDialog = new ProgressDialog(getActivity());
		//pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		pDialog.setMessage("Procesando...");
		pDialog.setCancelable(true);
		pDialog.setMax(100);
		
		tarea2 = new MiTareaAsincronaDialog();
		tarea2.execute(url);
		
	}
	
	private class MiTareaAsincronaDialog extends AsyncTask<String, Integer, Boolean> {
    	

    	private int count=0;

    	@SuppressLint("SdCardPath")
		@Override
    	protected Boolean doInBackground(String... params) {
 
    		try {
				URL url=new URL(params[0]);
				URLConnection connection=url.openConnection();
				connection.connect();
				// getting file length
				int lengthOfFile2=connection.getContentLength();
				// input stream to read file - with 8k buffer
				InputStream input =new BufferedInputStream(url.openStream(),8192);
				// Output stream to write file
                OutputStream output = new FileOutputStream("/data/data/com.proyecto.spaincomputing/temp.xml");
				byte data[]= new byte[1024];
				long total=0;
				
				System.out.println("lengthOfFile2---"+lengthOfFile2);
				
				while((count = input .read(data))!= -1){
					
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				cancel(true);
			}
    		

    		return true;
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		int progreso = values[0].intValue();
    		
    		pDialog.setProgress(progreso);
    		
    	}
    	
    	@Override
    	protected void onPreExecute() {
    		
            pDialog = new ProgressDialog(getActivity());
    		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		
    		//pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		
    		pDialog.setMessage("Procesando...");
    		pDialog.setCancelable(true);
    		pDialog.setMax(100);
    		
    		pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					MiTareaAsincronaDialog.this.cancel(true);
				}
			});
    		
    		pDialog.setProgress(0);
    		pDialog.show();
    		
    		getActivity().setProgressBarIndeterminateVisibility(true);

    	}
    	
    	@SuppressLint("SdCardPath")
		@SuppressWarnings("resource")
		@Override
    	protected void onPostExecute(Boolean result) {
    		
    		pDialog.setProgress(100);
     		
     		File yourFile = new File("/data/data/com.proyecto.spaincomputing/temp.xml");
     	    try {
				@SuppressWarnings("unused")
				InputStream input =  new BufferedInputStream(new FileInputStream(yourFile), 8086);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
     	     
    		if(result!=null && result==true)
    		{
    			try { 
    				Thread.sleep(1000); 
    			} catch(InterruptedException e) {}
    			
    			pDialog.dismiss();
    			Toast.makeText(getActivity(), "Carga finalizada", Toast.LENGTH_SHORT).show();
    			mostrarToast("Carga finalizada");
    		}
    		
    		getActivity().setProgressBarIndeterminateVisibility(false);
    		
    	}
    	
    	@Override
    	protected void onCancelled() {
    		Toast.makeText(getActivity(), "Carga cancelada", Toast.LENGTH_SHORT).show();
    		mostrarToast("Carga cancelada");
    	}
    }

	private void mostrarToast(String text){
	
	Toast toast=new Toast(getActivity());
    LayoutInflater inflater=getActivity().getLayoutInflater();
    View layout=inflater.inflate(R.layout.layout_toast,(ViewGroup)getActivity().findViewById(R.id.lytLayout));
    TextView texto=(TextView)layout.findViewById(R.id.txtMensaje);
    texto.setText(text);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(layout);
    toast.show();
	}
}
