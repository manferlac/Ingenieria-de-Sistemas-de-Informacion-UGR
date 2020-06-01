package myapphardware;

import java.util.ArrayList;

public class Componente {
	private String id;
	private String nombre;
	private String imagen;
	
	private ArrayList<OfertasComponentes> ofertas;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public ArrayList<OfertasComponentes> getOfertas() {
		return ofertas;
	}

	public void setOfertas(ArrayList<OfertasComponentes> ofertas) {
		this.ofertas = ofertas;
	}
	
	
}