/**
  MainFrame.java
 ***********************************************************************************************************************
 Description: 	

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 01-Aug-2017	Gurpreet Singh Saini	Initial Version
 
 ***********************************************************************************************************************
 */
package com.poster.grabber.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFrame extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {

		/**
		 * Scene
		 */
		Parent sceneRoot = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
		Scene scene = new Scene(sceneRoot);
		primaryStage.setTitle("Movie Poster Grabber");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
