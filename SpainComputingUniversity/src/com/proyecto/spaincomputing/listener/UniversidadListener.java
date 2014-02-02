package com.proyecto.spaincomputing.listener;

import com.proyecto.spaincomputing.entity.UniversidadBean;


public interface UniversidadListener {
	  void onUniversidadSelected(UniversidadBean ub);
	  void onUniversidadLink(String link);
	  void onUniversidadCompartir(UniversidadBean ub);
	  void onUniversidadFavorito(UniversidadBean ub);
	  void onUniversidadContactos(UniversidadBean ub);
	  void onUniversidadContactosSearch(UniversidadBean ub);
	  void onUniversidadMapa(double latitud,double longitud,String nombre,String descripcion,int idImagen,String flag);
}