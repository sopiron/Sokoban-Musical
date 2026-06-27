package modelo;

public interface NivelFactory {
    Caja crearCaja(Posicion posicion);
    Pared crearPared(Posicion posicion);
    Destino crearDestino(Posicion posicion);
    Jugador crearJugador(Posicion posicion);
    void crearElemento(char simbolo, Posicion posicion, Tablero tablero);
}
