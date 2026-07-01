package modelo.puntaje;

import modelo.EstadisticasNivel;

public class CriterioPuntajeMedio implements CriterioPuntaje {

    @Override
    public int calcularPuntaje(EstadisticasNivel estadisticas, int segundos) {
        int puntajeBase = 3500;

        int penalizacionMovimientos = estadisticas.getMovimientos() * 4;
        int penalizacionEmpujes = estadisticas.getEmpujes() * 8;
        int penalizacionUndo = estadisticas.getUsosUndo() * 75;
        int penalizacionTiempo = segundos;

        int puntajeFinal = puntajeBase
                - penalizacionMovimientos
                - penalizacionEmpujes
                - penalizacionUndo
                - penalizacionTiempo;

        return Math.max(puntajeFinal, 0);
    }
}