package org.palomafp.aadd.inm.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.palomafp.aadd.inm.dao.IPaisDAO;
import org.palomafp.aadd.inm.dao.impl.PaisDaoJDBC;
import org.palomafp.aadd.inm.gestores.GestorConexionesBBDD;
import org.palomafp.aadd.inm.model.Pais;

/**
 *
 * PaisService: Clase que se encarga de las operaciones de servicio de un Pais (Casos de uso de países).
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 29 oct 2026
 * 
 * 
 */

public class PaisService {

	public List<Pais> obtenerListaPaises() {
		List<Pais> listaPaises = null;

		try (Connection conexion = GestorConexionesBBDD.obtenerConexion();) {
			IPaisDAO iPaisDAO = new PaisDaoJDBC(conexion);
			listaPaises = iPaisDAO.obtenerListaPaises();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaPaises;
	}

	public Pais obtenerPaisPorID(int idPais) {
		Pais pais = null;

		try (Connection conexion = GestorConexionesBBDD.obtenerConexion();) {
			IPaisDAO iPaisDAO = new PaisDaoJDBC(conexion);
			pais = iPaisDAO.obtenerPaisPorIdentificador(idPais);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pais;
	}

	public void insertarPais(Pais pais) {

		try (Connection conexion = GestorConexionesBBDD.obtenerConexion();) {
			IPaisDAO iPaisDAO = new PaisDaoJDBC(conexion);
			iPaisDAO.insertarPais(pais);
			;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void actualizarPais(Pais pais) {

		try (Connection conexion = GestorConexionesBBDD.obtenerConexion();) {
			IPaisDAO iPaisDAO = new PaisDaoJDBC(conexion);
			iPaisDAO.actualizarPais(pais);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void borrarPais(int idPais) {

		try (Connection conexion = GestorConexionesBBDD.obtenerConexion()) {

			IPaisDAO iPaisDAO = new PaisDaoJDBC(conexion);
			iPaisDAO.borrarPais(idPais);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
