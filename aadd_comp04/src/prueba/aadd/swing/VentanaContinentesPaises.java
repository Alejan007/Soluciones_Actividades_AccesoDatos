package prueba.aadd.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.palomafp.aadd.inm.dao.IContinenteDAO;
import org.palomafp.aadd.inm.dao.IPaisDAO;
import org.palomafp.aadd.inm.model.Continente;
import org.palomafp.aadd.inm.model.Pais;

public class VentanaContinentesPaises extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String RUTA_FICHERO_CONFIGURACION = "application.properties";

	private JComboBox<Continente> comboContinentes;
	private JList<Pais> listaPaises;
	private DefaultListModel<Pais> modeloPaises;
	private JTextArea areaInfo;

	private List<Pais> paisesActuales;

	private IContinenteDAO continenteDAO;
	private IPaisDAO paisDAO;

	public VentanaContinentesPaises() {

		super("Continentes y países");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 350);
		setLocationRelativeTo(null);

		// Inicializar DAO
		try {
			inicializarDAOs();
		} catch (Exception e) {
			throw new IllegalStateException("No se pudieron cargar los DAO", e);
		}

		// Componentes
		comboContinentes = new JComboBox<>();
		modeloPaises = new DefaultListModel<>();
		listaPaises = new JList<>(modeloPaises);

		areaInfo = new JTextArea(4, 30);
		areaInfo.setEditable(false);
		areaInfo.setLineWrap(true);
		areaInfo.setWrapStyleWord(true);

		// Mostrar nombre del continente en el desplegable
		comboContinentes.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof Continente) {
					setText(((Continente) value).getNombre());
				}

				return this;
			}
		});

		// Diseño
		JPanel panel = new JPanel(new BorderLayout(5, 5));

		JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
		panelSuperior.add(new JLabel("Selecciona un continente:"), BorderLayout.WEST);
		panelSuperior.add(comboContinentes, BorderLayout.CENTER);

		panel.add(panelSuperior, BorderLayout.NORTH);
		panel.add(new JScrollPane(listaPaises), BorderLayout.CENTER);
		panel.add(new JScrollPane(areaInfo), BorderLayout.SOUTH);

		add(panel);

		// Eventos
		comboContinentes.addActionListener(e -> cargarPaises());
		listaPaises.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				Pais seleccionado = listaPaises.getSelectedValue();

				if (seleccionado != null) {
					mostrarInfo(seleccionado);
				}
			}
		});

		// Cargar continentes al iniciar
		cargarContinentes();
	}

	private void inicializarDAOs() throws Exception {
		Properties config = new Properties();

		try (InputStream inputStream = VentanaContinentesPaises.class.getClassLoader().getResourceAsStream(RUTA_FICHERO_CONFIGURACION)) {

			if (inputStream == null) {
				throw new IOException("Fichero application.properties no encontrado Oen el classpath");
			}

			config.load(inputStream);
		}

		String nombreClaseImplementacionContinente = config.getProperty("continente.implementacion.clase");
		String nombreClaseImplementacionPais = config.getProperty("pais.implementacion.clase");

		if (nombreClaseImplementacionContinente == null || nombreClaseImplementacionPais == null) {
			throw new IllegalArgumentException("Las propiedades asociadas a las clases del DAO no existen en el fichero application.properties");
		}

		// Se obtiene el objeto con la implementación de continenteDAO(por reflexión)
		continenteDAO = (IContinenteDAO) Class.forName(nombreClaseImplementacionContinente.trim()).getDeclaredConstructor().newInstance();

		// Se obtiene el objeto con la implementación de paisDAO (por reflexión)
		paisDAO = (IPaisDAO) Class.forName(nombreClaseImplementacionPais.trim()).getDeclaredConstructor(IContinenteDAO.class)
				.newInstance(continenteDAO);
	}

	private void cargarContinentes() {

		// Sustituir por el nombre real del método de tu DAO
		List<Continente> continentes = continenteDAO.obtenerListaContinentes();

		comboContinentes.removeAllItems();

		for (Continente continente : continentes) {
			comboContinentes.addItem(continente);
		}
	}

	private void cargarPaises() {

		Continente seleccionado = (Continente) comboContinentes.getSelectedItem();

		modeloPaises.clear();
		areaInfo.setText("");

		if (seleccionado == null) {
			return;
		}

		paisesActuales = paisDAO.obtenerPaisesPorContinente(seleccionado.getCodigo());

		if (paisesActuales == null || paisesActuales.isEmpty()) {
			areaInfo.setText("No hay países disponibles para el continente seleccionado.");
			return;
		}

		for (Pais pais : paisesActuales) {
			modeloPaises.addElement(pais);
		}

		if (!paisesActuales.isEmpty()) {
			listaPaises.setSelectedIndex(0);
		}
	}

	private void mostrarInfo(Pais pais) {

		String texto = String.format("%s, capital de %s – %d, pertenece al continente %s (%s)", pais.getCapital(),
				pais.getNombre(), pais.getIdentificador(), pais.getContinente().getNombre(),
				pais.getContinente().getCodigo());

		areaInfo.setText(texto);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			VentanaContinentesPaises ventana = new VentanaContinentesPaises();

			ventana.setVisible(true);
		});
	}
}
