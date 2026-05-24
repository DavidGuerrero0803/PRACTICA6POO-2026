package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BetweenleGUI extends Application {

    private BorderPane contenedorPrincipal;
    private Betweenle juego;
    private Label intentosRestantes;
    private ListView<String> historialPalabras;
    private FlowPane panelTeclado;
    private Button pistas;
    private Label[] casillasEntrada;
    private int letraIngresada = 0;
    private HBox contenedorSuperior;
    private HBox contenedorIntermedio;
    private HBox contenedorInferior;
    private boolean juegoBloqueado = false;

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

            juego = new Betweenle(longitudLetras);
            boolean iniciado = juego.iniciarPartida(idiomaElegido, dificultadElegida, intentosElegidos);

            if (iniciado) {
                Stage stageActual = (Stage) contenedorPrincipal.getScene().getWindow();
                stageActual.setWidth(950);
                stageActual.setHeight(850);
                stageActual.centerOnScreen();

                mostrarInterfazJuego();
            } else {
                mostrarAlerta("Error", "No se encontraron palabras de " + longitudLetras +
                        " letras en el diccionario", Alert.AlertType.ERROR);
            }

        });

        contenedorPrincipal.setCenter(contenedorCompleto);
    }

    private void mostrarInterfazJuego() {
        int longitud = juego.getRondaActual().getLongitudPalabra();
        juegoBloqueado = false;
        letraIngresada = 0;

        intentosRestantes = new Label("Intentos restantes: " + juego.getRondaActual().getIntentosRestantes());
        intentosRestantes.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox contenedorIntentos = new HBox(50, intentosRestantes);
        contenedorIntentos.setAlignment(Pos.CENTER);
        contenedorIntentos.setPadding(new Insets(15, 0, 15, 0));

        contenedorSuperior = new HBox(5);
        contenedorSuperior.setAlignment(Pos.CENTER);

        contenedorIntermedio = new HBox(5);
        contenedorIntermedio.setAlignment(Pos.CENTER);

        Region espaciadorInvisible = new Region();
        espaciadorInvisible.setPrefWidth(35);
        contenedorIntermedio.getChildren().add(espaciadorInvisible);

        casillasEntrada = new Label[longitud];
        for (int i = 0; i < longitud; i++) {
            Label casilla = new Label("");
            casilla.setStyle("-fx-border-color: #5c5c5c; -fx-border-width: 2; -fx-background-color: white; " +
                    "-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-min-width: 40px; " +
                    "-fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
            casillasEntrada[i] = casilla;
            contenedorIntermedio.getChildren().add(casilla);
        }

        contenedorInferior = new HBox(5);
        contenedorInferior.setAlignment(Pos.CENTER);

        VBox componentesCentrales = new VBox(12, contenedorSuperior, contenedorIntermedio, contenedorInferior);
        componentesCentrales.setAlignment(Pos.CENTER);
        componentesCentrales.setPadding(new Insets(20, 0, 20, 0));

        Label tituloHistorial = new Label("PALABRAS UTILIZADAS");
        tituloHistorial.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555555;");

        historialPalabras = new ListView<>();
        historialPalabras.setPrefHeight(80);
        historialPalabras.setMaxWidth(600);
        historialPalabras.setFocusTraversable(false);
        historialPalabras.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

        VBox contenedorHistorialAbajo = new VBox(5, tituloHistorial, historialPalabras);
        contenedorHistorialAbajo.setAlignment(Pos.CENTER);

        panelTeclado = new FlowPane(6, 6);
        panelTeclado.setAlignment(Pos.CENTER);
        panelTeclado.setMaxWidth(550);


        pistas = new Button("PEDIR PISTA");
        pistas.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #FFB300; -fx-text-fill: white; -fx-background-radius: 5;");
        pistas.setFocusTraversable(false);
        pistas.setOnAction(e -> {

        });

        VBox componentesInferiores = new VBox(15, contenedorHistorialAbajo, panelTeclado, pistas);
        componentesInferiores.setAlignment(Pos.CENTER);
        componentesInferiores.setPadding(new Insets(10, 0, 20, 0));

        BorderPane interfazCompleta = new BorderPane();
        interfazCompleta.setTop(contenedorIntentos);
        interfazCompleta.setCenter(componentesCentrales);
        interfazCompleta.setBottom(componentesInferiores);
        interfazCompleta.setRight(null);

        contenedorPrincipal.setCenter(interfazCompleta);

        Platform.runLater(() -> {
            contenedorPrincipal.getScene().setOnKeyPressed(this::manejarTeclado);
            contenedorPrincipal.requestFocus();
        });
        mostrarCursor();

    }

    private void manejarTeclado(KeyEvent event) {
        if (juegoBloqueado) {
            return;
        }
        int longitud = juego.getRondaActual().getLongitudPalabra();

        switch (event.getCode()) {
            case ENTER:
                if (letraIngresada == longitud) {
                    procesarPalabra();
                } else {
                    mostrarAlerta(" ", "La palabra no está en la lista.", Alert.AlertType.WARNING);
                }
                break;
            case BACK_SPACE:
                if (letraIngresada > 0) {
                    letraIngresada--;
                    casillasEntrada[letraIngresada].setText("");
                    mostrarCursor();
                }
                break;
            default:
                String letra = event.getText().toUpperCase();
                if (letra.matches("[A-ZÑ]") && letraIngresada < longitud) {
                    casillasEntrada[letraIngresada].setText(letra);
                    casillasEntrada[letraIngresada].setStyle("-fx-border-width: 2; " +
                            "-fx-background-color: #f57242; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; " +
                            "-fx-min-width: 40px; -fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
                    letraIngresada++;
                    mostrarCursor();
                }
                break;
        }
    }

    private void mostrarCursor() {
        if (juegoBloqueado) {
            return;
        }

        for (int i = 0; i < casillasEntrada.length; i++) {
            if (i == letraIngresada && casillasEntrada[i].getText().equals("•")) {
                casillasEntrada[i].setText("•");
                 casillasEntrada[i].setStyle("-fx-border-color: #5c5c5c; -fx-border-width: 2; -fx-background-color: #f57242; " +
                         "-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-min-width: 40px; " +
                         "-fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
            }
            else if (casillasEntrada[i].getText().isEmpty()) {
                casillasEntrada[i].setText("");
                casillasEntrada[i].setStyle("-fx-border-color: #5c5c5c; -fx-border-width: 2; -fx-background-color: white; " +
                        "-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-min-width: 40px; " +
                        "-fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
            }
        }
    }

    private void procesarPalabra() {
        StringBuilder sb = new StringBuilder();
        for (Label casilla : casillasEntrada) {
            sb.append(casilla.getText());
        }
        String intento = sb.toString().toLowerCase();

        String resultado = juego.procesarIntento(intento);

        switch (resultado) {
            case "longitud":
                break;
            case "no encontrada":
                limpiarCasillas();
                break;
            case "fuera de rango":
                mostrarAlerta(" ", "La palabra " + intento.toUpperCase() + " está fuera de los límites actuales.", Alert.AlertType.WARNING);
                limpiarCasillas();
                break;
            case "antes":
            case "despues":
                limpiarCasillas();
                break;
            case "correcto":
                juegoBloqueado = true;
                break;
            case "sin intentos":
                juegoBloqueado = true;
                break;
        }
    }

    private void limpiarCasillas() {
        letraIngresada = 0;
        for (Label casilla : casillasEntrada) {
            casilla.setText("");
        }
        mostrarCursor();
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}