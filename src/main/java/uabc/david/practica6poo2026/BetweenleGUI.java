package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
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
    private Button menu;
    private Label[] casillasEntrada;
    private Label titulo;
    private int letraIngresada = 0;
    private HBox contenedorSuperior;
    private HBox contenedorIntermedio;
    private HBox contenedorInferior;
    private boolean juegoBloqueado = false;
    private final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Stage stagePrincipal;

    @Override
    public void start(Stage stage) {
        this.stagePrincipal = stage;
        contenedorPrincipal = new BorderPane();

        titulo = new Label("BETWEENLE");
        Label subtitulo = new Label("Adivina la palabra secreta escondida entre otras palabras");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 50px;");
        subtitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        subtitulo.setTextFill(Color.GRAY);

        VBox contenedorTitulo = new VBox(titulo, subtitulo);
        contenedorTitulo.setAlignment(Pos.TOP_CENTER);
        contenedorTitulo.setPadding(new Insets(20, 0, 20, 0));

        contenedorPrincipal.setTop(contenedorTitulo);

        mostrarMenuPrincipal();

        Scene scene = new Scene(contenedorPrincipal, 550, 700);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarMenuPrincipal() {
        titulo = new Label("BETWEENLE");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 50px;");

        Label subtitulo = new Label("Adivina la palabra secreta escondida entre otras palabras");
        subtitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        subtitulo.setTextFill(Color.GRAY);

        VBox contenedorTitulo = new VBox(titulo, subtitulo);
        contenedorTitulo.setAlignment(Pos.TOP_CENTER);
        contenedorTitulo.setPadding(new Insets(20, 0, 20, 0));

        contenedorSuperior = new HBox(contenedorTitulo);
        contenedorSuperior.setAlignment(Pos.CENTER);
        contenedorPrincipal.setTop(contenedorSuperior);

        SoundButton jugar = new SoundButton("JUGAR");
        jugar.setPrefWidth(400);
        jugar.setPrefHeight(50);
        jugar.setOnAction(event -> {
            mostrarConfiguracion();
        });

        Button salir = new Button("SALIR");
        salir.setPrefWidth(400);
        salir.setPrefHeight(50);
        salir.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: #f54242; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        salir.setOnAction(e -> {
            Platform.exit();
        });

        VBox contenedorBotones = new VBox(20, jugar, salir);
        contenedorBotones.setAlignment(Pos.CENTER);

        contenedorPrincipal.setBottom(null);
        contenedorPrincipal.setCenter(contenedorBotones);
        if (stagePrincipal != null) {
            stagePrincipal.setWidth(550);
            stagePrincipal.setHeight(700);
            stagePrincipal.centerOnScreen();
        }
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
        comenzar.setPrefWidth(220);
        comenzar.setPrefHeight(40);
        comenzar.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");

        Button regresar = new Button("VOLVER");
        regresar.setPrefWidth(220);
        regresar.setPrefHeight(40);
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
            juego = new Betweenle(longitudLetras);
            boolean iniciado = juego.iniciarPartida(idiomaElegido, dificultadElegida, intentosElegidos);

            if (iniciado) {
                Stage stageActual = (Stage) contenedorPrincipal.getScene().getWindow();
                stageActual.setWidth(850);
                stageActual.setHeight(700);
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

        titulo.setText("BETWEENLE");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 50px;");

        VBox contenedorTitulo = new VBox(titulo);
        contenedorTitulo.setAlignment(Pos.TOP_CENTER);
        contenedorTitulo.setPadding(new Insets(10, 0, 10, 0));

        contenedorSuperior = new HBox(contenedorTitulo);
        contenedorSuperior.setAlignment(Pos.CENTER);
        contenedorPrincipal.setTop(contenedorSuperior);

        intentosRestantes = new Label("INTENTO 0 / " + juego.getRondaActual().getIntentosRestantes());
        intentosRestantes.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox contenedorIntentos = new HBox(intentosRestantes);
        contenedorIntentos.setAlignment(Pos.CENTER);


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
                    "-fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px;");
            casillasEntrada[i] = casilla;
            contenedorIntermedio.getChildren().add(casilla);
        }

        contenedorInferior = new HBox(5);
        contenedorInferior.setAlignment(Pos.CENTER);

        VBox componentesCentrales = new VBox(8, contenedorSuperior, contenedorIntermedio, contenedorInferior);
        componentesCentrales.setAlignment(Pos.CENTER);

        Label tituloHistorial = new Label("PALABRAS UTILIZADAS");
        tituloHistorial.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555555;");
        tituloHistorial.setPadding(new Insets(20, 0, 0, 0));

        historialPalabras = new ListView<>();
        historialPalabras.setPrefHeight(60);
        historialPalabras.setMaxWidth(600);
        historialPalabras.setFocusTraversable(false);
        historialPalabras.setOrientation(Orientation.HORIZONTAL);

        VBox contenedorHistorialAbajo = new VBox(5, tituloHistorial, historialPalabras);
        contenedorHistorialAbajo.setAlignment(Pos.CENTER);

        panelTeclado = new FlowPane(2, 2);
        panelTeclado.setAlignment(Pos.CENTER);
        panelTeclado.setMaxWidth(550);
        crearAbecedario();

        pistas = new Button("PEDIR PISTA");
        pistas.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #FFB300; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        pistas.setFocusTraversable(false);
        pistas.setOnAction(e -> {
            manejarPista();
        });

        menu = new Button("MENU");
        menu.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #545454; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        menu.setOnAction(e -> {
            mostrarMenuPrincipal();
        });

        VBox componentesInferiores = new VBox(15, panelTeclado, contenedorHistorialAbajo, pistas, menu);
        componentesInferiores.setAlignment(Pos.CENTER);
        componentesInferiores.setPadding(new Insets(0, 0, 20, 0));

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

        actualizarInterfaz();
        mostrarCursor();

        if (stagePrincipal != null) {
            stagePrincipal.setWidth(850);
            stagePrincipal.setHeight(700);
            stagePrincipal.centerOnScreen();
        }
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
                    actualizarAlfabeto();
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
                    actualizarAlfabeto();
                }
                break;
        }
    }

    private void actualizarAlfabeto() {
        if (juegoBloqueado) {
            return;
        }

        ProcesadorRonda ronda = juego.getRondaActual();

        String limiteInferior = ronda.getLimiteInferior().toUpperCase();
        String limiteSuperior = ronda.getLimiteSuperior().toUpperCase();

        // Se construye el prefijo con las letras que el jugador lleva escritas.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letraIngresada; i++) {
            sb.append(casillasEntrada[i].getText());
        }
        String prefijo = sb.toString();

        // Por defecto el teclado está habilitado.
        int indiceMin = 0;
        int indiceMax = ALFABETO.length() - 1;

        // La palabra debe ser menor que limiteInferior.
        if (letraIngresada < limiteInferior.length()) {
            String prefijoInferior = limiteInferior.substring(0, letraIngresada);
            int comparacion = prefijo.compareTo(prefijoInferior);

            if (comparacion == 0) {
                // Se valida si ambos límites comparten misma letra en una misma posición,
                // o también si tienen letras distintas en dicha posición.
                indiceMax = ALFABETO.indexOf(limiteInferior.charAt(letraIngresada));

            } else if (comparacion > 0) {
                // Si el prefijo sobrepasó los límites, el teclado se apagará.
                indiceMax = -1;
            }
        } else {
            // Si las casillas están llenas con un prefijo mayor
            // que el límite inferior, el teclado se apagará.
            if (prefijo.compareTo(limiteInferior) > 0) {
                indiceMax = -1;
            }
        }

        // La palabra debe ser menor que limiteSuperior.
        if (letraIngresada < limiteSuperior.length()) {
            String prefijoSuperior = limiteSuperior.substring(0, letraIngresada);
            int comparacion = prefijo.compareTo(prefijoSuperior);

            if (comparacion == 0) {
                indiceMin = ALFABETO.indexOf(limiteSuperior.charAt(letraIngresada));

            } else if (comparacion < 0) {
                indiceMin = ALFABETO.length();
            }
        } else {
            // Si las casillas están llenas con un prefijo menor
            // que el límite superior, el teclado se apagará.
            if (prefijo.compareTo(limiteSuperior) < 0) {
                indiceMin = ALFABETO.length();
            }
        }

        for (Node nodo : panelTeclado.getChildren()) {
            if (nodo instanceof Label) {
                Label etiquetaLetra = (Label) nodo;
                char letraBoton = etiquetaLetra.getText().charAt(0);
                int indiceLetraActual = ALFABETO.indexOf(letraBoton);

                if (indiceLetraActual >= indiceMin && indiceLetraActual <= indiceMax) {
                    etiquetaLetra.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50em; -fx-font-weight: bold;" +
                            "-fx-font-size: 18px; -fx-alignment: center; -fx-min-width: 40px; -fx-min-height: 40px;");
                    etiquetaLetra.setTextFill(Color.GRAY);
                } else {
                    etiquetaLetra.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 50em; -fx-font-weight: bold;" +
                            "-fx-font-size: 18px; -fx-alignment: center; -fx-min-width: 40px; -fx-min-height: 40px;");
                    etiquetaLetra.setTextFill(Color.LIGHTGRAY);
                }
            }
        }
    }

    private void mostrarCursor() {
        if (juegoBloqueado) {
            return;
        }

        for (int i = 0; i < casillasEntrada.length; i++) {
            if (i == letraIngresada) {
                casillasEntrada[i].setStyle("-fx-border-color: #595858; -fx-border-width: 2; -fx-background-color: white; " +
                        "-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-min-width: 40px; -fx-min-height: " +
                        "40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
            }
            else if (casillasEntrada[i].getText().isEmpty()) {
                casillasEntrada[i].setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 2; -fx-background-color: white; " +
                        "-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-min-width: 40px; -fx-min-height: " +
                        "40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
            }
            else {
                casillasEntrada[i].setTextFill(Color.WHITE);
                casillasEntrada[i].setStyle("-fx-border-color: #FF9800; -fx-border-width: 2; -fx-background-color: #FF9800; " +
                        "-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-min-width: 40px; -fx-min-height: 40px; " +
                        "-fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
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
                manejarPalabraInexistente(intento);
                limpiarCasillas();
                break;
            case "fuera de rango":
                String limSuperior = juego.getRondaActual().getLimiteSuperior().toUpperCase();
                String limInferior = juego.getRondaActual().getLimiteInferior().toUpperCase();

                if (intento.toLowerCase().compareTo(limSuperior.toLowerCase()) <= 0) {
                    mostrarAlerta(" ", "Ingresa una palabra alfabéticamente posterior a " + limSuperior + ".",
                            Alert.AlertType.WARNING);
                } else {
                    mostrarAlerta(" ", "Ingresa una palabra alfabéticamente anterior a " + limInferior + ".",
                            Alert.AlertType.WARNING);
                }

                limpiarCasillas();
                break;
            case "antes":
            case "despues":
                limpiarCasillas();
                actualizarInterfaz();
                if (juego.getRondaActual().getIntentosRestantes() <= 0) {
                    juegoBloqueado = true;
                    for (Label casilla : casillasEntrada) {
                        casilla.setTextFill(Color.WHITE);
                        casilla.setStyle("-fx-border-color: #ffffff; -fx-border-width: 2; -fx-background-color: #ffffff; " +
                                "-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; " +
                                "-fx-min-width: 40px; -fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
                    }
                    mostrarFinJuego(false);
                }
                break;
            case "correcto":
                actualizarInterfaz();
                juegoBloqueado = true;
                for (Label casilla : casillasEntrada) {
                    casilla.setTextFill(Color.WHITE);
                    casilla.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2; -fx-background-color: #4CAF50; " +
                            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; " +
                            "-fx-min-width: 40px; -fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; -fx-background-radius: 3;");
                }
                mostrarFinJuego(true);
                break;
            case "sin intentos":
                juegoBloqueado = true;
                actualizarInterfaz();
                mostrarFinJuego(false);
                break;
        }
    }

    private void limpiarCasillas() {
        letraIngresada = 0;
        for (Label casilla : casillasEntrada) {
            casilla.setText("");
        }
        mostrarCursor();
        actualizarAlfabeto();
    }

    private void actualizarInterfaz() {
        ProcesadorRonda ronda = juego.getRondaActual();
        boolean sinIntentos = ronda.getHistorialIntentos().isEmpty();
        int longitud = ronda.getLongitudPalabra();

        int totalIntentos = ronda.getHistorialIntentos().size() + ronda.getIntentosRestantes();

        int intentoActual = ronda.getHistorialIntentos().size() + 1;

        if (intentoActual > totalIntentos) {
            intentoActual = totalIntentos;
        }

        intentosRestantes.setText("INTENTO " + intentoActual + " / " + totalIntentos);

        String etiquetaSuperior = sinIntentos ? "?" : String.valueOf(ronda.getProximidadSuperior());
        String palabraArriba = sinIntentos ? "A".repeat(longitud) : ronda.getLimiteSuperior();

        dibujarFilaLimite(contenedorSuperior, palabraArriba, etiquetaSuperior, "#03a9f4");

        String etiquetaInferior = sinIntentos ? "?" : String.valueOf(ronda.getProximidadInferior());
        String palabraAbajo = sinIntentos ? "Z".repeat(longitud) : ronda.getLimiteInferior();

        dibujarFilaLimite(contenedorInferior, palabraAbajo, etiquetaInferior, "#03a9f4");

        historialPalabras.getItems().clear();
        historialPalabras.getItems().addAll(juego.getHistorial());

        actualizarAlfabeto();
    }

    private void dibujarFilaLimite(HBox contenedorLimites, String palabra, String proximidad, String colorHex) {
        contenedorLimites.getChildren().clear();

        Label etiquetaAprox = new Label(proximidad);
        etiquetaAprox.setStyle("-fx-background-color: #03a9f4; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-alignment: center; -fx-min-width: 35px; -fx-min-height: 25px; -fx-font-size: 12px;");
        contenedorLimites.getChildren().add(etiquetaAprox);

        for (char c : palabra.toUpperCase().toCharArray()) {
            Label casilla = new Label(String.valueOf(c));
            casilla.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; " +
                    "-fx-alignment: center; -fx-min-width: 40px; -fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; ");
            contenedorLimites.getChildren().add(casilla);
        }
    }

    private void crearAbecedario() {
        panelTeclado.getChildren().clear();
        String alfabeto = ALFABETO;
        for (char c : alfabeto.toCharArray()) {
            Label letrasAbecedario = new Label(String.valueOf(c));
            letrasAbecedario.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 50em; -fx-font-weight: bold; " +
                    "-fx-alignment: center; -fx-min-width: 40px; -fx-min-height: 40px;");
            panelTeclado.getChildren().add(letrasAbecedario);
        }
    }

    private void manejarPalabraInexistente(String palabra) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(" ");
        alerta.setHeaderText("La palabra " + palabra.toUpperCase() + " no está en el diccionario.");
        alerta.setContentText("¿Puedes demostrar que es una palabra válida?");

        ButtonType aceptarIngreso = new ButtonType("Sí, agregarla al diccionario");
        ButtonType denegarIngreso = new ButtonType("No, escribir otra palabra", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(aceptarIngreso, denegarIngreso);

        alerta.showAndWait().ifPresent(tipo -> {
            if (tipo == aceptarIngreso) {
                juego.agregarPalabraAlDiccionario(palabra);
                mostrarAlerta(" ", "La palabra fue agregada al diccionario. Ahora puedes usarla.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void manejarPista() {
        if (juego.getRondaActual().pistaUtilizada()) {
            mostrarAlerta("", "Ya usaste tu pista en esta partida.", Alert.AlertType.INFORMATION);
            return;
        }

        if (juegoBloqueado) {
            mostrarAlerta("", "La partida acabó, ya no puedes usar pistas.", Alert.AlertType.INFORMATION);
            return;
        }

        ChoiceDialog<String> dialogo = new ChoiceDialog<>("1. Acercar límite superior",
                "1. Acercar límite superior",
                "2. Acercar límite inferior",
                "3. Revelar letra inicial");

        dialogo.setTitle(" ");
        dialogo.setHeaderText("Elige tu pista (solo puedes usar 1 por partida)");
        dialogo.setContentText("Opción:");

        dialogo.showAndWait().ifPresent(seleccion -> {
            int opcion = Integer.parseInt(seleccion.substring(0, 1));
            String resultado = juego.pedirPista(opcion);

            if (resultado.equals("requiere intento")) {
                mostrarAlerta("Pista no disponible", "Ingresa al menos una palabra para establecer los límites iniciales.",
                        Alert.AlertType.WARNING);
            } else if (resultado.equals("demasiado cerca")) {
                mostrarAlerta("Pista no disponible", "No puedes usar ya esta pista, estás muy cerca.",
                        Alert.AlertType.WARNING);
            } else {
                mostrarAlerta("Pista", resultado, Alert.AlertType.INFORMATION);
                actualizarInterfaz();
            }
        });
    }

    private void mostrarFinJuego(boolean victoria) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(victoria ? "ADIVINASTE LA PALABRA" : "TE QUEDASTE SIN INTENTOS");
        alerta.setContentText("LA PALABRA ERA: " + juego.getRondaActual().getPalabraSecreta().toUpperCase());

        alerta.showAndWait();
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