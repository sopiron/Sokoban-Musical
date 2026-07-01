package modelo.dificultad;

public class DificultadMedia implements DificultadPorNivel{

     @Override
    public String getNombre() {
        return "Media";
    }

    @Override
    public String calcularNotas(int segundos) {
        if (segundos <= 100) {
            return "♫♫♫";
        }

        if (segundos <= 150) {
            return "♫♫";
        }

        return "♫";
    }
    
}
