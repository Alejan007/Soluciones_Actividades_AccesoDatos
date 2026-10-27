package org.palomafp.aadd.inm.dao;

import java.util.List;

import org.palomafp.aadd.inm.model.FamiliaProfesional;

/**
 *
 * IFamiliaProfesionalDAO: Interfaz que contiene los métodos para obtener información de continentes desde un origen de datos.
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 26 sept 2026
 * 
 * 
 */

public interface IFamiliaProfesionalDAO {
	List<FamiliaProfesional> obtenerListaFamiliasProfesionales();
	FamiliaProfesional obtenerFamiliaProfesionalPorClave(String codFamiliaProfesional);
	void insertarFamiliaProfesional(FamiliaProfesional familiaProfesional);
	void actualizarFamiliaProfesional(FamiliaProfesional familiaProfesional);
	void borrarFamiliaProfesional(String codFamiliaProfesional);
}

