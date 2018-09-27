package FXMLControllers;

import Types.Filename;
import Utilities.Config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static FXMLControllers.FullNamer.setFullNamerSharedFilename;
import static Utilities.Config.setProperty;

public class CompactNamer extends Namer implements Initializable {

    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField trialNumber;

    @FXML JFXTextField sampleNumber;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton closeButton;

    private static Filename sharedFilename;

    public static void setCompactNamerFilename(Filename sharedFilename) {
        CompactNamer.sharedFilename = sharedFilename;
    }

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



        experimentDate.setValue(LocalDate.now());
        trialNumber.setText(String.valueOf(sharedFilename.getTrialNumber()));
        sampleNumber.setText(String.valueOf(sharedFilename.getSampleNumber()));


        trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) ->
        {
            setProperty("trialNumber",newTrialNumber);
            sharedFilename.setTrialNumber(Integer.parseInt(newTrialNumber));
            setFullNamerSharedFilename(sharedFilename);
        });

        sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) ->
        {
            setProperty("sampleNumber",newSampleNumber);
            sharedFilename.setSampleNumber(Integer.parseInt(newSampleNumber));
            setFullNamerSharedFilename(sharedFilename);
        });
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) {
        String nameToCopy = updateName(sharedFilename);
        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
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
        popupScreen("FXML/fullNamer.fxml", switchNamers.getScene().getWindow(),"Full Namer");
    }

    @FXML
    public void closeCompactNamer(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

}
