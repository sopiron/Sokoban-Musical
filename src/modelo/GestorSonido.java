package modelo;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class GestorSonido {

    private static GestorSonido gestorSonido;
    
    private Clip musicaActual;
    private String rutaActual;
    private boolean muteado = false;

    private GestorSonido(){}

    public static GestorSonido getInstance(){
        if (gestorSonido == null){
            gestorSonido = new GestorSonido();
        }

        return gestorSonido;
    }

    public void reproducirMusica(String ruta) {
        detenerMusica();
        rutaActual = ruta;

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

    public void setMutear(boolean mutear) {
        this.muteado = mutear;
        aplicarMuteAMusica();
    }

    private void aplicarMuteAMusica() {
        if (musicaActual == null) {
            return;
        }

        try {
            FloatControl volumen = (FloatControl) musicaActual.getControl(
                    FloatControl.Type.MASTER_GAIN
            );

            if (muteado) {
                volumen.setValue(volumen.getMinimum());
            } else {
                volumen.setValue(0.0f);
            }

        } catch (Exception e) {
            // Si el control de volumen no está disponible, lo ignoramos.
        }
    }

    public void reproducirEfecto(String ruta) {
        if (muteado) {
            return;
        }

        try {
            URL url = getClass().getResource(ruta);

            if (url == null) {
                throw new IllegalArgumentException("No se encontró el sonido: " + ruta);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            Clip efecto = AudioSystem.getClip();
            efecto.open(audioInputStream);
            efecto.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
