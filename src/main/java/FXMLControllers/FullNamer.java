package FXMLControllers;

import Types.*;
import Utilities.AutocompleteTextField;
import Singletons.Config;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
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


public class FullNamer extends Namer implements Initializable, ITypeObserver {
    @FXML
    public JFXCheckBox researcherCheckbox;
    @FXML
    public JFXCheckBox experimentCheckbox;
    @FXML
    public JFXCheckBox dateCheckbox;

    @FXML
    private Label projectName;

    @FXML
    private DatePicker experimentDate;

    @FXML
    private AutocompleteTextField researcherName;

    @FXML
    private TextField trialNumber;


    @FXML
    private TextField sampleNumber;

    @FXML
    private TextField outputText;

    @FXML
    private AutocompleteTextField experimentTextField;

    @FXML
    private JFXButton addKeywordButton;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton helpButtonInput;

    @FXML
    private TableView<Keyword> keywordsTable;

    @FXML
    private TableColumn columnName;

    @FXML
    private TableColumn columnDataValue;

    //list of keywords and its parameters that the user inputs in full namer
    private final static ObservableList<Keyword> listOfKeywords = FXCollections.observableArrayList();

    //list of file names in the timeline when the user clicked on the copy button, and additional parameters such as time and descriptions
    private static ObservableList<LogEntry> logEntryList = FXCollections.observableArrayList();

    //list of the menu options to access windows such as project preferences and the keyword, experiments, and researcher databases
    private static ArrayList<AnchorPane> drawerList = new ArrayList<>();

    private Image doubleWindowIcon = new Image("Images/doubleWindowIcon.png");
    private Image singleWindowIcon = new Image("Images/singleWindowIcon.png");
    private boolean isMenuOpen = false;
    private boolean isMenuPlaying = false;
    private boolean isFullScreen = false;


    //checking if the sample and trial number fields have been checked off in full namer to see whether to keep data persistent
    public static void setSampleChecked(boolean sampleChecked) {
        FullNamer.sampleChecked = sampleChecked;
    }

    public static void setIsTrialChecked(boolean isTrialChecked) {
        FullNamer.isTrialChecked = isTrialChecked;
    }

    private static boolean sampleChecked = true;

    public static boolean isSampleChecked() {
        return sampleChecked;
    }

    public static boolean isIsTrialChecked() {
        return isTrialChecked;
    }

    private static boolean isTrialChecked = true;

    static void setFullNamerSharedFilename(Filename sharedFilename) {
        FullNamer.sharedFilename = sharedFilename;
    }

    //getter to use the list of log entries in other classes
    static ObservableList<LogEntry> getLogEntryList() {
        return logEntryList;
    }

    static void addAllToLogEntryList(ObservableList<LogEntry> logEntryList) {
        FullNamer.logEntryList.addAll(logEntryList);
    }

    static void setLogEntryList(ObservableList<LogEntry> logEntryList) {
        FullNamer.logEntryList = logEntryList;
    }

    //getter to use the list of keywords in other classes
    static ObservableList<Keyword> getData() {
        return listOfKeywords;
    }

    private boolean isRememberData; //variable to see whether to keep data persistent or not across different windows


    @Override
    //initialize the fields and other features/windows in full namer
    public void initialize(URL location, ResourceBundle resources)
    {
        dateCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            experimentDate.setDisable(!newIsSelected);
            if(!newIsSelected)
            {
                sharedFilename.setDate(LocalDate.MIN);
            }else{
                sharedFilename.setDate(experimentDate.getValue());
            }
            outputText.setText(updateName());
        });

        experimentCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            experimentTextField.setDisable(!newIsSelected);
            if(!newIsSelected)
            {
                sharedFilename.setExperiment("");
            }else{
                sharedFilename.setExperiment(experimentTextField.getText());
            }
            outputText.setText(updateName());
        });

        researcherCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            researcherName.setDisable(!newIsSelected);
            if(!newIsSelected)
            {
                sharedFilename.setResearcher("");
            }else{
                sharedFilename.setResearcher(researcherName.getText());
            }
            outputText.setText(updateName());
        });

        trialNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            trialNumber.setDisable(!newIsSelected);
            isTrialChecked = newIsSelected;
            outputText.setText(updateName());
        });

        sampleNumberCheckbox.selectedProperty().addListener((obs, oldIsSelected, newIsSelected) -> {
            sampleNumber.setDisable(!newIsSelected);
            sampleChecked = newIsSelected;
            outputText.setText(updateName());
        });


        //mouseover the help buttons to find additional functionality information
        final JFXButton inputHelp = helpButtonInput;
        final Tooltip inputTooltip = new Tooltip();
        inputTooltip.setText("Fill in the fields with your desired parameters");
        inputHelp.setTooltip(inputTooltip);

        /*final JFXButton outputHelp = helpButtonOutput;
        final Tooltip outputTooltip = new Tooltip();
        outputTooltip.setText("The output format is: YYYY_MM_DD_ExperimentAbbreviation_ResearchInitials_TrialNumber_SampleNumber_KeywordAbbreviations");
        outputHelp.setTooltip(outputTooltip);*/

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
        researcherName.setMinWidth(Region.USE_PREF_SIZE);

        isRememberData = Boolean.valueOf(Config.getInstance().getProperty("rememberData"));
        if(sharedFilename == null) {
            sharedFilename = new Filename();
        }
        //Set fields from memory
        Config.getInstance().setFieldFromConfig(researcherName,"researcherName");
        Config.getInstance().setFieldFromConfig(experimentTextField,"experimentName");
        if(!Config.getInstance().setFieldFromConfig(trialNumber,"trialNumber"))
        {
            trialNumber.setText("0");
            sharedFilename.setTrialNumber(0);
        }
        if(!Config.getInstance().setFieldFromConfig(sampleNumber,"sampleNumber"))
        {
            sampleNumber.setText("0");
            sharedFilename.setSampleNumber(0);
        }
        listOfKeywords.clear(); //clears the list of keywords and its data value inputted in the keywords table in full namer
        String configListOfKeywords = Config.getInstance().getProperty("listOfKeywords");
        if(configListOfKeywords != null && !configListOfKeywords.trim().isEmpty())
        {
            String[] keywords = configListOfKeywords.split(",");
            for(int i = 0; i < keywords.length; i += 2)
            {
                listOfKeywords.add(new Keyword(keywords[i],"","","",keywords[i+1],""));
            }
        }
        sharedFilename.setKeywords(listOfKeywords);

        //sets the project name if the user inputs this field in the project preferences menu
        String configProjectName = Config.getInstance().getProperty("projectName");
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

        if(!sampleChecked){
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
        researcherName.setAutocompleteWidth(350);
        columnName.setMinWidth(100);
        columnDataValue.setMinWidth(100);

        //set the column name to populate the corresponding column with the input data the user provided
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
                        outputText.setText(updateName());
                    }
                }
        );

        keywordsTable.setItems(listOfKeywords); //sets the user inputs in the table in the respective columns

        //These listeners are added to make sure the final file output name is updated as the user enters in information for the fields in full namer
        //Also make sure to keep data persistent between windows and closing out of the program if user selects that option
        experimentDate.valueProperty().addListener((obs, oldDate, newDate) -> outputText.setText(updateName()));
        experimentTextField.textProperty().addListener((obs, oldExperimentTextField, newExperimentTextField) -> {
            if(experimentTextField.isValidText())
            {
                System.out.println("Set new experiment!" + newExperimentTextField);
                sharedFilename.setExperiment(newExperimentTextField);
                outputText.setText(updateName());
                experimentTextField.setValidText(false);
                if(isRememberData)
                {
                    Config.getInstance().setProperty("experimentName", newExperimentTextField);
                }else{
                    Config.getInstance().setProperty("experimentName", "");
                }
            }else if(experimentTextField.isTriggerPopup()) {
                try {
                    experimentTextField.setTriggerPopup(false);
                    popupScreen("FXML/addExperimentToDatabase.fxml", switchNamers.getScene().getWindow());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
           if(researcherName.isValidText()) {
               sharedFilename.setResearcher(newResearcherName);
               outputText.setText(updateName());
               researcherName.setValidText(false);
               if (isRememberData) {
                   Config.getInstance().setProperty("researcherName", newResearcherName);
               } else {
                   Config.getInstance().setProperty("researcherName", "");
               }
           } else if(researcherName.isTriggerPopup()){
               try{
                   researcherName.setTriggerPopup(false);
                   popupScreen("FXML/addResearcherToDatabase.fxml", researcherName.getScene().getWindow());
               }catch (IOException e){
                   e.printStackTrace();
               }
           }
        });
        trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
            sharedFilename.setTrialNumber(Integer.valueOf(newTrialNumber));
            outputText.setText(updateName());
            if(isRememberData) {
                Config.getInstance().setProperty("trialNumber", newTrialNumber);
            }
            else{
                Config.getInstance().setProperty("trialNumber", "");
            }
        });
        sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) -> {
            sharedFilename.setSampleNumber(Integer.valueOf(newSampleNumber));
            outputText.setText(updateName());
            if(isRememberData) {
                Config.getInstance().setProperty("sampleNumber", newSampleNumber);
            }
            else{
                Config.getInstance().setProperty("sampleNumber", "");
            }
        });
        listOfKeywords.addListener((ListChangeListener<Keyword>) keywords -> {
            sharedFilename.setKeywords(listOfKeywords);
            outputText.setText(updateName());
        });
        outputText.setText(updateName());
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
    //update the experiment name and parameters in the database
    public void updateResearcher(ActionEvent e) throws IOException{
        whenUpdating();
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
        String nameToCopy = updateName();

        if(isRememberData) {
            Config.getInstance().setProperty("experimentTextField",experimentTextField.getText());
        }
        else{
            Config.getInstance().setProperty("experimentName", "");
        }

        if(isRememberData) {
            Config.getInstance().setProperty("researcherName",researcherName.getText());
        }
        else{
            Config.getInstance().setProperty("theResearcherName", "");
        }

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        outputText.setText(nameToCopy);
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
    //Switch between full namer and compact namer on the click of a button
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/compactNamer.fxml", switchNamers.getScene().getWindow());
    }

    @FXML
    //Navigate to project preferences
    public void handlePreferences(ActionEvent e) throws IOException {
        popupScreen("FXML/preferences.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    //Opens the logger menu for printing out a timeline of file names when the user clicked on "copy" to clipboard
    public void handleLogger(ActionEvent e) throws IOException{
        Stage popup = popupScreen("FXML/loggerMenu.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    //Add entries to the table view for keywords in full namer
    public void handleAddButton (ActionEvent e) throws IOException {
        popupScreen("FXML/selectKeywordForFilename.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    //Window for detailed instructions on how to use the application
    public void handleGettingStarted(ActionEvent e) throws IOException{
        Stage popup = popupScreenSwitchable("FXML/gettingStarted.fxml", addKeywordButton.getScene().getWindow());
    }

    @FXML
    //Window for information about the application
    public void handleAbout(ActionEvent e) throws IOException{
        Stage popup = popupScreen("FXML/about.fxml", addKeywordButton.getScene().getWindow());
    }


    @FXML
    //Delete selected row in the table view for keywords in full namer and also the keyword, experiment, and researchers databases
    public void handleDeleteButton (ActionEvent e) throws IOException {

        Keyword selectedItem = keywordsTable.getSelectionModel().getSelectedItem();
        keywordsTable.getItems().remove(selectedItem);
        StringBuilder configListOfKeywords = new StringBuilder();
        for(Keyword keyword : listOfKeywords){
            configListOfKeywords.append(",");
            configListOfKeywords.append(keyword.getLongName());
            configListOfKeywords.append(",");
            configListOfKeywords.append(keyword.getDataValue());
        }
        if(configListOfKeywords.length() != 0)
        {
            configListOfKeywords.deleteCharAt(0);
        }
        if(isRememberData) {
            Config.getInstance().setProperty("listOfKeywords", configListOfKeywords.toString());
        }
        else{
            Config.getInstance().setProperty("listOfKeywords","");
        }
    }

/*
    private void addListenerToTextfield(TextField textField){
        researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
            outputText.setText(updateName());
            sharedFilename.setResearcher(newResearcherName);
            if(isRememberData) {
                Config.getInstance().setProperty("researcherName", newResearcherName);
            }
            else{
                Config.getInstance().setProperty("researcherName", "");
            }
        });
    }*/


    @Override
    //updates the experiment/researcher names and its parameters (such as abbreviation) in the database
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentTextField.getEntries().addAll(experiments);

        ArrayList<String> researchers = (ArrayList<String>) ResearcherManager.getInstance().getAllResearcherLongNames();
        researcherName.getEntries().addAll(researchers);
    }

    public void whenUpdating(){

    }

    static ObservableList<Keyword> getdata() {
        return listOfKeywords;
    }

    @FXML
    //closes the whole application
    public void closeFullNamer(ActionEvent e) {
        closeProgram(closeButton);
    }

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
        listOfKeywords.clear();
    }

    @FXML
    public void handleMinimize(ActionEvent actionEvent) {
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        thisStage.setIconified(true);
    }

    /* currently doesn't handle fullscreen
    @FXML
    public void handleFullscreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        isFullScreen = !isFullScreen;
        thisStage.setFullScreen(isFullScreen);
        if(isFullScreen)
        {
            fullscreenButtonImage.setImage(doubleWindowIcon);
        }else{
            fullscreenButtonImage.setImage(singleWindowIcon);
        }
    }
    */
}
