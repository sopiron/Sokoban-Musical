package util;

import modelo.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NivelLoader {

    public Tablero cargarNivel(String rutaArchivo) {

        Tablero tablero = new Tablero();

        try (BufferedReader br = new BufferedReader(
                new FileReader(rutaArchivo))) {

            String linea;
            int fila = 0;

            while ((linea = br.readLine()) != null) {

                for (int columna = 0;
                     columna < linea.length();
                     columna++) {

                    char simbolo = linea.charAt(columna);

                    Posicion posicion =
                            new Posicion(fila, columna);

                    switch (simbolo) {

                        case '#':
                            tablero.getParedes()
                                   .add(new Pared(posicion));
                            break;

                        case '$':
                            tablero.getCajas()
                                   .add(new Caja(posicion));
                            break;

                        case '.':
                            tablero.getDestinos()
                                   .add(new Destino(posicion));
                            break;

                        case '@':
                            tablero.setJugador(
                                    new Jugador(posicion));
                            break;
                    }
                }

                fila++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tablero;
    }

    public NivelLoader() {
    }
}