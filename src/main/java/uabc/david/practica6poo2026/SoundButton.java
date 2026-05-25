package uabc.david.practica6poo2026;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import java.io.File;

/**
 * Esta clase hereda de Button y permite reproducir
 * un efecto de sonido al hacer un clic.
 */
public class SoundButton extends Button {
    private AudioClip clickSound;
    private String colorNormal;
    private String versionOscura;
    private String colorTexto;

    public SoundButton(String text) {
        super(text);
        this.colorNormal = "#4CAF50";
        this.versionOscura = "#45a049";
        this.colorTexto = "white";
        setClicSonido("src/main/java/uabc/david/practica6poo2026/Menu_Tick.wav");
        inicializarBoton();
    }

    public SoundButton(String text, String rutaSonido, String colorNormal, String versionOscura, String colorTexto) {
        super(text);
        this.colorNormal = colorNormal;
        this.versionOscura = versionOscura;
        this.colorTexto = colorTexto;
        setClicSonido(rutaSonido);
        inicializarBoton();
    }

    private void inicializarBoton() {
        // Cambia el cursor a una mano al pasar sobre el botón
        this.setCursor(Cursor.HAND);
        // Aplica el estilo inicial
        aplicarEstilo(this.colorNormal);
        // Cambia el color al entrar y salir del área del botón
        setOnMouseEntered(e -> {
            aplicarEstilo(this.versionOscura);
        });
        setOnMouseExited(e -> {
            aplicarEstilo(this.colorNormal);
        });

        // Reproduce el sonido
        setOnMousePressed(e -> reproducirSonido());
    }

    private void aplicarEstilo(String colorFondo) {
        setStyle("-fx-background-color: " + colorFondo + "; " +
                "-fx-text-fill: " + colorTexto + "; " +
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5;");
    }

    public void setClicSonido(String archivoSonido) {
        try {
            File file = new File(archivoSonido);
            if (file.exists()) {
                this.clickSound = new AudioClip(file.toURI().toString());
            } else {
                System.err.println("El archivo de sonido no se encontró en la ruta " + archivoSonido);
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
}