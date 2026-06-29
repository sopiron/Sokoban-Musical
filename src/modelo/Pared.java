package modelo;

public class Pared implements ElementoInteractuable {
    private Posicion posicion;

    public Pared(Posicion posicion) {
        this.posicion = posicion;
    }

    @Override
    public boolean interactuar(int difFila, int difColumna, Tablero tablero) {
        // La pared no hace nada y no deja pasar al jugador.
        return false;
    }

    @Override
    public boolean bloquea() {
        return true;
    }

    public String getRutaImagen() {
        return "/images/pared.png";
    }

    public Posicion getPosicion() { return posicion; }
    public void setPosicion(Posicion posicion) { this.posicion = posicion; }
}