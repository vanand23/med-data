package FXMLControllers;

import Launcher.Launcher;
import Singletons.FXMLManager;
import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.AutocompleteTextField;
import Utilities.Config;
import Utilities.ITypeObserver;
import Utilities.KeywordAutocompleteTextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Utilities.Config.setProperty;

public class FullNamer extends Namer implements Initializable, ITypeObserver {
    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField researcherName;

    @FXML
    private JFXTextField trialNumber;


    @FXML
    private JFXTextField sampleNumber;

    @FXML
    private JFXTextField outputText;

    @FXML
    JFXButton updateNameButton;

    @FXML
    private AutocompleteTextField experimentType;

    @FXML
    private JFXButton backButton;

    @FXML
    private VBox vboxOfKeywords;

    @FXML
    private JFXButton addKeywordButton;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton projectPreferencesButton;

    @FXML
    private JFXButton closeButton;


    private Image removeObjectIcon = new Image("Images/closeIcon.png",30,30,true,true); //pass in the image path

    private static ArrayList<KeywordAutocompleteTextField> sharedListOfKeywords = new ArrayList<>();

    private ArrayList<String> keywords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {

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
            Config config = new Config();
            String configResearcherName = config.getProperty("researcherName");
            if(configResearcherName != null && !configResearcherName.trim().isEmpty())
            {
                researcherName.setText(configResearcherName);
            }
            String configExperimentType = config.getProperty("experimentType");
            if(configExperimentType != null && !configExperimentType.trim().isEmpty())
            {
                experimentType.setText(configExperimentType);
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

            experimentDate.valueProperty().addListener((obs, oldDate, newDate) -> {
                        outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
            });
            experimentType.textProperty().addListener((obs, oldExperimentType, newExperimentType) -> {
                if(experimentType.isValidText())
                {
                    outputText.setText(updateName(
                            experimentType.getText(),
                            trialNumber.getText(),
                            sampleNumber.getText(),
                            researcherName.getText(),
                            experimentDate.getValue(),
                            sharedListOfKeywords));
                    setProperty("experimentType",newExperimentType);
                    System.out.println("updated!");
                }
            });
            researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                setProperty("researcherName",newResearcherName);
            });
            trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                setProperty("trialNumber",newTrialNumber);
            });
            sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) -> {
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                setProperty("sampleNumber",newSampleNumber);
            });
            outputText.setText(updateName(
                    experimentType.getText(),
                    trialNumber.getText(),
                    sampleNumber.getText(),
                    researcherName.getText(),
                    experimentDate.getValue(),
                    sharedListOfKeywords));
        }
    }

    @FXML
    public void addKeyword(ActionEvent e) throws IOException{
        FXMLManager fxmlManager = FXMLManager.getInstance();
        //fxmlManager.setSearchDirectory(System.getProperty("user.dir") + "/src/main/resources/");

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
        sharedListOfKeywords.add(textField);

        removeObjectButton.setOnAction(e1 -> {
            sharedListOfKeywords.remove(textField);
            vboxOfKeywords.getChildren().remove(hbox);
        });

        vboxOfKeywords.getChildren().add(hbox);
        onTypeUpdate();
    }

    @FXML
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String nameToCopy = updateName(
                experimentType.getText(),
                trialNumber.getText(),
                sampleNumber.getText(),
                researcherName.getText(),
                experimentDate.getValue(),
                sharedListOfKeywords);
        setProperty("experimentType",experimentType.getText());

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        outputText.setText(nameToCopy);
        int currTrial = Integer.parseInt(trialNumber.getText());
        currTrial++;
        trialNumber.setText(String.valueOf(currTrial));
    }

    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/simpleNamer.fxml", switchNamers.getScene().getWindow(),"Compact Namer");
    }

    @FXML
    public void handlePreferences(ActionEvent e) throws IOException {
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/myProjectPreferences.fxml", projectPreferencesButton.getScene().getWindow(),
                        "Project Preferences");
    }


    @Override
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentType.getEntries().addAll(experiments);

        ArrayList<String> keywords = (ArrayList<String>) KeywordManager.getInstance().getAllKeywordLongNames();
        for(AutocompleteTextField autocompleteTextField : sharedListOfKeywords){
            autocompleteTextField.getEntries().addAll(keywords);
        }
    }
    public static ArrayList<KeywordAutocompleteTextField> getSharedListOfKeywords() {
        return sharedListOfKeywords;
    }

    @FXML
    public void closeFullNamer(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
