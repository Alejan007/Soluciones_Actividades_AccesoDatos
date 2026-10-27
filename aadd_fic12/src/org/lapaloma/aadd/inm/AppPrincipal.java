package org.lapaloma.aadd.inm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.palomafp.aadd.inm.gestores.GestorConfiguracion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AppPrincipal {
	private static final String CICLO_DAM = "DAM";
	private static final String CURSO1 = "1";

	public static void main(String[] args) {
		AppPrincipal app = new AppPrincipal();

		// Se carga el árbol DOM que más tarde se usará para las consultas.
		Document docArbolDOM = app.obtenerArbolDOMFicheroXML();

		Scanner sc = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("Elige una opción:");
			System.out.println("1. Crear XML información instituto");
			System.out.println("2. Crear XML resumen ciclos");
			System.out.println("3. Crear XML módulos 1º - Ciclo DAM");
			System.out.println("4. Crear informe TXT Ciclos");

			opcion = sc.nextInt();

			if (opcion < 1 || opcion > 4) {
				System.out.println("Opción no válida. Inténtalo de nuevo.");
			}
			switch (opcion) {
			case 1:
				app.crearXMLInformacionInstituto(docArbolDOM);
				break;
			case 2:
				app.crearXMLInformacionCiclos(docArbolDOM);
				break;
			case 3:
				app.crearXMLInformacionModulos(docArbolDOM);
				break;
			case 4:
				app.crearInformeTXTCiclos(docArbolDOM);
				break;
			}

		} while (opcion < 1 || opcion > 4);

		System.out.println("Has elegido la opción " + opcion);

		sc.close();
	}

	private void crearXMLInformacionInstituto(Document docArbolDOM) {
		// Se crea un nuevo documento XML para almacenar la información del instituto
		Document docNuevoXMLIES = crearDocumentoXML();

		// Se crea el elemento raíz "instituto" y se añade al nuevo documento
		Element elementoIES = docNuevoXMLIES.createElement("instituto");
		docNuevoXMLIES.appendChild(elementoIES);

		// Se crea el elemento "nombre", se obtiene su valor del documento original y se
		// añade al elemento raíz
		Element elementoNombreIES = docNuevoXMLIES.createElement("nombre");
		String nombreIES = docArbolDOM.getElementsByTagName("nombre").item(0).getTextContent();
		elementoNombreIES.setTextContent(nombreIES);

		// Se añade el elemento "nombre" al elemento raíz "instituto"
		elementoIES.appendChild(elementoNombreIES);

		guardarDocumentoXMLFichero(docNuevoXMLIES, GestorConfiguracion.getRutaFicheroSalidaIES());
	}

	private void crearXMLInformacionCiclos(Document docArbolDOM) {
		// Se crea un nuevo documento XML para almacenar la información del instituto
		Document docNuevoXMLIES = crearDocumentoXML();

		// Se crea el elemento raíz "resumen" y se añade al nuevo documento
		Element elementoResumen = docNuevoXMLIES.createElement("resumen");
		docNuevoXMLIES.appendChild(elementoResumen);

		// Se obtiene los valores de instituto del documento original
		Element elementoInstitutoOrigen = (Element) docArbolDOM.getElementsByTagName("instituto").item(0);
		String nombre = elementoInstitutoOrigen.getElementsByTagName("nombre").item(0).getTextContent();
		String tipo = elementoInstitutoOrigen.getAttribute("tipo");
		// Se crea el elemento "instituto" y se añade al elemento raíz "resumen"
		Element elementoIES = docNuevoXMLIES.createElement("instituto");
		elementoIES.setTextContent(nombre);
		elementoIES.setAttribute("tipo", tipo);

		// Se añade el elemento "nombre" al elemento raíz "instituto"
		elementoResumen.appendChild(elementoIES);

		Element elementoCiclos = docNuevoXMLIES.createElement("ciclos");
		elementoResumen.appendChild(elementoCiclos);

		// Se obtiene los valores de ciclos del documento original
		NodeList nodosCiclo = docArbolDOM.getElementsByTagName("ciclo");
		for (int i = 0; i < nodosCiclo.getLength(); i++) {
			Element elementoCicloOrigen = (Element) nodosCiclo.item(i);

			String codigo = elementoCicloOrigen.getAttribute("codigo");
			String nombreCorto = elementoCicloOrigen.getElementsByTagName("nombre_corto").item(0).getTextContent();

			// Se crea el elemento "ciclo" y se añade al elemento "ciclos"
			Element elementoCicloNuevo = docNuevoXMLIES.createElement("ciclo");
			elementoCicloNuevo.setAttribute("codigo", codigo);
			elementoCicloNuevo.setTextContent(nombreCorto);

			elementoCiclos.appendChild(elementoCicloNuevo);
		}

		guardarDocumentoXMLFichero(docNuevoXMLIES, GestorConfiguracion.getRutaFicheroSalidaCiclos());
	}

	private void crearXMLInformacionModulos(Document docArbolDOM) {
		// Se crea un nuevo documento XML para almacenar la información del instituto
		Document docNuevoXMLIES = crearDocumentoXML();

		// Se crea el elemento raíz "instituto" y se añade al nuevo documento
		Element elementoIES = docNuevoXMLIES.createElement("ies");
		docNuevoXMLIES.appendChild(elementoIES);

		// Se obtiene los valores de instituto del documento original
		Element elementoInstitutoOrigen = (Element) docArbolDOM.getElementsByTagName("instituto").item(0);
		String nombre = elementoInstitutoOrigen.getElementsByTagName("nombre").item(0).getTextContent();
		String tipo = elementoInstitutoOrigen.getAttribute("tipo");
		// Se crea el elemento "instituto" y se añade al elemento raíz "resumen"
		elementoIES.setAttribute("nombre", nombre);
		elementoIES.setAttribute("tipo", tipo);

		// Se obtiene los valores de ciclos del documento original
		NodeList nodosCiclo = docArbolDOM.getElementsByTagName("ciclo");
		for (int i = 0; i < nodosCiclo.getLength(); i++) {
			Element elementoCicloOrigen = (Element) nodosCiclo.item(i);

			String codigo = elementoCicloOrigen.getAttribute("codigo");
			String nombreCorto = elementoCicloOrigen.getElementsByTagName("nombre_corto").item(0).getTextContent();
			String nombreLargo = elementoCicloOrigen.getElementsByTagName("nombre_largo").item(0).getTextContent();

			// Saltar al siguiente ciclo si el código no es "DAM"
			if (!CICLO_DAM.equals(nombreCorto.trim())) {
				continue;
			}

			// Se crea el elemento "ciclo" y se añade al elemento "ciclos"
			Element elementoCicloNuevo = docNuevoXMLIES.createElement("ciclo");
			elementoCicloNuevo.setAttribute("codigo", codigo);

			Element elementoNombreCortoCicloNuevo = docNuevoXMLIES.createElement("nombre_corto");
			elementoNombreCortoCicloNuevo.setTextContent(nombreCorto);
			elementoCicloNuevo.appendChild(elementoNombreCortoCicloNuevo);

			Element elementoNombreLargoCicloNuevo = docNuevoXMLIES.createElement("nombre_largo");
			elementoNombreLargoCicloNuevo.setTextContent(nombreLargo);
			elementoCicloNuevo.appendChild(elementoNombreLargoCicloNuevo);

			elementoIES.appendChild(elementoCicloNuevo);

			NodeList nodosCurso = elementoCicloOrigen.getElementsByTagName("curso");
			for (int j = 0; j < nodosCurso.getLength(); j++) {
				Element elementoCursoOrigen = (Element) nodosCurso.item(j);
				String numeroCurso = elementoCursoOrigen.getAttribute("numero");

				// Saltar al siguiente curso si el número no es "1"
				if (!CURSO1.equals(numeroCurso.trim())) {
					continue;
				}
				// Se crea el elemento "curso" y se añade al elemento "ciclo"
				Element elementoCursoCicloNuevo = docNuevoXMLIES.createElement("curso");
				elementoCursoCicloNuevo.setAttribute("numero", numeroCurso);
				elementoCicloNuevo.appendChild(elementoCursoCicloNuevo);

				NodeList nodosModulo = elementoCursoOrigen.getElementsByTagName("modulo");
				for (int k = 0; k < nodosModulo.getLength(); k++) {
					Element elementoModuloOrigen = (Element) nodosModulo.item(k);
					String id = elementoModuloOrigen.getAttribute("id");
					String ref = elementoModuloOrigen.getAttribute("ref");
					String nombreModulo = elementoModuloOrigen.getElementsByTagName("titulo").item(0).getTextContent();
					String numeroHoras = elementoModuloOrigen.getElementsByTagName("numerohoras").item(0)
							.getTextContent();

					// Se crea el elemento "modulo" y se añade al elemento "curso"
					Element elementoModuloNuevo = docNuevoXMLIES.createElement("modulo");
					elementoModuloNuevo.setAttribute("id", id);
					elementoModuloNuevo.setAttribute("ref", ref);
					elementoModuloNuevo.setAttribute("numero_horas", numeroHoras);
					Element elementoTituloModuloNuevo = docNuevoXMLIES.createElement("titulo");
					elementoTituloModuloNuevo.setTextContent(nombreModulo);
					elementoModuloNuevo.appendChild(elementoTituloModuloNuevo);

					// Se añade el elemento "modulo" al elemento "curso"
					elementoCursoCicloNuevo.appendChild(elementoModuloNuevo);
				}
			}
		}

		guardarDocumentoXMLFichero(docNuevoXMLIES, GestorConfiguracion.getRutaFicheroSalidaModulos());

	}

	private void crearInformeTXTCiclos(Document docArbolDOM) {
		File ficheroInforme = new File(GestorConfiguracion.getRutaFicheroInformeCiclos());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroInforme))) {

			bw.write("INFORME DE CICLOS FORMATIVOS");
			bw.newLine();
			bw.write("IES Virgen de la Paloma");
			bw.newLine();
			bw.write("========================================");
			bw.newLine();
			// Obtener los ciclos del documento original
			NodeList nodosCiclo = docArbolDOM.getElementsByTagName("ciclo");
			for (int i = 0; i < nodosCiclo.getLength(); i++) {
				bw.newLine();
				bw.write("----------------------------------------");
				bw.newLine();
				Element elementoCiclo = (Element) nodosCiclo.item(i);
				String codigo = elementoCiclo.getAttribute("codigo");
				String nombreCorto = elementoCiclo.getElementsByTagName("nombre_corto").item(0).getTextContent();

				bw.write("Código: " + codigo);
				bw.newLine();
				bw.write("Nombre corto: " + nombreCorto);
				bw.newLine();
				bw.write("Observaciones: ");
				bw.newLine();
				// Se obtienen los nodos hijos del ciclo para buscar los comentarios.
				NodeList nodosHijos = elementoCiclo.getChildNodes();
				for (int j = 0; j < nodosHijos.getLength(); j++) {
					Node nodoHijoCiclo = nodosHijos.item(j);
					if (nodoHijoCiclo.getNodeType() == Node.COMMENT_NODE) {
						String obsevacion = nodoHijoCiclo.getNodeValue();
						bw.write("- " + obsevacion);
						bw.newLine();
					}
				}
			}

			bw.newLine();
			bw.write("========================================");
			bw.newLine();
			bw.write("Fin del informe");
		} catch (IOException e) {
			throw new RuntimeException("Error al guardar el fichero de texto", e);
		}
	}

	private Document obtenerArbolDOMFicheroXML() {
		Document documentoRaiz = null;
		try {
			// 1. Crear la fábrica
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// 2. Crear el parser DOM
			DocumentBuilder builder = factory.newDocumentBuilder();

			// 3. Leer el fichero XML
			documentoRaiz = builder.parse(new File(GestorConfiguracion.getRutaFicheroEntradaIES()));

			// 4. Obtener el elemento raíz que voy a devolver
			documentoRaiz.getDocumentElement().normalize();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentoRaiz;
	}

	private Document crearDocumentoXML() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			return builder.newDocument();

		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Error al crear el documento XML", e);
		}
	}

	private void guardarDocumentoXMLFichero(Document doc, String ruta) {
		try {

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			// Configurar la salida XML
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

			// Transformar el DOM en un fichero
			DOMSource origen = new DOMSource(doc);
			StreamResult destino = new StreamResult(new File(ruta));

			transformer.transform(origen, destino);
		} catch (TransformerException e) {
			e.printStackTrace(); // Mostrar la causa original

			throw new RuntimeException("Error al transforma el documento XML en fichero", e);
		}
	}

}
