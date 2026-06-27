package modelo;

public class ResultadoNivel {
     private int segundos;
    private String notas;

    public ResultadoNivel(int segundos, String notas) {
        this.segundos = segundos;
        this.notas = notas;
    }

    public int getSegundos() {
        return segundos;
    }

    public String getNotas() {
        return notas;
    }

    public String getTiempoFormateado() {
        int minutos = segundos / 60;
        int restoSegundos = segundos % 60;

        return String.format("%02d:%02d", minutos, restoSegundos);
    }
}
