package FXMLControllers;

import Singletons.ExperimentManager;
import Singletons.KeywordManager;
import Singletons.ResearcherManager;
import Types.*;
import Singletons.Config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import javax.naming.NameNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public abstract class Namer extends ScreenController{

    //Checkboxes so the user can choose whether to have the fields in the final file name output
    @FXML
    JFXCheckBox sampleNumberCheckbox;

    @FXML
    JFXCheckBox trialNumberCheckbox;

    //Filename is a separate class that has all the full namer field variables and its respective getters and setters
    public static Filename sharedFilename;

    //list of keywords and its parameters that the user inputs in full namer
    final static ObservableList<Keyword> listOfKeywords = FXCollections.observableArrayList();

    //list of file names in the timeline when the user clicked on the copy button, and additional parameters such as time and descriptions
    static ObservableList<LogEntry> logEntryList = FXCollections.observableArrayList();

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

    //
    String updateFilename()
    {
        //Accessing the stored variables for fields in full namer from Filename class
        String experimentTypeText = sharedFilename.getExperiment();
        int trialNumberText = sharedFilename.getTrialNumber();
        int sampleNumberText = sharedFilename.getSampleNumber();
        String researcherNameText = sharedFilename.getResearcher();
        LocalDate experimentDate =  sharedFilename.getDate();
        ObservableList<Keyword> sharedListOfKeywords = sharedFilename.getKeywords();

        //setting the separation character based on user selection from 3 different characters (underscore, dash, asterix) which is used in the final file name output
        String delimiter = Config.getInstance().getProperty("delimiter");
        if(delimiter == null){ //default is set to underscore
            delimiter = "_";
        }

        //initializing the variable to store the final file name output
        StringBuilder fname = new StringBuilder();


        if(experimentDate != null && experimentDate != LocalDate.MIN) //if user inputted a date
        {
            //add the day, month, year to the file name output separated by the user specified delimiter
            fname.append(experimentDate.getYear());
            fname.append(delimiter);
            fname.append(experimentDate.getMonthValue());
            fname.append(delimiter);
            fname.append(experimentDate.getDayOfMonth());
        }

        //store the experiment abbreviation
        String experimentShorthand = "";

        if(experimentTypeText != null && !(experimentTypeText.trim().isEmpty())) //if user inputs an experiment name
        {
            fname.append(delimiter); //user specified delimiter
            try {
                //access the Experiment Manager database that stores all the experiment names, add the long name, and get the short name
                experimentShorthand = ExperimentManager.getInstance().getExperimentByName("long",experimentTypeText).getShortName();
                fname.append(experimentShorthand); //put short name (As Known As (AKA) abbreviation) in the file name
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        //store the researcher intials
        String researcherShorthand = "";

        if(researcherNameText != null && !researcherNameText.trim().isEmpty()) //if user inputs a researcher name
        {
            fname.append(delimiter); //user specified delimiter
            try {
                //access the Researcher Manager database that stores all the researcher names, add the long name, and get the short name
                researcherShorthand = ResearcherManager.getInstance().getResearcherByName("long",researcherNameText).getShortName();
                System.out.println(researcherShorthand);
                fname.append(researcherShorthand); //put short name (AKA initials) in the file name
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }


        if(trialNumberText != -1) //if user inputs a trial number
        {
            fname.append(delimiter); //user specified delimiter
            fname.append("T");
            fname.append(trialNumberText); //add trial number to final file name
        }

        if(sampleNumberText != -1) //if user inputs a sample number
        {
            fname.append(delimiter); //user specified delimiter
            fname.append("S");
            fname.append(sampleNumberText); //add sample number to final file name
        }

        if(!sharedListOfKeywords.isEmpty()) { //if user inputs keywords
            for (Keyword keywordCell : sharedListOfKeywords) {
                if (keywordCell.getLongName() != null && !keywordCell.getLongName().trim().isEmpty()) {
                    //keywords in the table have two parameters, the name and data value
                    String keywordName;
                    String keywordValue = keywordCell.getDataValue();
                    fname.append(delimiter); //user specified delimiter
                    try {
                        //access the Keyword Manager database that stores all the keyword names, add the long name, and get the short name
                        keywordName = KeywordManager.getInstance().getKeywordByName("long", keywordCell.getLongName()).getShortName();
                        //user specifies whether the abbreviated keyword comes before or after the data value
                        String affix = KeywordManager.getInstance().getKeywordByName("long", keywordCell.getLongName()).getAffix();
                        //checking to see whether user picked one of the given affixes and adds the keyword and data value if applicable to the file name based on that input
                        switch (affix) {
                            case "prefix":
                                fname.append(keywordName);
                                if (keywordValue != null && !keywordValue.trim().isEmpty()) {
                                    fname.append(keywordValue);
                                }
                                break;
                            case "suffix":
                                if (keywordValue != null && !keywordValue.trim().isEmpty()) {
                                    fname.append(keywordValue);
                                }
                                fname.append(keywordName);
                                break;
                            case "none":
                                fname.append(keywordName);
                                break;
                            default:
                                fname.append(keywordName);
                                break;
                        }
                    } catch (NameNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return fname.toString();
    }
    //remember user inputted data even after the program is closed
    //config is used to store all the values of the fields in full namer and keep it persistent across all screens such as compact namer or project preferences
    void closeProgram(JFXButton closeButton) {

        String configRememberData = Config.getInstance().getProperty("rememberData");
        if(configRememberData != null && !configRememberData.trim().isEmpty())
        {
            if(configRememberData.equals("false"))
            {
                try{
                    Properties configFile = new Properties();
                    configFile.load(new FileInputStream("config.properties"));
                    configFile.clear();
                    File tempFile = new File("config.properties");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    configFile.store(fos, "");
                    fos.flush();
                    fos.close();
                    Config.getInstance().setProperty("rememberData", "false");
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}