package modelo;

import java.util.ArrayList;

public class Tablero {

    private Jugador jugador;
    private ArrayList<Caja> cajas;
    private ArrayList<Pared> paredes;
    private ArrayList<Destino> destinos;

    public Tablero() {
        cajas = new ArrayList<>();
        paredes = new ArrayList<>();
        destinos = new ArrayList<>();
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public ArrayList<Caja> getCajas() {
        return cajas;
    }

    public void setCajas(ArrayList<Caja> cajas) {
        this.cajas = cajas;
    }

    public ArrayList<Pared> getParedes() {
        return paredes;
    }

    public void setParedes(ArrayList<Pared> paredes) {
        this.paredes = paredes;
    }

    public ArrayList<Destino> getDestinos() {
        return destinos;
    }

    public void setDestinos(ArrayList<Destino> destinos) {
        this.destinos = destinos;
    }

    // getters y setters


    //MELANIE
    // Unificamos la búsqueda. Si encuentra algo (Caja o Pared), lo devuelve. Si está vacío, devuelve null.
    public ElementoInteractuable obtenerElemento(int fila, int columna) {
        for (Caja caja : cajas) {
            if (caja.getPosicion().getFila() == fila && caja.getPosicion().getColumna() == columna) {
                return caja;
            }
        }
        for (Pared pared : paredes) {
            if (pared.getPosicion().getFila() == fila && pared.getPosicion().getColumna() == columna) {
                return pared;
            }
        }
        return null; // Casillero libre
    }

    // El movimiento ahora es puro polimorfismo, sin ifs preguntando qué tipo de objeto es.
    public boolean moverJugador(int difFila, int difColumna) {
        if (jugador == null) return false;

        int nuevaFila = jugador.getPosicion().getFila() + difFila;
        int nuevaCol = jugador.getPosicion().getColumna() + difColumna;

        ElementoInteractuable elementoFrente = obtenerElemento(nuevaFila, nuevaCol);

        // Si no hay nada adelante, el jugador camina tranquilo
        if (elementoFrente == null) {
            jugador.mover(difFila, difColumna);
            return true;
        }

        // Si hay un elemento, DELEGAMOS la decisión. El elemento interactúa y decide si nos deja pasar.
        if (elementoFrente.interactuar(difFila, difColumna, this)) {
            jugador.mover(difFila, difColumna);
            return true;
        }
        return false; // El elemento nos bloqueó
    }
    // Este método revisa si todas las cajas están paradas exactamente sobre un destino
    public boolean verificarVictoria() {

        if (cajas.isEmpty() || destinos.isEmpty()) {
            return false;
        }

        int cajasEnDestino = 0;

        for (Caja caja : cajas) {
            for (Destino destino : destinos) {
                if (caja.getPosicion().getFila() == destino.getPosicion().getFila() &&
                    caja.getPosicion().getColumna() == destino.getPosicion().getColumna()) {
                    cajasEnDestino++;
                    break;
                }
            }
        }
        
        // Si la cantidad de cajas en destino coincide con el total de cajas, devuelve true (ganaste)
        return cajasEnDestino == cajas.size();
    }
}