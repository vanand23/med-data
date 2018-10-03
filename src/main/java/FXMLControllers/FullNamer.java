package FXMLControllers;

import Types.*;
import Utilities.AutocompleteTextField;
import Utilities.Config;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
    public AnchorPane gettingStartedPane;

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
    public JFXCheckBox sampleNumberCheckbox;
    @FXML
    public JFXCheckBox trialNumberCheckbox;
    @FXML
    public JFXCheckBox researcherCheckbox;
    @FXML
    public JFXCheckBox experimentCheckbox;
    @FXML
    public JFXCheckBox dateCheckbox;

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

    @FXML
    private JFXButton gettingStartedButton;

    //list of keywords and its parameters that the user inputs in full namer
    private final static ObservableList<Keyword> data = FXCollections.observableArrayList();

    //list of file names in the timeline when the user clicked on the copy button, and additional parameters such as time and descriptions
    private static ObservableList<LogEntry> logEntryArrayList = FXCollections.observableArrayList();

    //list of the menu options to access windows such as project preferences and the keyword, experiments, and researcher databases
    private static ArrayList<AnchorPane> drawerList = new ArrayList<>();

    //adding the menu icon
    private static Filename sharedFilename;
    private static Image menuIcon = new Image("Images/hamburgerIcon.png");
    private static Image backIcon = new Image("Images/leftIcon.png");
    private boolean isMenuOpen = false;
    private boolean isMenuPlaying = false;


    //checking if the sample and trial number fields have been checked off in full namer to see whether to keep data persistent
    public static void setIsSampleChecked(boolean isSampleChecked) {
        FullNamer.isSampleChecked = isSampleChecked;
    }

    public static void setIsTrialChecked(boolean isTrialChecked) {
        FullNamer.isTrialChecked = isTrialChecked;
    }

    private static boolean isSampleChecked = true;

    public static boolean isIsSampleChecked() {
        return isSampleChecked;
    }

    public static boolean isIsTrialChecked() {
        return isTrialChecked;
    }

    private static boolean isTrialChecked = true;

    static void setFullNamerSharedFilename(Filename sharedFilename) {
        FullNamer.sharedFilename = sharedFilename;
    }

    //getter to use the list of log entries in other classes
    static ObservableList<LogEntry> getLogEntryArrayList() {
        return logEntryArrayList;
    }

    //getter to use the list of keywords in other classes
    static ObservableList<Keyword> getData() {
        return data;
    }

    private boolean isRememberData; //variable to see whether to keep data persistent or not across different windows


    @Override
    //intialize the fields and other features/windows in full namer
    public void initialize(URL location, ResourceBundle resources)
    {

    //creating menu option boxes to display in the menu side bar
        drawerList.add(menu);
        drawerList.add(gettingStartedPane);
        drawerList.add(projectPreferencesPane);
        drawerList.add(loggerPane);
        drawerList.add(experimentsPane);
        drawerList.add(keywordsPane);
        drawerList.add(researchersPane);

        //User can optionally check the checkboxes to keep data persistent once the app is closed or across other windows such as compact namer and project preferences
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
            isTrialChecked = newIsSelected;
            outputText.setText(updateName(sharedFilename));
        });

        sampleNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            sampleNumber.setDisable(!newIsSelected);
           isSampleChecked = newIsSelected;
            outputText.setText(updateName(sharedFilename));
        });



        shadingOverlay.setMouseTransparent(true);

        //mouseover the help buttons to find additional functionality information
        final JFXButton inputHelp = helpButtonInput;
        final Tooltip inputTooltip = new Tooltip();
        inputTooltip.setText("Fill in the fields with your desired parameters");
        inputHelp.setTooltip(inputTooltip);

        final JFXButton outputHelp = helpButtonOutput;
        final Tooltip outputTooltip = new Tooltip();
        outputTooltip.setText("The output format is: YYYY_MM_DD_ExperimentAbbreviation_ResearchInitials_TrialNumber_SampleNumber_KeywordAbbreviations");
        outputHelp.setTooltip(outputTooltip);

        //Experiment Manager is the database that contains the experiment names and its parameters such as abbreviation and description
        ExperimentManager.getInstance().subscribe(this);
        //Keyword Manager is the database that contains the keyword names and its parameters such as abbreviation, affix, data type, and data value
        KeywordManager.getInstance().subscribe(this);
        outputText.setEditable(false); //cannnot edit the final output file name
        String pattern = "dd/MM/yyyy"; //formatting the date as per European standards
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
        //the config file stores the data inputted by the user that need to be persistent across the windows
        // such as full/compact namer, project preferences, and the keyword/experiments/researchers databases
        experimentTextField.setMinWidth(Region.USE_PREF_SIZE);
        Config config = new Config();
        isRememberData = Boolean.valueOf(config.getProperty("rememberData"));
        if(sharedFilename == null) {
            sharedFilename = new Filename();
        }
        String configResearcherName = config.getProperty("researcherName");
        if(configResearcherName != null && !configResearcherName.trim().isEmpty())
        {
            researcherName.setText(configResearcherName);
            sharedFilename.setResearcher(configResearcherName);
        }
        String configExperimentName = config.getProperty("experimentName");
        if(configExperimentName != null && !configExperimentName.trim().isEmpty())
        {
            experimentTextField.setText(configExperimentName);
            sharedFilename.setExperiment(configExperimentName);
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
        data.clear(); //clears the list of keywords and its data value inputted in the keywords table in full namer
        String configListOfKeywords = config.getProperty("listOfKeywords");
        if(configListOfKeywords != null && !configListOfKeywords.trim().isEmpty())
        {
            String[] keywords = configListOfKeywords.split(",");
            for(int i = 0; i < keywords.length; i += 2)
            {
                data.add(new Keyword(keywords[i],"","","",keywords[i+1],""));
            }
        }
        sharedFilename.setKeywords(data);
        setCompactNamerFilename(sharedFilename);

        //sets the project name if the user inputs this field in the project preferences menu
        String configProjectName = config.getProperty("projectName");
        if(configProjectName != null && !configProjectName.trim().isEmpty()) //if there is a project name input
        {
            projectName.setText("Project: " + configProjectName); //set this input as project name
            projectName.setFont(new Font(19.0));
        }else{
            projectName.setText(""); //if empty, do not display anything
            projectName.setFont(new Font(19.0));
        }

        if(!isTrialChecked){
            trialNumber.setDisable(true);
            trialNumberCheckbox.setSelected(false);
        }
        else{
            trialNumber.setText(String.valueOf(sharedFilename.getTrialNumber()));
        }

        if(!isSampleChecked){
            sampleNumber.setDisable(true);
            sampleNumberCheckbox.setSelected(false);
        }
        else{
            sampleNumber.setText(String.valueOf(sharedFilename.getTrialNumber()));
        }

        //presetting the date to display today's date
        experimentDate.setValue(LocalDate.now());
        //allocatting space for the text field for the user input
        experimentTextField.setAutocompleteWidth(350);
        columnName.setMinWidth(100);
        columnDataValue.setMinWidth(100);

        //set the column name to populate the corresponding columsn with the input data the user provided
        columnName.setCellValueFactory(new PropertyValueFactory<Keyword, String>("longName"));
        columnDataValue.setCellValueFactory(new PropertyValueFactory<Keyword, String>("dataValue"));

        keywordsTable.setEditable(true); //the table can be edited by the user

        //The following are to get the new, edited value in the table
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

        keywordsTable.setItems(data); //sets the user inputs in the table in the respective columns
        System.out.println(data);

        //These listeners are added to make sure the final file output name is updated as the user enters in information for the fields in full namer
        //Also make sure to keep data persistent between windows and closing out of the program if user selects that option
        experimentDate.valueProperty().addListener((obs, oldDate, newDate) -> outputText.setText(updateName(sharedFilename)));
        experimentTextField.textProperty().addListener((obs, oldexperimentTextField, newexperimentTextField) -> {
            if(experimentTextField.isValidText())
            {outputText.setText(updateName(sharedFilename));
            setCompactNamerFilename(sharedFilename);
            experimentTextField.setValidText(false);
            if(isRememberData){
                setProperty("experimentName",newexperimentTextField);
            }
            else{
                setProperty("experimentName", "");
            }
            }else if(experimentTextField.isTriggerPopup())
            {
                try {
                    experimentTextField.setTriggerPopup(false);
                    popupScreen("FXML/addExperimentToDatabase.fxml", switchNamers.getScene().getWindow());
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

    //The following functions are to increment/decrement the sample and trial number on the click of the "+" and "-" buttons next to the field
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
    //update the experiment name and parameters in the database
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    //Adds the ability for the user to copy the final file output name and paste into the file directory
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
            setProperty("experimentName", "");
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
    //Switch between full namer and compact namer on the click of a button
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/compactNamer.fxml", switchNamers.getScene().getWindow());
    }

    @FXML
    //Navigate to project preferences
    public void handlePreferences(ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        Stage popup = popupScreen("FXML/projectPreferences.fxml", projectPreferencesButton.getScene().getWindow());
    }

    @FXML
    //Opens the logger menu for printing out a timeline of file names when the user clicked on "copy" to clipboard
    public void handleLogger(ActionEvent e) throws IOException{
        Stage popup = popupScreen("FXML/loggerMenu.fxml", loggerButton.getScene().getWindow());
    }

    @FXML
    //Add entries to the table view for keywords in full namer
    public void handleAddButton (ActionEvent e) throws IOException {
        popupScreen("FXML/selectKeywordForFilename.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    //Window for detailed instructions on how to use the application
    public void handleGettingStarted(ActionEvent e) throws IOException{
        Stage popup = popupScreen("FXML/gettingStarted.fxml", gettingStartedButton.getScene().getWindow());
    }


    @FXML
    //Delete selected row in the table view for keywords in full namer and also the keyword, experiment, and researchers databases
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
    //Functionality for the menu transitions and icons
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
        int duration = 0;
        for (AnchorPane drawer : drawerList) {
            SequentialTransition pauseAndPlay = new SequentialTransition();
            duration += 60;
            pauseAndPlay.getChildren().add(new PauseTransition(Duration.millis(duration)));
            p.getChildren().add(pauseAndPlay);
            if(drawer.getId().equals("menu"))
            {
                pauseAndPlay.getChildren().add(slidingTransition(drawer, direction, 133.0));
            }else{
                pauseAndPlay.getChildren().add(slidingTransition(drawer, direction, 200.0));
            }
        }
        p.setOnFinished(event -> isMenuPlaying = false);
        return p;
    }


    @Override
    //updates the experiment names and its parameters (such as abbreviation) in the database
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentTextField.getEntries().addAll(experiments);
    }

    static ObservableList<Keyword> getdata() {
        return data;
    }

    public static TreeTableView getTableOfKeywords() {
        return tableOfKeywords;
    }

    @FXML
    //closes the whole application
    public void closeFullNamer(ActionEvent e) {
        closeProgram(closeButton);
    }


    //Adds keywords, experiments, researchers and their corresponding parameters to the table view
    @FXML
    public void handleExperiments(ActionEvent actionEvent)throws IOException{
        popupScreen("FXML/experimentsTable.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    public void handleKeywords(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/keywordsTable.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    public void handleResearchers(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/researchersTable.fxml", addKeywordButton.getScene().getWindow());
    }

    //clears all the fields in full namer
    public void clearFields(ActionEvent e) {
        researcherName.setText("");
        sampleNumber.setText("0");
        trialNumber.setText("0");
        experimentTextField.setText("");
        data.clear();
    }
}
