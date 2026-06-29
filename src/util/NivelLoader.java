package util;

import modelo.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NivelLoader {

    private NivelFactory factory;

    public NivelLoader(NivelFactory factory) {
        this.factory = factory;
    }

    public Tablero cargarNivel(String rutaArchivo) {

        Tablero tablero = new Tablero();

        try (BufferedReader br = new BufferedReader(
                new FileReader(rutaArchivo))) {

            String linea;
            int fila = 0;

            while ((linea = br.readLine()) != null) {

                    if (linea.startsWith("GENERO=")) {
                        continue;
                    }

                for (int columna = 0;
                     columna < linea.length();
                     columna++) {

                    char simbolo = linea.charAt(columna);

                    Posicion posicion =
                            new Posicion(fila, columna);

                    factory.crearElemento(simbolo, posicion, tablero);
                }

                fila++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        tablero.conectarCerrojosConMuros();

        return tablero;
    }

}