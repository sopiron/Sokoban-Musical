package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.movimientoCaja.MovimientoCaja;
import modelo.movimientoCaja.MovimientoNormal;
import modelo.movimientoCaja.MovimientoResbaladizo;

public class Tablero {

    private EstadisticasNivel estadisticasNivel;
    private Jugador jugador;
    private ArrayList<Caja> cajas;
    private ArrayList<Pared> paredes;
    private ArrayList<Destino> destinos;
    private ArrayList<PisoResbaladizo> pisosResbaladizos;
    private HistorialMovimientos historial;

    private MovimientoCaja movimientoNormal;
    private MovimientoCaja movimientoResbaladizo;

    //Guarda qué caja se está deslizando actualmente.
    private Caja cajaDeslizandose;
    private int difFilaDeslizamiento; //Arriba o abajo
    private int difColumnaDeslizamiento; //Derecha o izquierda

    public Tablero() {
        cajas = new ArrayList<>();
        paredes = new ArrayList<>();
        destinos = new ArrayList<>();
        pisosResbaladizos = new ArrayList<>();

        estadisticasNivel = new EstadisticasNivel();
        historial = new HistorialMovimientos();
        movimientoNormal = new MovimientoNormal();
        movimientoResbaladizo = new MovimientoResbaladizo();
    }

    public void registrarEmpuje() {
        estadisticasNivel.registrarEmpuje();
    }

    public void registrarUndo() {
        estadisticasNivel.registrarUndo();
    }

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
            historial.guardar(guardarMemento());
            jugador.mover(difFila, difColumna);
            estadisticasNivel.registrarMovimiento();
            return true;
        }

        // Guardamos el snapshot ANTES de que el elemento (ej: caja) cambie su estado
        TableroMemento snapshotPrevio = guardarMemento();

        // Si hay un elemento, DELEGAMOS la decisión. El elemento interactúa y decide si nos deja pasar.
        if (elementoFrente.interactuar(difFila, difColumna, this)) {
            historial.guardar(snapshotPrevio);
            jugador.mover(difFila, difColumna);
            estadisticasNivel.registrarMovimiento();
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

    public boolean esDestino(int fila, int columna) {
        for (Destino destino : destinos) {
            if (destino.getPosicion().getFila() == fila &&
                destino.getPosicion().getColumna() == columna) {
                return true;
            }
        }

        return false;
    }

    //---------------------------------------------------------
    //
    //Caja con efecto de deslizamiento
    //
    //---------------------------------------------------------

    //Este método se llama cuando la caja toca el piso resbaladizo.
    public void iniciarDeslizamiento(Caja caja, int difFila, int difColumna) {
        this.cajaDeslizandose = caja;
        this.difFilaDeslizamiento = difFila;
        this.difColumnaDeslizamiento = difColumna;
    }

    public boolean hayCajaDeslizandose() {
        return cajaDeslizandose != null;
    }

    public boolean deslizarCajaUnPaso() {
        if (cajaDeslizandose == null) {
            return false;
        }

        int siguienteFila = cajaDeslizandose.getPosicion().getFila() + difFilaDeslizamiento;
        int siguienteColumna = cajaDeslizandose.getPosicion().getColumna() + difColumnaDeslizamiento;

        ElementoInteractuable elementoSiguiente = obtenerElemento(
                siguienteFila,
                siguienteColumna
        );

        if (elementoSiguiente != null) {
            detenerDeslizamiento();
            return false;
        }

        cajaDeslizandose.setPosicion(
                new Posicion(siguienteFila, siguienteColumna)
        );

        // Si llegó a un destino, se queda ahí y deja de deslizar
        if (esDestino(siguienteFila, siguienteColumna)) {
            detenerDeslizamiento();
            return false;
        }

        return true;
    }

    private void detenerDeslizamiento() {
        cajaDeslizandose = null;
    }

    public MovimientoCaja obtenerMovimientoCaja(int fila, int columna) {
        for (PisoResbaladizo piso : pisosResbaladizos) {
            if (piso.ocupa(fila, columna)) {
                return movimientoResbaladizo;
            }
        }

        return movimientoNormal;
    }

    // ── Memento ────────────────────────────────────────────────────────────────

    public TableroMemento guardarMemento() {
        List<Posicion> posCajas = new ArrayList<>();
        for (Caja caja : cajas) {
            posCajas.add(new Posicion(
                    caja.getPosicion().getFila(),
                    caja.getPosicion().getColumna()
            ));
        }
        Posicion posJugador = new Posicion(
                jugador.getPosicion().getFila(),
                jugador.getPosicion().getColumna()
        );
        return new TableroMemento(posCajas, posJugador);
    }

    public void restaurarMemento(TableroMemento memento) {
        List<Posicion> posCajas = memento.getPosicionesCajas();
        for (int i = 0; i < cajas.size() && i < posCajas.size(); i++) {
            cajas.get(i).setPosicion(posCajas.get(i));
        }
        jugador.setPosicion(memento.getPosicionJugador());
    }

    public boolean deshacerMovimiento() {
        TableroMemento memento = historial.undo();
        if (memento == null) return false;
        restaurarMemento(memento);
        estadisticasNivel.registrarUndo();
        return true;
    }

    public boolean puedeDeshacer() {
        return historial.puedeDeshacer();
    }

    public HistorialMovimientos getHistorial() {
        return historial;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public ArrayList<Caja> getCajas() { return cajas; }
    public void setCajas(ArrayList<Caja> cajas) { this.cajas = cajas; }

    public ArrayList<Pared> getParedes() { return paredes; }
    public void setParedes(ArrayList<Pared> paredes) { this.paredes = paredes; }

    public ArrayList<Destino> getDestinos() { return destinos; }
    public void setDestinos(ArrayList<Destino> destinos) { this.destinos = destinos; }

    public ArrayList<PisoResbaladizo> getPisosResbaladizos() { return pisosResbaladizos; }

    public EstadisticasNivel getEstadisticasNivel() { return estadisticasNivel; }
}