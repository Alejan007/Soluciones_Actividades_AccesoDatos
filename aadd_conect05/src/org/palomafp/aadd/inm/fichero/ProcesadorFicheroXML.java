package org.palomafp.aadd.inm.fichero;

import java.io.File;

import org.palomafp.aadd.inm.excepcion.FormacionException;
import org.palomafp.aadd.inm.gestor.GestorConfiguracion;
import org.palomafp.aadd.inm.model.CicloFormativo;
import org.palomafp.aadd.inm.model.FamiliaProfesional;
import org.palomafp.aadd.inm.model.TipoFormacion;
import org.palomafp.aadd.inm.service.CicloFormativoService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 *
 * ProcesadorFicheroXL: Clase que realiza el procesamiento de un fichero XML
 * para obtener información de ciclos formativos y realizar la inserción en la
 * base de datos.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 6 nov 2026
 * 
 * 
 */

public class ProcesadorFicheroXML {
	public void realizarTratamientoCiclosFormativos() throws FormacionException {

		JsonNode arbolXMLCatalogoFormacion = cargarArbolXMLFichero();

		CicloFormativoService cicloFormativoService = new CicloFormativoService();

		JsonNode listaCiclos = arbolXMLCatalogoFormacion.path("informacion_adicional").path("ciclos_formativos")
				.path("ciclo");

		for (JsonNode elementoCiclo : listaCiclos) {

			// Tratamiento de atributo y contenido de etiquetas con JsonNode
			String codCicloFormativo = elementoCiclo.path("cod_cf").asText();
			String nombreCortoCicloFormativo = elementoCiclo.path("nomb_corto").asText();
			String nombreLargoCicloFormativo = elementoCiclo.path("nombrelargo").asText();
			String numeroHorasCF = elementoCiclo.path("num_horas_duracion").asText();
			String codigoFamiliaProfesional = elementoCiclo.path("codigo_familia").asText();
			String codigoTipoFormacion = elementoCiclo.path("codigo_tipoestudio").asText();

			CicloFormativo cicloFormativo = new CicloFormativo();
			cicloFormativo.setCodigo(codCicloFormativo);
			cicloFormativo.setNombreCorto(nombreCortoCicloFormativo);
			cicloFormativo.setNombreLargo(nombreLargoCicloFormativo);
			cicloFormativo.setNumeroHorasDuracion(Integer.parseInt(numeroHorasCF));

			FamiliaProfesional familiaProfesional = new FamiliaProfesional();
			familiaProfesional.setCodigo(codigoFamiliaProfesional);
			cicloFormativo.setFamiliaProfesional(familiaProfesional);

			TipoFormacion tipoFormacion = new TipoFormacion();
			tipoFormacion.setCodigo(codigoTipoFormacion);
			cicloFormativo.setTipoFormacion(tipoFormacion);

			System.out.println(cicloFormativo);

			cicloFormativoService.insertarCicloFormativo(cicloFormativo);
		}
	}

	private JsonNode cargarArbolXMLFichero() throws FormacionException {

		JsonNode nodoRaiz = null;

		try {
			XmlMapper xmlMapper = new XmlMapper();

			File fichero = new File(GestorConfiguracion.getRutaFicheroXMLCatalogoFormacion());

			nodoRaiz = xmlMapper.readTree(fichero);

		} catch (Exception e) {
			throw new FormacionException("Error al leer el fichero con los ciclos formativos.", e);
		}

		return nodoRaiz;
	}

}
