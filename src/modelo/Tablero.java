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
}