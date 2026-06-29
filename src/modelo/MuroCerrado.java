
package modelo;

import modelo.observer.ObserverCerrojo;

//MuroCerrado se comporta como pared
//pero puede abrirse cuando un cerrojo lo notifica.
public class MuroCerrado extends Pared implements ObserverCerrojo {

    private boolean abierto;

    public MuroCerrado(Posicion posicion) {
        super(posicion);
        this.abierto = false;
    }

    @Override
    public void cerrojoActivado() {
        abierto = true;
    }

    @Override
    public boolean bloquea() {
        return !abierto;
    }

    public boolean estaAbierto() {
        return abierto;
    }

    public void restaurarAbierto(boolean abierto) {
        this.abierto = abierto;
    }

    @Override
    public String getRutaImagen() {
        if (abierto) {
            return "";
        }

        return "/images/muroCerrado.png";
    }
}