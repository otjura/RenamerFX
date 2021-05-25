/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

import java.util.ResourceBundle;
import java.net.URL;

import static com.github.otjura.renamerfx.core.Logic.*;

/**
 * Methods and fields for GUI.fxml view. Has to be public or FXML fails to load with javafx.fxml.LoadException
 */
public final class MainViewController implements Initializable
{
    private static final String NO_SUCH_DIR = "Directory doesn't exist or is empty";
    private static final String NOTHING_RENAMED = "Nothing was renamed";
    private static final String APP_START_DIR = System.getProperty("user.dir");
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String[] HOME_DIR_ALIASES = { "~", "$HOME" };

    @FXML private VBox inputArea;   // intellij says these boxes are not used,
    @FXML private HBox buttonGroup; // but actually they are in the fxml file
    @FXML private Text statusText;
    @FXML private TextField dirField;
    @FXML private TextField replaceWhatField;
    @FXML private TextField replaceToField;
    @FXML private Button renamerButton;
    @FXML private Button previewButton;
    @FXML private Button dirButton;
    @FXML private GridPane grid;
    @FXML private TextArea resultTextArea;
    @FXML private Button setDir;

    /**
     * Changes current directory to given path in UI directory field.
     */
    private void changeDirectory()
    {
        String newDir = dirField.getText();

        boolean aliasedHomeDir = false;
        for (String s : HOME_DIR_ALIASES) {
            if (s.equals(newDir)) {
                newDir = HOME_DIR;
                aliasedHomeDir = true;
                break;
            }
        }
        if (isValidFolder(newDir)) {
            System.setProperty("user.dir", newDir);
            if (aliasedHomeDir) {
                dirField.setText(toCanonicalPath(System.getProperty("user.dir")));
            }
            statusText.setText(toCanonicalPath(System.getProperty("user.dir")));
        }
    }

    /**
     * Renames files in directory given on dir field of UI, replacing text according to values on replaceWhat and
     * replaceTo fields. Can be run in simulated mode, where it only presents the would-be results but doesn't rename
     * anything.
     *
     * @param simulate
     *         TRUE to skip renaming and only display would-be results, FALSE to run the rename and display results
     */
    private void runRename(boolean simulate)
    {
        String dir = dirField.getText();
        String what = replaceWhatField.getText();
        String to = replaceToField.getText();

        if (isValidFolder(dir)) {
            String filesRenamed = pprint(renameRecursively(dir, what, to, simulate));
            if (filesRenamed.isBlank()) {
                resultTextArea.setText(NOTHING_RENAMED);
            }
            else if (simulate) {
                resultTextArea.setText(filesRenamed + "\nWould rename the files as shown.");
            }
            else {
                resultTextArea.setText(filesRenamed + "\nSuccessfully renamed files!");
            }
        }
        else {
            resultTextArea.setText(NO_SUCH_DIR);
        }
    }

    /**
     * Lists given directory contents on text area
     */
    private void listDirectory()
    {
        String input = dirField.getText();

        for (String s : HOME_DIR_ALIASES) {
            if (input.equals(s)) {
                input = HOME_DIR;
                break;
            }
        }

        String files = fileListing(input);

        if (files.isBlank() && !input.isBlank()) {
            resultTextArea.setText(NO_SUCH_DIR);
        }
        else if (input.isBlank()) {
            resultTextArea.setText(fileListing(System.clearProperty("user.dir")));
        }
        else {
            resultTextArea.setText(files);
        }
    }

    /**
     * First thing that is run when GUI.fxml is loaded. Initializes component actions, attributes and events.
     * In other words, this is where the defaults are set.
     *
     * @param location
     *         URL, either local or remote.
     * @param resources
     *         Locale-specific objects.
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        resultTextArea.setEditable(false);
        resultTextArea.setPromptText("Results appear here.");

        statusText.setText(APP_START_DIR);

        dirField.setPromptText("Requires folder path");
        dirField.setText(APP_START_DIR);
        dirField.requestFocus();

        // shitty hack to work around javafx bug/feature where selected field contents are also selected
        dirField.setFocusTraversable(false); // cant come back to this with tab anymore so find a better way

        // Rename files, display result on result area
        renamerButton.setOnAction(e -> runRename(false));

        // Preview renaming on result area without renaming files
        previewButton.setOnAction(e -> runRename(true));

        // List directory content on result area
        dirButton.setOnAction(e -> listDirectory());

        // changes current directory to whatever is on dirField
        setDir.setOnAction(e -> changeDirectory());

        // hotkeys bound on grid layer
        grid.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F1) {
                listDirectory();
            }
            else if (e.getCode() == KeyCode.F2) {
                runRename(true);
            }
            else if (e.getCode() == KeyCode.F3) {
                runRename(false);
            }
            else if (e.getCode() == KeyCode.F4) {
                changeDirectory();
            }
            else if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });
    }
}
