/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package renamerfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import java.io.File;
import java.util.ResourceBundle;
import java.net.URL;
import static renamerfx.Logic.*;

/*
 * Methods and stuff for FXML GUI to use.
 * Has to be public or FXML fails to load with javafx.fxml.LoadException
 */
public final class Controller implements Initializable {

    @FXML private Text statusText;
    @FXML private TextField dirField;
    @FXML private TextField replaceWhatField;
    @FXML private TextField replaceToField;
    @FXML private Button renamerButton;
    @FXML private GridPane grid;

    public void initialize(URL url, ResourceBundle rb) {

        // effects whole grid no need to set for other textfields,
        // they follow automatically with
        dirField.setPrefWidth(240);

        String appStartDir = new File(".").getAbsolutePath();
        statusText.setText("Current directory:\n"+appStartDir);

        renamerButton.setOnAction(e -> {
            String dir = dirField.getText();
            String what = replaceWhatField.getText();
            String to = replaceToField.getText();

            boolean valid = checkFolderValidity(dir);

            if (valid) {
                int filesRenamed = renameRecursively(dir, what, to).size();
                statusText.setText("Success! Renamed "+filesRenamed+" files.");
            }
            else {
            statusText.setText("Invalid folder.");
            }
        });
    }
}