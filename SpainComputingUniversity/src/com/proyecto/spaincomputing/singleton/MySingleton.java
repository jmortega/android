package com.proyecto.spaincomputing.singleton;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.proyecto.spaincomputing.R;
import com.proyecto.spaincomputing.adapter.DBAdapter;
import com.proyecto.spaincomputing.entity.PlaceBean;
import com.proyecto.spaincomputing.entity.UniversidadBean;

public class MySingleton extends Application
{
  private static MySingleton instance;
   
  public String customVar;
  public static Context context;
  private ArrayList<PlaceBean> places;
  private RequestQueue requestQueue;
  private ArrayList<UniversidadBean> listado=new ArrayList<UniversidadBean>();
  private LruCache<PlaceBean, Bitmap> thumbnails;
  private DBAdapter dataBase;
  
  private static final int CACHE_SIZE_BYTES = 4 * 1024 * 1024; //4 MB
	
  
  @Override public void onCreate() {
      super.onCreate();
      context = getApplicationContext();
      dataBase = new DBAdapter(context);		
      places = dataBase.getPlaces();

      setThumbnails(new LruCache<PlaceBean, Bitmap>(CACHE_SIZE_BYTES) {
          @Override
          protected int sizeOf(PlaceBean key, Bitmap bitmap) {
              return bitmap.getByteCount();

          }});     		
		
		requestQueue = Volley.newRequestQueue(this);
		requestQueue.start();
  }
  
  

  public static MySingleton getInstance()
  {
	 if (instance == null)
	    {
	      // Create the instance
	      instance = new MySingleton();
	  }
	  
    // Return the instance
    return instance;
  }
  
  //Constructor
  public MySingleton()
  {
	  instance = this;
  }
   
  public ArrayList<UniversidadBean> getUniversitiesList()
  {
	  listado=new ArrayList<UniversidadBean>();
	  
	  UniversidadBean universidadBean1=new UniversidadBean(1,R.drawable.ua, getText(R.string.universidad_alicante).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.eps.ua.es",getText(R.string.universidad_publica).toString(),getText(R.string.universidad_alicante_graduado).toString(), 38.38675, -0.51133);
	  UniversidadBean universidadBean2=new UniversidadBean(2,R.drawable.umh, getText(R.string.universidad_elche).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.umh.es",getText(R.string.universidad_publica).toString(),getText(R.string.umh_graduado).toString(),38.2790, -0.6872);
	  UniversidadBean universidadBean3=new UniversidadBean(3,R.drawable.uib, getText(R.string.universidad_illes_balears).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://eps.uib.es/",getText(R.string.universidad_publica).toString(),getText(R.string.uib_graduado).toString(),39.64121, 2.64539);
	  UniversidadBean universidadBean4=new UniversidadBean(4,R.drawable.upv_alcoy, getText(R.string.universidad_politecnica_valencia).toString(), getText(R.string.escuela_tecnica_superior_alcoy).toString(), "http://www.epsa.upv.es/",getText(R.string.universidad_publica).toString(),getText(R.string.upv_graduado).toString(),38.69486, -0.47596);
	  UniversidadBean universidadBean5=new UniversidadBean(5,R.drawable.upv, getText(R.string.universidad_politecnica_valencia).toString(), getText(R.string.escuela_tecnica_superior).toString(), "http://www.inf.upv.es/",getText(R.string.universidad_publica).toString(),getText(R.string.upv_graduado).toString(),39.48293, -0.34697);
	  UniversidadBean universidadBean6=new UniversidadBean(6,R.drawable.urjc, getText(R.string.universidad_rey_juan_carlos).toString(), getText(R.string.escuela_tecnica_superior_urjc).toString(), "http://www.etsii.urjc.es/",getText(R.string.universidad_publica).toString(),getText(R.string.urjc_graduado_juegos).toString(),40.33558, -3.87293);
	  UniversidadBean universidadBean7=new UniversidadBean(7,R.drawable.uji, getText(R.string.universidad_jaume_castellon).toString(), getText(R.string.escuela_tecnica_superior_uij).toString(), "http://www.uji.es/CA/uji/acad/",getText(R.string.universidad_publica).toString(),getText(R.string.uij_graduado_juegos).toString(),39.99270, -0.06752);
	  UniversidadBean universidadBean8=new UniversidadBean(8,R.drawable.uma, getText(R.string.universidad_malaga).toString(), getText(R.string.escuela_tecnica_superior).toString(), "http://www.informatica.uma.es/",getText(R.string.universidad_publica).toString(),getText(R.string.uma_graduado_computadores).toString(),36.71720, -4.47413);
	  UniversidadBean universidadBean9=new UniversidadBean(9,R.drawable.ucm, getText(R.string.universidad_complutense_madrid).toString(), getText(R.string.facultad_informatica).toString(), "http://www.fdi.ucm.es",getText(R.string.universidad_publica).toString(),getText(R.string.ucm_graduado_computadores).toString(),40.45282, -3.73349);
	  UniversidadBean universidadBean10=new UniversidadBean(10,R.drawable.ceu, getText(R.string.universidad_ceu).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://eps.uspceu.es/",getText(R.string.universidad_privada).toString(),getText(R.string.ucm_graduado_computadores).toString(),40.39970, -3.8354);
	  UniversidadBean universidadBean11=new UniversidadBean(11,R.drawable.upc, getText(R.string.universidad_upc).toString(), getText(R.string.escuela_tecnica_superior_manresa).toString(), "http://www.epsem.upc.edu/",getText(R.string.universidad_publica).toString(),getText(R.string.ucm_graduado_computadores).toString(),41.73730, 1.82879);
	  UniversidadBean universidadBean12=new UniversidadBean(12,R.drawable.eps_uam, getText(R.string.universidad_uam).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.ii.uam.es/",getText(R.string.universidad_publica).toString(),getText(R.string.uam_graduado_ingenieria_tic).toString(),40.54700, -3.69170);
	  UniversidadBean universidadBean121=new UniversidadBean(121,R.drawable.uam_eps, getText(R.string.universidad_uam).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.uam.es/ss/Satellite/EscuelaPolitecnica/es/estudios/grado-3/Page/contenidoFinal/grado-en-ingenieria-informatica-3.htm",getText(R.string.universidad_publica).toString(),getText(R.string.uam_ingenieria_informatica).toString(),40.54700, -3.69170);
	  UniversidadBean universidadBean13=new UniversidadBean(13,R.drawable.uza, getText(R.string.universidad_uza).toString(), getText(R.string.escuela_ingenieria_arquitectura).toString(), "http://eina.unizar.es/",getText(R.string.universidad_publica).toString(),getText(R.string.uza_graduado_ingenieria_tic).toString(),41.68381, -0.88732);
	  UniversidadBean universidadBean14=new UniversidadBean(14,R.drawable.euitt, getText(R.string.universidad_upm).toString(), getText(R.string.escuela_ingenieria_teleco).toString(), "http://www.etsit.upm.es/",getText(R.string.universidad_publica).toString(),getText(R.string.upm_graduado_ingenieria_tic).toString(),40.389012, -3.628718);
	  UniversidadBean universidadBean15=new UniversidadBean(15,R.drawable.upv, getText(R.string.universidad_politecnica_valencia).toString(), getText(R.string.escuela_ingenieria_teleco).toString(), "http://www.etsit.upv.es/",getText(R.string.universidad_publica).toString(),getText(R.string.upv_graduado_ingenieria_tic).toString(),39.47967, -0.34278);
	  UniversidadBean universidadBean16=new UniversidadBean(16,R.drawable.ucm, getText(R.string.universidad_complutense_madrid).toString(), getText(R.string.escuela_ingenieria_teleco).toString(), "http://www.fdi.ucm.es/grados/gradoIS/",getText(R.string.universidad_publica).toString(),getText(R.string.ucm_graduado_ingenieria_software).toString(),40.45278, -3.73356);
	  UniversidadBean universidadBean161=new UniversidadBean(16,R.drawable.ucm, getText(R.string.universidad_complutense_madrid).toString(), getText(R.string.facultad_informatica).toString(), "http://www.fdi.ucm.es/grados/gradoII/",getText(R.string.universidad_publica).toString(),getText(R.string.ucm_graduado_ingenieria_informatica).toString(),40.45278, -3.73356);
	  UniversidadBean universidadBean17=new UniversidadBean(17,R.drawable.umon, getText(R.string.universidad_mondragon).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.mondragon.edu/eps/",getText(R.string.universidad_privada).toString(),getText(R.string.ucm_graduado_ingenieria_software).toString(),43.061865, -2.497061);
	  UniversidadBean universidadBean18=new UniversidadBean(18,R.drawable.umon, getText(R.string.universidad_mondragon).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.mondragon.edu/eps/",getText(R.string.universidad_privada).toString(),getText(R.string.master_tic).toString(),43.061865, -2.497061);
	  UniversidadBean universidadBean19=new UniversidadBean(19,R.drawable.udima, getText(R.string.universidad_udima).toString(), getText(R.string.facultad_ensenyanza_tecnica).toString(), "http://www.udima.es/es/grado-ingenieria-informatica.html",getText(R.string.universidad_privada).toString(),getText(R.string.udima_ingenieria_informatica).toString(),40.63255, -4.00051);
	  UniversidadBean universidadBean20=new UniversidadBean(20,R.drawable.udima, getText(R.string.universidad_udima).toString(), getText(R.string.facultad_ensenyanza_tecnica).toString(), "http://www.udima.es/es/master-arquitectura-software.html",getText(R.string.universidad_privada).toString(),getText(R.string.udima_master_arquitectura_software).toString(),40.63255, -4.00051);
	  UniversidadBean universidadBean21=new UniversidadBean(21,R.drawable.udima, getText(R.string.universidad_udima).toString(), getText(R.string.facultad_ensenyanza_tecnica).toString(), "http://www.udima.es/es/grado-ingenieria-informatica.html",getText(R.string.universidad_privada).toString(),getText(R.string.udima_master_ingenieria_software).toString(),40.63255, -4.00051);
	  UniversidadBean universidadBean22=new UniversidadBean(22,R.drawable.uax, getText(R.string.universidad_uax).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.uax.es/que-estudiar/grados-en-la-uax/ingenierias/grado-en-ingenieria-informatica.html",getText(R.string.universidad_privada).toString(),getText(R.string.uax_ingenierio_informatica).toString(),40.45099, -3.98750);
	  UniversidadBean universidadBean23=new UniversidadBean(23,R.drawable.uax, getText(R.string.universidad_uax).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.uax.es/que-estudiar/grados-en-la-uax/ingenierias/grado-en-ingenieria-de-sistemas-de-informacion.html",getText(R.string.universidad_privada).toString(),getText(R.string.uax_ingenierio_sistemas).toString(),40.45099, -3.98750);
	  UniversidadBean universidadBean24=new UniversidadBean(24,R.drawable.uan, getText(R.string.universidad_uan).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.nebrija.com/la_universidad/facultades/escuela-politecnica-superior-arquitectura/index.htm",getText(R.string.universidad_privada).toString(),getText(R.string.uan_ingenierio_informatica).toString(),40.453352, -3.718885);
	  UniversidadBean universidadBean25=new UniversidadBean(25,R.drawable.uab, getText(R.string.universidad_uab).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.uab.es/escola-enginyeria/",getText(R.string.universidad_publica).toString(),getText(R.string.uab_ingenierio_informatica).toString(),41.50019, 2.11166);
	  UniversidadBean universidadBean26=new UniversidadBean(26,R.drawable.genesis, getText(R.string.universidad_uab).toString(), getText(R.string.escuela_ingeneria_cerda).toString(), "http://informatica.eug.es/es",getText(R.string.universidad_publica).toString(),getText(R.string.uab_informatica_servicios).toString(),41.90815, 2.071454);
	  UniversidadBean universidadBean27=new UniversidadBean(27,R.drawable.ucjc, getText(R.string.universidad_ucjc).toString(), getText(R.string.centro_universitario_tecnologia).toString(), "http://www.u-tad.com/es/",getText(R.string.universidad_privada).toString(),getText(R.string.ucjc_ingenieria_informatica).toString(),40.53951, -3.89329);
	  UniversidadBean universidadBean28=new UniversidadBean(28,R.drawable.ceu, getText(R.string.universidad_ceu).toString(), getText(R.string.escuela_superior_ensenyanza_tecnica).toString(), "http://www.uchceu.es/estudios/grado/ingenieria_sistemas_informacion.aspx",getText(R.string.universidad_privada).toString(),getText(R.string.ceu_ingenieria_informatica).toString(),39.542679, -0.385881);
	  UniversidadBean universidadBean29=new UniversidadBean(29,R.drawable.uc3m, getText(R.string.universidad_c3madrid).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.uc3m.es/portal/page/portal/escu_polit_sup",getText(R.string.universidad_publica).toString(),getText(R.string.uc3m_ingenierio_informatica).toString(),40.33256, -3.76576);
	  UniversidadBean universidadBean30=new UniversidadBean(30,R.drawable.ucam, getText(R.string.universidad_san_antonio).toString(), getText(R.string.escuela_universitaria_politecnica).toString(), "http://www.ucam.edu/estudios/grados/informatica-presencial",getText(R.string.universidad_privada).toString(),getText(R.string.ingenierio_informatica_san_antonio).toString(),37.991301, -1.185898);
	  UniversidadBean universidadBean31=new UniversidadBean(31,R.drawable.ucoruna, getText(R.string.universidad_coruna).toString(), getText(R.string.facultad_informatica).toString(), "http://www.fic.udc.es/MainPage.do",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_coruna).toString(),43.33293, -8.41091);
	  UniversidadBean universidadBean32=new UniversidadBean(32,R.drawable.alcala, getText(R.string.universidad_alcala).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www2.uah.es/etsii/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_alcala).toString(),40.51319, -3.34966);
	  UniversidadBean universidadBean33=new UniversidadBean(33,R.drawable.almeria, getText(R.string.universidad_almeria).toString(), getText(R.string.escuela_universitaria_ingenieria).toString(), "http://cms.ual.es/UAL/estudios/grados/GRADO4010",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_almeria).toString(),36.82968, -2.40469);
	  UniversidadBean universidadBean34=new UniversidadBean(34,R.drawable.barcelona, getText(R.string.universidad_barcelona).toString(), getText(R.string.facultad_matematicas).toString(), "http://www.mat.ub.edu/es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_ub).toString(),41.38681, 2.16472);
	  UniversidadBean universidadBean35=new UniversidadBean(35,R.drawable.burgos, getText(R.string.universidad_burgos).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.ubu.es/ubu/cm/eps?locale=es_ES&amp;textOnly=false",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_uburgos).toString(),42.343622, -3.735260);
	  UniversidadBean universidadBean36=new UniversidadBean(36,R.drawable.esi_cadiz, getText(R.string.universidad_cadiz).toString(), getText(R.string.escuela_universitaria_ingenieria).toString(), "http://www.uca.es/esingenieria/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_ucadiz).toString(),36.533549, -6.303344);
	  UniversidadBean universidadBean37=new UniversidadBean(37,R.drawable.cantabria, getText(R.string.universidad_cantabria).toString(), getText(R.string.facultad_ciencias).toString(), "http://www.unican.es/Centros/ciencias/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_ucadiz).toString(),43.471433, -3.801135);
	  UniversidadBean universidadBean38=new UniversidadBean(38,R.drawable.uclm, getText(R.string.universidad_uclm).toString(), getText(R.string.escuela_superior_informatica).toString(), "http://webpub.esi.uclm.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_creal).toString(),38.990368, -3.920883);
	  UniversidadBean universidadBean39=new UniversidadBean(39,R.drawable.uclm, getText(R.string.universidad_uclm).toString(), getText(R.string.escuela_superior_informatica).toString(), "http://www.esiiab.uclm.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_albacete).toString(),38.97879, -1.85673);
	  UniversidadBean universidadBean40=new UniversidadBean(40,R.drawable.cordoba, getText(R.string.universidad_cordoba).toString(), getText(R.string.escuela_superior_cordoba).toString(), "http://www.uco.es/eps/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),37.91435, -4.7193);
	  UniversidadBean universidadBean41=new UniversidadBean(41,R.drawable.extremadura, getText(R.string.universidad_extremadura).toString(), getText(R.string.centro_universitario_merida).toString(), "http://www.unex.es/conoce-la-uex/estructura-academica/centros/cum/titulaciones/grado",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_extremadura).toString(),38.907797, -6.338669);
	  UniversidadBean universidadBean42=new UniversidadBean(42,R.drawable.extremadura, getText(R.string.universidad_extremadura).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.unex.es/conoce-la-uex/estructura-academica/centros/epcc/titulaciones/grado",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_extremadura2).toString(),39.47901, -6.34279);
	  UniversidadBean universidadBean43=new UniversidadBean(43,R.drawable.girona, getText(R.string.universidad_girona).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.udg.edu/tabid/12337/Default.aspx?ID=3105G0710&language=ca-ES&IDE=99",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),41.96419, 2.82934);
	  UniversidadBean universidadBean44=new UniversidadBean(44,R.drawable.granada, getText(R.string.universidad_granada).toString(), getText(R.string.escuela_superior_iit).toString(), "http://etsiit.ugr.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_granada).toString(),37.19698, -3.62425);
	  UniversidadBean universidadBean45=new UniversidadBean(45,R.drawable.huelva, getText(R.string.universidad_huelva).toString(), getText(R.string.escuela_universitaria_ingenieria).toString(), "http://www.uhu.es/eps/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),37.20177, -6.9192);
	  UniversidadBean universidadBean46=new UniversidadBean(46,R.drawable.jaen, getText(R.string.universidad_jaen).toString(), getText(R.string.escuela_superior_jaen).toString(), "http://eps.ujaen.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),37.78718, -3.77784);
	  UniversidadBean universidadBean47=new UniversidadBean(47,R.drawable.laguna, getText(R.string.universidad_laguna).toString(), getText(R.string.escuela_tecnica_superior).toString(), "http://www.ull.es/view/centros/etsii/Inicio/es",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),28.48304, -16.32133);
	  UniversidadBean universidadBean48=new UniversidadBean(48,R.drawable.rioja, getText(R.string.universidad_rioja).toString(), getText(R.string.escuela_rioja).toString(), "http://www.ull.es/view/centros/etsii/Inicio/es",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),42.465607, -2.426546);
	  UniversidadBean universidadBean49=new UniversidadBean(49,R.drawable.canarias, getText(R.string.universidad_canarias).toString(), getText(R.string.escuela_canarias).toString(), "http://www.eii.ulpgc.es/tb_university_ex/?q=frontpage&destination=frontpage",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),28.073332, -15.451447);
	  UniversidadBean universidadBean50=new UniversidadBean(50,R.drawable.leon, getText(R.string.universidad_leon).toString(), getText(R.string.escuela_leon).toString(), "http://centros.unileon.es/eiii/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),42.61408, -5.56098);
	  UniversidadBean universidadBean51=new UniversidadBean(51,R.drawable.lleida, getText(R.string.universidad_lleida).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.eps.udl.cat/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica).toString(),41.60811, 0.62356);
	  UniversidadBean universidadBean52=new UniversidadBean(52,R.drawable.murcia, getText(R.string.universidad_murcia).toString(), getText(R.string.facultad_informatica).toString(), "http://www.um.es/informatica/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_murcia).toString(),38.02374, -1.17442);
	  UniversidadBean universidadBean53=new UniversidadBean(53,R.drawable.oviedo, getText(R.string.universidad_oviedo).toString(), getText(R.string.escuela_ingeneria).toString(), "http://www.ingenieriainformatica.uniovi.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_oviedo).toString(),43.354993, -5.851437);
	  UniversidadBean universidadBean54=new UniversidadBean(54,R.drawable.gijon, getText(R.string.universidad_oviedo).toString(), getText(R.string.escuela_gijon).toString(), "http://www.epigijon.uniovi.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_gijon).toString(),43.52439, -5.63441);
	  UniversidadBean universidadBean55=new UniversidadBean(55,R.drawable.zamora, getText(R.string.universidad_salamanca).toString(), getText(R.string.escuela_zamora).toString(), "http://poliz.usal.es/politecnica/v1r00/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_zamora).toString(),41.51182, -5.73517);
	  UniversidadBean universidadBean56=new UniversidadBean(56,R.drawable.salamanca, getText(R.string.universidad_salamanca).toString(), getText(R.string.facultad_ciencias).toString(), "http://informatica.usal.es/gii",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_salamanca).toString(),40.96055, -5.67060);
	  UniversidadBean universidadBean57=new UniversidadBean(57,R.drawable.compostela, getText(R.string.universidad_compostela).toString(), getText(R.string.escuela_universitaria_ingenieria).toString(), "http://www.usc.es/etse/grii",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_compostela).toString(),42.87408, -8.55712);
	  UniversidadBean universidadBean58=new UniversidadBean(58,R.drawable.sevilla, getText(R.string.universidad_sevilla).toString(), getText(R.string.escuela_tecnica_superior).toString(), "http://www.informatica.us.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_sevilla).toString(),37.36240, -5.98654);
	  UniversidadBean universidadBean59=new UniversidadBean(59,R.drawable.valladolid, getText(R.string.universidad_valladolid).toString(), getText(R.string.escuela_tecnica_superior).toString(), "https://www.inf.uva.es/",getText(R.string.universidad_publica).toString(),getText(R.string.ingenierio_informatica_valladolid).toString(),41.662717, -4.705378);
	  UniversidadBean universidadBean60=new UniversidadBean(60,R.drawable.uvic, getText(R.string.universidad_vic).toString(), getText(R.string.escuela_politecnica_superior).toString(), "http://www.uvic.es/es/estudi/multimedia",getText(R.string.universidad_privada).toString(),getText(R.string.multimedia_vic).toString(),41.930684, 2.245556);
	  UniversidadBean universidadBean61=new UniversidadBean(61,R.drawable.vigo, getText(R.string.universidad_vigo).toString(), getText(R.string.escuela_tecnica_superior).toString(), "http://www.esei.uvigo.es/",getText(R.string.universidad_publica).toString(),getText(R.string.multimedia_vic).toString(),42.16999, -8.68852);
		

	  listado.add(universidadBean1);listado.add(universidadBean2);listado.add(universidadBean3);listado.add(universidadBean4);listado.add(universidadBean5);
	  listado.add(universidadBean6);listado.add(universidadBean7);listado.add(universidadBean8);listado.add(universidadBean9);listado.add(universidadBean10);
	  listado.add(universidadBean11); listado.add(universidadBean12);listado.add(universidadBean13);listado.add(universidadBean14);listado.add(universidadBean15);
	  listado.add(universidadBean16);listado.add(universidadBean17);listado.add(universidadBean18);listado.add(universidadBean19);listado.add(universidadBean20);
	  listado.add(universidadBean21);listado.add(universidadBean22);listado.add(universidadBean23);listado.add(universidadBean24);listado.add(universidadBean25);
	  listado.add(universidadBean26);listado.add(universidadBean27);listado.add(universidadBean28);listado.add(universidadBean29);listado.add(universidadBean30);
	  listado.add(universidadBean31);listado.add(universidadBean32);listado.add(universidadBean33);listado.add(universidadBean34);listado.add(universidadBean35);
	  listado.add(universidadBean36);listado.add(universidadBean37);listado.add(universidadBean38);listado.add(universidadBean39);listado.add(universidadBean40);
	  listado.add(universidadBean41);listado.add(universidadBean42);listado.add(universidadBean43);listado.add(universidadBean44);listado.add(universidadBean45);
	  listado.add(universidadBean46);listado.add(universidadBean47);listado.add(universidadBean48);listado.add(universidadBean49);listado.add(universidadBean50);
	  listado.add(universidadBean51);listado.add(universidadBean52);listado.add(universidadBean53);listado.add(universidadBean54);listado.add(universidadBean55);
	  listado.add(universidadBean56);listado.add(universidadBean57);listado.add(universidadBean58);listado.add(universidadBean59);listado.add(universidadBean60);
	  listado.add(universidadBean61);listado.add(universidadBean121); listado.add(universidadBean161);
	  
	  return listado;
	  
  }

	public ArrayList<PlaceBean> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<PlaceBean> places) {
		this.places = places;
	}

	public LruCache<PlaceBean, Bitmap> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(LruCache<PlaceBean, Bitmap> thumbnails) {
		this.thumbnails = thumbnails;
	}
	
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public void setRequestQueue(RequestQueue requestQueue) {
		this.requestQueue = requestQueue;
	}
	
	public DBAdapter getDataBase() {
		return dataBase;
	}
	
	public ArrayList<PlaceBean> getPlacesDataBase() {
		places = dataBase.getPlaces();
		return places;
	}
}