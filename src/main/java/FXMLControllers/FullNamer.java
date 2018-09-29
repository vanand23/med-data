package FXMLControllers;

import Types.*;
import Utilities.AutocompleteTextField;
import Utilities.Config;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.TreeTableColumn;

import static Animation.PaneTransitions.partialFadeIn;
import static Animation.PaneTransitions.partialFadeOut;
import static Animation.PaneTransitions.slidingTransition;

import static FXMLControllers.CompactNamer.setCompactNamerFilename;
import static Utilities.Config.setProperty;

public class FullNamer extends Namer implements Initializable, ITypeObserver {
    @FXML
    public AnchorPane projectPreferencesPane;

    @FXML
    public AnchorPane loggerPane;

    @FXML
    public AnchorPane experimentsPane;

    @FXML
    public AnchorPane keywordsPane;

    @FXML
    public AnchorPane researchersPane;

    @FXML
    public AnchorPane menu;

    @FXML
    public AnchorPane shadingOverlay;

    @FXML
    public ImageView menuButtonIcon;

    @FXML
    public JFXButton menuButton;

    @FXML
    public CheckBox sampleNumberCheckbox;
    @FXML
    public CheckBox trialNumberCheckbox;
    @FXML
    public CheckBox researcherCheckbox;
    @FXML
    public CheckBox experimentCheckbox;
    @FXML
    public CheckBox dateCheckbox;

    @FXML
    private Label projectName;

    @FXML
    private VBox mainVBox;

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
    private AutocompleteTextField experimentTextField;

    @FXML
    private JFXButton backButton;

    @FXML
    private VBox vboxOfKeywords;

    @FXML
    private JFXButton addKeywordButton;

    @FXML
    private JFXButton keywordsToDBButton;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton projectPreferencesButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private AnchorPane anchorPaneOfKeywords;

    @FXML
    private static TreeTableView tableOfKeywords;

    @FXML
    static TreeTableColumn<Keyword, String> dataValueColumn;

    @FXML
    static TreeTableColumn<Keyword, String> nameColumn;

    @FXML
    private JFXButton helpButtonInput;

    @FXML
    private JFXButton helpButtonOutput;

    @FXML
    private JFXButton loggerButton;

    @FXML
    private TableView<Keyword> keywordsTable;

    @FXML
    private TableColumn columnName;

    @FXML
    private TableColumn columnDataValue;

    @FXML
    private JFXButton clearButton;

    private Image removeObjectIcon = new Image("Images/closeIcon.png",30,30,true,true); //pass in the image path
    
    private final static ObservableList<Keyword> data = FXCollections.observableArrayList();

    private static ObservableList<LogEntry> logEntryArrayList = FXCollections.observableArrayList();

    private static ArrayList<AnchorPane> drawerList = new ArrayList<>();

    private static Filename sharedFilename;
    private static Image menuIcon = new Image("Images/hamburgerIcon.png");
    private static Image backIcon = new Image("Images/leftIcon.png");
    private boolean isMenuOpen = false;
    private boolean isMenuPlaying = false;

    static void setFullNamerSharedFilename(Filename sharedFilename) {
        FullNamer.sharedFilename = sharedFilename;
    }

    static ObservableList<LogEntry> getLogEntryArrayList() {
        return logEntryArrayList;
    }

    static ObservableList<Keyword> getData() {
        return data;
    }

    private boolean isRememberData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            drawerList.add(menu);
            drawerList.add(projectPreferencesPane);
            drawerList.add(loggerPane);
            drawerList.add(experimentsPane);
            drawerList.add(keywordsPane);
            drawerList.add(researchersPane);

            dateCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
                experimentDate.setDisable(!newIsSelected);
                if(!newIsSelected)
                {
                    sharedFilename.setDate(LocalDate.MIN);
                }else{
                    sharedFilename.setDate(experimentDate.getValue());
                }
                outputText.setText(updateName(sharedFilename));
            });

            experimentCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
                experimentTextField.setDisable(!newIsSelected);
                if(!newIsSelected)
                {
                    sharedFilename.setExperiment("");
                }else{
                    sharedFilename.setExperiment(experimentTextField.getText());
                }
                outputText.setText(updateName(sharedFilename));
            });

            researcherCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
                researcherName.setDisable(!newIsSelected);
                if(!newIsSelected)
                {
                    sharedFilename.setResearcher("");
                }else{
                    sharedFilename.setResearcher(researcherName.getText());
                }
                outputText.setText(updateName(sharedFilename));
            });

            trialNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
                trialNumber.setDisable(!newIsSelected);
                if(!newIsSelected)
                {
                    sharedFilename.setTrialNumber(-1);
                }else{
                    sharedFilename.setTrialNumber(Integer.parseInt(trialNumber.getText()));
                }
                outputText.setText(updateName(sharedFilename));
            });

            sampleNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
                sampleNumber.setDisable(!newIsSelected);
                if(!newIsSelected)
                {
                    sharedFilename.setSampleNumber(-1);
                }else{
                    sharedFilename.setSampleNumber(Integer.parseInt(sampleNumber.getText()));
                }
                outputText.setText(updateName(sharedFilename));
            });


            shadingOverlay.setMouseTransparent(true);

            final JFXButton inputHelp = helpButtonInput;
            final Tooltip inputTooltip = new Tooltip();
            inputTooltip.setText("Fill in the fields with your desired parameters");
            inputHelp.setTooltip(inputTooltip);

            final JFXButton outputHelp = helpButtonOutput;
            final Tooltip outputTooltip = new Tooltip();
            outputTooltip.setText("The output format is: YYYY_MM_DD_ExperimentAbbreviation_ResearchInitials_TrialNumber_SampleNumber_KeywordAbbreviations");
            outputHelp.setTooltip(outputTooltip);

            ExperimentManager.getInstance().subscribe(this);
            KeywordManager.getInstance().subscribe(this);
            outputText.setEditable(false);
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
            experimentTextField.setMinWidth(Region.USE_PREF_SIZE);
            Config config = new Config();
            isRememberData = Boolean.valueOf(config.getProperty("rememberData"));
            if(sharedFilename == null)
            {
                sharedFilename = new Filename();
                String configResearcherName = config.getProperty("researcherName");
                if(configResearcherName != null && !configResearcherName.trim().isEmpty())
                {
                    researcherName.setText(configResearcherName);
                    sharedFilename.setResearcher(configResearcherName);
                }
                String configexperimentTextField = config.getProperty("experimentTextField");
                if(configexperimentTextField != null && !configexperimentTextField.trim().isEmpty())
                {
                    experimentTextField.setText(configexperimentTextField);
                    sharedFilename.setExperiment(configexperimentTextField);
                }
                String configTrialNumber = config.getProperty("trialNumber");
                if(configTrialNumber != null && !configTrialNumber.trim().isEmpty())
                {
                    trialNumber.setText(configTrialNumber);
                    sharedFilename.setTrialNumber(Integer.parseInt(configTrialNumber));
                }else {
                    trialNumber.setText("0");
                    sharedFilename.setTrialNumber(0);
                }
                String configSampleNumber = config.getProperty("sampleNumber");
                if(configSampleNumber != null && !configSampleNumber.trim().isEmpty())
                {
                    sampleNumber.setText(configSampleNumber);
                    sharedFilename.setSampleNumber(Integer.parseInt(configSampleNumber));
                }else {
                    sampleNumber.setText("0");
                    sharedFilename.setSampleNumber(0);
                }
                data.clear();
                String configListOfKeywords = config.getProperty("listOfKeywords");
                if(configListOfKeywords != null && !configListOfKeywords.trim().isEmpty())
                {
                    String[] keywords = configListOfKeywords.split(",");
                    for(int i = 0; i < keywords.length; i += 2)
                    {
                        data.add(new Keyword("",keywords[i],"","","",keywords[i+1],""));
                    }
                }
                sharedFilename.setKeywords(data);
                setCompactNamerFilename(sharedFilename);

                String configProjectName = config.getProperty("projectName");
                if(configProjectName != null && !configProjectName.trim().isEmpty())
                {
                    projectName.setText("Project: " + configProjectName);
                    projectName.setFont(new Font(18));
                }else{
                    projectName.setText("");
                    projectName.setFont(new Font(18));
                }
            }


            experimentDate.setValue(LocalDate.now());
            experimentTextField.setAutocompleteWidth(350);
            columnName.setMinWidth(100);
            columnDataValue.setMinWidth(100);

            columnName.setCellValueFactory(new PropertyValueFactory<Keyword, String>("longName"));
            columnDataValue.setCellValueFactory(new PropertyValueFactory<Keyword, String>("dataValue"));

            keywordsTable.setEditable(true);

            columnName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnName.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent event) {
                            ((Keyword) event.getTableView().getItems().get(
                                    event.getTablePosition().getRow())
                            ).setLongName((String) event.getNewValue());
                        }
                    }
            );

            columnDataValue.setCellFactory(TextFieldTableCell.forTableColumn());
            columnDataValue.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent event) {
                            ((Keyword) event.getTableView().getItems().get(
                                    event.getTablePosition().getRow())
                            ).setDataValue((String) event.getNewValue());
                        }
                    }
            );

            keywordsTable.setItems(data);
            System.out.println(data);

            experimentDate.valueProperty().addListener((obs, oldDate, newDate) -> {
                        outputText.setText(updateName(sharedFilename));
            });
            experimentTextField.textProperty().addListener((obs, oldexperimentTextField, newexperimentTextField) -> {
                if(experimentTextField.isValidText())
                {outputText.setText(updateName(sharedFilename));
                setCompactNamerFilename(sharedFilename);
                experimentTextField.setValidText(false);
                if(isRememberData){
                    setProperty("experimentType",newexperimentTextField);
                }
                else{
                    setProperty("experimentType", "");
                }
                }else if(experimentTextField.isTriggerPopup())
                {
                    try {
                        experimentTextField.setTriggerPopup(false);
                        popupScreen("FXML/addExperimentToDatabase.fxml", switchNamers.getScene().getWindow(),"Experiment Type");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
               outputText.setText(updateName(sharedFilename));
               sharedFilename.setResearcher(newResearcherName);
               setCompactNamerFilename(sharedFilename);
               if(isRememberData) {
                   setProperty("researcherName", newResearcherName);
               }
               else{
                   setProperty("researcherName", "");
               }
            });
            trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
                sharedFilename.setTrialNumber(Integer.valueOf(newTrialNumber));
                outputText.setText(updateName(sharedFilename));
                setCompactNamerFilename(sharedFilename);
                if(isRememberData) {
                    setProperty("trialNumber", newTrialNumber);
                }
                else{
                    setProperty("trialNumber", "");
                }
            });
            sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) -> {
                sharedFilename.setSampleNumber(Integer.valueOf(newSampleNumber));
                outputText.setText(updateName(sharedFilename));
                setCompactNamerFilename(sharedFilename);
                if(isRememberData) {
                    setProperty("sampleNumber", newSampleNumber);
                }
                else{
                    setProperty("sampleNumber", "");
                }
            });
            data.addListener((ListChangeListener<Keyword>) keywords -> {
                sharedFilename.setKeywords(data);
                outputText.setText(updateName(sharedFilename));
                setCompactNamerFilename(sharedFilename);
            });
            outputText.setText(updateName(sharedFilename));
        }
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
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String stringexperimentTextField = experimentTextField.getText();
        String stringTrialNumber = trialNumber.getText();
        String stringSampleNumber = sampleNumber.getText();
        String stringResearcherName = researcherName.getText();
        LocalDate stringExperimentDate = experimentDate.getValue();
        String comment = "";
        String nameToCopy = updateName(sharedFilename);

        if(isRememberData) {
            setProperty("experimentTextField",experimentTextField.getText());
        }
        else{
            setProperty("experimentType", "");
        }

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        outputText.setText(nameToCopy);
        logEntryArrayList.add(new LogEntry(
                stringExperimentDate,
                stringResearcherName,
                stringexperimentTextField,
                stringTrialNumber,
                stringSampleNumber,
                nameToCopy,
                data,
                comment
                ));
    }

    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/compactNamer.fxml", switchNamers.getScene().getWindow(),"Compact Namer");
    }

    @FXML
    public void handlePreferences(ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        Stage popup = popupScreen("FXML/projectPreferences.fxml", projectPreferencesButton.getScene().getWindow(),
                        "Project Preferences");
    }

    @FXML
    public void handleLogger(ActionEvent e) throws IOException{
        Stage popup = popupScreen("FXML/loggerMenu.fxml", loggerButton.getScene().getWindow(), "Logger");
    }

    @FXML
    public void handleAddButton (ActionEvent e) throws IOException {
        popupScreen("FXML/addKeywordsUI.fxml", addKeywordButton.getScene().getWindow(),"Add Keyword Menu");
    }

    @FXML
    public void handleDeleteButton (ActionEvent e) throws IOException {

        Keyword selectedItem = keywordsTable.getSelectionModel().getSelectedItem();
        keywordsTable.getItems().remove(selectedItem);
        StringBuilder listOfKeywords = new StringBuilder();
        for(Keyword keyword : data){
            listOfKeywords.append(",");
            listOfKeywords.append(keyword.getLongName());
            listOfKeywords.append(",");
            listOfKeywords.append(keyword.getDataValue());
        }
        if(listOfKeywords.length() != 0)
        {
            listOfKeywords.deleteCharAt(0);
        }
        if(isRememberData) {
            setProperty("listOfKeywords", listOfKeywords.toString());
        }
        else{
            setProperty("listOfKeywords","");
        }
    }


    @FXML
    private void menuPressed () {
        if(!isMenuPlaying) {

            ParallelTransition p = new ParallelTransition();
            if (!isMenuOpen) {
                menuButtonIcon.setImage(backIcon);
                menuButtonIcon.setFitWidth(35);
                menuButtonIcon.setFitWidth(35);
                shadingOverlay.setMouseTransparent(false);
                p.getChildren().add(slideDrawers(1));
                p.getChildren().add(partialFadeIn(shadingOverlay));
                isMenuOpen = true;
            } else {
                menuButtonIcon.setImage(menuIcon);
                menuButtonIcon.setFitWidth(40);
                menuButtonIcon.setFitWidth(40);
                shadingOverlay.setMouseTransparent(true);
                p.getChildren().add(slideDrawers(-1));
                p.getChildren().add(partialFadeOut(shadingOverlay));
                isMenuOpen = false;
            }
            isMenuPlaying = true;
            p.play();
        }
    }

    /**
     * Slides the arrayList of drawers in a direction
     *
     * @param direction the direction the drawer is sliding
     */
    private ParallelTransition slideDrawers(int direction) {
        ParallelTransition p = new ParallelTransition();
        PauseTransition pause = new PauseTransition();
        int duration = 60;
        for (AnchorPane drawer : drawerList) {
            if ((drawer.getTranslateX() == -200 && direction == 1)
                    || (drawer.getTranslateX() >= 0 && direction == -1)) {
                SequentialTransition pauseAndPlay = new SequentialTransition();
                pause.setDuration(Duration.millis(duration));
                duration += 60;
                pauseAndPlay.getChildren().add(new PauseTransition(Duration.millis(duration)));
                pauseAndPlay.getChildren().add(slidingTransition(drawer, direction, 200.0));
                p.getChildren().add(pauseAndPlay);
            }
        }
        p.setOnFinished(event -> {
            isMenuPlaying = false;
        });
        return p;
    }


    @Override
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentTextField.getEntries().addAll(experiments);
    }

    static ObservableList<Keyword> getdata() {
        return data;
    }

    @FXML
    public void closeFullNamer(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    public static TreeTableView getTableOfKeywords() {
        return tableOfKeywords;
    }

    @FXML
    public void handleExperiments(ActionEvent actionEvent)throws IOException{
        popupScreen("FXML/experimentsTable.fxml", addKeywordButton.getScene().getWindow(),"Add Experiments to DB");
    }

    @FXML
    public void handleKeywords(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/KeywordsDBTable.fxml", addKeywordButton.getScene().getWindow(),"Add Keywords to DB");
    }
    public void clearFields(ActionEvent e) {
        researcherName.setText("");
        sampleNumber.setText("0");
        trialNumber.setText("0");
        experimentTextField.setText("");
        data.clear();
    }

    @FXML
    public void handleResearchers(ActionEvent actionEvent) {
    }
}
