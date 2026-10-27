package org.palomafp.aadd.inm.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.palomafp.aadd.inm.dao.IContinenteDAO;
import org.palomafp.aadd.inm.dao.IPaisDAO;
import org.palomafp.aadd.inm.model.Continente;
import org.palomafp.aadd.inm.model.Pais;

/**
 *
 * PaisDaoText: Clase que realiza la implementación de la interfaz IPaisDAO para
 * obtener información de países desde un origen de datos basado en texto.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 26 sept 2026
 * 
 * 
 */

public class PaisDaoText implements IPaisDAO {
	private static final String RUTA_FICHERO = "C:\\Users\\ineva\\INM\\tmp\\aadd\\fic04\\informacion_paises.txt";
	private IContinenteDAO continenteDao;
	
	public PaisDaoText(IContinenteDAO continenteDao) {
		this.continenteDao = (ContinenteDaoCSV) continenteDao;
	} 
	
	@Override
	public Pais obtenerPaisPorID(int identificador) {
		Pais paisEncontrado = null;

		File ficheroPaises = new File(RUTA_FICHERO);

		try (BufferedReader reader = new BufferedReader(new FileReader(ficheroPaises));) {

			while (reader.ready()) {
				String linea = reader.readLine();

				// Nos saltamos las líneas que comienzan con el carácter #, que son comentarios.
				if (linea.trim().startsWith("#")) {
					continue;
				}

				String[] camposPais = linea.split(",");

				String idPais = camposPais[1].trim();
				// Comprobamos si el identificador del país coincide con el que estamos
				// buscando.
				if (Integer.parseInt(idPais) == identificador) {
					String codigoContinente = camposPais[0].trim().replace("\"", "");
					String nombrePais = camposPais[2].trim().replace("\"", "");
					String capitalPais = camposPais[3].trim().replace("\"", "");

					paisEncontrado = obtenerPaisDeLinea(codigoContinente, Integer.parseInt(idPais), nombrePais, capitalPais);
					
					break; // Salimos del bucle ya que hemos encontrado el país.
				}
			}
		} catch (IOException e) {
			System.err.println(e);;
		}

		return paisEncontrado;
	}

	@Override
	public List<Pais> obtenerPaisesPorContinente(String codContinente) {
		List<Pais> listaPaisesEncontrados = null;

		File ficheroPaises = new File(RUTA_FICHERO);

		try (BufferedReader reader = new BufferedReader(new FileReader(ficheroPaises));) {

			while (reader.ready()) {
				String linea = reader.readLine();

				// Nos saltamos las líneas que comienzan con el carácter #, que son comentarios.
				if (linea.trim().startsWith("#")) {
					continue;
				}

				String[] camposPais = linea.split(",");

				String codigoContinente = camposPais[0].trim().replace("\"", "");
				if (codigoContinente.equals(codContinente)) {
					// La primera vez que entramos en este bloque, inicializamos la lista de países
					// encontrados.
					if (listaPaisesEncontrados == null) {
						listaPaisesEncontrados = new ArrayList<Pais>();
					}
					String idPais = camposPais[1].trim();
					String nombrePais = camposPais[2].trim().replace("\"", "");
					String capitalPais = camposPais[3].trim().replace("\"", "");

					Pais pais = obtenerPaisDeLinea(codigoContinente, Integer.parseInt(idPais), nombrePais, capitalPais);

					listaPaisesEncontrados.add(pais);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listaPaisesEncontrados;
	}
	private Pais  obtenerPaisDeLinea(String codigoContinente, int identificador, String nombrePais, String capitalPais) {
		// Obtenemos el objeto Continente a partir del código.
		Continente continente = continenteDao.obtenerContinentePorCodigo(codigoContinente);

		Pais pais = new Pais();
		pais.setIdentificador(identificador);
		pais.setNombre(nombrePais);
		pais.setCapital(capitalPais);
		pais.setContinente(continente);

		return pais;
	}
}
