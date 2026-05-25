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
    private Label[] casillasEntrada;
    private Label titulo;
    private int letraIngresada = 0;
    private HBox contenedorSuperior;
    private HBox contenedorIntermedio;
    private HBox contenedorInferior;
    private boolean juegoBloqueado = false;
    private final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private boolean enIngles = false;
    private Stage stagePrincipal;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Betweenle");
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

        SoundButton jugar = new SoundButton("JUGAR",
                "src/main/java/uabc/david/practica6poo2026/Menu_Open.wav",
                "#42adf5", "#2a8cd8", "#ffffff");
        jugar.setPrefWidth(400);
        jugar.setPrefHeight(50);
        jugar.setOnAction(event -> {
            mostrarConfiguracion();
        });

        SoundButton salir = new SoundButton("SALIR",
                "src/main/java/uabc/david/practica6poo2026/Menu_Close.wav",
                "#f54242", "#d61a1a", "#ffffff");
        salir.setPrefWidth(400);
        salir.setPrefHeight(50);
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

        Label idioma = new Label("Selecciona el idioma del diccionario");
        idioma.setStyle(estiloEtiqueta);

        ToggleButton toggleESP = new ToggleButton("Español");
        ToggleButton toggleENG = new ToggleButton("Inglés");

        configurarToggle(toggleESP, "#E0E0E0", "#CCCCCC", "#42adf5");
        configurarToggle(toggleENG, "#E0E0E0", "#CCCCCC", "#42adf5");

        ToggleGroup grupoIdioma = new ToggleGroup();
        toggleESP.setToggleGroup(grupoIdioma);
        toggleENG.setToggleGroup(grupoIdioma);
        toggleESP.setSelected(true);

        grupoIdioma.selectedToggleProperty().addListener((observador, anterior, nuevo) -> {
            if (nuevo == null) {
                anterior.setSelected(true);
            }
        });

        HBox contenedorIdiomas = new HBox(15, toggleESP, toggleENG);
        contenedorIdiomas.setAlignment(Pos.CENTER);

        Label dificultad = new Label("Selecciona la dificultad:");
        dificultad.setStyle(estiloEtiqueta);

        ToggleButton modoFacil = new ToggleButton("Fácil (5)");
        ToggleButton modoMedio = new ToggleButton("Intermedio (6)");
        ToggleButton modoDificil = new ToggleButton("Difícil (n)");

        configurarToggle(modoFacil, "#E0E0E0", "#CCCCCC", "#4CAF50");
        configurarToggle(modoMedio, "#E0E0E0", "#CCCCCC", "#FF9800");
        configurarToggle(modoDificil, "#E0E0E0", "#CCCCCC", "#f54242");

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

        grupoDificultad.selectedToggleProperty().addListener((observador, anterior, nuevo) -> {
            if (nuevo == null) {
                anterior.setSelected(true);
            }
        });

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

        configurarToggle(intentos_10, "#E0E0E0", "#CCCCCC", "#42adf5");
        configurarToggle(intentos_12, "#E0E0E0", "#CCCCCC", "#42adf5");
        configurarToggle(intentos_14, "#E0E0E0", "#CCCCCC", "#42adf5");

        ToggleGroup grupoIntentos = new ToggleGroup();
        intentos_10.setToggleGroup(grupoIntentos);
        intentos_12.setToggleGroup(grupoIntentos);
        intentos_14.setToggleGroup(grupoIntentos);
        intentos_10.setSelected(true);

        grupoIntentos.selectedToggleProperty().addListener((observador, anterior, nuevo) -> {
            if (nuevo == null) {
                anterior.setSelected(true);
            }
        });

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

        SoundButton comenzar = new SoundButton("COMENZAR",
                "src/main/java/uabc/david/practica6poo2026/Menu_Open.wav",
                "#4CAF50", "#358f38", "#ffffff");
        comenzar.setPrefWidth(220);
        comenzar.setPrefHeight(40);

        SoundButton regresar = new SoundButton("VOLVER",
                "src/main/java/uabc/david/practica6poo2026/Menu_Close.wav",
                "#ff641c", "#d45920", "#ffffff");
        regresar.setPrefWidth(220);
        regresar.setPrefHeight(40);
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
            enIngles = idiomaElegido.equals("inglés");

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
                stageActual.setWidth(800);
                stageActual.setHeight(650);
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

        intentosRestantes = new Label((enIngles ? "GUESS 0 / " : "INTENTO 0 / ") + juego.getRondaActual().getIntentosRestantes());
        intentosRestantes.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        ImageButton menu = new ImageButton("src/main/java/uabc/david/practica6poo2026/casa_menu.png", 50, 50);
        menu.setFocusTraversable(false);
        menu.setOnAction(e -> mostrarMenuPrincipal());

        ImageButton pistas = new ImageButton("src/main/java/uabc/david/practica6poo2026/idea_pista.png", 50, 50);
        pistas.setFocusTraversable(false);
        pistas.setOnAction(e -> manejarPista());

        BorderPane barraEstado = new BorderPane();
        barraEstado.setLeft(menu);
        barraEstado.setCenter(intentosRestantes);
        barraEstado.setRight(pistas);

        barraEstado.setPadding(new Insets(10, 160, 20, 160));

        BorderPane.setAlignment(menu, Pos.CENTER_LEFT);
        BorderPane.setAlignment(intentosRestantes, Pos.CENTER);
        BorderPane.setAlignment(pistas, Pos.CENTER_RIGHT);

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

        Label tituloHistorial = new Label(enIngles ? "USED WORDS" : "PALABRAS UTILIZADAS");
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

        VBox componentesInferiores = new VBox(15, panelTeclado, contenedorHistorialAbajo);
        componentesInferiores.setAlignment(Pos.CENTER);
        componentesInferiores.setPadding(new Insets(0, 0, 40, 0));

        BorderPane interfazCompleta = new BorderPane();
        interfazCompleta.setTop(barraEstado);
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
            stagePrincipal.setWidth(800);
            stagePrincipal.setHeight(650);
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
                    mostrarAlerta(" ", enIngles ? "Word not in list" :
                            "La palabra no está en la lista", Alert.AlertType.WARNING);
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
                    mostrarAlerta(" ", enIngles ? "Enter word placed in dictionary after " + limSuperior + "." :
                            "Ingresa una palabra alfabéticamente posterior a " + limSuperior + ".", Alert.AlertType.WARNING);
                } else {
                    mostrarAlerta(" ", enIngles ? "Enter word placed in dictionary before " + limInferior + "." :
                            "Ingresa una palabra alfabéticamente anterior a " + limInferior + ".", Alert.AlertType.WARNING);
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

        intentosRestantes.setText((enIngles ? "GUESS " : "INTENTO ") + intentoActual + " / " + totalIntentos);

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
        alerta.setHeaderText(enIngles ? "The word " + palabra.toUpperCase() + " is not in the dictionary." :
                "La palabra " + palabra.toUpperCase() + " no está en el diccionario.");
        alerta.setContentText(enIngles ? "Can you prove it is a valid word?" : "¿Puedes demostrar que es una palabra válida?");

        ButtonType aceptarIngreso = new ButtonType(enIngles ? "Yes, add it to the dictionary" : "Sí, agregarla al diccionario");
        ButtonType denegarIngreso = new ButtonType(enIngles ? "No, type another word" : "No, escribir otra palabra", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(aceptarIngreso, denegarIngreso);

        alerta.showAndWait().ifPresent(tipo -> {
            if (tipo == aceptarIngreso) {
                juego.agregarPalabraAlDiccionario(palabra);
                mostrarAlerta(" ", enIngles ? "The word was added to the dictionary. You can now use it." :
                        "La palabra fue agregada al diccionario. Ahora puedes usarla.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void manejarPista() {
        if (juego.getRondaActual().pistaUtilizada()) {
            mostrarAlerta("", enIngles ? "You already used your hint in this game." : 
                    "Ya usaste tu pista en esta partida.", Alert.AlertType.INFORMATION);
            return;
        }

        if (juegoBloqueado) {
            mostrarAlerta("", enIngles ? "The game is over, you cannot use hints anymore." : 
                    "La partida acabó, ya no puedes usar pistas.", Alert.AlertType.INFORMATION);
            return;
        }

        String op1 = enIngles ? "1. Move upper limit closer" : "1. Acercar límite superior";
        String op2 = enIngles ? "2. Move lower limit closer" : "2. Acercar límite inferior";
        String op3 = enIngles ? "3. Reveal starting letter" : "3. Revelar letra inicial";

        ChoiceDialog<String> dialogo = new ChoiceDialog<>(op1, op1, op2, op3);
        dialogo.setTitle(" ");
        dialogo.setHeaderText(enIngles ? "Choose your hint (only 1 per game)" : 
                "Elige tu pista (solo puedes usar 1 por partida)");
        dialogo.setContentText(enIngles ? "Option:" : "Opción:");

        dialogo.showAndWait().ifPresent(seleccion -> {
            int opcion = Integer.parseInt(seleccion.substring(0, 1));
            String resultado = juego.pedirPista(opcion);

            if (resultado.equals("requiere intento")) {
                mostrarAlerta(enIngles ? "Hint unavailable" : "Pista no disponible",
                        enIngles ? "Enter at least one word to establish the initial limits." : 
                                "Ingresa al menos una palabra para establecer los límites iniciales.",
                        Alert.AlertType.WARNING);
            } else if (resultado.equals("demasiado cerca")) {
                mostrarAlerta(enIngles ? "Hint unavailable" : "Pista no disponible",
                        enIngles ? "You cannot use this hint anymore, you are too close." : 
                                "No puedes usar ya esta pista, estás muy cerca.",
                        Alert.AlertType.WARNING);
            } else {
                mostrarAlerta(enIngles ? "Hint" : "Pista", resultado, Alert.AlertType.INFORMATION);
                actualizarInterfaz();
            }
        });
    }

    private void mostrarFinJuego(boolean victoria) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        String tituloWin = enIngles ? "YOU GUESSED THE WORD" : "ADIVINASTE LA PALABRA";
        String tituloLose = enIngles ? "YOU DIDN'T GUESS THE WORD" : "TE QUEDASTE SIN INTENTOS";

        alerta.setHeaderText(victoria ? tituloWin : tituloLose);
        alerta.setContentText((enIngles ? "THE WORD WAS " : "LA PALABRA ERA ") +
                juego.getRondaActual().getPalabraSecreta().toUpperCase());

        alerta.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Configura los estados dinámicos para el ToggleButton.
     */
    private void configurarToggle(ToggleButton boton, String colorNormal, String colorHover, String colorSeleccionado) {
        boton.setCursor(Cursor.HAND);

        // Aplica el color inicial basado en su estado actual.
        actualizarColorToggle(boton, colorNormal, colorHover, colorSeleccionado);

        // Reacciona cuando el botón se selecciona o deselecciona.
        boton.selectedProperty().addListener((observador, valorViejo, valorNuevo) -> {
            actualizarColorToggle(boton, colorNormal, colorHover, colorSeleccionado);
        });

        // Reacciona cuando el mouse pasa por encima.
        boton.hoverProperty().addListener((observador, valorViejo, valorNuevo) -> {
            actualizarColorToggle(boton, colorNormal, colorHover, colorSeleccionado);
        });
    }

    /**
     * Aplica el CSS correspondiente según el estado actual del botón.
     */
    private void actualizarColorToggle(ToggleButton boton, String colorNormal, String colorHover, String colorSeleccionado) {
        String colorFondo;
        String colorTexto;

        if (boton.isSelected()) {
            colorFondo = colorSeleccionado;
            colorTexto = "white";
        } else if (boton.isHover()) {
            colorFondo = colorHover;
            colorTexto = "#333333";
        } else {
            colorFondo = colorNormal;
            colorTexto = "#333333";
        }

        boton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 110px; -fx-min-height: 35px; " +
                "-fx-background-color: " + colorFondo + "; " +
                "-fx-text-fill: " + colorTexto + "; " +
                "-fx-background-radius: 5;");
    }

    public static void main(String[] args) {
        launch();
    }
}