package org.palomafp.aadd.inm;

import java.util.List;

import org.palomafp.aadd.inm.dao.IContinenteDAO;
import org.palomafp.aadd.inm.dao.impl.ContinenteDaoCSV;
import org.palomafp.aadd.inm.model.Continente;

/**
 *
 * AppPrincipal: Clase que realiza el tratamiento de un fichero con información
 * de países usando patrón DAO.
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 24 sept 2026
 * 
 */

public class GestorMapaMundi {
	private IContinenteDAO continenteDAO = new ContinenteDaoCSV();

	public static void main(String[] args) {
		GestorMapaMundi gestor = new GestorMapaMundi();

		String operacion = args[0];
		switch (operacion) {
		case "1": {
			System.out.println("Obtener lista de Continentes");
			List<Continente> continentes = gestor.obtenerListaDeContinentes();
			System.out.println(continentes);
			break;
		}
		case "2": {
			String codigoContinente = args[1];
			System.out.println("Obtener Continente por código: " + codigoContinente);
			Continente continente = gestor.obtenerContinenteDeCodigo(codigoContinente);
			System.out.println(continente);
			break;
		}
		case "3": {
			String codigoContinente = args[1];
			String nombreContinente = args[2];
			System.out.println("Insertar Continente: " + codigoContinente + " con nombre: " + nombreContinente);
			gestor.insertarContinente(codigoContinente, nombreContinente);
			break;
		}
		case "4": {
			String codigoContinente = args[1];
			System.out.println("Borrar continente: " + codigoContinente);
			gestor.borrarContinente(codigoContinente);
			break;
		}
		case "5": {
			String codigoContinente = args[1];
			String nombreContinente = args[2];
			System.out.println("Actualizar Continente: " + codigoContinente + " con nombre: " + nombreContinente);
			gestor.actualizarContinente(codigoContinente, nombreContinente);
			break;
		}
		default: {
			System.out.println("Operación no válida");
			break;
		}

		}
	}

	private void actualizarContinente(String codigoContinente, String nombreContinente) {
		Continente continente = new Continente();
		continente.setCodigo(codigoContinente);
		continente.setNombre(nombreContinente);

		Continente continenteFichero = continenteDAO.obtenerContinentePorCodigo(codigoContinente);
		if (continenteFichero != null) {
			continenteDAO.actualizarContinente(continente);
		}
	}

	private void borrarContinente(String codigoContinente) {
		Continente continenteFichero = continenteDAO.obtenerContinentePorCodigo(codigoContinente);
		if (continenteFichero != null) {
			continenteDAO.borrarContinente(codigoContinente);
		}
	}

	private void insertarContinente(String codigoContinente, String nombreContinente) {

		Continente continente = new Continente();
		continente.setCodigo(codigoContinente);
		continente.setNombre(nombreContinente);

		Continente continenteFichero = continenteDAO.obtenerContinentePorCodigo(codigoContinente);
		if (continenteFichero == null) {
			continenteDAO.insertarContinente(continente);
		}

	}

	private Continente obtenerContinenteDeCodigo(String codigoContinente) {
		Continente continente = continenteDAO.obtenerContinentePorCodigo(codigoContinente);

		return continente;
	}

	private List<Continente> obtenerListaDeContinentes() {
		List<Continente> listaContinentes = continenteDAO.obtenerListaContinentes();

		return listaContinentes;
	}
}
