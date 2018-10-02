package FXMLControllers;

import Types.Experiment;
import Types.ExperimentManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static FXMLControllers.ExperimentsTable.getExperimentTypeObservableList;

public class AddExperimentToDatabase extends ScreenController implements Initializable {

    @FXML
    private JFXTextField experimentName;

    @FXML
    private JFXTextField experimentAbbrev;

    @FXML
    private JFXTextArea experimentDescription;

    @FXML
    private ComboBox<String> pickExperimentDatabase;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton cancelButton;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        pickExperimentDatabase.getItems().addAll(ExperimentManager.getInstance().getExperimentFiles());
        pickExperimentDatabase.getSelectionModel().selectFirst();
    }

    @FXML
    public void handleSubmitButton(ActionEvent e) throws IOException {
        Experiment newExperiment = new Experiment(
                                                experimentName.getText(),
                                                experimentAbbrev.getText(),
                                                experimentDescription.getText(),
                                                pickExperimentDatabase.getValue());
        getExperimentTypeObservableList().add(newExperiment);
        ExperimentManager.getInstance().addExperiment(newExperiment);

        Stage primaryStage = (Stage) experimentAbbrev.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    public void handleCancelButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

}
