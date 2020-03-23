package FXMLControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static FXMLControllers.KeywordsTable.setSelectedValue;
import static Utilities.DirectorySearcher.doesFileExist;

public class CreateNewResearcherDatabase {
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

    //actually generates the new database (csv) file and can be viewed in the dropdown on the Add Researchers to Database page
    @FXML
    public void generateDatabase(ActionEvent actionEvent) throws IOException {
        String newDatabaseNameString = newDatabaseName.getText(); //get user input
        if(!doesFileExist(newDatabaseNameString, "researchers")){ //check to see if the file exists
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();

            //creates file path and adds the file to the Libraries/researchers directory
            File file = new File(path.substring(0, path.length() - 1) + "/Libraries/researchers/" + newDatabaseNameString + ".csv");
            file.createNewFile(); //file created
        }
        setSelectedValue(newDatabaseNameString);
        handleCloseButton(actionEvent); //closes the window after "save" button is clicked
    }
}
