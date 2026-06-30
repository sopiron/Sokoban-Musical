package modelo.puntaje;

import modelo.EstadisticasNivel;

public class CriterioPuntajeSimple implements CriterioPuntaje{

    @Override
    public int calcularPuntaje(
            EstadisticasNivel estadisticas,
            int segundos
    ) {
       int puntajeBase = 1000;

        int penalizacionMovimientos = estadisticas.getMovimientos() * 5;
        int penalizacionEmpujes = estadisticas.getEmpujes() * 10;
        int penalizacionUndo = estadisticas.getUsosUndo() * 50;
        int penalizacionTiempo = segundos * 2;

        int puntajeFinal = puntajeBase
                - penalizacionMovimientos
                - penalizacionEmpujes
                - penalizacionUndo
                - penalizacionTiempo;

        return Math.max(puntajeFinal, 0);
    }
}


