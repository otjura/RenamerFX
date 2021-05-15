/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

/**
 * GUI starter class, responsible for loading resources for GUI before diplaying it.
 */
public final class FXMLGuiStarter extends Application
{

    @Override
    public void start(Stage stage) {
        try {
            Scene scene = new Scene(FXMLLoader.load(
					Objects.requireNonNull(getClass().getClassLoader().getResource("GUI.fxml"))));
            scene.getStylesheets().add("style.css");

            stage.setTitle("RenamerFX");
            stage.getIcons().add(new Image("icon.png"));
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Entry point for GUI. See javafx.application.Application lifecycle for what's going on here.
	 *
	 * @param args
	 * 		command-line arguments
	 */
	public static void launcher(String[] args)
	{
		launch(args);
	}

}