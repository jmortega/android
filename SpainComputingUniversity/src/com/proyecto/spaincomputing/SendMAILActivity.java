package com.proyecto.spaincomputing;




import com.proyecto.spaincomputing.utils.Constants;
//import com.proyecto.spaincomputing.utils.GMailSender;
import com.proyecto.spaincomputing.utils.NetWorkStatus;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Actividad que muestra un formulario para enviar un MAIL a los contactos
 */
public class SendMAILActivity extends ActionMainActivity implements OnClickListener {
	
	//Contexto
	private Context context = this;

	//Controles formulario
	private EditText destinoTextView;
	private EditText mensajeTextView;
	private TextView asuntoTextView;
	private ImageView imgFoto;
	private CheckBox chkImagenAdjunta;
    private Button boton;

	private String destino="";
	private String asunto="";
	private String texto="";
	private String contact_search="";
	private Integer imagen;
	
	private LocationManager locManager;
    
	 /**
     * Crea la vista y los componentes
     * @param savedInstanceState Bundle
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar ab = getSupportActionBar();
		 
		 if(ab!=null){

			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ab.setTitle(getString(R.string.enviar_mail));
			 }
			 
		 }

		//Activamos el modo fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Bundle extras = getIntent().getExtras();
	      if(extras != null) {
	        	currentColor = extras.getInt(Constants.EXTRA_COLOR);
	      }
	      
        /*Drawable colorDrawable = new ColorDrawable(currentColor);
		Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });
		
        FadingActionBarHelper helper = new FadingActionBarHelper()
        	.actionBarBackground(layerDrawable)
        	.headerLayout(R.layout.header)
            .contentLayout(R.layout.enviar_mail);
        
        setContentView(helper.createView(this));
        helper.initActionBar(this);*/
        
		//Cargamos el layout
		setContentView(R.layout.enviar_mail);

		//inicializar controles
        initControls();
        
		//Obtenemos la ultima posicion conocida por el Proveedor de Red
        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        //obtenemos datos para mostrar en el mail
        if(extras != null) {
            texto= extras.getString(Constants.EXTRA_DATOS);
            imagen= extras.getInt(Constants.EXTRA_ID_IMAGEN);
            
            contact_search= extras.getString(Constants.EXTRA_CONTACT_SEARCH);
            
            asuntoTextView.setText("Spain Computing University");
            
            if(texto!=null){
                mensajeTextView.setText(texto.toString());
            
                imgFoto.setImageResource(imagen);
                
                chkImagenAdjunta.setChecked(true);
                chkImagenAdjunta.setClickable(true);
            }
        }
        
        boton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                destino =  destinoTextView.getText().toString();
                asunto = asuntoTextView.getText().toString();
                texto = mensajeTextView.getText().toString();
                
                enviaMAIL(destino,asunto, texto, imagen);
                
                
            }
        });
        
        //establezco listener para boton mostrar contactos
        findViewById(R.id.BotonMostrarContactos).setOnClickListener(this);

	}
	 
    /**
     * Se muestran las opciones de menú de la lista de lugares
     * 
     * @param menu
     *            Objeto menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_mail, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
	    
    /**
     * Definimos las acciones correspondientes con cada opción de menú
     * 
     * @param item
     *            MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
      
        	case android.R.id.home: 
	  		
        		NavUtils.navigateUpTo(this, new Intent(this, ListadoActivity.class).putExtra(Constants.EXTRA_COLOR, this.currentColor));
            
        		break;
            
            case R.id.btnEnviarMAIL:
                
                enviaMAIL(destino,asunto, texto,imagen);
                
                break;

        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
    * Metodo que realiza el envio con validación previa del formulario
    * @param String destino
    * @param String asunto
    * @param String mensaje
    * @param String foto
    */
    public void enviaMAIL(String destino, String asunto,String mensaje,Integer imagen) {

        boolean red_disponible = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        boolean redOnline=false;
        
        if (NetWorkStatus.getInstance(context).isOnline()) {
            
            redOnline=true;
        }

        
      //Comprobamos que los datos introducidos en el formulario son validos
        if (validarDatosFormulario()) {
            
        //comprobamos si hay red y esta Online
        
        if(red_disponible && redOnline){
            
                try {
                    
                    // Se envia el correo a traves del intent ACTION_SEND
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
          
                    //vamos a enviar texto plano a menos que el checkbox esté marcado
                    intent.setType("plain/text");
             
                    //colocamos los datos para el envío
                    if(destino!=null){
                        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{destino});
                    }
                    
                    if(asunto!=null){
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto.toString());
                    }
                    
                    if(mensaje!=null){
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje.toString());
                    }
             
                    //revisamos si el checkbox está marcado enviamos la imagen adjunta
                    if (chkImagenAdjunta!=null && chkImagenAdjunta.isChecked()) {

                        //colocamos la imagen adjunta en el stream
                        //intent.putExtra(Intent.EXTRA_STREAM, R.id.imagenFoto);
     
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://" + getPackageName() + "/" +imagen));
                        

                        //indicamos el tipo de dato
                        intent.setType("image/png");
                    }
             
                    //iniciamos la actividad de envio de mail
                    startActivityForResult(Intent.createChooser(intent, getText(R.string.app_name).toString()),Constants.ACTIVIDAD_MAIL);
                    
                    /*try {   
                        GMailSender sender = new GMailSender("username@gmail.com", "password");
                        sender.sendMail("This is Subject",   
                                "This is Body",   
                                "user@gmail.com",   
                                "user@yahoo.com");   
                    } catch (Exception e) {   
                        Log.e("SendMail", e.getMessage(), e);   
                    } */


                } catch (Exception e) {
                	
 
                    mostrarToast(getText(R.string.envio_mail_error).toString());

                }
        
            }else{
                
                mostrarToast(getText(R.string.conexion_internet_info).toString());
                
            }
        }else{

            mostrarToast(getText(R.string.comprobar_campos_mail).toString());
            
        }

    }
    
    
    /**
    * Metodo que valida los datos del formulario
    */
    public boolean validarDatosFormulario() {
        
        String destinoText = destinoTextView.getText().toString();
        String mensajeText = mensajeTextView.getText().toString();
        
        boolean result=false;
        
        //si el campo destino está vacio mostramos mensaje 
        if(destinoText==null || destinoText.equals("")){
            Toast.makeText(getApplicationContext(),R.string.informar_destinatario,Toast.LENGTH_LONG).show();
            mostrarToast(getText(R.string.informar_destinatario).toString());
        }
        
        //si el campo texto está vacio mostramos mensaje
        if(mensajeText==null || mensajeText.equals("")){
            Toast.makeText(getApplicationContext(),R.string.informar_mensaje,Toast.LENGTH_LONG).show();
            mostrarToast(getText(R.string.informar_mensaje).toString());
        }
        
        if(mensajeText!=null && !mensajeText.equals("") && destinoText!=null && !destinoText.equals("")){
            result=true;
        }
        
        return result;
    }


    /**
     * Metodo que llama a la activity de mostrar contactos
     */
    @Override
    public void onClick(View arg0) {

    	if(contact_search!=null && contact_search.equals("CONTACT_SEARCH")){
    		
    		Intent intent = new Intent(this,ContactsListActivity.class);
    		startActivityForResult(intent, Constants.ACTIVIDAD_CONTACTOS);
    		
    	}else{
    		
    		Intent intent = new Intent(this,MostrarContactosActivity.class);
    		intent.putExtra("contactos", "mail");
    		startActivityForResult(intent, Constants.ACTIVIDAD_CONTACTOS);
    	}
        
    }
    
    /**
     * Metodo que gestiona las respuestas de las actividades
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if ((requestCode == Constants.ACTIVIDAD_CONTACTOS) && (resultCode == Activity.RESULT_OK)) {
            try {
                String destino = data.getStringExtra(Constants.EXTRA_EMAIL);
                
                destino =destinoTextView.getText().toString()+destino+";";
                
                destinoTextView.setText(destino);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if ((requestCode == Constants.ACTIVIDAD_MAIL)) {
  
            	mostrarToast(getText(R.string.envio_mail_ok).toString());
            
        }
    }
    
    
    private void initControls(){

        //Recogemos los elementos de la interfaz que nos hacen falta para cargar los datos
        destinoTextView = (EditText) findViewById(R.id.Destino);
        mensajeTextView = (EditText) findViewById(R.id.Texto);
        asuntoTextView = (TextView) findViewById(R.id.Asunto);
        imgFoto = (ImageView) findViewById(R.id.imagenFoto);
        chkImagenAdjunta = (CheckBox) findViewById(R.id.checkImagenAdjunta);
        boton = (Button) findViewById(R.id.BotonEnviarMAIL);
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            return super.dispatchKeyEvent(event);
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
