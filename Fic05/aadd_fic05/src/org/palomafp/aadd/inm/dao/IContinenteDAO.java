package org.palomafp.aadd.inm.dao;

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
	Continente obtenerContinentePorCodigo(String codigoContinente);
}

