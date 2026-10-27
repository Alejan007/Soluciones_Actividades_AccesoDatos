package org.palomafp.aadd.inm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.palomafp.aadd.inm.model.Continente;

/**
 *
 * AppPrincipal: Clase que realiza pruebas sobre las características de Introspección y Reflexión de JAVA
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 7 oct 2026
 * 
 * 
 */

public class AppPrincipal {
	public static void main(String[] args) {
		AppPrincipal app = new AppPrincipal();
		
		System.out.println("------------------- Probar introspección -------------------");
		app.probarIntrospeccion();

		System.out.println("\n------------------- Probar reflexión -------------------");
		try {
			app.probarReflexion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}
}

	private void probarIntrospeccion() {
		Class<Continente> claseContinente = Continente.class;
		
		Field[] listaCampos= claseContinente.getDeclaredFields();
		Method[] listaMetodos = claseContinente.getDeclaredMethods();
		System.out.println("Campos de la clase Continente:");
		for (Field campo : listaCampos) {
			System.out.println("\t" + campo.getName() + " - " + campo.getType().getSimpleName());
		}
		
		System.out.println("Métodos de la clase Continente:");
		for (Method metodo : listaMetodos) {
			System.out.println("\t" + metodo.getName() + " - " + metodo.getReturnType().getSimpleName());
		}
	}

	private void probarReflexion() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		String nombreClase = "org.palomafp.aadd.inm.model.Continente";
		Class<?> claseContinente = Class.forName(nombreClase);
		
		Object objetoContinente = claseContinente.getDeclaredConstructor().newInstance();
		
        // OPCIÓN 1: Ejecutar el método "setCodigo" directamente mediante reflexión y ejecutarlo
		// Obtener el "método" "setCodigo"
		Method metodoSet = claseContinente.getMethod("setCodigo", String.class);

		// Ejecutar el método
		metodoSet.invoke(objetoContinente, "01");
		
        System.out.println("Opción 1:" + objetoContinente);
        
        // OPCIÓN 2: Asignar el valor del atributo "codigo" directamente mediante reflexión
        // Obtener el atributo "codigo"
        Field campoCodigo = claseContinente.getDeclaredField("codigo");

        // Permitir acceso al atributo, en tiempo de ejecución, aunque haya sido declarado como private
        campoCodigo.setAccessible(true);

        // Asignar el valor
        campoCodigo.set(objetoContinente, "02");

        System.out.println("Opción 2:" + objetoContinente);

	}


}

