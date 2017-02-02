package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controlador.ControladorJuego;
import modelo.Estado;
import modelo.Posicion;
import modelo.Turno;


public class Tablero extends JFrame implements ActionListener {
	private int ladoTablero = 8;
	private Turno turno;
	private JMenuBar jmb;
	private JMenu jmInicio, jmAyuda;
	private JMenuItem jmiNuevaPartida, jmiSalir, jmiAyuda, jmiVersion;
	private JPanel jpFichas, jpPuntuaciones, jpPuntNegras, jpPuntBlancas, jpTurno, jpPasarTurno;
	private JLabel jlNegro, jlBlanco, jlPuntNegro, jlPuntBlanco, jlTurnoEtiqueta, jlTurno;
	private JButton[][] jbFichas;
	private JButton jbPasarTurno;
	private Estado[][] estados;
	
	public Tablero() {
		super("JReversi");
		iniciarGUI();
	}
	
	public void iniciarGUI() {
		
		// barra de menú
		jmb = new JMenuBar();
		setJMenuBar(jmb);
		
		jmInicio = new JMenu();
		jmAyuda = new JMenu();
		
		jmInicio.setText("Inicio");
		jmAyuda.setText("Ayuda");
		
		jmiNuevaPartida = new JMenuItem("Nueva partida");
		jmiSalir = new JMenuItem("Salir");
		jmiAyuda = new JMenuItem("Ayuda");
		jmiVersion = new JMenuItem("Versión");
		
		jmiNuevaPartida.addActionListener(this);
		jmiSalir.addActionListener(this);
		jmiAyuda.addActionListener(this);
		jmiVersion.addActionListener(this);
		
		jmInicio.add(jmiNuevaPartida);
		jmInicio.add(jmiSalir);
		jmAyuda.add(jmiAyuda);
		jmAyuda.add(jmiVersion);
		
		jmb.add(jmInicio);
		jmb.add(jmAyuda);
		
		// paneles
		jpPuntuaciones = new JPanel();
		jpPuntNegras = new JPanel();
		jpPuntBlancas = new JPanel();
		jpTurno = new JPanel();
		jpFichas = new JPanel(new GridLayout(ladoTablero, ladoTablero));
		jpPasarTurno = new JPanel();
		
		jlNegro = new JLabel("Negras:");
		jlBlanco = new JLabel("Blancas:");
		jlPuntNegro = new JLabel();
		jlPuntBlanco = new JLabel();
		jlTurnoEtiqueta = new JLabel("Turno de:");
		jlTurno = new JLabel();
		
		// array de botones para las fichas
		jbFichas = new JButton[ladoTablero][ladoTablero];
	
		for(int i = 0; i < jbFichas.length; i++) {
			for(int j = 0; j < jbFichas[i].length; j++) {
				jbFichas[i][j] = new JButton();
	
				jbFichas[i][j].setBackground(Color.LIGHT_GRAY);
				jbFichas[i][j].addActionListener(this);
				jbFichas[i][j].setActionCommand(i + "," + j); // almacenamos la posición de cada botón(ficha)
				
				jpFichas.add(jbFichas[i][j]);
			}
		}
	
		// bootn pasar turno
		jbPasarTurno = new JButton();
		jbPasarTurno.setText("Pasar Turno");
		jbPasarTurno.addActionListener(this);
		
		// añadimos los paneles
		jpPuntNegras.add(jlNegro);
		jpPuntNegras.add(jlPuntNegro);
		jpPuntBlancas.add(jlBlanco);
		jpPuntBlancas.add(jlPuntBlanco);
		jpTurno.add(jlTurnoEtiqueta);
		jpTurno.add(jlTurno);
		
		jpPuntuaciones.add(jpPuntNegras);
		jpPuntuaciones.add(jpPuntBlancas);
		jpPuntuaciones.add(jpTurno);
		
		jpPasarTurno.add(jbPasarTurno);
		
		// añadimos y distribuimos los paneles generales
		add(jpPuntuaciones, BorderLayout.PAGE_START);
		add(jpFichas, BorderLayout.CENTER);
		add(jpPasarTurno, BorderLayout.PAGE_END);
		
		// diseño ventana
		setSize(300, 415);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// iniciar una nueva partida
	public void nuevaPartida() {
		estados = new Estado[][]{
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.NEGRO, Estado.BLANCO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.BLANCO, Estado.NEGRO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
				{Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO, Estado.VACIO},
		};
		
		this.turno = new Turno(Estado.BLANCO);
		
		actualizarColor(estados);
		actualizarTurno(turno);
	}
	
	// actualiza las fichas segun su color en el array de Estados y llama al metodo que actualiza la puntuación.
	// si no hya fichas de un color termina la partida
	public void actualizarColor(Estado[][] estados) {
		int contadorVacio = 0;
		int contadorNegras = 0;
		int contadorBlancas = 0;
		
		for(int i = 0; i < estados.length; i++) {
			for(int j = 0; j < estados[i].length; j++) {
				if(estados[i][j] == Estado.VACIO) {
					jbFichas[i][j].setBackground(Color.LIGHT_GRAY);
					contadorVacio++;
				}
				
				if(estados[i][j] == Estado.NEGRO) {
					jbFichas[i][j].setBackground(Color.BLACK);
					contadorNegras++;
				}

				if(estados[i][j] == Estado.BLANCO) {
					jbFichas[i][j].setBackground(Color.WHITE);
					contadorBlancas++;
				}
			}
		}
		
		actualizarPuntuacion(contadorNegras, contadorBlancas);
		
		// si el tablero esta lleno miramos quién ha ganado
		if(contadorVacio == 0)
			if(JOptionPane.showOptionDialog(this, "¡Ganan las " + (contadorBlancas > contadorNegras ? "blancas" : "negras")
											+ "!\n\nPulsa Aceptar para empezar una nueva partida", "",
											JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null) == 0)
				nuevaPartida();
		
		// si no está completamente lleno
		if(contadorBlancas == 0 || contadorNegras == 0)
			if(JOptionPane.showOptionDialog(this, "¡Ganan las " + (contadorNegras == 0 ? "blancas" : "negras")
											+ "!\n\nPulsa Aceptar para empezar una nueva partida", "",
											JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null) == 0)
				nuevaPartida();	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// funciones de las fichas
		if(e.getSource() instanceof JButton) {
			
			// boton para pasar turno
			if(e.getSource() == jbPasarTurno) {
				turno.setEstadoActual((turno.getEstadoActual() == Estado.BLANCO ? Estado.NEGRO : Estado.BLANCO));				
				actualizarTurno(turno);
			} else {
			
				// recuperamos las posiciones
				final String[] coordenadas = ((JButton) e.getSource()).getActionCommand().split(",");
				final Posicion posicion = new Posicion(Integer.valueOf(coordenadas[0]), Integer.valueOf(coordenadas[1]));
			
				// si pulsas sobe una ficha
				if(estados[posicion.getX()][posicion.getY()] == Estado.BLANCO || estados[posicion.getX()][posicion.getY()] == Estado.NEGRO) {
					mostrarJugadaNoValida();
					
				// si la jugada es váldia	
				} else if(ControladorJuego.cambiarEstados(estados, posicion, turno)) {
					estados[posicion.getX()][posicion.getY()] = turno.getEstadoActual();
					actualizarColor(estados);
					
					// cambiamos el turno y lo actualizamos
					turno.setEstadoActual((turno.getEstadoActual() == Estado.BLANCO ? Estado.NEGRO : Estado.BLANCO));				
					actualizarTurno(turno);
					
				// por si acaso	
				} else {
					mostrarJugadaNoValida();
				}
			}
		}	
				
		// funciones del menú
		if(e.getSource() == jmiNuevaPartida)
			nuevaPartida();
		else if(e.getSource() == jmiSalir)
			System.exit(0);
		else if(e.getSource() == jmiAyuda)
			try {
				Runtime.getRuntime().exec("hh.exe help/jreversi_help.chm");
			} catch(IOException ex) {}
		else if(e.getSource() == jmiVersion)
			JOptionPane.showMessageDialog(this, "Version 0.9\n\nJavier Morales\nLuis Morales");
	}
	
	// actualiza los JLabel de las puntuaciones
	public void actualizarPuntuacion(int contadorNegras, int contadorBlancas) {
		jlPuntNegro.setText(String.valueOf(contadorNegras));
		jlPuntBlanco.setText(String.valueOf(contadorBlancas));
	}
	
	// cambia la JLabel de turno
	public void actualizarTurno(Turno turno) {
		jlTurno.setText(turno.getEstadoActual() == Estado.NEGRO ? "Negras" : "Blancas");
	}
	
	// si una jugada no es valida lo muestra
	public void mostrarJugadaNoValida() {
		JOptionPane.showMessageDialog(this, "Jugada no válida");
	}
}
