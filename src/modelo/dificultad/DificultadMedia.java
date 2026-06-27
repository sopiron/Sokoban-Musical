package modelo.dificultad;

public class DificultadMedia implements DificultadPorNivel{

     @Override
    public String getNombre() {
        return "Media";
    }

    @Override
    public String calcularNotas(int segundos) {
        if (segundos <= 45) {
            return "♫♫♫";
        }

        if (segundos <= 75) {
            return "♫♫";
        }

        return "♫";
    }
    
}
