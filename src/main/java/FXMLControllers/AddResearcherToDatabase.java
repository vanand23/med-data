package FXMLControllers;

import Types.Researcher;
import Singletons.ResearcherManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static FXMLControllers.ResearchersTable.getListOfResearchersFromDatabase;

public class AddResearcherToDatabase extends ScreenController implements Initializable {

    //The fields to create a new researcher in the database
    @FXML
    private TextField researcherName; //name

    @FXML
    private TextField researcherAbbrev; //abbreviation

    @FXML
    private ComboBox<String> pickResearcherDatabase; //can choose which database to add the new researcher

    //basic functionality buttons
    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton cancelButton;


    @Override
    //initialize the database with any previously inputted values
    public void initialize(URL location, ResourceBundle resources){
        pickResearcherDatabase.getItems().addAll(ResearcherManager.getInstance().getResearcherFiles());
        pickResearcherDatabase.getSelectionModel().selectFirst();
    }

    @FXML
    //Researcher name and abbreviation are added to the table and Researcher Manager database when the submit button is clicked
    //the database chosen to store the above information is also stored
    public void handleSubmitButton(ActionEvent e) throws IOException {
        Researcher newResearcher = new Researcher( //create a new researcher
                researcherName.getText(),
                researcherAbbrev.getText(),
                pickResearcherDatabase.getValue());
        getListOfResearchersFromDatabase().add(newResearcher); //add this new researcher to the table
        ResearcherManager.getInstance().addResearcher(newResearcher); //add this new researcher to the database

        Stage primaryStage = (Stage) researcherAbbrev.getScene().getWindow();
        primaryStage.close();
        //close the window
    }

    @FXML
    //closes the window and does not save any inputted values
    public void handleCancelButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

}
