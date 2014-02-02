package com.proyecto.spaincomputing.entity;

public class UniversidadBean {
	
	private int id; 
	private int idImagen; 
	private String nombre; 
	private String descripcion;
	private String enlace;
	private double latitud;
	private double longitud;
	private String tipo;
	private String grado;
	private double distancia;
	
	public UniversidadBean(int id,int idImagen, String nombre, String descripcion,
			String enlace,String tipo,String grado, double latitud, double longitud) {
		super();
		this.id=id;
		this.idImagen = idImagen;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.latitud = latitud;
		this.longitud = longitud;
		this.tipo = tipo;
		this.grado = grado;
	}
	
	public UniversidadBean(int id,int idImagen, String nombre, String descripcion,
			String enlace,String tipo,String grado, double latitud, double longitud,double distancia) {
		super();
		this.id=id;
		this.idImagen = idImagen;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.latitud = latitud;
		this.longitud = longitud;
		this.tipo = tipo;
		this.grado = grado;
		this.distancia = distancia;
	}
	
	public UniversidadBean() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getIdImagen() {
		return idImagen;
	}
	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEnlace() {
		return enlace;
	}
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	
}
