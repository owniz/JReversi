package modelo;

/*
 * Clase que define los turnos
 */

public class Turno {
	private Estado estadoActual;

	public Turno (Estado estadoActual) {
		this.estadoActual = estadoActual;
	}
	
	public Estado getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(Estado estadoActual) {
		this.estadoActual = estadoActual;
	}
}
