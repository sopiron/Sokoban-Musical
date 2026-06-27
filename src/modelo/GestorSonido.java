package modelo;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GestorSonido {

    private static GestorSonido gestorSonido;
    private Clip musicaActual;

    private GestorSonido(){

    }

    public static GestorSonido getInstance(){
        if (gestorSonido == null){
            gestorSonido = new GestorSonido();
        }

        return gestorSonido;
    }

    public void reproducirMusica(String ruta) {
        detenerMusica();

        try {
            URL url = getClass().getResource(ruta);

            if (url == null) {
                throw new IllegalArgumentException("No se encontró el sonido: " + ruta);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            musicaActual = AudioSystem.getClip();
            musicaActual.open(audioInputStream);
            musicaActual.loop(Clip.LOOP_CONTINUOUSLY);
            musicaActual.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detenerMusica() {
        if (musicaActual != null && musicaActual.isRunning()) {
            musicaActual.stop();
            musicaActual.close();
        }
    }
    
}
