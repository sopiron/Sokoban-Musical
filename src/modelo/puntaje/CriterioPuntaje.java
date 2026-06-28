package modelo.puntaje;

import modelo.EstadisticasNivel;

public interface CriterioPuntaje {
    
    int calcularPuntaje(EstadisticasNivel estadisticas, int segundos);
}
