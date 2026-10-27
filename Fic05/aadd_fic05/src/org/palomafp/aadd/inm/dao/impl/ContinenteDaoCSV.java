package org.palomafp.aadd.inm.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.palomafp.aadd.inm.dao.IContinenteDAO;
import org.palomafp.aadd.inm.model.Continente;

/**
 *
 * PaisDaoText: Clase que realiza la implementación de la interfaz IContinenteDAO para
 * obtener información de continentes desde un origen de datos basado en texto.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 26 sept 2026
 * 
 * 
 */

public class ContinenteDaoCSV implements IContinenteDAO {
	private static final String RUTA_FICHERO = "C:\\Users\\ineva\\INM\\tmp\\aadd\\fic05\\informacion_continentes.csv";

	@Override
	public Continente obtenerContinentePorCodigo(String codigoContinente) {
		Continente continenteEncontrado = null;

		File ficheroContinentes= new File(RUTA_FICHERO);

		try (BufferedReader reader = new BufferedReader(new FileReader(ficheroContinentes));) {

			// Nos saltamos la primera línea que contiene los nombres de los campos.
			if (reader.ready()) {
				reader.readLine();
			}
			
			while (reader.ready()) {
				String linea = reader.readLine();

				String[] camposPais = linea.split("#");
				String codContinente = camposPais[1].trim().replace("\"", "");

				// Comprobamos si el código de continente coincide con el que estamos
				// buscando.
				if (codContinente.equals(codigoContinente)) {
					String nombreContinente = camposPais[2].trim().replace("\"", "");

					continenteEncontrado = new Continente();
					continenteEncontrado.setCodigo(codigoContinente);
					continenteEncontrado.setNombre(nombreContinente);
					
					break; // Salimos del bucle ya que hemos encontrado el continente.
				}
			}
		} catch (IOException e) {
			System.err.println(e);;
		}

		return continenteEncontrado;
	}

}
