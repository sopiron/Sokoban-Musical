package modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class NivelRockFactory implements NivelFactory{

    private Map<Character, BiConsumer<Posicion, Tablero>> creadores;

    public NivelRockFactory() {
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
    }

    @Override
    public Caja crearCaja(Posicion posicion) {
        return new Caja(posicion, "/images/CajaGuitarra.png"); // después puede ser CajaSaxo
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
    public void crearElemento(char simbolo, Posicion posicion, Tablero tablero) {
        creadores
                .getOrDefault(simbolo, (p, t) -> {})
                .accept(posicion, tablero);
    }

    @Override
    public String getRutaMusicaFondo() {
        return "/sounds/rock.wav";
    }
    
}
