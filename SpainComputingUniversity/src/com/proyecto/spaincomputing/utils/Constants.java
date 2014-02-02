package com.proyecto.spaincomputing.utils;

import java.util.Arrays;
import java.util.List;

import android.view.Menu;

/**
 * Clase para constantes que se utilizan para las ventanas de dialogo y actividades
 */
public class Constants {

    public static final int DIALOGO_SALIR = 1;
    public static final int DIALOGO_INFO = 2;
    public static final int DIALOGO_BORRADO_LUGARES = 3;
    public static final int DIALOGO_BORRADO_LUGAR = 4;
    public static final int DIALOGO_VOICE = 5;
    public static final int DIALOGO_ENABLE_PROVIDER = 6 ;
    
    public static final int ACTIVIDAD_IDIOMA = 7;
    public static final int ACTIVIDAD_MAPA = 8;
    public static final int ACTIVIDAD_CAMARA = 9;
    public static final int ACTIVIDAD_GALERIA= 10;
    public static final int ACTIVIDAD_BUSQUEDA= 11;
    public static final int ACTIVIDAD_CONTACTOS= 12;
    public static final int ACTIVIDAD_TEST_CONEXION= 13;
    public static final int ACTIVIDAD_MAIL= 14;
    
    public static final int MENU_ELIMINAR = 15;
    public static final int MENU_EDITAR = 16;
    public static final int MENU_MOSTRAR = 17;
    public static final int MENU_ENLACE = 18;
    public static final int MENU_RUTA = 19;
    public static final int MENU_MAPA = 20;
    public static final int MENU_COMPARTIR = 21;
    public static final int MENU_FAVORITO = 22;
    public static final int MENU_APLICACION = 23;
    public static final int MENU_CONTACTOS = 24;
    public static final int MENU_PANORAMIO = 25;
    public static final int MENU_MAPA_DISTANCIA = 26;
    public static final int MENU_PAGER = 27;
    
    public static final int MENU_AR_VIEWER = Menu.FIRST + 1;
    public static final int MENU_INFO_VIEWER = Menu.FIRST + 2;
    public static final int MENU_ORDER_BY_ASC = Menu.FIRST + 3;
    public static final int MENU_ORDER_BY_DESC = Menu.FIRST + 4;

    public static final String EXTRA_ID = "com.proyecto.spaincomputing.fragments.EXTRA_ID";
    public static final String EXTRA_ID_IMAGEN = "com.proyecto.spaincomputing.fragments.EXTRA_ID_IMAGEN";
    public static final String EXTRA_NOMBRE = "com.proyecto.spaincomputing.fragments.EXTRA_NOMBRE";
    public static final String EXTRA_DESCRIPCION = "com.proyecto.spaincomputing.fragments.EXTRA_DESCRIPCION";
    public static final String EXTRA_ENLACE = "com.proyecto.spaincomputing.fragments.EXTRA_ENLACE";
    public static final String EXTRA_GRADO = "com.proyecto.spaincomputing.fragments.EXTRA_GRADO";
    public static final String EXTRA_TIPO = "com.proyecto.spaincomputing.fragments.EXTRA_TIPO";
    public static final String EXTRA_LATITUD = "com.proyecto.spaincomputing.fragments.EXTRA_LATIDUD";
    public static final String EXTRA_LONGITUD = "com.proyecto.spaincomputing.fragments.EXTRA_LONGITUD";
    public static final String EXTRA_DATOS = "com.proyecto.spaincomputing.fragments.EXTRA_DATOS";
    public static final String EXTRA_URL = "com.proyecto.spaincomputing.fragments.EXTRA_URL";
    public static final String EXTRA_MI_POSICION = "com.proyecto.spaincomputing.fragments.EXTRA_MI_POSICION";
    public static final String EXTRA_TAG= "com.proyecto.spaincomputing.fragments.EXTRA_TAG";
    public static final String EXTRA_FLAG= "com.proyecto.spaincomputing.fragments.EXTRA_FLAG";
    public static final String EXTRA_EMAIL= "com.proyecto.spaincomputing.fragments.EXTRA_EMAIL";
    public static final String EXTRA_CONTACT_SEARCH= "com.proyecto.spaincomputing.fragments.EXTRA_CONTACT_SEARCH";
    public static final String EXTRA_COLOR= "com.proyecto.spaincomputing.fragments.EXTRA_COLOR";
    public static final String EXTRA_LISTADO= "com.proyecto.spaincomputing.fragments.EXTRA_LISTADO";
    
    // Number of columns of Grid View
 	public static final int NUM_OF_COLUMNS = 3;

 	// Gridview image padding
 	public static final int GRID_PADDING = 8; // in dp

 	// SD card image directory
 	public static final String PHOTO_ALBUM = "Pictures/Screenshots";

 	// supported file formats
 	public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg","png");
}
