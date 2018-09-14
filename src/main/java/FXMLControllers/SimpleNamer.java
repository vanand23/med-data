package FXMLControllers;

import Types.ExperimentManager;
import Types.KeywordManager;
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

public class SimpleNamer extends ScreenController implements Initializable {

    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField trialNumber;

    @FXML JFXTextField sampleNumber;

    @FXML
    private JFXButton copyButton;

    @FXML
    private JFXToggleButton switchNamers;


    @Override

    public void initialize(URL location, ResourceBundle resources){

        String pattern = "dd-MM-yyyy";
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

        trialNumber.setText("0");
       sampleNumber.setText("0");
       experimentDate.setValue(LocalDate.now());
    }

    @FXML
    public void copy(ActionEvent e) throws IOException {
        String myString = "test";
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String nameToCopy = updateName();

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        //int currTrial = Integer.parseInt(trialNumber.getText());
        //currTrial++;
       // trialNumber.setText(String.valueOf(currTrial));
    }

    private String updateName() {
        StringBuilder fname = new StringBuilder();
        String trialNumberText = trialNumber.getText();
        String sampleNumberText = sampleNumber.getText();

        if(experimentDate.getValue() != null)
        {
            fname.append(experimentDate.getValue().toString());
        }

        String experimentShorthand = "";


        if(trialNumberText != null && !trialNumberText.trim().isEmpty())
        {
            fname.append("_");
            fname.append(trialNumberText);
        }

        if(sampleNumberText != null && !trialNumberText.trim().isEmpty())
        {
            fname.append("_");
            fname.append(sampleNumberText);
        }

        System.out.println(fname.toString());
        return fname.toString();
    }

    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/fullNamer.fxml", switchNamers.getScene().getWindow(),"Full Namer");
    }





}


