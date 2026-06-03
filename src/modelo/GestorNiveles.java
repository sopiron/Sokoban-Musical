package modelo;

public class GestorNiveles {

    private int nivelActual = 1;

    public String getRutaNivelActual() {
        return "src/niveles/nivel" + nivelActual + ".txt";
    }

    public void siguienteNivel() {
        if (nivelActual < 7) {
            nivelActual++;
        }
    }

    public int getNivelActual() {
        return nivelActual;
    }
}
