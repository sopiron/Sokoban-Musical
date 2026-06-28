package modelo;

import modelo.dificultad.DificultadPorNivel;
import modelo.puntaje.CriterioPuntaje;

public interface NivelFactory {
    Caja crearCaja(Posicion posicion);
    Pared crearPared(Posicion posicion);
    Destino crearDestino(Posicion posicion);
    Jugador crearJugador(Posicion posicion);
    void crearElemento(char simbolo, Posicion posicion, Tablero tablero);
    PisoResbaladizo crearPisoResbaladizo(Posicion posicion);
    String getRutaMusicaFondo();
    DificultadPorNivel crearDificultadPorNivel();
    CriterioPuntaje crearCriterioPuntaje();
}
