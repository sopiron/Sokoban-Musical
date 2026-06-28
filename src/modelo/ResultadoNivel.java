package modelo;

public class ResultadoNivel {
    private int segundos;
    private String notas;
    private int movimientos;
    private int empujes;
    private int usosUndo;
    private int puntajeFinal;

    public ResultadoNivel(int segundos, String notas, int movimientos, int empujes, int usosUndo, int puntajeFinal) {
        this.segundos = segundos;
        this.notas = notas;
        this.movimientos = movimientos;
        this.empujes = empujes;
        this.usosUndo = usosUndo;
        this.puntajeFinal = puntajeFinal;
    }

    public int getSegundos() {
        return segundos;
    }

    public String getNotas() {
        return notas;
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

    public int getPuntajeFinal() {
        return puntajeFinal;
    }

    public String getTiempoFormateado() {
        int minutos = segundos / 60;
        int restoSegundos = segundos % 60;

        return String.format("%02d:%02d", minutos, restoSegundos);
    }
}
