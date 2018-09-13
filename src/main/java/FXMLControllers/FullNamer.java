package FXMLControllers;

import Singletons.FXMLManager;
import Types.ExperimentManager;
import Types.KeywordManager;
import Types.KeywordType;
import Utilities.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import javax.naming.NameNotFoundException;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FullNamer extends ScreenController implements Initializable, ITypeObserver {
    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField researcherName;

    @FXML
    private JFXTextField trialNumber;

    @FXML
    private JFXTextField outputText;

    @FXML
    JFXButton updateNameButton;

    public JFXTextField getResearcherName() {
        return researcherName;
    }

    public void setResearcherName(JFXTextField researcherName) {
        this.researcherName = researcherName;
    }

    @FXML
    private AutocompleteTextField experimentType;

    @FXML
    private JFXButton backButton;

    @FXML
    private VBox vboxOfKeywords;

    @FXML
    private JFXButton addKeywordButton;

    private Image removeObjectIcon = new Image("Images/closeIcon.png",30,30,true,true); //pass in the image path
    private int numKeywords;
    private List<KeywordAutocompleteTextField> listofkeywords = new ArrayList<>();


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

            experimentDate.valueProperty().addListener((obs, oldDate, newDate) -> {
                outputText.setText(updateName());
            });
            experimentType.textProperty().addListener((obs, oldExperimentType, newExperimentType) -> {
                outputText.setText(updateName());
            });

            trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
                outputText.setText(updateName());
            });

            researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
                outputText.setText(updateName());
            });

            updateName();
        }
    }

    /**
     * Show stairs toggled
     */
    private String updateName() {
        StringBuilder fname = new StringBuilder();
        String experimentTypeText = experimentType.getText();
        String trialNumberText = trialNumber.getText();
        String researcherNameText = researcherName.getText();

        if(experimentDate.getValue() != null)
        {
            fname.append(experimentDate.getValue().toString());
        }

        String experimentShorthand = "";

        if(experimentTypeText != null && !(experimentTypeText.trim().isEmpty()))
        {
            fname.append("_");
            try {
                experimentShorthand = ExperimentManager.getInstance().getExperimentByName("long",experimentType.getText()).getShortName();
                fname.append(experimentShorthand);
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        if(trialNumberText != null && !trialNumberText.trim().isEmpty())
        {
            fname.append("_");
            fname.append(trialNumberText);
        }

        String initial = "";
        if(researcherNameText != null && researcherNameText.length() != 0)
        {
            String name = researcherNameText.toUpperCase();

            String firstletter = name.substring(0,1);
            for (int i=0; i<name.length(); i++){
                char c1 = name.charAt(i);

                if(c1 == ' '){

                    initial = firstletter + (name.charAt(i + 1));
                }
            }

            fname.append("_");
            fname.append(initial);

        }

        for(KeywordAutocompleteTextField autocompleteTextField : listofkeywords)
        {
            System.out.println("foundkeyword");
            if(autocompleteTextField.getText() != null && !autocompleteTextField.getText().trim().isEmpty())
            {
                System.out.println("stringstringstring");
                String keyword;
                fname.append("_");
                try {
                    JFXTextField keywordValue = autocompleteTextField.getKeywordValueField();
                    keyword = KeywordManager.getInstance().getKeywordByName("long",autocompleteTextField.getText()).getShortName();
                    if(autocompleteTextField.getState() == 1 && keywordValue != null && keywordValue.getText() != null)
                    {
                        String affix = KeywordManager.getInstance().getKeywordByName("long",autocompleteTextField.getText()).getAffix();
                        switch (affix){
                            case "prefix":
                                fname.append(keyword);
                                fname.append(keywordValue.getText());
                                break;
                            case "suffix":
                                fname.append(keywordValue.getText());
                                fname.append(keyword);
                                break;
                            case "no value":
                                fname.append(keyword);
                                break;
                            default:
                                fname.append(keyword);
                                break;
                        }
                    }
                } catch (NameNotFoundException e1) {
                    e1.printStackTrace();
                }
                System.out.println(fname.toString());
            }
        }

        System.out.println(fname.toString());
        return fname.toString();
    }

    @FXML
    public void addKeyword(ActionEvent e) throws IOException{
        JFXButton removeObjectButton = new JFXButton("", new ImageView(removeObjectIcon));
        removeObjectButton.setPrefSize(15,15);
        removeObjectButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().add(removeObjectButton);
        KeywordAutocompleteTextField textField = new KeywordAutocompleteTextField(hbox);

        textField.setMinWidth(350);
        textField.setPromptText("Choose keyword");
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.setLabelFloat(true);
        textField.setUnFocusColor(Paint.valueOf("#000000"));
        textField.setFont(new Font("Times New Roman", 20));

        hbox.getChildren().add(textField);
        listofkeywords.add(textField);

        textField.textProperty().addListener((obs, oldTextNumber, newTextNumber) -> {
            outputText.setText(updateName());
        });

        removeObjectButton.setOnAction(e1 -> {
            listofkeywords.remove(textField);
            vboxOfKeywords.getChildren().remove(hbox);
        });

        vboxOfKeywords.getChildren().add(hbox);
        numKeywords++;
        onTypeUpdate();
    }

    @FXML
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String nameToCopy = updateName();

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        outputText.setText(nameToCopy);
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