package FXMLControllers;

import Singletons.Config;
import Types.Filename;
import Types.LogEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ResourceBundle;

import static FXMLControllers.FullNamer.*;

public class CompactNamer extends Namer implements Initializable {

    @FXML
    private DatePicker experimentDate;

    @FXML
    private TextField trialNumber;

    @FXML
    private TextField sampleNumber;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Font.loadFont(CompactNamer.class.getResource("/CSS/AlteHaasGroteskBold.ttf").toExternalForm(),
                19.0);

        trialNumberCheckbox.setSelected(isIsTrialChecked());
        sampleNumberCheckbox.setSelected(isSampleChecked());
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

        experimentDate.setValue(LocalDate.now());
        trialNumNotNeg();
        sampleNumNotNeg();


        trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) ->
        {
            Config.getInstance().setProperty("trialNumber",newTrialNumber);
            sharedFilename.setTrialNumber(Integer.parseInt(newTrialNumber));
            setFullNamerSharedFilename(sharedFilename);
        });

        sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) ->
        {
            Config.getInstance().setProperty("sampleNumber",newSampleNumber);
            sharedFilename.setSampleNumber(Integer.parseInt(newSampleNumber));
            setFullNamerSharedFilename(sharedFilename);
        });

        trialNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            trialNumber.setDisable(!newIsSelected);
            setIsTrialChecked(newIsSelected);
        });

        sampleNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            sampleNumber.setDisable(!newIsSelected);
            setSampleChecked(newIsSelected);
        });
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) {
        String stringexperimentTextField = sharedFilename.getExperiment();
        String stringTrialNumber = String.valueOf(sharedFilename.getTrialNumber());
        String stringSampleNumber = String.valueOf(sharedFilename.getSampleNumber());
        String stringResearcherName = sharedFilename.getResearcher();
        LocalDate stringExperimentDate = sharedFilename.getDate();
        String comment = "";
        String nameToCopy = updateName();

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        logEntryList.add(new LogEntry(
                stringExperimentDate,
                stringResearcherName,
                stringexperimentTextField,
                stringTrialNumber,
                stringSampleNumber,
                nameToCopy,
                listOfKeywords,
                comment
        ));

    }

    @FXML
    public void incrementSampleNumber(ActionEvent e) {
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
        popupScreen("FXML/fullNamer.fxml", switchNamers.getScene().getWindow());
    }

    @FXML
    public void closeCompactNamer(ActionEvent e) {
        closeProgram(closeButton);
    }

    public void sampleNumNotNeg(){
        if(!isSampleChecked()){
            sampleNumber.setDisable(true);
            sampleNumberCheckbox.setSelected(false);
        }
        else{
            sampleNumber.setText(String.valueOf(sharedFilename.getSampleNumber()));
        }
    }

    public void trialNumNotNeg(){
        if(!isIsTrialChecked()){
            trialNumber.setDisable(true);
            trialNumberCheckbox.setSelected(false);
        }
        else{
            trialNumber.setText(String.valueOf(sharedFilename.getTrialNumber()));
        }
    }
}
