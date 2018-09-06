package FXMLControllers;

import Types.ExperimentManager;
import Utilities.AutocompleteTextField;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

import javax.naming.NameNotFoundException;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable, ITypeObserver {
    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField trialNumber;

    @FXML
    private JFXTextArea fileName;

    @FXML
    JFXButton generateNameButton;

    @FXML
    private AutocompleteTextField experimentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            ExperimentManager.getInstance().subscribe(this);
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
            experimentDate.setValue(LocalDate.now());
            trialNumber.setText("1");
            experimentType.setAutocompleteWidth(350);
            onTypeUpdate();
        }
    }

    /**
     * Show stairs toggled
     */
    private String generateName() {
        StringBuilder fname = new StringBuilder();
        //int numParams = 3;
        fname.append(experimentDate.getValue().toString());
        fname.append("_");
        String experimentShorthand = "";
        try {
             experimentShorthand = ExperimentManager.getInstance().getExperimentByName("long",experimentType.getText()).getShortName();
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
        fname.append(experimentShorthand);
        fname.append("_");
        fname.append(trialNumber.getText());

        fileName.setText(fname.toString());

        /*
        for(int i = 0; i < numParams - 1; i++)
        {

        }*/
        return fname.toString();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        System.out.println("I pressed a button");
        String myString = generateName();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Override
    public void onTypeUpdate() {
        ArrayList<String> locations = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentType.getEntries().addAll(locations);
    }



}