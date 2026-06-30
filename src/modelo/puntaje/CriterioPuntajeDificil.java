package modelo.puntaje;

import modelo.EstadisticasNivel;

public class CriterioPuntajeDificil implements CriterioPuntaje {

    @Override
    public int calcularPuntaje(EstadisticasNivel estadisticas, int segundos) {
        int puntajeBase = 3500;

        int penalizacionMovimientos = estadisticas.getMovimientos() * 3;
        int penalizacionEmpujes = estadisticas.getEmpujes() * 6;
        int penalizacionUndo = estadisticas.getUsosUndo() * 100;
        int penalizacionTiempo = segundos;

        int puntajeFinal = puntajeBase
                - penalizacionMovimientos
                - penalizacionEmpujes
                - penalizacionUndo
                - penalizacionTiempo;

        return Math.max(puntajeFinal, 0);
    }
}