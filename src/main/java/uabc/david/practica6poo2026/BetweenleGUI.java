package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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
        String estiloBoton = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-min-width: 250px; -fx-background-radius: 10;";

        Button jugar = new Button("JUGAR");
        jugar.setPrefWidth(400);
        jugar.setPrefHeight(50);
        jugar.setStyle(estiloBoton);

        jugar.setOnAction(e -> {

        });

        Button salir = new Button("SALIR");
        salir.setPrefWidth(400);
        salir.setPrefHeight(50);
        salir.setStyle(estiloBoton);

        salir.setOnAction(e -> {

        });

        VBox contenedorBotones = new VBox(20, jugar, salir);
        contenedorBotones.setAlignment(Pos.CENTER);

        contenedorPrincipal.setCenter(contenedorBotones);
    }

    public static void main(String[] args) {
        launch();
    }
}