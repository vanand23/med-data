package Launcher;

import Singletons.Database;
import Singletons.FXMLManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;


import java.io.*;

public class Launcher extends Application {

    //define your offsets here
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        double width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        double height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        /*Font.loadFont(
                Launcher.class.getResource("/Fonts/ArialBlack.ttf").toExternalForm(),
                10
        );*/


        Database.initDatabase();
        FXMLManager fxmlManager = FXMLManager.getInstance();

        //fxmlManager.setSearchDirectory(System.getProperty("user.dir") + "/src/main/resources/");
        fxmlManager.loadFXML("/FXML/fullNamer.fxml");

        Parent root = fxmlManager.getFXMLNode("/FXML/fullNamer.fxml");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        //primaryStage.setX(width - 400);
        //primaryStage.setY(height - 700 - 50);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.toFront();

        /*Parent aroot = FXMLLoader.load(getClass().getResource("FXML/fullNamer.fxml"));
        Scene scene = new Scene(aroot);
        primaryStage.setScene(scene);
        primaryStage.show();*/


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
