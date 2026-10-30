package org.palomafp.aadd.inm.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.palomafp.aadd.inm.dao.IPaisDAO;
import org.palomafp.aadd.inm.model.Continente;
import org.palomafp.aadd.inm.model.Pais;

/**
 *
 * PaisDaoJDBC: Clase que realiza ....
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 29 oct 2026
 * 
 * 
 */

public class PaisDaoJDBC implements IPaisDAO {
	private final Connection conexion;

	public PaisDaoJDBC(Connection conexion) {
		this.conexion = conexion;
	}

	@Override
	public List<Pais> obtenerListaPaises() {
		List<Pais> listaPaises = new ArrayList<Pais>();

		String sentenciaSQL = """
				SELECT *
				FROM T_PAIS p JOIN T_CONTINENTE c
					ON p.cod_continente=c.codigo
				""";
//		System.out.println(sentenciaSQL);
		try (Statement sentencia = conexion.createStatement();
				ResultSet resultados = sentencia.executeQuery(sentenciaSQL);) {
			while (resultados.next()) {
				int identificador = resultados.getInt("p.identificador");
				String nombrePais = resultados.getString("p.nombre_pais");
				String capital = resultados.getString("p.capital");
				String codigoContinente = resultados.getString("c.codigo");
				String nombreContinente = resultados.getString("c.nombre_continente");

				Pais pais = new Pais();
				pais.setIdentificador(identificador);
				pais.setNombre(nombrePais);
				pais.setCapital(capital);

				Continente continente = new Continente();
				continente.setCodigo(codigoContinente);
				continente.setNombre(nombreContinente);
				pais.setContinente(continente);

				listaPaises.add(pais);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaPaises;
	}

	@Override
	public Pais obtenerPaisPorIdentificador(int idPais) {
		Pais pais = null;

		String sentenciaSQL = """
				SELECT *
				FROM T_PAIS p JOIN T_CONTINENTE c
					ON p.cod_continente=c.codigo
				WHERE p.identificador = %d
				""";
		sentenciaSQL = String.format(sentenciaSQL, idPais);
//		System.out.println(sentenciaSQL);
		try (Statement sentencia = conexion.createStatement();
				ResultSet resultados = sentencia.executeQuery(sentenciaSQL);) {
			if (resultados.next()) {
				int identificador = resultados.getInt("p.identificador");
				String nombrePais = resultados.getString("p.nombre_pais");
				String capital = resultados.getString("p.capital");
				String codigoContinente = resultados.getString("c.codigo");
				String nombreContinente = resultados.getString("c.nombre_continente");

				pais = new Pais();
				pais.setIdentificador(identificador);
				pais.setNombre(nombrePais);
				pais.setCapital(capital);

				Continente continente = new Continente();
				continente.setCodigo(codigoContinente);
				continente.setNombre(nombreContinente);
				pais.setContinente(continente);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pais;
	}

	@Override
	public void insertarPais(Pais pais) {
		String sentenciaSQL = """
				INSERT INTO T_PAIS (identificador, nombre_pais, capital, cod_continente)
				VALUES (%d, '%s', '%s', '%s')
				""";
		sentenciaSQL = String.format(sentenciaSQL, pais.getIdentificador(), pais.getNombre(), pais.getCapital(),
				pais.getContinente().getCodigo());
//		System.out.println(sentenciaSQL);
		try (Statement sentencia = conexion.createStatement();) {
			sentencia.executeUpdate(sentenciaSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actualizarPais(Pais pais) {
		String sentenciaSQL = """
				UPDATE T_PAIS
				SET nombre_pais = '%s',
					capital = '%s',
					cod_continente= '%s'
				WHERE identificador= %d
				""";
		sentenciaSQL = String.format(sentenciaSQL, pais.getNombre(), pais.getCapital(),
				pais.getContinente().getCodigo(), pais.getIdentificador());
//		System.out.println(sentenciaSQL);
		try (Statement sentencia = conexion.createStatement();) {
			sentencia.executeUpdate(sentenciaSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void borrarPais(int idPais) {
		String sentenciaSQL = """
				DELETE FROM T_PAIS
				WHERE identificador = %d
				""";
		sentenciaSQL = String.format(sentenciaSQL, idPais);
//		System.out.println(sentenciaSQL);
		try (Statement sentencia = conexion.createStatement();) {
			sentencia.executeUpdate(sentenciaSQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
