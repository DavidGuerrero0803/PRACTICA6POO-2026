package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BetweenleGUI extends Application {

    private BorderPane contenedorPrincipal;

    @Override
    public void start(Stage stage) {
        contenedorPrincipal = new BorderPane();

        Label titulo = new Label("BETWEENLE");
        Label subtitulo = new Label("Adivina la palabra secreta escondida entre otras palabras");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 50px;");
        subtitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        subtitulo.setTextFill(Color.GRAY);

        VBox contenedorTitulo = new VBox(titulo, subtitulo);
        contenedorTitulo.setAlignment(Pos.TOP_CENTER);
        contenedorTitulo.setPadding(new Insets(20, 0, 20, 0));

        contenedorPrincipal.setTop(contenedorTitulo);

        mostrarMenuPrincipal();

        Scene scene = new Scene(contenedorPrincipal, 700, 850);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarMenuPrincipal() {
        Button jugar = new Button("JUGAR");
        jugar.setPrefWidth(400);
        jugar.setPrefHeight(50);
        jugar.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: #42adf5; -fx-text-fill: white; -fx-background-radius: 5;");

        jugar.setOnAction(e -> {
            mostrarConfiguracion();
        });

        Button salir = new Button("SALIR");
        salir.setPrefWidth(400);
        salir.setPrefHeight(50);
        salir.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: #f54242; -fx-text-fill: white; -fx-background-radius: 5;");

        salir.setOnAction(e -> {

        });

        VBox contenedorBotones = new VBox(20, jugar, salir);
        contenedorBotones.setAlignment(Pos.CENTER);

        contenedorPrincipal.setCenter(contenedorBotones);
    }

    private void mostrarConfiguracion() {
        String estiloEtiqueta = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;";
        String estiloToggle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 110px; -fx-min-height: 35px;";

        Label idioma = new Label("Selecciona el idioma del diccionario");
        idioma.setStyle(estiloEtiqueta);

        ToggleButton toggleESP = new ToggleButton("Español");
        ToggleButton toggleENG = new ToggleButton("Inglés");
        toggleESP.setStyle(estiloToggle);
        toggleENG.setStyle(estiloToggle);

        ToggleGroup grupoIdioma = new ToggleGroup();
        toggleESP.setToggleGroup(grupoIdioma);
        toggleENG.setToggleGroup(grupoIdioma);
        toggleESP.setSelected(true);

        HBox contenedorIdiomas = new HBox(15, toggleESP, toggleENG);
        contenedorIdiomas.setAlignment(Pos.CENTER);

        Label dificultad = new Label("Selecciona la dificultad:");
        dificultad.setStyle(estiloEtiqueta);

        ToggleButton modoFacil = new ToggleButton("Fácil (5)");
        ToggleButton modoMedio = new ToggleButton("Intermedio (6)");
        ToggleButton modoDificil = new ToggleButton("Difícil (n)");
        modoFacil.setStyle(estiloToggle);
        modoMedio.setStyle(estiloToggle);
        modoDificil.setStyle(estiloToggle);

        ToggleGroup grupoDificultad = new ToggleGroup();
        modoFacil.setToggleGroup(grupoDificultad);
        modoMedio.setToggleGroup(grupoDificultad);
        modoDificil.setToggleGroup(grupoDificultad);
        modoFacil.setSelected(true);

        HBox contenedorDificultades = new HBox(15, modoFacil, modoMedio, modoDificil);
        contenedorDificultades.setAlignment(Pos.CENTER);

        Label letras = new Label("Letras en difícil (7-15)");
        letras.setStyle(estiloEtiqueta);
        Spinner<Integer> spinnerLetras = new Spinner<>(7, 15, 7);
        spinnerLetras.setPrefWidth(100);
        spinnerLetras.setDisable(true);

        grupoDificultad.selectedToggleProperty().addListener((observable, viejoToggle, nuevoToggle) -> {
            if (nuevoToggle == modoDificil) {
                spinnerLetras.setDisable(false);
            } else {
                spinnerLetras.setDisable(true);
            }
        });

        VBox contenedorSpinner = new VBox(5, letras, spinnerLetras);
        contenedorSpinner.setAlignment(Pos.CENTER);

        Label intentos = new Label("Selecciona el número de intentos");
        intentos.setStyle(estiloEtiqueta);

        ToggleButton intentos_10 = new ToggleButton("10");
        ToggleButton intentos_12 = new ToggleButton("12");
        ToggleButton intentos_14 = new ToggleButton("14");
        intentos_10.setStyle(estiloToggle);
        intentos_12.setStyle(estiloToggle);
        intentos_14.setStyle(estiloToggle);

        ToggleGroup grupoIntentos = new ToggleGroup();
        intentos_10.setToggleGroup(grupoIntentos);
        intentos_12.setToggleGroup(grupoIntentos);
        intentos_14.setToggleGroup(grupoIntentos);
        intentos_10.setSelected(true);

        HBox cajaIntentos = new HBox(15, intentos_10, intentos_12, intentos_14);
        cajaIntentos.setAlignment(Pos.CENTER);

        VBox contenedorOpciones = new VBox(20,
                idioma, contenedorIdiomas,
                dificultad, contenedorDificultades,
                contenedorSpinner,
                intentos, cajaIntentos
        );
        contenedorOpciones.setAlignment(Pos.CENTER);
        contenedorOpciones.setPadding(new Insets(20));

        Button comenzar = new Button("COMENZAR");
        comenzar.setPrefWidth(250);
        comenzar.setPrefHeight(50);
        comenzar.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");

        Button regresar = new Button("VOLVER");
        regresar.setPrefWidth(250);
        regresar.setPrefHeight(50);
        regresar.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: #f57242; -fx-text-fill: white; -fx-background-radius: 5;");
        regresar.setOnAction(e -> {
            mostrarMenuPrincipal();
        });

        HBox contenedorAcciones = new HBox(30, regresar, comenzar);
        contenedorAcciones.setAlignment(Pos.CENTER);
        contenedorAcciones.setPadding(new Insets(20, 0, 0, 0));

        VBox contenedorCompleto = new VBox(10, contenedorOpciones, contenedorAcciones);
        contenedorCompleto.setAlignment(Pos.CENTER);

        comenzar.setOnAction(e -> {
            ToggleButton idiomaSeleccionado = (ToggleButton) grupoIdioma.getSelectedToggle();
            String idiomaElegido = (idiomaSeleccionado != null && idiomaSeleccionado.getText().equals("Español")) ? "español" : "inglés";

            ToggleButton intentoSeleccionado = (ToggleButton) grupoIntentos.getSelectedToggle();
            int intentosElegidos = (intentoSeleccionado != null) ? Integer.parseInt(intentoSeleccionado.getText()) : 14;

            ToggleButton modoSeleccionado = (ToggleButton) grupoDificultad.getSelectedToggle();
            String dificultadElegida = "fácil";
            int longitudLetras = 5;

            if (modoSeleccionado != null) {
                if (modoSeleccionado == modoMedio) {
                    dificultadElegida = "intermedio";
                    longitudLetras = 6;
                } else if (modoSeleccionado == modoDificil) {
                    dificultadElegida = "difícil";
                    longitudLetras = spinnerLetras.getValue();
                }
            }

            System.out.println("idioma: " + idiomaElegido + ", dificultad: " + dificultadElegida + " (" + longitudLetras + " letras), intentos: " + intentosElegidos);

        });

        contenedorPrincipal.setCenter(contenedorCompleto);
    }

    public static void main(String[] args) {
        launch();
    }
}