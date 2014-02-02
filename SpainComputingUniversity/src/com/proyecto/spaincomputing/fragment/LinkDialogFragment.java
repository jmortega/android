package com.proyecto.spaincomputing.fragment;

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

import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.VisorWebActivity;
import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LinkDialogFragment extends DialogFragment {    
	
	private NoticeDialogListener listener;
	private String enlace;
	private TextView text_source_code;
    private TextView text_loading_source_code;
    private ProgressBar progressBar;
	
    protected int currentColor = 0xFFC74B46;
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
		try {
			listener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			Log.e("ERROR",Log.getStackTraceString(e));
		}
    }
   
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	LayoutInflater factory2 = LayoutInflater.from(getActivity());
        final View textEntryView2 = factory2.inflate(R.layout.custom_dialog, null);
        
        
        text_source_code = (TextView) textEntryView2.findViewById (R.id.source_code_text);
        text_loading_source_code = (TextView) textEntryView2.findViewById (R.id.source_code_loading);    
        Button btn = (Button) textEntryView2.findViewById (R.id.button_source_code);
        
        progressBar = (ProgressBar)textEntryView2.findViewById(R.id.progressBarSourceCode);
        
      //obtengo string enlace por parámetros
        Bundle args = getArguments();
        enlace=args.getString("ENLACE");  
        
        //btn.setOnClickListener(this);
        
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				new RequestTask().execute(enlace);

			}
		});
        
        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getText(R.string.latitud).toString());
        builder.setIcon(R.drawable.link);
        builder.setView(textEntryView2) ;
        builder.setMessage(enlace.toString())

               .setPositiveButton(getText(R.string.ir_al_sitio).toString(), new DialogInterface.OnClickListener() {

            	   @Override
            	   public void onClick(DialogInterface dialog, int which) {
                
            		   //listener.onDialogPositiveClick(LinkDialogFragment.this);
            	
            		   if(enlace!=null){
                    
            			   //comprobar conexion a internet
            			   if(enlace!=null && NetWorkStatus.getInstance(getActivity().getApplicationContext()).isOnline()){
                		
            				   String content = enlace.toString();
            				   Intent showContent = new Intent(getActivity(),VisorWebActivity.class);
            				   showContent.putExtra(Constants.EXTRA_COLOR, currentColor);
            				   showContent.setData(Uri.parse(content));
            				   startActivity(showContent);
                	    
            			   }else{

            				   	mostrarToast(getText(R.string.sin_conexion).toString());
            			   }
            		   }
            	
            	   }
               })
               
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   listener.onDialogNegativeClick(LinkDialogFragment.this);
                   }
               });
        
        return builder.create();
    }
    
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	private void mostrarToast(String text){
		
		Toast toast=new Toast(getActivity().getApplicationContext());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View layout=inflater.inflate(R.layout.layout_toast,(ViewGroup)getActivity().findViewById(R.id.lytLayout));
        TextView texto=(TextView)layout.findViewById(R.id.txtMensaje);
        texto.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
	}
	
	class RequestTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {			
			
			progressBar.setVisibility(View.VISIBLE);
			text_loading_source_code.setVisibility(View.VISIBLE);
			text_source_code.setVisibility(View.INVISIBLE);
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
            
        }
    }
	
	public void setColor(int color){
		  
		  this.currentColor=color;
	}
}
