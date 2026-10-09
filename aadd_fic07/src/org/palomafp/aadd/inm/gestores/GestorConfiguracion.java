package org.palomafp.aadd.inm.gestores;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * GestorConfiguracion: Clase que carga la configuración de la aplicación desde
 * un fichero de propiedades
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 8 oct 2026
 * 
 * 
 */

public class GestorConfiguracion {
	private static final String RUTA_FICHERO_CONFIGURACION = "config/application.properties";
	private static final Properties PROPIEDADES_CONFIGURACION = new Properties();
	
	// Constructor privado: de esta manera conseguimos que esta clase no se puede instanciar. 
	private GestorConfiguracion() { }
	
	// Este bloque se ejecuta una sola vez, cuando se carga la clase GestorConfiguracion
	static {
		try (FileInputStream fichero = new FileInputStream(RUTA_FICHERO_CONFIGURACION)) {

			PROPIEDADES_CONFIGURACION.load(fichero);

		} catch (IOException e) {
			throw new RuntimeException("No se ha podido cargar el fichero de configuración", e);
		}
	}

	public static String getRutaFicheroContinente() {
		return PROPIEDADES_CONFIGURACION.getProperty("continente.ruta.fichero");
	}

	public static String getSeparadorCsvFicheroContinente() {
		return PROPIEDADES_CONFIGURACION.getProperty("continente.csv.separador");
	}
}
