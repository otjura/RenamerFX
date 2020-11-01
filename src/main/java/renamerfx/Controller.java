package renamerfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import java.io.File;
import static renamerfx.Logic.*;

/*
 * Methods and stuff for FXML GUI to use.
 * Has to be public or FXML fails to load with javafx.fxml.LoadException
 */
public final class Controller {

    @FXML
    private Text statusText;

    @FXML
    private TextField dirField;

    @FXML
    private TextField replaceWhatField;

    @FXML
    private TextField replaceToField;

    @FXML
    private void initialize() {
        String appStartDir = new File(".").getAbsolutePath();
        statusText.setText(appStartDir);
    }
  
    @FXML
    private void renameFilesAction(ActionEvent event) {
        
        String dir = dirField.getText();
        String what = replaceWhatField.getText();
        String to = replaceToField.getText();

        boolean valid = checkFolderValidity(dir);

        if (valid) {
            renameRecursively(dir, what, to);
            statusText.setText("Success!");
        }
        else {
            statusText.setText("Invalid folder.");
        }
        
    }

}