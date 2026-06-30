package modelo.dificultad;

public class DificultadBaja implements DificultadPorNivel{

    @Override
    public String getNombre() {
        return "Baja";
    }

    @Override
    public String calcularNotas(int segundos) {
        if (segundos <= 60) {
            return "♫♫♫";
        }

        if (segundos <= 100) {
            return "♫♫";
        }

        return "♫";
    }
    
}
