package FXMLControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static FXMLControllers.ExperimentsTable.setSelectedValue;
import static Utilities.DirectorySearcher.doesFileExist;


public class CreateNewExperimentDatabase {
    @FXML
    private TextField newDatabaseName; //store the new database name
    @FXML
    private JFXButton closeButton; //basic functionality button

    @FXML
    //closes the window and does not save any fields that may have been entered
    public void handleCloseButton(ActionEvent actionEvent) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    //actually generates the new database (csv) file and can be viewed in the dropdown on the Add Experiment to Database page
    @FXML
    public void generateDatabase(ActionEvent actionEvent) throws IOException {
        String newDatabaseNameString = newDatabaseName.getText(); //get the user input
        if(!doesFileExist(newDatabaseNameString, "experiments")){ //check to see if the file exists
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();

            //creates file path and adds the file to the Libraries/experiments directory
            File file = new File(path.substring(0, path.length() - 1) + "/Libraries/experiments/" + newDatabaseNameString + ".csv");
            file.createNewFile(); //file created
        }
        setSelectedValue(newDatabaseNameString);
        handleCloseButton(actionEvent); //closes the window after "save" button is clicked
    }
}
