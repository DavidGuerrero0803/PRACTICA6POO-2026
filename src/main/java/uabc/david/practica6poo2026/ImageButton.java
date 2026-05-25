package uabc.david.practica6poo2026;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import java.io.File;

/**
 * ImageButton - Clase que representa un botón que muestra imágenes en lugar de texto.
 */
public class ImageButton extends Button {
    private ImageView imageView;
    private ColorAdjust colorAdjust;

    /**
     * Constructor principal: Crea el botón cargando la imagen en su tamaño original.
     * @param imagePath Ruta de la imagen.
     */
    public ImageButton(String imagePath) {
        super();
        inicializarImageView();
        cargarImagen(imagePath);
        setearEfectos();
        aplicarEstilo();
    }

    /**
     * Constructor parametrizado: Crea el botón y ajusta la imagen a un tamaño específico.
     * @param imagePath Ruta de la imagen.
     * @param width Ancho deseado.
     * @param height Alto deseado.
     */
    public ImageButton(String imagePath, double width, double height) {
        this(imagePath);
        setImageSize(width, height);
    }

    /**
     * Inicializa el ImageView y lo establece como el gráfico del botón.
     */
    private void inicializarImageView() {
        this.imageView = new ImageView();
        setGraphic(imageView);
    }

    /**
     * Configura los cursores al pasar por la imagen.
     */
    private void setearEfectos() {
        this.setCursor(Cursor.HAND);
        colorAdjust = new ColorAdjust();
        imageView.setEffect(colorAdjust);
    }

    /**
     * Carga la imagen desde la ruta proporcionada.
     */
    private void cargarImagen(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
            } else {
                System.err.println("Imagen no encontrada en la ruta: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    /**
     * Quita el fondo y bordes por defecto del botón normal de JavaFX.
     */
    private void aplicarEstilo() {
        setStyle("-fx-background-color: transparent; -fx-padding: 0;");
    }

    /**
     * Ajusta el tamaño de visualización de la imagen.
     */
    public void setImageSize(double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
}
