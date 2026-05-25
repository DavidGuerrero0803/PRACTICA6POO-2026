package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Esta clase provee la interfaz de usuario gráfica (GUI) para el juego Betweenle.
 * Gestiona los menús, captura las entradas del teclado y actualiza la vista de los resultados.
 */
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

    /**
     * Ejecuta y muestra el principal del juego.
     * @param stage El stage principal de la aplicación.
     */
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

        // Agrupa el título y subtítulo en la parte superior central.
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

    /**
     * Crea y muestra la pantalla inicial con los botones de JUGAR y SALIR.
     */
    private void mostrarMenuPrincipal() {
        // Se reconstruye el encabezado principal para el menú por si se regresa.
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

        // Uso de la subclase ImageButton para crear el botón de JUGAR.
        ImageButton jugar = new ImageButton("src/main/java/uabc/david/practica6poo2026/boton_jugar.png", 400, 50);
        jugar.setOnAction(e -> {
            // Al presionarlo, mostrará al jugador el menú de configuración de partida.
            mostrarConfiguracion();
        });

        // Uso de la subclase ImageButton para crear el botón de SALIR.
        ImageButton salir = new ImageButton("src/main/java/uabc/david/practica6poo2026/boton_salir.png", 400, 50);
        salir.setOnAction(e -> {
            // Al presionarlo, el programa se cerrará.
            Platform.exit();
        });

        // Contenedor para alinear los botones principales en el centro de la pantalla.
        VBox contenedorBotones = new VBox(20, jugar, salir);
        contenedorBotones.setAlignment(Pos.CENTER);
        contenedorPrincipal.setBottom(null);
        contenedorPrincipal.setCenter(contenedorBotones);

        // En caso de volver del juego, se restaura el tamaño de la ventana del menú.
        if (stagePrincipal != null) {
            stagePrincipal.setWidth(550);
            stagePrincipal.setHeight(700);
            stagePrincipal.centerOnScreen();
        }
    }

    /**
     * Despliega la interfaz del menú de configuración inicial.
     * Permite al usuario seleccionar el idioma, la dificultad y los intentos.
     */
    private void mostrarConfiguracion() {
        String estiloEtiqueta = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;";

        // Configuración del idioma de acuerdo a lo que elija el usuario con el ToggleButton.
        Label idioma = new Label("Selecciona el idioma del diccionario");
        idioma.setStyle(estiloEtiqueta);

        ToggleButton toggleESP = new ToggleButton("Español");
        ToggleButton toggleENG = new ToggleButton("Inglés");

        // Si el cursor pasa o selecciona uno de los botones, se pintará de ciertos colores.
        configurarToggle(toggleESP, "#E0E0E0", "#CCCCCC", "#42adf5");
        configurarToggle(toggleENG, "#E0E0E0", "#CCCCCC", "#42adf5");

        // Los ToggleButton se guardan dentro de su grupo respectivo.
        ToggleGroup grupoIdioma = new ToggleGroup();
        toggleESP.setToggleGroup(grupoIdioma);
        toggleENG.setToggleGroup(grupoIdioma);
        toggleESP.setSelected(true);

        // Previene que ningún botón se quede sin seleccionar.
        grupoIdioma.selectedToggleProperty().addListener((observador, anterior, nuevo) -> {
            if (nuevo == null) {
                anterior.setSelected(true);
            }
        });

        // Guarda los botones de idiomas dentro de su contenedor horizontal.
        HBox contenedorIdiomas = new HBox(15, toggleESP, toggleENG);
        contenedorIdiomas.setAlignment(Pos.CENTER);

        // Configuración de la dificultad de acuerdo a lo que elija el usuario con el ToggleButton.
        Label dificultad = new Label("Selecciona la dificultad:");
        dificultad.setStyle(estiloEtiqueta);

        ToggleButton modoFacil = new ToggleButton("Fácil (5)");
        ToggleButton modoMedio = new ToggleButton("Intermedio (6)");
        ToggleButton modoDificil = new ToggleButton("Difícil (n)");

        // De acuerdo a lo que elija de modo, el color de cada botón seleccionado será diferente.
        configurarToggle(modoFacil, "#E0E0E0", "#CCCCCC", "#4CAF50");
        configurarToggle(modoMedio, "#E0E0E0", "#CCCCCC", "#FF9800");
        configurarToggle(modoDificil, "#E0E0E0", "#CCCCCC", "#f54242");

        ToggleGroup grupoDificultad = new ToggleGroup();
        modoFacil.setToggleGroup(grupoDificultad);
        modoMedio.setToggleGroup(grupoDificultad);
        modoDificil.setToggleGroup(grupoDificultad);
        modoFacil.setSelected(true);

        // Guarda los botones de dificultad dentro de su contenedor.
        HBox contenedorDificultades = new HBox(15, modoFacil, modoMedio, modoDificil);
        contenedorDificultades.setAlignment(Pos.CENTER);

        // Si el jugador elige "Difícil", se mostrará un Spinner que le deja elegir entre 7 y 15 (letras).
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

        // Activa el spinner únicamente si se seleccionó la dificultad difícil.
        grupoDificultad.selectedToggleProperty().addListener((observable, viejoToggle, nuevoToggle) -> {
            if (nuevoToggle == modoDificil) {
                spinnerLetras.setDisable(false);
            } else {
                spinnerLetras.setDisable(true);
            }
        });

        // Guarda el Spinner dentro de su propio contenedor.
        VBox contenedorSpinner = new VBox(5, letras, spinnerLetras);
        contenedorSpinner.setAlignment(Pos.CENTER);

        // Configuración de la dificultad de acuerdo a lo que elija el usuario con el ToggleButton.
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

        // Guarda los botones de cantidad de intentos dentro de su contenedor.
        HBox contenedorIntentos = new HBox(15, intentos_10, intentos_12, intentos_14);
        contenedorIntentos.setAlignment(Pos.CENTER);

        // Almacena todos los contenedores previos a un contenedor vertical.
        VBox contenedorOpciones = new VBox(20,
                idioma, contenedorIdiomas,
                dificultad, contenedorDificultades,
                contenedorSpinner,
                intentos, contenedorIntentos
        );
        contenedorOpciones.setAlignment(Pos.CENTER);
        contenedorOpciones.setPadding(new Insets(20));

        // Uso de la subclase SoundButton para emitir un sonido de "abrir" para empezar.
        SoundButton comenzar = new SoundButton("COMENZAR",
                "src/main/java/uabc/david/practica6poo2026/Menu_Open.wav",
                "#4CAF50", "#358f38", "#ffffff");
        comenzar.setPrefWidth(220);
        comenzar.setPrefHeight(40);

        // Uso de la subclase SoundButton para emitir un sonido de "cerrar" para volver.
        SoundButton regresar = new SoundButton("VOLVER",
                "src/main/java/uabc/david/practica6poo2026/Menu_Close.wav",
                "#ff641c", "#d45920", "#ffffff");
        regresar.setPrefWidth(220);
        regresar.setPrefHeight(40);
        regresar.setOnAction(e -> {
            // En caso de elegir regresar, mostrará de nuevo el menú principal.
            mostrarMenuPrincipal();
        });

        // Guarda ambos botones dentro del contenedor.
        HBox contenedorAcciones = new HBox(30, regresar, comenzar);
        contenedorAcciones.setAlignment(Pos.CENTER);
        contenedorAcciones.setPadding(new Insets(20, 0, 0, 0));

        // Este último VBox almacena todos los componentes anteriormente creados.
        VBox contenedorCompleto = new VBox(10, contenedorOpciones, contenedorAcciones);
        contenedorCompleto.setAlignment(Pos.CENTER);

        // Captura los valores escogidos e inicializa el juego.
        comenzar.setOnAction(e -> {
            ToggleButton idiomaSeleccionado = (ToggleButton) grupoIdioma.getSelectedToggle();
            String idiomaElegido = (idiomaSeleccionado != null && idiomaSeleccionado.getText().equals("Español")) ? "español" : "inglés";
            enIngles = idiomaElegido.equals("inglés");

            ToggleButton intentoSeleccionado = (ToggleButton) grupoIntentos.getSelectedToggle();
            int intentosElegidos = (intentoSeleccionado != null) ? Integer.parseInt(intentoSeleccionado.getText()) : 14;

            ToggleButton modoSeleccionado = (ToggleButton) grupoDificultad.getSelectedToggle();
            String dificultadElegida = "fácil";
            int longitudLetras = 5;

            // Se ajusta la longitud de la palabra secreta basado en la dificultad.
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

            // Verifica que el diccionario pueda usarse para poder mostrar la interfaz del juego.
            if (iniciado) {
                Stage stageActual = (Stage) contenedorPrincipal.getScene().getWindow();
                stageActual.setWidth(800);
                stageActual.setHeight(650);
                stageActual.centerOnScreen();

                mostrarInterfazJuego();
            } else {
                // En caso de haber un error, lanzará un Alert avisando al respecto.
                mostrarAlerta("Error", "No se encontraron palabras de " + longitudLetras +
                        " letras en el diccionario", Alert.AlertType.ERROR);
            }
        });

        contenedorPrincipal.setCenter(contenedorCompleto);
    }

    /**
     * Inicializa y despliega la pantalla principal de la ronda del juego.
     * Muestra los intentos, imágenes botón, las casillas de texto y el historial.
     */
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

        // Uso de ImageButton para crear un botón con el icono del Betweenle para volver al menú.
        ImageButton menu = new ImageButton("src/main/java/uabc/david/practica6poo2026/casa_menu.png", 50, 50);
        menu.setFocusTraversable(false);
        menu.setOnAction(e -> {
            mostrarMenuPrincipal();
        });

        // Uso de ImageButton para crear un botón con forma de bombilla para solicitar pistas.
        ImageButton pistas = new ImageButton("src/main/java/uabc/david/practica6poo2026/idea_pista.png", 50, 50);
        pistas.setFocusTraversable(false);
        pistas.setOnAction(e -> {
            manejarPista();
        });

        // Uso de ImageButton para crear un botón con forma de estadística para mostrar eso.
        ImageButton estadisticas = new ImageButton("src/main/java/uabc/david/practica6poo2026/stats_estadisticas.png", 50, 50);
        estadisticas.setFocusTraversable(false);
        estadisticas.setOnAction(e -> {
            mostrarEstadisticas();
        });

        HBox contenedorDerecho = new HBox(10, pistas, estadisticas);
        contenedorDerecho.setAlignment(Pos.CENTER_RIGHT);

        // Panel que distribuye los botones de imágenes y la etiqueta de intentos.
        BorderPane barraEstado = new BorderPane();
        barraEstado.setLeft(menu);
        barraEstado.setCenter(intentosRestantes);
        barraEstado.setRight(contenedorDerecho);

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

        // Genera las etiquetas visuales (casillas) según la longitud de la palabra.
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
        tituloHistorial.setPadding(new Insets(5, 0, 0, 0));

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

        // Uso de la subclase SoundButon para tener un botón que al dar clic
        // hará la función del Enter del teclado, haciendo un sonido de confirmación.
        SoundButton ingresar = new SoundButton(enIngles ? "ENTER" : "INGRESAR",
                "src/main/java/uabc/david/practica6poo2026/Research_0.wav",
                "#a9aaab", "#8c8c8c", "#ffffff");
        // Se simula la pulsación de la tecla ENTER.
        ingresar.setOnAction(e -> {
            KeyEvent enter = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                    false, false, false, false);
            manejarTeclado(enter);
        });

        VBox componentesInferiores = new VBox(15, panelTeclado, ingresar, contenedorHistorialAbajo);
        componentesInferiores.setAlignment(Pos.CENTER);
        componentesInferiores.setPadding(new Insets(20, 0, 20, 0));

        BorderPane interfazCompleta = new BorderPane();
        interfazCompleta.setTop(barraEstado);
        interfazCompleta.setCenter(componentesCentrales);
        interfazCompleta.setBottom(componentesInferiores);
        interfazCompleta.setRight(null);

        contenedorPrincipal.setCenter(interfazCompleta);
        // Fuerza el foco hacia la escena para capturar lo que escriba del teclado.
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

    /**
     * Ejecuta la acción lógica asociada a la tecla usada del teclado (Letra, Delete o Enter).
     * @param event El texto introducido con el teclado.
     */
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
                // Retrocede el índice de la casilla y la deja vacía.
                if (letraIngresada > 0) {
                    letraIngresada--;
                    casillasEntrada[letraIngresada].setText("");
                    mostrarCursor();
                    actualizarAlfabeto();
                }
                break;
            default:
                String letra = event.getText().toUpperCase();
                // Verifica que el caracter ingresado sea válido y que aún queden casillas libres.
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

    /**
     * Recorre todos los botones del teclado virtual y los restaura a su estado inicial.
     */
    private void reiniciarTeclado() {
        if (panelTeclado != null) {
            for (Node nodo : panelTeclado.getChildren()) {
                if (nodo instanceof Button) {
                    Button botonLetra = (Button) nodo;
                    // Reactiva el botón en caso de que esté deshabilitado.
                    botonLetra.setDisable(false);
                    // Restaura el estilo original.
                    botonLetra.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 50em; -fx-font-weight: bold;" +
                            "-fx-font-size: 18px; -fx-alignment: center; -fx-min-width: 40px; -fx-min-height: 40px;");
                }
            }
        }
    }

    /**
     * Actualiza el teclado visual, apagando las letras que alfabéticamente, ya no se pueden usar.
     */
    private void actualizarAlfabeto() {
        if (juegoBloqueado) {
            return;
        }

        ProcesadorRonda ronda = juego.getRondaActual();

        // Se obtienen los límites y formamos el prefijo actual escrito.
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

        // Itera sobre las letras y las apaga o prende.
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

    /**
     * Mueve la casilla seleccionada simulando el movimiento de un cursor.
     */
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

    /**
     * Captura el texto de las casillas y lo evalúa con las validaciones establecidas.
     */
    private void procesarPalabra() {
        StringBuilder sb = new StringBuilder();
        for (Label casilla : casillasEntrada) {
            sb.append(casilla.getText());
        }
        String intento = sb.toString().toLowerCase();

        String resultado = juego.procesarIntento(intento);

        // Dentro del switch-case se evalúa la respuesta dada por el jugador (palabra).
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

                // Si al final de la partida el contador de intentos bajó a 0, se acaba la partida.
                if (juego.getRondaActual().getIntentosRestantes() <= 0) {
                    juegoBloqueado = true;
                    for (Label casilla : casillasEntrada) {
                        // Todas las casillas de ingreso se pintan de blanco.
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
                // Si el jugador adivina la palabra, las casillas se pintan de verde.
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

    /**
     * Vacía el contenido de todas las casillas de entrada y reinicia el índice para poder escribir de nuevo.
     */
    private void limpiarCasillas() {
        letraIngresada = 0;
        for (Label casilla : casillasEntrada) {
            casilla.setText("");
        }
        mostrarCursor();
        actualizarAlfabeto();
    }

    /**
     * Actualiza la interfaz con las etiquetas, historial y el alfabeto/teclado inferior.
     */
    private void actualizarInterfaz() {
        ProcesadorRonda ronda = juego.getRondaActual();
        boolean sinIntentos = ronda.getHistorialIntentos().isEmpty();
        int longitud = ronda.getLongitudPalabra();
        int totalIntentos = ronda.getHistorialIntentos().size() + ronda.getIntentosRestantes();
        int intentoActual = ronda.getHistorialIntentos().size() + 1;

        if (intentoActual > totalIntentos) {
            intentoActual = totalIntentos;
        }

        // Muestra el número de intentos que ha realizado de manera ascendente.
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

    /**
     * Dibuja las filas de casillas de los límites superior e inferior.
     * @param contenedorLimites El contenedor de los caracteres de los límites.
     * @param palabra La palabra dividida en casillas dentro de los límites.
     * @param proximidad Etiqueta de las aproximaciones.
     * @param colorHex Color que tendrán las casillas.
     */
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

    /**
     * Genera los botones que conforman el teclado dinámico inferior.
     */
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

    /**
     * Muestra un Alert interactivo en caso de escribir una palabra que no está en los diccionarios.
     * Permite integrarla al diccionario de forma permanente si el jugador la toma como válida.
     * @param palabra La palabra que no se encontró en los archivos de texto.
     */
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

    /**
     * Muestra la interfaz interactiva para seleccionar y consumir la pista de la ronda.
     */
    private void manejarPista() {
        // Condición que se ejecuta si la pista ya fue utilizada en la sesión.
        if (juego.getRondaActual().pistaUtilizada()) {
            mostrarAlerta("", enIngles ? "You already used your hint in this game." : 
                    "Ya usaste tu pista en esta partida.", Alert.AlertType.INFORMATION);
            return;
        }

        // Condición que se ejecuta si la partida ya terminó.
        if (juegoBloqueado) {
            mostrarAlerta("", enIngles ? "The game is over, you cannot use hints anymore." : 
                    "La partida acabó, ya no puedes usar pistas.", Alert.AlertType.INFORMATION);
            return;
        }

        // Opciones que se le darán al usuario para escoger su pista.
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

            // Se valida que las pistas 1 y 2 requieren al menos un intento previo.
            if (resultado.equals("requiere intento")) {
                mostrarAlerta(enIngles ? "Hint unavailable" : "Pista no disponible",
                        enIngles ? "Enter at least one word to establish the initial limits." : 
                                "Ingresa al menos una palabra para establecer los límites iniciales.",
                        Alert.AlertType.WARNING);
                // Se valida también que pueda usar la pista en una aproximación mayor o igual a 2.00.
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

    /**
     * Genera la alerta de finalización de partida mostrando si el jugador ganó o perdió.
     * @param victoria true si la palabra secreta fue descubierta, false en caso contrario.
     */
    private void mostrarFinJuego(boolean victoria) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        reiniciarTeclado();
        String tituloWin = enIngles ? "YOU GUESSED THE WORD" : "ADIVINASTE LA PALABRA";
        String tituloLose = enIngles ? "YOU DIDN'T GUESS THE WORD" : "TE QUEDASTE SIN INTENTOS";

        alerta.setHeaderText(victoria ? tituloWin : tituloLose);
        alerta.setContentText((enIngles ? "THE WORD WAS " : "LA PALABRA ERA ") +
                juego.getRondaActual().getPalabraSecreta().toUpperCase());

        alerta.showAndWait();
    }

    /**
     * Construye y despliega un cuadro emergente (Alert).
     * @param titulo El título de la ventana de la alerta.
     * @param mensaje El contenido del texto a mostrar en el cuerpo.
     * @param tipo El tipo de la alerta (WARNING, INFORMATION o ERROR).
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra las estadísticas generales de la partida actual.
     */
    private void mostrarEstadisticas() {
        if (juego == null || juego.getRondaActual() == null) {
            return;
        }

        int palabrasUsadas = juego.getRondaActual().getHistorialIntentos().size();
        int totalIntentos = palabrasUsadas + juego.getRondaActual().getIntentosRestantes();

        ArrayList<String> listaLetras = new ArrayList<>(juego.getRondaActual().getLetrasUsadas());
        Collections.sort(listaLetras);

        String letrasFormateadas = listaLetras.isEmpty() ?
                (enIngles ? "None yet" : "Ninguna aún") :
                String.join(", ", listaLetras);

        String tituloAlert = enIngles ? "GAME STATISTICS" : "ESTADÍSTICAS DEL JUEGO";
        String mensaje = (enIngles ? "Total attempts allowed: " : "Intentos totales permitidos: ") + totalIntentos + "\n\n" +
                (enIngles ? "Words used so far: " : "Palabras usadas hasta ahora: ") + palabrasUsadas + "\n\n" +
                (enIngles ? "Letters used: " : "Letras usadas: ") + letrasFormateadas;

        mostrarAlerta(tituloAlert, mensaje, Alert.AlertType.INFORMATION);
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

    /**
     * Función main qur arranca la ejecución de la aplicación.
     */
    public static void main(String[] args) {
        launch();
    }
}