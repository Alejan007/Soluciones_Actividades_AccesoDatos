package org.palomafp.aadd.inm.dao;

import java.util.List;

import org.palomafp.aadd.inm.model.Pais;

/**
 *
 * IPaisDAO: Interfaz que contiene los métodos para obtener información de países desde un origen de datos.
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 26 sept 2026
 * 
 * 
 */

public interface IPaisDAO {
	Pais obtenerPaisPorID(int identificador);

	List<Pais> obtenerPaisesPorContinente(String codContinente); 
}

