/**
  MovieController.java
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

import java.io.File;
import java.io.IOException;

import com.poster.grabber.service.PostGrabber;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainController {

	private static final String SELECT_DIR = "Select Directory";
	private static final String NOT_SELECTED = "No Directory selected";
	private static final String DELETE_ACTION = "Delete Thumbnails";
	private static final String UPDATE_ACTION = "Update Thumbnails";
	

	@FXML
	private VBox vBoxMain;
	
	/**
	 * Selected directory
	 */
	@FXML
	private Text selectedDirectory;

	/**
	 * Select directory action
	 * 
	 * @param event
	 */
	@FXML
	protected void selectDirAction(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		Stage stage = (Stage) vBoxMain.getScene().getWindow();
		File select = directoryChooser.showDialog(stage);
		if (select == null) {
			selectedDirectory.setText(NOT_SELECTED);
		} else {
			selectedDirectory.setText(select.getAbsolutePath());
		}
	}

	/**
	 * Update thumbnails
	 * 
	 * @param event
	 */
	@FXML
	protected void updateAction(ActionEvent event) {
		String dir = selectedDirectory.getText();
		if (dir.equalsIgnoreCase(SELECT_DIR) || dir.equalsIgnoreCase(NOT_SELECTED)) {
			alertNoSelection(UPDATE_ACTION);
		} else {
			PostGrabber postGrabber = new PostGrabber(selectedDirectory.getText());
			try {
				postGrabber.update();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Movie Poster Grabber");
				alert.setHeaderText("Update Thumnails");
				alert.setContentText("Completed");
				alert.showAndWait();
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Movie Poster Grabber");
				alert.setHeaderText("Update Thumnails");
				alert.setContentText("Error Occurred");
				alert.showAndWait();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Delete thumbnails
	 * 
	 * @param event
	 */
	@FXML
	protected void deleteAction(ActionEvent event) {
		String dir = selectedDirectory.getText();
		if (dir.equalsIgnoreCase(SELECT_DIR) || dir.equalsIgnoreCase(NOT_SELECTED)) {
			alertNoSelection(DELETE_ACTION);
		} else {
			PostGrabber postGrabber = new PostGrabber(selectedDirectory.getText());
			try {
				postGrabber.delete();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Movie Poster Grabber");
				alert.setHeaderText("Delete Thumnails");
				alert.setContentText("Completed");
				alert.showAndWait();

			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Movie Poster Grabber");
				alert.setHeaderText("Delete Thumnails");
				alert.setContentText("Error Occurred");
				alert.showAndWait();
				e.printStackTrace();
			}

		}
	}
	
	private void alertNoSelection(String action) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Movie Poster Grabber");
		alert.setHeaderText(action);
		alert.setContentText("Select directory first !");
		alert.showAndWait();
	}

}
