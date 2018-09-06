package Launcher;

import Singletons.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import java.io.IOException;
import java.util.Timer;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Database.initDatabase();
        URL url = new File("C:/Users/feyfo/IdeaProjects/med-data/src/main/resources/FXML/autoNamer.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private void parseParameters(String[] params){

    }
    private void parseAdditional(){

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
