package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene.fxml")));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        
        stage.setTitle("RunSet");
        stage.setScene(scene);
        stage.show();

        DDragon dDragon = new DDragon();
        dDragon.checkVersion();
        new ClientApi().init();
    }

    public static void main(String[] args) {
        launch(args);
    }

}