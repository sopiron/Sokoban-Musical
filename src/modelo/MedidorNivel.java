package modelo;

import modelo.dificultad.DificultadPorNivel;
import modelo.observer.ObserverBarraJuego;
import modelo.puntaje.CriterioPuntaje;

import java.util.ArrayList;
import javax.swing.Timer;

public class MedidorNivel {

    private ArrayList<ObserverBarraJuego> observadores;
    private Timer timer;
    private long inicioNivel;
    private int nivelActual;
    private String notasUltimoNivel;
    private DificultadPorNivel dificultadPorNivel;
    private ResultadoNivel resultadoUltimoNivel;

    public MedidorNivel() {
        observadores = new ArrayList<>();
        notasUltimoNivel = "♫♫♫";
    }

    public void agregarObservador(ObserverBarraJuego observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }

        notificar();
    }

    public void iniciarNivel(int nivelActual, DificultadPorNivel dificultadPorNivel) {
        this.nivelActual = nivelActual;
        this.dificultadPorNivel = dificultadPorNivel;
        this.inicioNivel = System.currentTimeMillis();

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, e -> notificar());
        timer.start();

        notificar();
    }

    public ResultadoNivel finalizarNivel(EstadisticasNivel estadisticas, CriterioPuntaje criterioPuntaje) {
        int segundos = getSegundosTranscurridos();
        String notas = dificultadPorNivel.calcularNotas(segundos);

        int puntajeFinal = criterioPuntaje.calcularPuntaje(
            estadisticas,
            segundos
        );

        if (timer != null) {
            timer.stop();
        }

        return new ResultadoNivel(
            segundos,
            notas,
            estadisticas.getMovimientos(),
            estadisticas.getEmpujes(),
            estadisticas.getUsosUndo(),
            puntajeFinal
        );
    }

    public String getNotasUltimoNivel() {
        return notasUltimoNivel;
    }

    private int getSegundosTranscurridos() {
        return (int) ((System.currentTimeMillis() - inicioNivel) / 1000);
    }

    private String calcularNotas() {
        return dificultadPorNivel.calcularNotas(getSegundosTranscurridos());
    }

    private void notificar() {
        if (dificultadPorNivel == null) {
            return;
        }
        int segundos = getSegundosTranscurridos();
        String notas = calcularNotas();

        for (ObserverBarraJuego observador : observadores) {
            observador.actualizarBarra(nivelActual, segundos, notas);
        }
    }

    public ResultadoNivel getResultadoUltimoNivel() {
        return resultadoUltimoNivel;
    }
    
}