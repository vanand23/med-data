package FXMLControllers;

import Types.Experiment;
import Types.ExperimentManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private JFXButton submitButton;

    @FXML
    private JFXButton cancelButton;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        Font.loadFont(
                AddExperimentToDatabase.class.getResource("/Fonts/AlteHaasGroteskBold.ttf").toExternalForm(),18

        );
        Font.loadFont(
                AddExperimentToDatabase.class.getResource("/Fonts/AlteHaasGroteskRegular.ttf").toExternalForm(),
                18
        );

    }

    @FXML
    public void handleSubmitButton(ActionEvent e) throws IOException {
        int experimentListSize = ExperimentManager.getInstance().getNumberOfExperiments();
        Experiment lastExperiment = ExperimentManager.getInstance().getExperiments().get(String.valueOf(experimentListSize));
        if(lastExperiment == null)
        {
            lastExperiment = new Experiment("0","","","");
        }
        String newID = String.valueOf(Integer.valueOf(lastExperiment.getID())+1);
        Experiment newExperiment = new Experiment(
                                                newID,
                                                experimentName.getText(),
                                                experimentAbbrev.getText(),
                                                experimentDescription.getText());
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
