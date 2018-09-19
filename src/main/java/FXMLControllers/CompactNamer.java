package FXMLControllers;

import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.Config;
import Utilities.KeywordAutocompleteTextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.naming.NameNotFoundException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Utilities.Config.setProperty;

public class CompactNamer extends Namer implements Initializable {

    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField trialNumber;

    @FXML JFXTextField sampleNumber;

    @FXML
    private JFXButton copyButton;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton plusSampleButton;

    @FXML
    private JFXButton plusTrialButton;

    @FXML
    private JFXButton minusSampleButton;

    @FXML
    private JFXButton minusTrialButton;

    @FXML
    private JFXButton closeButton;

    private String experimentType;
    private String researcherName;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        String pattern = "dd/MM/yyyy";
        experimentDate.setPromptText(pattern.toLowerCase());
        experimentDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        Config config = new Config();
        String configResearcherName = config.getProperty("researcherName");
        if(configResearcherName != null && !configResearcherName.trim().isEmpty())
        {
            researcherName = configResearcherName;
        }
        String configExperimentType = config.getProperty("experimentType");
        if(configExperimentType != null && !configExperimentType.trim().isEmpty())
        {
            experimentType = configExperimentType;
        }
        String configTrialNumber = config.getProperty("trialNumber");
        if(configTrialNumber != null && !configTrialNumber.trim().isEmpty())
        {
            trialNumber.setText(configTrialNumber);
        }
        String configSampleNumber = config.getProperty("sampleNumber");
        if(configSampleNumber != null && !configSampleNumber.trim().isEmpty())
        {
            sampleNumber.setText(configSampleNumber);
        }

        experimentDate.setValue(LocalDate.now());

        trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
            setProperty("trialNumber",newTrialNumber);
        });
        sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) -> {
            setProperty("sampleNumber",newSampleNumber);
        });
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String nameToCopy = updateName(
                experimentType,
                trialNumber.getText(),
                sampleNumber.getText(),
                researcherName,
                experimentDate.getValue(),
                FullNamer.getSharedListOfKeywords());
        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @FXML
    public void incrementSampleNumber(ActionEvent e) throws IOException{
        int currSample = Integer.parseInt(sampleNumber.getText());
        currSample++;
        sampleNumber.setText(String.valueOf(currSample));
    }

    @FXML
    public void incrementTrialNumber(ActionEvent e) throws IOException{
        int currTrial = Integer.parseInt(trialNumber.getText());
        currTrial++;
        trialNumber.setText(String.valueOf(currTrial));
    }

    @FXML
    public void decrementSampleNumber(ActionEvent e) throws IOException{
        int currSample = Integer.parseInt(sampleNumber.getText());
        if (currSample >= 1)
        {
            currSample--;
            sampleNumber.setText(String.valueOf(currSample));
        }
    }

    @FXML
    public void decrementTrialNumber(ActionEvent e) throws IOException{
        int currTrial = Integer.parseInt(trialNumber.getText());
        if (currTrial >= 1)
        {
            currTrial--;
            trialNumber.setText(String.valueOf(currTrial));
        }
    }

    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/fullNamer.fxml", switchNamers.getScene().getWindow(),"Full Namer");
    }

    @FXML
    public void closeCompactNamer(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

}
