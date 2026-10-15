package org.palomafp.aadd.inm.dao.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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
	private final String rutaFichero = GestorConfiguracion.getRutaFicheroContinente();
	private final String separadorCSV = GestorConfiguracion.getSeparadorCsvFicheroContinente();
	private final String rutaFicheroTmp = "archivo.tmp";

	@Override
	public Continente obtenerContinentePorCodigo(String codigoContinente) {
		Continente continenteEncontrado = null;

		Path rutaFicheroContinentes = Path.of(rutaFichero);

		try (var reader = Files.newBufferedReader(rutaFicheroContinentes, StandardCharsets.UTF_8)) {
			String linea = null;

			// Nos saltamos la primera línea que contiene los nombres de los campos.
			reader.readLine();

			while ((linea = reader.readLine()) != null) {

				String[] camposContinente = linea.split(separadorCSV);
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
		}

		return continenteEncontrado;
	}

	@Override
	public List<Continente> obtenerListaContinentes() {
		List<Continente> listaContinentes = null;

		Path rutaFicheroContinentes = Path.of(rutaFichero);

		try (var reader = Files.newBufferedReader(rutaFicheroContinentes, StandardCharsets.UTF_8)) {
			String linea = null;

			// Nos saltamos la primera línea que contiene los nombres de los campos.
			reader.readLine();

			while ((linea = reader.readLine()) != null) {

				// La primera vez que entramos en este bloque, inicializamos la lista de
				// continentes
				if (listaContinentes == null) {
					listaContinentes = new ArrayList<Continente>();
				}

				String[] camposContinente = linea.split(separadorCSV);
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

		try (var writer = Files.newBufferedWriter(Path.of(rutaFichero), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {

			writer.write(System.lineSeparator());

			String lineaContinente = String.format("2026" + separadorCSV + "%s" + separadorCSV + "%s",
					continente.getCodigo(), continente.getNombre());

			writer.write(lineaContinente);

		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	public void actualizarContinente(Continente continente) {
		Path rutaFicheroOriginal = Path.of(rutaFichero);
		Path rutaFicheroTemporal = Path.of(rutaFicheroTmp);

		boolean actualizado = false;

		// 1. Leer el original y generar el temporal
		try (var readerOriginal = Files.newBufferedReader(rutaFicheroOriginal, StandardCharsets.UTF_8);
				var writerTemporal = Files.newBufferedWriter(rutaFicheroTemporal, StandardCharsets.UTF_8)) {

			String linea=null;
			boolean primeraLinea = true;

			while ((linea = readerOriginal.readLine()) != null) {

				if (linea.trim().isEmpty()) {
					continue;
				}

				String[] campos = linea.split(separadorCSV, -1);

				if (campos.length < 3) {
					throw new IOException("Línea CSV no válida: " + linea);
				}

				String anyo = campos[0].trim().replace("\"", "");
				String codContinente = campos[1].trim().replace("\"", "");

				if (!primeraLinea) {
					writerTemporal.newLine();
				}

				if (codContinente.equals(continente.getCodigo())) {
					String nuevaLinea = String.join(separadorCSV, anyo, continente.getCodigo(),
							continente.getNombre());

					writerTemporal.write(nuevaLinea);
					actualizado = true;
				} else {
					writerTemporal.write(linea);
				}

				primeraLinea = false;
			}

		} catch (IOException e) {
			System.err.println("Error al procesar el CSV: " + e.getMessage());
			return; // No sustituimos el original si falla la escritura
		}

		// 2. Sustituir el original solo si se ha actualizado el registro
		if (!actualizado) {
			System.err.println("No se encontró el continente que se desea actualizar.");
			return;
		}

		try {
			Files.move(rutaFicheroTemporal, rutaFicheroOriginal, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println("No se pudo sustituir el fichero original: " + e.getMessage());
		}

	}

	@Override
	public void borrarContinente(String codigoContinente) {
		Path rutaFicheroOriginal = Path.of(rutaFichero);
		Path rutaFicheroTemporal = Path.of(rutaFicheroTmp);

		// 1. Leer el original y generar el temporal
		try (var readerOriginal = Files.newBufferedReader(rutaFicheroOriginal, StandardCharsets.UTF_8);
				var writerTemporal = Files.newBufferedWriter(rutaFicheroTemporal, StandardCharsets.UTF_8)) {

			String linea = null;
			boolean primeraLinea = true;

			while ((linea = readerOriginal.readLine()) != null) {

				String[] camposContinente = linea.split(separadorCSV);
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

		try {
		    Files.move(
		        rutaFicheroTemporal,
		        rutaFicheroOriginal,
		        StandardCopyOption.REPLACE_EXISTING
		    );
		} catch (IOException e) {
		    System.err.println(
		        "No se pudo sustituir el fichero original: " + e.getMessage()
		    );
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
