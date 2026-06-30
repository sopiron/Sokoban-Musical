package modelo.dificultad;

public class DificultadAlta implements DificultadPorNivel{
    
    @Override
    public String getNombre() {
        return "Alta";
    }

    @Override
    public String calcularNotas(int segundos) {
        if (segundos <= 150) {
            return "♫♫♫";
        }

        if (segundos <= 240) {
            return "♫♫";
        }

        return "♫";
    }
}
