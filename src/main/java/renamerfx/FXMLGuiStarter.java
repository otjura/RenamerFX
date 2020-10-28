package renamerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class FXMLGuiStarter extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // tl;dr it's a Maven thing https://stackoverflow.com/questions/20507591/javafx-location-is-required-even-though-it-is-in-the-same-package 
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI.fxml"));
        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}