package org.palomafp.aadd.inm;

import java.util.List;

import org.palomafp.aadd.inm.dao.IPaisDAO;
import org.palomafp.aadd.inm.dao.impl.PaisDaoText;
import org.palomafp.aadd.inm.model.Pais;

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

	public static void main(String[] args) {
		GestorMapaMundi app = new GestorMapaMundi();

		int identificadorPais = 105;
		Pais pais = app.obtenerPaisPorIdentificador(identificadorPais);

		// Imprimir la información del país en la consola.
		System.out.println(pais);

		System.out.println("----------------------------------------------------");

		String codigoContinente = "04";
		List<Pais> listaPaises = app.obtenerListaPaisesPorContinente(codigoContinente);
		// Imprimir la información de los paises en la consola.
		if (listaPaises != null) {
			for (Pais pais2 : listaPaises) {
				System.out.println(pais2);
			}
		}
	}

	public Pais obtenerPaisPorIdentificador(int identificador) {
		Pais pais = null;

		IPaisDAO paisDAO = new PaisDaoText();
		pais = paisDAO.obtenerPaisPorID(identificador);

		return pais;
	}

	public List<Pais> obtenerListaPaisesPorContinente(String codContinente) {
		List<Pais> listaPaises = null;

		IPaisDAO paisDAO = new PaisDaoText();
		listaPaises = paisDAO.obtenerPaisesPorContinente(codContinente);

		return listaPaises;
	}

}
