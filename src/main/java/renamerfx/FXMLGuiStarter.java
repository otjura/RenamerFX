/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 * 
 */

package renamerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * GUI starter class
 */
public final class FXMLGuiStarter extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI.fxml")); // getClassLoader() or Maven won't run this properly
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");

        stage.setTitle("RenamerFX");
        stage.getIcons().add(new Image("icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Entry point for GUI
     * 
     * @param args passed to Application.launch(args)
     */
    static void launcher(String[] args) {
        launch(args);
    }

}