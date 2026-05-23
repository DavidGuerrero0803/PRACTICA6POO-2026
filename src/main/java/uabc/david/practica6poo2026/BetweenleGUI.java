package uabc.david.practica6poo2026;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class BetweenleGUI extends Application {
    @Override
    public void start(Stage stage)  {
        stage.setTitle("Betweenle");

        Label titulo = new Label("Bewteenle");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 36px;");

        VBox contenedor = new VBox(titulo);
        contenedor.setAlignment(Pos.CENTER);
        Scene scene = new Scene(contenedor, 320, 240);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}