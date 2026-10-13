package org.palomafp.aadd.inm.dao;

import java.util.List;

import org.palomafp.aadd.inm.model.Continente;

/**
 *
 * IPaisDAO: Interfaz que contiene los métodos para obtener información de continentes desde un origen de datos.
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 26 sept 2026
 * 
 * 
 */

public interface IContinenteDAO {
	List<Continente> obtenerListaContinentes();
	Continente obtenerContinentePorCodigo(String codigoContinente);
	void insertarContinente(Continente continente);
	void actualizarContinente(Continente continente);
	void borrarContinente(String codigoContinente);
}

