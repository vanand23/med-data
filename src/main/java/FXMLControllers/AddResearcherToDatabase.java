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

    @FXML
    private TextField researcherName;

    @FXML
    private TextField researcherAbbrev;

    @FXML
    private ComboBox<String> pickResearcherDatabase;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton cancelButton;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        pickResearcherDatabase.getItems().addAll(ResearcherManager.getInstance().getResearcherFiles());
        pickResearcherDatabase.getSelectionModel().selectFirst();
    }

    @FXML
    public void handleSubmitButton(ActionEvent e) throws IOException {
        Researcher newResearcher = new Researcher(
                researcherName.getText(),
                researcherAbbrev.getText(),
                pickResearcherDatabase.getValue());
        getListOfResearchersFromDatabase().add(newResearcher);
        ResearcherManager.getInstance().addResearcher(newResearcher);

        Stage primaryStage = (Stage) researcherAbbrev.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    public void handleCancelButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

}
