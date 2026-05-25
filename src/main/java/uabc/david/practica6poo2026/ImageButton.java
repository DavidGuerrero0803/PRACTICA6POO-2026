package uabc.david.practica6poo2026;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import java.io.File;

/**
 * ImageButton - Clase que representa un botón que muestra imágenes en lugar de texto.
 * Hereda de Button y carga una imagen predefinida automáticamente.
 */
public class ImageButton extends Button {
    private ImageView imageView;
    private ColorAdjust colorAdjust;

    private static final String DEFAULT_IMAGE_PATH = "";

    /**
     * Constructor por defecto que crea un ImageButton con imagen predefinida.
     * Carga automáticamente la imagen desde la ruta fija.
     */
    public ImageButton() {
        super();
        inicializarImageView();
        cargarImagen();
        setearEfectos();
        aplicarEstilo();
    }

    /**
     * Constructor con tamaño personalizado.
     * @param width Ancho de la imagen.
     * @param height Alto de la imagen.
     */
    public ImageButton(double width, double height) {
        this();
        setImageSize(width, height);
    }

    /**
     * Inicializa el ImageView y configura propiedades básicas.
     */
    private void inicializarImageView() {
        this.imageView = new ImageView();
        setGraphic(imageView);
    }

    /**
     * Configura los efectos visuales para la imagen.
     */
    private void setearEfectos() {
        this.setCursor(Cursor.HAND);
        // Efecto para oscurecer la imagen al presionar.
        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);

        // Aplicar efecto a la imagen
        imageView.setEffect(colorAdjust);
    }

    /**
     * Carga la imagen por defecto
     */
    private void cargarImagen() {
        try {
            File file = new File(DEFAULT_IMAGE_PATH);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
            } else {
                System.err.println("Imagen por defecto no encontrada: " + DEFAULT_IMAGE_PATH);
                System.err.println("Ruta absoluta: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen por defecto: " + e.getMessage());
        }
    }

    /**
     * Aplica el estilo visual por defecto al botón con efectos mejorados
     */
    private void aplicarEstilo() {
        // Estilo inicial del botón.
        setStyle("-fx-background-color: transparent; -fx-background-radius: 5;");
    }

    /**
     * Ajusta el tamaño de visualización de la imagen.
     * @param width Ancho deseado para la imagen.
     * @param height Alto deseado para la imagen.
     */
    public void setImageSize(double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
}
