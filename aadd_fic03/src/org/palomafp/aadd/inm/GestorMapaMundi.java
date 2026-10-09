package org.palomafp.aadd.inm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.palomafp.aadd.inm.model.Continente;
import org.palomafp.aadd.inm.model.Pais;

/**
 *
 * AppPrincipal: Clase que realiza el tratamiento de un fichero con información
 * de países.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 24 sept 2026
 * 
 */

public class GestorMapaMundi {

	private static final String RUTA_FICHERO = "C:\\Users\\ineva\\INM\\tmp\\aadd\\fic03\\informacion_paises.txt";

	public static void main(String[] args) {
		GestorMapaMundi app = new GestorMapaMundi();

		int identificadorPais = 105;
		Pais pais = app.obtenerPaisPorIdentificador(identificadorPais);

		// Imprimir la información del país en la consola.
		System.out.println(pais);

		System.out.println("----------------------------------------------------");

		String codigoContinente = "04";
		List<Pais> listaPaises = app.obtenerPaisesPorContinente(codigoContinente);
		// Imprimir la información de los paises en la consola.
		for (Pais pais2 : listaPaises) {
			System.out.println(pais2);
		}

	}

	private Pais obtenerPaisPorIdentificador(int identificador) {
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
					;

					Continente continente = new Continente();
					continente.setCodigo(codigoContinente);

					paisEncontrado = new Pais();
					paisEncontrado.setIdentificador(Integer.parseInt(idPais));
					paisEncontrado.setNombre(nombrePais);
					paisEncontrado.setCapital(capitalPais);
					paisEncontrado.setContinente(continente);

                    // una vez obtenido el país se sale del bucle
                    break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return paisEncontrado;
	}

	private List<Pais> obtenerPaisesPorContinente(String codContinente) {
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

					Continente continente = new Continente();
					continente.setCodigo(codigoContinente);

					Pais pais = new Pais();
					pais.setIdentificador(Integer.parseInt(idPais));
					pais.setNombre(nombrePais);
					pais.setCapital(capitalPais);
					pais.setContinente(continente);

					listaPaisesEncontrados.add(pais);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listaPaisesEncontrados;
	}

}
