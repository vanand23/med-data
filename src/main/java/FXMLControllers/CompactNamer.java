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

//Compact namer is a smaller version of full namer for users who want to name numerous amounts of files in their file directory
//Only the date, trial number, and sample number cna be changed
//Other parameters in full namer will stay persistent in compact namer and will display in the final file name output
public class CompactNamer extends Namer implements Initializable {

    //fields the users can input in compact namer
    @FXML
    private DatePicker experimentDate;

    @FXML
    private TextField trialNumber;

    @FXML
    private TextField sampleNumber;

    //transition between compact name and full namer and vice versa
    @FXML
    private JFXToggleButton switchNamers;

    //basic in-app functionality
    @FXML
    private JFXButton closeButton;

    @Override
    //initializing the parameters in compact namer
    public void initialize(URL location, ResourceBundle resources){
        Font.loadFont(CompactNamer.class.getResource("/CSS/AlteHaasGroteskBold.ttf").toExternalForm(),
                19.0);

        trialNumberCheckbox.setSelected(isTrialChecked());
        sampleNumberCheckbox.setSelected(isSampleChecked());

        //formatting the date as per European standards
        String pattern = "dd/MM/yyyy";
        experimentDate.setPromptText(pattern.toLowerCase());
        experimentDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            //parse the date into a string in order to format it in European standards
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            //parse the string back into the date and automatically set it as today's date
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

        //add listeners to update the final file name output
        trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) ->
        {
            Config.getInstance().setProperty("trialNumber",newTrialNumber);
            sharedFilename.setTrialNumber(Integer.parseInt(newTrialNumber));
        });

        sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) ->
        {
            Config.getInstance().setProperty("sampleNumber",newSampleNumber);
            sharedFilename.setSampleNumber(Integer.parseInt(newSampleNumber));
        });

        trialNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            trialNumber.setDisable(!newIsSelected);
            setTrialChecked(newIsSelected);
        });

        sampleNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            sampleNumber.setDisable(!newIsSelected);
            setSampleChecked(newIsSelected);
        });
    }

    @FXML
    //Adds the ability for the user to copy the final file output name and paste into the file directory
    public void copyFileToClipboard(ActionEvent e) {
        //Get the inputted parameters from the Filename Class
        String stringexperimentTextField = sharedFilename.getExperiment();
        String stringTrialNumber = String.valueOf(sharedFilename.getTrialNumber());
        String stringSampleNumber = String.valueOf(sharedFilename.getSampleNumber());
        String stringResearcherName = sharedFilename.getResearcher();
        LocalDate stringExperimentDate = sharedFilename.getDate();
        String comment = "";
        String nameToCopy = updateFilename();

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        //after the file name has been copied, add all the parameters into the logger table to keep track of the filenames whenever the user presses "copy"
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

    //The following functions are to increment/decrement the sample and trial number on the click of the "+" and "-" buttons next to the field
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

    //Switch between full namer and compact namer on the click of a button
    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/fullNamer.fxml", switchNamers.getScene().getWindow());
    }

    //closes the compact namer window
    @FXML
    public void closeCompactNamer(ActionEvent e) {
        closeProgram(closeButton);
    }

    private void sampleNumNotNeg(){
        if(!isSampleChecked()){ //if sample number has not been entered
            sampleNumber.setDisable(true);
            sampleNumberCheckbox.setSelected(false); //user will not be able to check the box to include sample number in the file name
        }
        else{
            sampleNumber.setText(String.valueOf(sharedFilename.getSampleNumber()));
        }
    }

    private void trialNumNotNeg(){
        if(!isTrialChecked()){ //if trial number has not been entered
            trialNumber.setDisable(true);
            trialNumberCheckbox.setSelected(false); //user will not be able to check the box to include trial number in the file name
        }
        else{
            trialNumber.setText(String.valueOf(sharedFilename.getTrialNumber()));
        }
    }
}
