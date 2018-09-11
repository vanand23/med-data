package Launcher;

import Singletons.Database;
import Singletons.FXMLManager;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Color;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import java.io.IOException;
import java.util.Timer;

public class Launcher extends Application {

    //define your offsets here
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        double width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        double height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

        Database.initDatabase();
        FXMLManager fxmlManager = FXMLManager.getInstance();

        //fxmlManager.setSearchDirectory(System.getProperty("user.dir") + "/src/main/resources/");
        fxmlManager.loadFXML("FXML/mainMenu.fxml");

        Parent root = fxmlManager.getFXMLNode("FXML/mainMenu.fxml");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        primaryStage.setX(width - 400);
        primaryStage.setY(height - 700 - 50);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.toFront();
    }

    public void renameFile(){
        //create source File object
        File oldName = new File("C://FileIO//source.txt");

        //create destination File object
        File newName = new File("C://FileIO//destination.txt");

        /*
         * To rename a file or directory, use
         * boolean renameTo(File destination) method of Java File class.
         *
         * This method returns true if the file was renamed successfully, false
         * otherwise.
         */

        boolean isFileRenamed = oldName.renameTo(newName);

        if(isFileRenamed)
            System.out.println("File has been renamed");
        else
            System.out.println("Error renaming the file");
        //regex out \/:*?"<>|
        //modes: numeric, alphanumeric, nothing,
    }


    public static void main(String[] args) {
        launch(args);
    }
}
