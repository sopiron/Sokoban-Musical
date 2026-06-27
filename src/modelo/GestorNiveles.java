package modelo;

import java.io.File;

public class GestorNiveles {

    private int nivelActual = 1;
    private static final String CARPETA_NIVELES = "src/niveles/";

    public String getRutaNivelActual() {
        return CARPETA_NIVELES + "nivel" + nivelActual + ".txt";
    }

    public boolean haySiguienteNivel() {
        int siguiente = nivelActual + 1;
        String rutaSiguiente = CARPETA_NIVELES + "nivel" + siguiente + ".txt";

        File archivo = new File(rutaSiguiente);

        return archivo.exists();
    }

    public boolean siguienteNivel() {
        if (haySiguienteNivel()) {
            nivelActual++;
            return true;
        }

        return false;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void reiniciar() {
        nivelActual = 1;
    }

    //Sirve para poder saber que genero es
    public String getGeneroNivelActual() {
    try (java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader(getRutaNivelActual()))) {

        String primeraLinea = br.readLine();

        if (primeraLinea != null && primeraLinea.startsWith("GENERO=")) {
            return primeraLinea.replace("GENERO=", "").trim();
        }

    } catch (java.io.IOException e) {
        e.printStackTrace();
    }

    return "ROCK";
}
}
