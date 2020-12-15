/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package renamerfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/*
 * GUI starter
 */
public final class FXMLGuiStarter extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("GUI.fxml")));
            scene.getStylesheets().add("style.css");

            stage.setTitle("RenamerFX");
            stage.getIcons().add(new Image("icon.png"));
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entry point for GUI.
     * See javafx.application.Application lifecycle for what's going on here.
     *
     * @param args command-line arguments
     */
    static void launcher(String[] args) {
        launch(args);
    }

}