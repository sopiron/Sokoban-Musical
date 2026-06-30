package controller;

import modelo.Caja;
import modelo.Cerrojo;
import modelo.Destino;
import modelo.GestorNiveles;
import modelo.GestorSonido;
import modelo.MedidorNivel;
import modelo.NivelFactory;
import modelo.NivelFactoryRegistry;
import modelo.Pared;
import modelo.PisoResbaladizo;
import modelo.ResultadoNivel;
import modelo.Tablero;
import modelo.observer.ObserverBarraJuego;
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
    private MedidorNivel medidorNivel;
    private NivelFactory factory;

    private JuegoController(){
        gestorNiveles = new GestorNiveles();
        factoryRegistry = new NivelFactoryRegistry();
        medidorNivel = new MedidorNivel();
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

        factory = factoryRegistry.obtenerFactory(genero);

        NivelLoader loader = new NivelLoader(factory);

        tablero = loader.cargarNivel(gestorNiveles.getRutaNivelActual());

        GestorSonido.getInstance().reproducirMusica(factory.getRutaMusicaFondo());

        medidorNivel.iniciarNivel(gestorNiveles.getNivelActual(), factory.crearDificultadPorNivel());
        medidorNivel.setEstadisticasNivel(tablero.getEstadisticasNivel());
    }

    public void iniciarJuegoEnNivel(int nivel) {
        gestorNiveles.setNivelActual(nivel);
        cargarNivelActual();
    }

    public boolean pasarAlSiguienteNivel() {

        boolean haySiguiente = gestorNiveles.siguienteNivel();

        if (haySiguiente) {
            cargarNivelActual();
            return true;
        }

        return false;
    }


    public boolean nivelCompletado() {
        return tablero.verificarVictoria();
    }

    public ResultadoNivel finalizarNivelActual() {
        return medidorNivel.finalizarNivel(
            tablero.getEstadisticasNivel(),
            factory.crearCriterioPuntaje()
        );
    }

    public boolean haySiguienteNivel() {
        return gestorNiveles.haySiguienteNivel();
    }


    //Teclas
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
        boolean seMovio = tablero.moverJugador(difFila, difColumna);
        if (seMovio) medidorNivel.notificarAhora(); // ← nuevo
        return seMovio;
    }



    //Views
    public List<ObjetoView> getParedesView() {
        List<ObjetoView> vistas = new ArrayList<>();

        for (Pared pared : tablero.getParedes()) {
            vistas.add(new ObjetoView(
                    pared.getPosicion().getFila(),
                    pared.getPosicion().getColumna(),
                    pared.getRutaImagen()
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

    public List<ObjetoView> getPisosResbaladizosView() {
        List<ObjetoView> vistas = new ArrayList<>();

        for (PisoResbaladizo piso : tablero.getPisosResbaladizos()) {
            vistas.add(new ObjetoView(
                    piso.getPosicion().getFila(),
                    piso.getPosicion().getColumna()
            ));
        }

        return vistas;
    }

    public List<ObjetoView> getCerrojosView() {
        List<ObjetoView> vistas = new ArrayList<>();

        for (Cerrojo cerrojo : tablero.getCerrojos()) {
            vistas.add(new ObjetoView(
                    cerrojo.getPosicion().getFila(),
                    cerrojo.getPosicion().getColumna(),
                    "/images/cerrojo.png"
            ));
        }

        return vistas;
    }



    public void agregarObservadorBarra(ObserverBarraJuego observador) {
        medidorNivel.agregarObservador(observador);
    }

    //Animacion de las cajas
    public boolean hayCajaDeslizandose() {
        return tablero.hayCajaDeslizandose();
    }

    public boolean deslizarCajaUnPaso() {
        return tablero.deslizarCajaUnPaso();
    }



    public String getNotasUltimoNivel() {
        return medidorNivel.getNotasUltimoNivel();
    }


    public int getCantidadNiveles() {
        return gestorNiveles.getCantidadNiveles();
    }

    /**
     * Ejecuta el undo: retrocede hasta 5 movimientos.
     * Devuelve true si se pudo deshacer.
     */
    public boolean accionUndo() {
        return tablero.deshacerMovimiento();
    }

    public boolean puedeDeshacer() {
        return tablero != null && tablero.puedeDeshacer();
    }

    public int getUsosUndoRestantes() {
        if (tablero == null) return 3;
        return 3 - tablero.getHistorial().getUsosConsecutivos();
    }

    public void pausar() {
        medidorNivel.pausar();
    }

    public void reanudar() {
        medidorNivel.reanudar();
    }

    public boolean isPausado() {
        return medidorNivel.isPausado();
    }

    public String getNotasActuales() {
        return medidorNivel.getNotasActuales();
    }

    public void reiniciarNivel() {
        medidorNivel.reanudar();
        cargarNivelActual();
    }
    public int getNivelActual() {
        return gestorNiveles.getNivelActual();
    }

    public void detenerMusica() {
        GestorSonido.getInstance().detenerMusica();
    }

    public void setMutearMusica(boolean mutear) {
        GestorSonido.getInstance().setMutear(mutear);
    }

    public boolean estaMusicaMuteada() {
        return GestorSonido.getInstance().estaMuteado();
    }

    public void toggleMusica() {
        boolean nuevoEstadoMuteado = !GestorSonido.getInstance().estaMuteado();
        GestorSonido.getInstance().setMutear(nuevoEstadoMuteado);
    }

    public void reproducirSonidoNivelCompletado() {
        GestorSonido.getInstance().reproducirEfecto(
                "/sounds/nivelCompletado.wav"
        );
    }
}