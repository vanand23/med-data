package FXMLControllers;

import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.AutocompleteTextField;
import Utilities.ITypeObserver;
import Utilities.Observable;
import Utilities.Observer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import javax.naming.NameNotFoundException;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FullNamer extends ScreenController implements Initializable, ITypeObserver {
    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField trialNumber;

    @FXML
    private JFXTextArea fileName;

    @FXML
    JFXButton updateNameButton;

    @FXML
    private AutocompleteTextField experimentType;

    @FXML
    private JFXButton backButton;

    @FXML
    private VBox listOfKeywords;

    @FXML
    private JFXButton addKeywordButton;

    private int numKeywords;
    private List<AutocompleteTextField> listofkeywords = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            numKeywords = 0;
            ExperimentManager.getInstance().subscribe(this);
            KeywordManager.getInstance().subscribe(this);
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
            trialNumber.setText("0");
            experimentType.setAutocompleteWidth(350);
        }
    }

    /**
     * Show stairs toggled
     */
    private String updateName() {
        StringBuilder fname = new StringBuilder();
        String experimentTypeText = experimentType.getText();
        String trialNumberText = trialNumber.getText();
        //int numParams = 3;
        if(experimentDate.getValue() != null)
        {
            fname.append(experimentDate.getValue().toString());
        }
        String experimentShorthand = "";
        if(experimentTypeText != null && experimentTypeText.length() != 0)
        {
            fname.append("_");
            try {
                experimentShorthand = ExperimentManager.getInstance().getExperimentByName("long",experimentType.getText()).getShortName();
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
            fname.append(experimentShorthand);
        }

        if(trialNumber.getText() != null)
        {
            fname.append("_");
            fname.append(trialNumber.getText());
        }


        fileName.setText(fname.toString());

        for(AutocompleteTextField autocompleteTextField : listofkeywords)
        {

        }
        return fname.toString();
    }

    @FXML
    public void addKeyword(ActionEvent e) throws IOException{
        AutocompleteTextField textField = new AutocompleteTextField();
        textField.setPromptText("Choose Keyword");
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.setLabelFloat(true);
        textField.setUnFocusColor(Paint.valueOf("#0d004d"));
        textField.setFont(new Font("Palatino Linotype", 20));
        listOfKeywords.getChildren().add(textField);
        listofkeywords.add(textField);
        numKeywords++;
        onTypeUpdate();
    }

    @FXML
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{String myString = updateName();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        int currTrial = Integer.parseInt(trialNumber.getText());
        currTrial++;
        trialNumber.setText(String.valueOf(currTrial));
    }

    @Override
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentType.getEntries().addAll(experiments);

        ArrayList<String> keywords = (ArrayList<String>) KeywordManager.getInstance().getAllKeywordLongNames();
        for(AutocompleteTextField autocompleteTextField : listofkeywords){
            autocompleteTextField.getEntries().addAll(keywords);
        }
    }
}