package modelo;

//Guarda los datos del desempeño del jugador durante un nivel. 
//Estos datos se usan para armar el resumen y calcular el puntaje final.
public class EstadisticasNivel {

    private int movimientos;
    private int empujes;
    private int usosUndo;

    public void registrarMovimiento() {
        movimientos++;
    }

    public void registrarEmpuje() {
        empujes++;
    }

    public void registrarUndo() {
        usosUndo++;
    }

    public int getMovimientos() {
        return movimientos;
    }

    public int getEmpujes() {
        return empujes;
    }

    public int getUsosUndo() {
        return usosUndo;
    }
}