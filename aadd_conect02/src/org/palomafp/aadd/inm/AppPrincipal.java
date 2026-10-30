package org.palomafp.aadd.inm;

import java.util.List;

import org.palomafp.aadd.inm.model.Pais;
import org.palomafp.aadd.inm.service.PaisService;

/**
 *
 * AppPrincipal: Clase que realiza ....
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 28 oct 2026
 * 
 * 
 */

public class AppPrincipal {
	public static void main(String[] args) {
		AppPrincipal app = new AppPrincipal();

		app.realizarTratamientoPaises();
	}

	private void realizarTratamientoPaises() {
		PaisService paisService = new PaisService();

		// 1. Imprimir lista Paises
		List<Pais> listaPaises = paisService.obtenerListaPaises();
		for (Pais pais : listaPaises) {
			System.out.println(pais);
		}
		System.out.println("----------Fin---obtenerListaPaises-------------");

		// 2. Imprimir país 152
		int idPais = 152;
		Pais pais = paisService.obtenerPaisPorID(idPais);
		System.out.println(pais);
		System.out.println("--------Fin---obtenerPaisPorIdentificador---------------");

		// Se realizan las operaciones si existe el país.
		if (pais != null) {
			// 3. Borrar Paises 152
			paisService.borrarPais(idPais);
			System.out.println("--------Fin---borrarPais---------------");

			// 4. Insertar Paises 152
			paisService.insertarPais(pais);
			System.out.println("---------Fin---insertarPais--------------");

			// 5. Actualizar País - Capital City
			pais.setCapital("Capital City");
			paisService.actualizarPais(pais);
			System.out.println("---------Fin---actualizarPais--------------");
		}
	}

}
