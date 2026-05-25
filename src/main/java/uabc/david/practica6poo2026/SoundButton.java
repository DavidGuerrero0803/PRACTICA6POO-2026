package uabc.david.practica6poo2026;

import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import java.io.File;

/**
 * Esta clase hereda de Button y permite reproducir
 * un efecto de sonido al hacer un clic.
 */
public class SoundButton extends Button {
    private AudioClip clickSound;

    public SoundButton(String text) {
        super(text);
        establecerSFX();
        aplicarEstilo();
    }

    private void establecerSFX() {
        setClicSonido("src/main/java/uabc/david/practica6poo2026/Menu_Tick.wav");
        setOnMousePressed(e -> {
            reproducirSonido();
        });
        aplicarEstilo();
    }

    public void setClicSonido(String soundFile) {
        try {
            File file = new File(soundFile);
            if (file.exists()) {
                this.clickSound = new AudioClip(file.toURI().toString());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo de sonido: " + e.getMessage());
        }
    }

    public void reproducirSonido() {
        if (clickSound != null) {
            clickSound.play();
        }
    }

    private void aplicarEstilo() {
        setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        setOnMouseEntered(e -> {
            setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");
        });

        setOnMouseExited(e -> {
            setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        });
    }
}