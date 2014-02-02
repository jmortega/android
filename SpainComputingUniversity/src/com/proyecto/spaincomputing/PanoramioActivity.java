package com.proyecto.spaincomputing;


import com.proyecto.spaincomputing.utils.Constants;
import com.proyecto.spaincomputing.utils.NetWorkStatus;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PanoramioActivity extends ActionMainActivity
{

    private String url="";
    
    public void onCreate(Bundle savedInstanceState) {
        
        
        super.onCreate(savedInstanceState);
 
        ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.panoramio));
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
        
        text1.setText(url);
        
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
        alertDialog.setTitle(getText(R.string.panoramio).toString());
        alertDialog.setIcon(R.drawable.panoramio);
        alertDialog.setView(textEntryView2) ;
        alertDialog.setPositiveButton(getText(R.string.ir_al_sitio).toString(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            	if(url!=null){
                    
            		//compborar conexion a internet
            		if(url!=null && NetWorkStatus.getInstance(getApplicationContext()).isOnline()){
                		
                		String content = url.toString();
                		Intent showContent = new Intent(getApplicationContext(),VisorWebActivity.class);
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
  

    @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		
	  switch (item.getItemId()) {
	  
	  	case android.R.id.home: 

        //NavUtils.navigateUpTo(this, new Intent(this, PrincipalActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
        
	  	Intent intent = new Intent(this, PrincipalActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra(Constants.EXTRA_COLOR, this.currentColor);
	    startActivity(intent);
        
        break;
	
	  }
	  
	  return super.onOptionsItemSelected(item);
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
    
}

