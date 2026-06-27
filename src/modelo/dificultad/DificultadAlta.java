package modelo.dificultad;

public class DificultadAlta implements DificultadPorNivel{
    
    @Override
    public String getNombre() {
        return "Alta";
    }

    @Override
    public String calcularNotas(int segundos) {
        if (segundos <= 30) {
            return "♫♫♫";
        }

        if (segundos <= 60) {
            return "♫♫";
        }

        return "♫";
    }
}
