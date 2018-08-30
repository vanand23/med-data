package Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/FXML/namingProgram.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    /**
     * Load a CSV file into the database
     * @param type either "edge" or "node", dictates whether to load edges or nodes from the file
     * @param filename the name of the file to read from (Currently one in the resource folder
     */
    public static void loadFromCSV(String type, String filename){
        List<String> fullName = new LinkedList<>();
        List<String> abbreviation = new LinkedList<>();


        String line = ""; // stores each line of the file
        String splitter = ","; // string to use to split each line into columns

        // Open the csv file
        try{
            // open the file from the resources folder
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            // use this line instead to load from the project directory instead
            //BufferedReader reader = new BufferedReader(new FileReader(filename)));

            // Skip the first line (the column titles)
            reader.readLine();

            // Go through each line of the file
            while((line = reader.readLine()) != null){
                // split the line by commas into its items
                String[] items = line.split(splitter);

            }
        }
        catch(IOException e){
            System.err.println("Error reading " + filename);
            e.printStackTrace();
        }
        catch(NumberFormatException e){
            System.out.println("Error when trying to convert a string into an integer.");
            e.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Unknown error.");
            e.printStackTrace();
        }
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
