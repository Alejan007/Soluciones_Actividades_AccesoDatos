package org.palomafp.aadd.inm.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.palomafp.aadd.inm.dao.IContinenteDAO;
import org.palomafp.aadd.inm.gestores.GestorConfiguracion;
import org.palomafp.aadd.inm.model.Continente;

/**
 *
 * PaisDaoText: Clase que realiza la implementación de la interfaz
 * IContinenteDAO para obtener información de continentes desde un origen de
 * datos basado en texto.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 26 sept 2026
 * 
 * 
 */

public class ContinenteDaoCSV implements IContinenteDAO {
	private static final String RUTA_FICHERO = GestorConfiguracion.getRutaFicheroContinente();	
	private static final String SEPARADOR_CSV = GestorConfiguracion.getSeparadorCsvFicheroContinente();	
	private static final String RUTA_FICHERO_TEMPORAL = "archivo.tmp";

	@Override
	public Continente obtenerContinentePorCodigo(String codigoContinente) {
		Continente continenteEncontrado = null;

		File ficheroContinentes = new File(RUTA_FICHERO);

		try (BufferedReader reader = new BufferedReader(new FileReader(ficheroContinentes));) {

			// Nos saltamos la primera línea que contiene los nombres de los campos.
			reader.readLine();

			String linea = null;
			while ((linea = reader.readLine()) != null) {

				String[] camposContinente = linea.split(SEPARADOR_CSV);
				String codContinente = camposContinente[1].trim().replace("\"", "");

				// Comprobamos si el código de continente coincide con el que estamos
				// buscando.
				if (codContinente.equals(codigoContinente)) {
					String nombreContinente = camposContinente[2].trim().replace("\"", "");

					continenteEncontrado = obtenerContinenteDeLinea(codigoContinente, nombreContinente);

					break; // Salimos del bucle ya que hemos encontrado el continente.
				}
			}
		} catch (IOException e) {
			System.err.println(e);
			;
		}

		return continenteEncontrado;
	}

	@Override
	public List<Continente> obtenerListaContinentes() {
		List<Continente> listaContinentes = null;
		File ficheroContinentes = new File(RUTA_FICHERO);

		try (BufferedReader reader = new BufferedReader(new FileReader(ficheroContinentes));) {

			// Nos saltamos la primera línea que contiene los nombres de los campos.
			reader.readLine();

			String linea = null;
			while ((linea = reader.readLine()) != null) {

				// La primera vez que entramos en este bloque, inicializamos la lista de
				// continentes
				if (listaContinentes == null) {
					listaContinentes = new ArrayList<Continente>();
				}

				String[] camposContinente = linea.split(SEPARADOR_CSV);
				String codContinente = camposContinente[1].trim().replace("\"", "");
				String nombreContinente = camposContinente[2].trim().replace("\"", "");

				// Se añade un nuevo continente a la lista de continentes obtenidos.
				listaContinentes.add(obtenerContinenteDeLinea(codContinente, nombreContinente));

			}
		} catch (IOException e) {
			System.err.println(e);
		}
		return listaContinentes;
	}

	@Override
	public void insertarContinente(Continente continente) {

		try (FileWriter fw = new FileWriter(RUTA_FICHERO, true);) {
			fw.write(System.lineSeparator());
			String lineaContinente = String.format("2026"+SEPARADOR_CSV+ "%s" + SEPARADOR_CSV + "%s", continente.getCodigo(), continente.getNombre());
			fw.write(lineaContinente);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	public void actualizarContinente(Continente continente) {
		File ficheroOriginal = new File(RUTA_FICHERO);
		File ficheroTemporal = new File(RUTA_FICHERO_TEMPORAL);

		// 1. Leer el fichero original
		try (BufferedReader readerOriginal = new BufferedReader(new FileReader(ficheroOriginal));
				FileWriter writerTemporal = new FileWriter(ficheroTemporal)) {

			String linea = null;
			boolean primeraLinea = true;

			while ((linea = readerOriginal.readLine()) != null) {

				String[] camposContinente = linea.split(SEPARADOR_CSV);
				String anyo = camposContinente[0].trim().replace("\"", "");
				String codContinente = camposContinente[1].trim().replace("\"", "");

				// 2. Escribimos todas las líneas excepto la que queremos actualizar, que la
				// escribimos con los nuevos datos.
				if (linea.trim().isEmpty()) {
					continue; // Saltamos líneas vacías
				} else {
			        if (!primeraLinea) {
			        	writerTemporal.write(System.lineSeparator());
			        }
					if (codContinente.equals(continente.getCodigo())) {

						String lineaContinente = String.format("%s" + SEPARADOR_CSV + "%s" + SEPARADOR_CSV + "%s", anyo, continente.getCodigo(),
								continente.getNombre());
						writerTemporal.write(lineaContinente);
					} else {
						writerTemporal.write(linea);
					}
					primeraLinea = false;
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		// 3. Eliminamos el fichero original
		if (!ficheroOriginal.delete()) {
			System.err.println("No se pudo eliminar el fichero original");
		}

		// 4. Renombramos el temporal con el nombre original
		if (!ficheroTemporal.renameTo(ficheroOriginal)) {
			System.err.println("No se pudo renombrar el fichero temporal");
		}

	}

	@Override
	public void borrarContinente(String codigoContinente) {
		File ficheroOriginal = new File(RUTA_FICHERO);
		File ficheroTemporal = new File(RUTA_FICHERO_TEMPORAL);

		// 1. Leer el fichero original
		try (BufferedReader readerOriginal = new BufferedReader(new FileReader(ficheroOriginal));
				FileWriter writerTemporal = new FileWriter(ficheroTemporal)) {

			String linea = null;
			boolean primeraLinea = true;

			while ((linea = readerOriginal.readLine()) != null) {

				String[] camposContinente = linea.split(SEPARADOR_CSV);
				String codContinente = camposContinente[1].trim().replace("\"", "");

				// 2. Escribimos todas las líneas excepto la que queremos eliminar
				if (!codContinente.equals(codigoContinente)) {
					if (!primeraLinea) {
						writerTemporal.write(System.lineSeparator());
					}
					writerTemporal.write(linea);
					primeraLinea = false;
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		// 3. Eliminamos el fichero original
		if (!ficheroOriginal.delete()) {
			System.err.println("No se pudo eliminar el fichero original");
		}

		// 4. Renombramos el temporal con el nombre original
		if (!ficheroTemporal.renameTo(ficheroOriginal)) {
			System.err.println("No se pudo renombrar el fichero temporal");
		}
	}

	private Continente obtenerContinenteDeLinea(String codigoContinente, String nombreContiente) {
		// Obtenemos el objeto Continente a partir del código.
		Continente continente = new Continente();

		continente.setCodigo(codigoContinente);
		continente.setNombre(nombreContiente);

		return continente;
	}

}
