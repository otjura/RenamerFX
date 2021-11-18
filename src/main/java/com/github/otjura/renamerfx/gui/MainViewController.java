/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.gui;

import com.github.otjura.renamerfx.core.StringTuple;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.otjura.renamerfx.core.Logic.*;

/**
 * Methods and fields for GUI.fxml view. Has to be public or FXML fails to load with javafx.fxml.LoadException
 */
public final class MainViewController implements Initializable {
private static final String APP_START_DIR = System.getProperty("user.dir");
private static final String HOME_DIR = System.getProperty("user.home");
private static final String[] HOME_DIR_ALIASES = {"~", "$HOME", "$home"};

@FXML
private TextField dirField;
@FXML
private TextField replaceWhatField;
@FXML
private TextField replaceToField;
@FXML
private Button renamerButton;
@FXML
private Button previewButton;
@FXML
private Button dirButton;
@FXML
private CheckBox recursionCheckBox;
@FXML
private GridPane grid;
@FXML
private TableView<StringTuple> resultTable;
@FXML
private TableColumn<StringTuple, String> oldNameCol;
@FXML
private TableColumn<StringTuple, String> newNameCol;

/**
 * Renames files in directory given on dir field of UI, replacing text according to values on replaceWhat and replaceTo
 * fields. Can be run in simulated mode, where it only presents the would-be results but doesn't rename anything.
 *
 * @param simulate TRUE to skip renaming and only display would-be results, FALSE to run rename and display results
 */
private void runRename(boolean simulate) {
	String dir = checkForHomeDirAlias(dirField.getText());
	String what = replaceWhatField.getText();
	String to = replaceToField.getText();
	if (isValidFolder(dir)) {
		Path path = Paths.get(dir);
		try {
			List<File> files = collectFiles(path, recursionCheckBox.isSelected());
			List<StringTuple> oldAndNewNames = renameFiles(files, what, to, simulate);
			initResultTable(oldAndNewNames);
		} catch (IOException e) {
			dirField.setText("ERROR READING FILES IN " + dir);
		}
	}
}

/**
 * If input path is any of the aliases for home directory, returns the usable format of it. Otherwise, returns input
 * path untouched.
 *
 * @param path Path to check.
 * @return Path string.
 */
private String checkForHomeDirAlias(String path) {
	for (String s : HOME_DIR_ALIASES) {
		if (path.equals(s)) {
			return HOME_DIR;
		}
	}
	return path;
}

/**
 * Lists given directory contents on the result table.
 */
private void listDirectory() {
	String dir = dirField.getText();
	if (isValidFolder(dir)) {
		Path path = Path.of(checkForHomeDirAlias(dir));
		try {
			List<File> files = collectFiles(path, recursionCheckBox.isSelected());
			List<StringTuple> fileNames = filesAsStringTuples(files);
			initResultTable(fileNames);
		} catch (IOException e) {
			dirField.setText("ERROR LISTING FILES IN " + dir);
		}
	}
}

private void initResultTable(List<StringTuple> values) {
	ObservableList<StringTuple> results = resultTable.getItems();
	if (!results.isEmpty()) {
		results.clear();
	}
	results.addAll(values);
	oldNameCol.setCellValueFactory(new PropertyValueFactory<>("string1"));
	newNameCol.setCellValueFactory(new PropertyValueFactory<>("string2"));
}

/**
 * First thing that is run when GUI.fxml is loaded. Initializes component actions, attributes and events. In other
 * words, this is where the defaults are set.
 *
 * @param location URL, either local or remote.
 * @param resources Locale-specific objects.
 */
public void initialize(URL location, ResourceBundle resources) {
	resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	resultTable.setEditable(false);
	dirField.setPromptText("Requires folder path");
	dirField.setText(APP_START_DIR);
	dirField.requestFocus();
	recursionCheckBox.setSelected(false);
	renamerButton.setOnAction(e -> runRename(false));
	previewButton.setOnAction(e -> runRename(true));
	dirButton.setOnAction(e -> listDirectory());

	// hotkeys bound on grid layer
	grid.setOnKeyPressed(e -> {
		switch (e.getCode()) {
			case F1 -> listDirectory();
			case F2 -> runRename(true);
			case F3 -> runRename(false);
			case ESCAPE -> Platform.exit();
		}
	});
}
}
