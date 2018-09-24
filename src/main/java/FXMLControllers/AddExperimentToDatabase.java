package FXMLControllers;

import Singletons.Database;
import Types.ExperimentManager;
import Types.ExperimentType;
import Types.KeywordManager;
import Types.KeywordType;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import org.apache.derby.client.am.SqlException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static FXMLControllers.ExperimentsTable.getExperimentTypeObservableList;
import static FXMLControllers.KeywordsDBTable.getDBdata;

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
        ExperimentType lastExperiment = ExperimentManager.getInstance().getExperiments().get(String.valueOf(experimentListSize));
        String newID = String.valueOf(Integer.valueOf(lastExperiment.getID())+1);
        getExperimentTypeObservableList().add(new ExperimentType(
                newID,
                experimentName.getText(),
                experimentAbbrev.getText(),
                experimentDescription.getText()));
        try {
            Database.insertExperiment(newID,
                    experimentName.getText(),
                    experimentAbbrev.getText(),
                    experimentDescription.getText());
            Database.writeExperimentsToCSV("Libraries/defaultExperiments.csv");
        }catch (SQLException e1){
            e1.printStackTrace();
            System.err.println("Could not insert experiment");
        }
        Stage primaryStage = (Stage) experimentAbbrev.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    public void handleCancelButton (ActionEvent e) throws IOException {

        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();

    }

}
