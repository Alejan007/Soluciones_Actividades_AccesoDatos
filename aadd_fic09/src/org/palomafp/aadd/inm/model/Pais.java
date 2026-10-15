package org.palomafp.aadd.inm.model;

/**
 *
 * Pais Clase que se asocia a la información de un país.
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 24 sept 2026
 * 
 * 
 */

public class Pais {
	private int identificador;
	private String nombre;
	private String capital;
	
	private Continente continente;

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public Continente getContinente() {
		return continente;
	}

	public void setContinente(Continente continente) {
		this.continente = continente;
	}

	@Override
	public String toString() {
		return "Pais [identificador=" + identificador + ", nombre=" + nombre + ", capital=" + capital + ", continente="
				+ continente + "]";
	}
	
}

