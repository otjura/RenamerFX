/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package renamerfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import java.io.File;
import java.util.Arrays;
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

    public void initialize(URL url, ResourceBundle rb) {

        // effects whole grid no need to set for other textfields,
        // they follow automatically with
        dirField.setPrefWidth(240);

        resultTextArea.setEditable(false);

        String appStartDir = new File(".").getAbsolutePath();
        statusText.setText("Current directory:\n"+appStartDir);

        renamerButton.setOnAction(e -> {
            String dir = dirField.getText();
            String what = replaceWhatField.getText();
            String to = replaceToField.getText();

            boolean valid = checkFolderValidity(dir);

            if (valid) {
                String filesRenamed = pprint(renameRecursively(dir, what, to));
                if (filesRenamed.isBlank()) {
                    resultTextArea.setText(NOTHINGRENAMED);
                }
                else {
                    resultTextArea.setText(filesRenamed+"\nSuccessfully renamed files!");
                }
            }
            else {
                resultTextArea.setText(NOSUCHDIR);
            }
        });

        // FIXME copypaste code
        previewButton.setOnAction(e -> {
            String dir = dirField.getText();
            String what = replaceWhatField.getText();
            String to = replaceToField.getText();

            boolean valid = checkFolderValidity(dir);
            if (valid) {
                String rens = pprint(simulateRenaming(dir, what, to));
                if (rens.isBlank()) {
                    rens = NOTHINGRENAMED;
                }
                resultTextArea.setText(rens);
            }
            else {
                resultTextArea.setText(NOSUCHDIR);
            }
        });

        dirButton.setOnAction(e -> {
            String files = fileListing(dirField.getText());
            if (files.isBlank()) {
                resultTextArea.setText(NOSUCHDIR);
            }
            else {
                resultTextArea.setText(files);
            }
        });
    }
}