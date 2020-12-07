/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package renamerfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextArea;
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

    private static final String NOSUCHDIR = "Directory doesn't exist or is empty";
    private static final String NOTHINGRENAMED = "Nothing was renamed";

    @FXML private Text statusText;
    @FXML private TextField dirField;
    @FXML private TextField replaceWhatField;
    @FXML private TextField replaceToField;
    @FXML private Button renamerButton;
    @FXML private Button previewButton;
    @FXML private Button dirButton;
    @FXML private GridPane grid;
    @FXML private TextArea resultTextArea;

    // set simulate to true to skip actual renaming and see would-be results
    private void runRename(boolean simulate) {
        String dir = dirField.getText();
        String what = replaceWhatField.getText();
        String to = replaceToField.getText();

        boolean valid = checkFolderValidity(dir);

        if (valid) {
            String filesRenamed = pprint(renameRecursively(dir, what, to, simulate));
            if (filesRenamed.isBlank()) {
                resultTextArea.setText(NOTHINGRENAMED);
            }
            else {
                if (simulate) {
                    resultTextArea.setText(filesRenamed+"\nWould rename the files as shown.");
                }
                else {
                    resultTextArea.setText(filesRenamed+"\nSuccessfully renamed files!");
                }
            }
        }
        else {
            resultTextArea.setText(NOSUCHDIR);
        }
    }

    // lists directory contents on text area
    private void listDirectory() {
        String files = fileListing(dirField.getText());
        if (files.isBlank()) {
            resultTextArea.setText(NOSUCHDIR);
        }
        else {
            resultTextArea.setText(files);
        }
    }

    // first thing that is run when GUI.fxml is loaded
    public void initialize(URL url, ResourceBundle rb) {

        // effects whole grid no need to set for other textfields,
        // they follow automatically with
        dirField.setPrefWidth(240);

        resultTextArea.setEditable(false);

        String appStartDir = new File(".").getAbsolutePath();
        statusText.setText("Current directory:\n"+appStartDir);

        // Rename files, display result on result area
        renamerButton.setOnAction(e -> {
            runRename(false);
        });

        // Preview renaming on result area without renaming files
        previewButton.setOnAction(e -> {
            runRename(true);
        });

        // List directory content on result area
        dirButton.setOnAction(e -> {
            listDirectory();
        });

        // hotkeys bound on grid layer
        grid.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F1) listDirectory();
            if (e.getCode() == KeyCode.F2) runRename(true);
            if (e.getCode() == KeyCode.F3) runRename(false);
        });
    }
}