package controller;

import modelo.Caja;
import modelo.Destino;
import modelo.GestorNiveles;
import modelo.GestorSonido;
import modelo.NivelFactory;
import modelo.NivelFactoryRegistry;
import modelo.NivelRockFactory;
import modelo.Pared;
import modelo.Tablero;
import util.NivelLoader;
import views.ObjetoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegoController{

    private Tablero tablero;
    private static JuegoController controller;
    private GestorNiveles gestorNiveles;
    private NivelFactoryRegistry factoryRegistry;

    private JuegoController(){
        gestorNiveles = new GestorNiveles();
        factoryRegistry = new NivelFactoryRegistry();
    }

    public static JuegoController getInstance(){
        if(controller == null){
            controller = new JuegoController();
        }
        return controller;
    }

    public void iniciarJuego() {
        gestorNiveles.reiniciar();
        cargarNivelActual();
    }

    private void cargarNivelActual() {
        String genero = gestorNiveles.getGeneroNivelActual();

        NivelFactory factory = factoryRegistry.obtenerFactory(genero);

        NivelLoader loader = new NivelLoader(factory);

        tablero = loader.cargarNivel(gestorNiveles.getRutaNivelActual());

        GestorSonido.getInstance().reproducirMusica(factory.getRutaMusicaFondo());
    }

    public boolean pasarAlSiguienteNivel() {
        boolean haySiguiente = gestorNiveles.siguienteNivel();

        if (haySiguiente) {
            cargarNivelActual();
            return true;
        }

        return false;
    }

    public boolean moverArriba() {
        return moverJugador(-1, 0);
    }

    public boolean moverAbajo() {
        return moverJugador(1, 0);
    }

    public boolean moverIzquierda() {
        return moverJugador(0, -1);
    }

    public boolean moverDerecha() {
        return moverJugador(0, 1);
    }

    private boolean moverJugador(int difFila, int difColumna) {
        return tablero.moverJugador(difFila, difColumna);
    }

    public boolean nivelCompletado() {
        return tablero.verificarVictoria();
    }

     public List<ObjetoView> getParedesView() {
        List<ObjetoView> vistas = new ArrayList<>();

        for (Pared pared : tablero.getParedes()) {
            vistas.add(new ObjetoView(
                    pared.getPosicion().getFila(),
                    pared.getPosicion().getColumna()
            ));
        }

        return vistas;
    }

    public List<ObjetoView> getDestinosView() {
        List<ObjetoView> vistas = new ArrayList<>();

        for (Destino destino : tablero.getDestinos()) {
            vistas.add(new ObjetoView(
                    destino.getPosicion().getFila(),
                    destino.getPosicion().getColumna()
            ));
        }

        return vistas;
    }

    public List<ObjetoView> getCajasView() {
        List<ObjetoView> vistas = new ArrayList<>();

        for (Caja caja : tablero.getCajas()) {
            vistas.add(new ObjetoView(
                    caja.getPosicion().getFila(),
                    caja.getPosicion().getColumna(),
                    caja.getRutaImagen()
            ));
        }

        return vistas;
    }

    public Optional<ObjetoView> getJugadorView() {
        return Optional.ofNullable(tablero.getJugador())
                .map(jugador -> new ObjetoView(
                        jugador.getPosicion().getFila(),
                        jugador.getPosicion().getColumna()
                ));
    }

    public int getNivelActual() {
        return gestorNiveles.getNivelActual();
    }
}