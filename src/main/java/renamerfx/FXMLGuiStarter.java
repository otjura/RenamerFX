/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 * 
 */

package renamerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/*
 * GUI starter class
 */
public class FXMLGuiStarter extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI.fxml")); // getClassLoader() or Maven won't run this properly
        Scene scene = new Scene(root);  // can't be themed
        scene.getStylesheets().add("style.css");

        stage.setTitle("RenamerFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}