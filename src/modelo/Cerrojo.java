package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.observer.ObserverCerrojo;

public class Cerrojo {

    private Posicion posicion;
    private boolean activado;
    private List<ObserverCerrojo> observadores;

    public Cerrojo(Posicion posicion) {
        this.posicion = posicion;
        this.activado = false;
        this.observadores = new ArrayList<>();
    }

    //Sirve para conectar un muro con un cerrojo
    public void agregarObservador(ObserverCerrojo observador) {
        observadores.add(observador);
    }

    //Marca cerrojo como activado
    public void activar() {
        if (activado) {
            return;
        }

        activado = true;
        notificarObservadores();
    }

    private void notificarObservadores() {
        for (ObserverCerrojo observador : observadores) {
            observador.cerrojoActivado();
        }
    }

    public boolean ocupa(int fila, int columna) {
        return posicion.getFila() == fila &&
               posicion.getColumna() == columna;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public boolean estaActivado() {
        return activado;
    }

    public void restaurarActivado(boolean activado) {
        this.activado = activado;
    }
}