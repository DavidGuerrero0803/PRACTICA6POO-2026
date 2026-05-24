package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class BetweenleGUI extends Application {
    @Override
    public void start(Stage stage)  {
        stage.setTitle("Betweenle");

        Label titulo = new Label("BETWEENLE");
        Label subtitulo = new Label("Adivina la palabra secreta escondida entre otras palabras");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 50px;");
        subtitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        subtitulo.setTextFill(Color.GRAY);

        VBox contenedorTitulo = new VBox(titulo, subtitulo);
        contenedorTitulo.setAlignment(Pos.TOP_CENTER);

        contenedorTitulo.setPadding(new Insets(10, 0, 0, 0));

        String estiloBoton = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-min-width: 250px; -fx-background-radius: 10;";

        Button jugar = new Button("JUGAR");
        jugar.setPrefWidth(400);
        jugar.setPrefHeight(50);
        jugar.setStyle(estiloBoton);
        jugar.setOnAction(event -> {

        });

        Button salir = new Button("SALIR");
        salir.setPrefWidth(400);
        salir.setPrefHeight(50);
        salir.setStyle(estiloBoton);
        salir.setOnAction(event -> {

        });

        VBox contenedorBotones = new VBox(20, jugar, salir);
        contenedorBotones.setAlignment(Pos.CENTER);

        BorderPane contenedorPrincipal = new BorderPane();
        contenedorPrincipal.setTop(contenedorTitulo);
        contenedorPrincipal.setCenter(contenedorBotones);

        Scene scene = new Scene(contenedorPrincipal, 700, 850);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}