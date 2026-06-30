package modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import modelo.dificultad.DificultadBaja;
import modelo.dificultad.DificultadPorNivel;
import modelo.puntaje.CriterioPuntaje;
import modelo.puntaje.CriterioPuntajeSimple;

public class NivelMetalicaFactory implements NivelFactory{

    private Map<Character, BiConsumer<Posicion, Tablero>> creadores;

    public NivelMetalicaFactory() {
        creadores = new HashMap<>();

        creadores.put('#', (posicion, tablero) ->
                tablero.getParedes().add(crearPared(posicion))
        );

        creadores.put('$', (posicion, tablero) ->
                tablero.getCajas().add(crearCaja(posicion))
        );

        creadores.put('.', (posicion, tablero) ->
                tablero.getDestinos().add(crearDestino(posicion))
        );

        creadores.put('@', (posicion, tablero) ->
                tablero.setJugador(crearJugador(posicion))
        );

        creadores.put('~', (posicion, tablero) ->
                tablero.getPisosResbaladizos().add(crearPisoResbaladizo(posicion))
        );

        creadores.put('F', (posicion, tablero) ->
                tablero.getCajas().add(crearCajaFragil(posicion))
        );

        creadores.put('K', (posicion, tablero) ->
                tablero.getCajas().add(crearCajaLlave(posicion))
        );

        creadores.put('C', (posicion, tablero) ->
                tablero.getCerrojos().add(crearCerrojo(posicion))
        );

        creadores.put('M', (posicion, tablero) ->
                tablero.agregarMuroCerrado(crearMuroCerrado(posicion))
        );
    }

    @Override
    public Caja crearCaja(Posicion posicion) {
        return new Caja(posicion, "/images/cajaTambor.png"); // después puede ser CajaSaxo
    }

    @Override
    public Pared crearPared(Posicion posicion) {
        return new Pared(posicion);
    }

    @Override
    public Destino crearDestino(Posicion posicion) {
        return new Destino(posicion);
    }

    @Override
    public Jugador crearJugador(Posicion posicion) {
        return new Jugador(posicion);
    }

    @Override
    public Caja crearCajaFragil(Posicion posicion) {
        return new CajaFragil(posicion);
    }

    @Override
    public Caja crearCajaLlave(Posicion posicion){
        return new CajaLlave(posicion);
    }

    @Override
    public Cerrojo crearCerrojo(Posicion posicion){
        return new Cerrojo(posicion);
    }

    @Override
    public MuroCerrado crearMuroCerrado(Posicion posicion){
        return new MuroCerrado(posicion);
    }

    @Override
    public void crearElemento(char simbolo, Posicion posicion, Tablero tablero) {
        creadores
                .getOrDefault(simbolo, (p, t) -> {})
                .accept(posicion, tablero);
    }

    @Override
    public PisoResbaladizo crearPisoResbaladizo(Posicion posicion) {
        return new PisoResbaladizo(posicion);
    }

    @Override
    public String getRutaMusicaFondo() {
        return "/sounds/reggue.wav";
    }

    @Override
    public DificultadPorNivel crearDificultadPorNivel() {
        return new DificultadBaja();
    }

    @Override
    public CriterioPuntaje crearCriterioPuntaje(){
        return new CriterioPuntajeSimple();
    }

}