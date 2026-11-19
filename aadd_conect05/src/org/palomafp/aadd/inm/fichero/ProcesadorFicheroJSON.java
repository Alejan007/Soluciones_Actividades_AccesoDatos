package org.palomafp.aadd.inm.fichero;

import java.io.File;
import java.io.IOException;

import org.palomafp.aadd.inm.excepcion.FormacionException;
import org.palomafp.aadd.inm.gestor.GestorConfiguracion;
import org.palomafp.aadd.inm.model.FamiliaProfesional;
import org.palomafp.aadd.inm.service.FamiliaProfesionalService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * ProcesadorFicheroCSV: Clase que realiza el procesamiento de un fichero CSV
 * para obtener información de familias profesionales y realizar la inserción o
 * actualización en la base de datos.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 6 nov 2026
 * 
 * 
 */

public class ProcesadorFicheroJSON {
	public void realizarTratamientoFamiliasProfesionales() throws FormacionException {

		File ficheroFamiliasProfesionales = new File(GestorConfiguracion.getRutaFicheroCSVFamiliaProfesional());
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode nodoFamilias = mapper.readTree(ficheroFamiliasProfesionales);
			for (JsonNode nodoFamilia : nodoFamilias) {
				String codFamiliaProfesional = nodoFamilia.get("cod_tipo_fam").asText();
				String nombreFamiliaProfesional = nodoFamilia.get("nombre_tf").asText();

				FamiliaProfesional familiaProfesional = new FamiliaProfesional();
				familiaProfesional.setCodigo(codFamiliaProfesional);
				familiaProfesional.setNombre(nombreFamiliaProfesional);

				// Realización de la inserción o actualización de la familia profesional en la
				// base de datos
				FamiliaProfesionalService familiaProfesionalService = new FamiliaProfesionalService();
				boolean existeFP = familiaProfesionalService.existeFamiliaProfesionalPorClave(codFamiliaProfesional);
				if (existeFP) {
					System.out.println("Actualizando familia profesional: " + familiaProfesional);

					familiaProfesionalService.actualizarFamiliaProfesional(familiaProfesional);
				} else {
					System.out.println("Insertando la familia profesional: " + familiaProfesional);
					familiaProfesionalService.insertarFamiliaProfesional(familiaProfesional);
				}
			}
		} catch (IOException e) {
			throw new FormacionException("Error al leer el fichero CSV de familias profesionales.", e);
		}
	}
}
