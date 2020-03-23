package FXMLControllers;

import Types.Experiment;
import Singletons.ExperimentManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static FXMLControllers.ExperimentsTable.getExperimentTypeObservableList;

public class AddExperimentToDatabase extends ScreenController implements Initializable {

    //parameters the user can input on the window
    @FXML
    private TextField experimentName; //name

    @FXML
    private TextField experimentAbbrev; //abbreviation

    @FXML
    private JFXTextArea experimentDescription; //description of the experiment

    @FXML
    private ComboBox<String> pickExperimentDatabase; //pick from the list of databases to store the experiment

    //basic functionality buttons
    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton cancelButton;


    @Override
    //initialize the database with any previously inputted values
    public void initialize(URL location, ResourceBundle resources){
        pickExperimentDatabase.getItems().addAll(ExperimentManager.getInstance().getExperimentFiles());
        pickExperimentDatabase.getSelectionModel().selectFirst();
    }

    @FXML
    //Experiment name, abbreviation, and description are added to the table and Experiment Manager database when the submit button is clicked
    //the database chosen to store the above information is also stored
    public void handleSubmitButton(ActionEvent e) throws IOException {
        Experiment newExperiment = new Experiment( //create new experiment with user inputs
                                                experimentName.getText(),
                                                experimentAbbrev.getText(),
                                                experimentDescription.getText(),
                                                pickExperimentDatabase.getValue());
        getExperimentTypeObservableList().add(newExperiment); //add this new experiment to the table
        ExperimentManager.getInstance().addExperiment(newExperiment); //add the newly created experiment to the Experiment Manager database

        Stage primaryStage = (Stage) experimentAbbrev.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    //closes the window and does not save any inputted values
    public void handleCancelButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

}
