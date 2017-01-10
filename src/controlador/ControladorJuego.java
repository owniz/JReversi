package controlador;

import modelo.Estado;
import modelo.Posicion;
import modelo.Turno;
import vista.Tablero;

public class ControladorJuego {
	
	// instanciamos el tablero y llamamos al método que inicia una nueva partida
	public static void main(String[] args) {
		new Tablero().nuevaPartida();
	}
	
	// si la jugada es posible cambia los estados
    public static boolean cambiarEstados(Estado[][] estados, Posicion posicion, Turno turno) {
    	boolean esValida = false;
    	
        for(int x = -1; x < 2; x++) {
            for(int y = -1; y < 2; y++) {
            	
            	// para no comprobar el boton pulsado
                if(x == 0 && y == 0)
                	continue;

                // comprueba las fichas de al lado
	            int filaAComprobar = posicion.getX() + x;
	            int columnaAComprobar = posicion.getY() + y;
	            
	            // para limitar las posiciones que no tienen casillas al lado en todas las direcciones
                if(filaAComprobar >= 0 && columnaAComprobar >= 0 && filaAComprobar < 8 && columnaAComprobar < 8) {
                	
                	// si es blanca comprobamos negra y al revés
                    if(estados[filaAComprobar][columnaAComprobar] == (turno.getEstadoActual() == Estado.NEGRO ? Estado.BLANCO : Estado.NEGRO)) { 
                    	
                    	// buscamos una casilla igual al turno actual
                        for(int distancia = 0; distancia < 8; distancia++) {   
                            int filaACambiar = posicion.getX() + distancia * x;
                            int columnaACambiar = posicion.getY() + distancia * y;
                            
                            // si nos salimos del tablero pasamos a la siguiente iteracion
                            if(filaACambiar < 0 || columnaACambiar < 0  || filaACambiar > 7 || columnaACambiar > 7)
                            	continue;
                            
                            // si encontramos una casilla igual al turno actual cambiamos las que hay entre ambas
                            if(estados[filaACambiar][columnaACambiar] == turno.getEstadoActual()) {
                            	
                            	// cambia de color las casillas
                                for(int distancia2 = 1; distancia2 < distancia; distancia2++) {
                                    int flipRow = posicion.getX() + distancia2 * x;
                                    int flipCol = posicion.getY() + distancia2 * y;
                                    
                                    estados[flipRow][flipCol] = turno.getEstadoActual();
                                }
                                
                                esValida = true;
                                break;
                            }
                        }
                    }
                }
             }     
          }
        
      return esValida;
    }
}