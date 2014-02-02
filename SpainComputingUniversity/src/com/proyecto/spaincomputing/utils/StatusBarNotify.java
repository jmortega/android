package com.proyecto.spaincomputing.utils;

import com.proyecto.spaincomputing.FavoritosActivity;
import com.proyecto.spaincomputing.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class StatusBarNotify{

    private static StatusBarNotify instance  = new StatusBarNotify();
    static Context               context;

    //barra de nofiticacion
    private static String ns;
    private static NotificationManager notificationManager;
    
    public static StatusBarNotify getInstance(Context ctx) {
        context = ctx;

        //barra de notificacion
        ns = Context.NOTIFICATION_SERVICE;
        notificationManager =(NotificationManager) context.getSystemService(ns);
        
        return instance;
    }


    /**
    * Muestra mensaje en la barra de notificacion indicando la accion realizada por el usuario
    * @param opcion String indica el tipo de accion realizada por el usuario
    * @param universidad String Nombre de la universidad a mostrar en la notificacion
    * @param descripcion String Descripcion de la universidad a mostrar en la notificacion
    */
   public void statusBarNotify(String opcion,String universidad,String descripcion) {
       
    CharSequence aviso = "";
    CharSequence titulo = "";
    
    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.favorito);
    
    if(opcion!=null && opcion.equals("nuevo_favorito")){
        aviso  =  context.getText(R.string.registro_favorito);
        titulo =  context.getText(R.string.registro_favorito_text)+" "+universidad;
    }
    
    if(opcion!=null && opcion.equals("eliminado_favorito")){
        aviso  =  context.getText(R.string.eliminado_favorito);
        titulo =  context.getText(R.string.eliminado_favorito_text)+" "+universidad;
    }
    

   // Definimos los detalles del aviso
    CharSequence texto = descripcion;

    Intent notificationIntent = new Intent(context,FavoritosActivity.class);
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
    
    
    //Asociar un intent a la notificación
    TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
    stackBuilder.addParentStack(FavoritosActivity.class);
    stackBuilder.addNextIntent(notificationIntent);
    PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    
    
    NotificationCompat.Builder myNotification = new NotificationCompat.Builder(context)
    .setContentTitle(titulo)
    .setContentText(texto)
    .setTicker(aviso)
    .setWhen(System.currentTimeMillis())
    .setContentIntent(contentIntent)
    .setDefaults(Notification.DEFAULT_SOUND)
    .setAutoCancel(true)
    .setSmallIcon(R.drawable.favorito)
    .setLargeIcon(largeIcon);

    myNotification.setContentIntent(resultPendingIntent);
    
    /*Create notification with builder*/
	Notification notification=myNotification.build();
	
	/*sending notification to system.Here we use unique id (when)for making different each notification
	 * if we use same id,then first notification replace by the last notification*/
	notificationManager.notify((int) System.currentTimeMillis(), notification);
     
   }

}
