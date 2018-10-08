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

    @FXML
    JFXCheckBox sampleNumberCheckbox;

    @FXML
    JFXCheckBox trialNumberCheckbox;

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

    String updateFilename()
    {
        String experimentTypeText = sharedFilename.getExperiment();
        int trialNumberText = sharedFilename.getTrialNumber();
        int sampleNumberText = sharedFilename.getSampleNumber();
        String researcherNameText = sharedFilename.getResearcher();
        LocalDate experimentDate =  sharedFilename.getDate();
        ObservableList<Keyword> sharedListOfKeywords = sharedFilename.getKeywords();


        String delimiter = Config.getInstance().getProperty("delimiter");
        if(delimiter == null){
            delimiter = "_";
        }

        StringBuilder fname = new StringBuilder();

        if(experimentDate != null && experimentDate != LocalDate.MIN)
        {
            fname.append(experimentDate.getYear());
            fname.append(delimiter);
            fname.append(experimentDate.getMonthValue());
            fname.append(delimiter);
            fname.append(experimentDate.getDayOfMonth());
        }

        String experimentShorthand = "";

        if(experimentTypeText != null && !(experimentTypeText.trim().isEmpty()))
        {
            fname.append(delimiter);
            try {
                experimentShorthand = ExperimentManager.getInstance().getExperimentByName("long",experimentTypeText).getShortName();
                fname.append(experimentShorthand);
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        String researcherShorthand = "";

        if(researcherNameText != null && !researcherNameText.trim().isEmpty())
        {
            fname.append(delimiter);
            try {
                researcherShorthand = ResearcherManager.getInstance().getResearcherByName("long",researcherNameText).getShortName();
                System.out.println(researcherShorthand);
                fname.append(researcherShorthand);
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }


        if(trialNumberText != -1)
        {
            fname.append(delimiter);
            fname.append("T");
            fname.append(trialNumberText);
        }

        if(sampleNumberText != -1)
        {
            fname.append(delimiter);
            fname.append("S");
            fname.append(sampleNumberText);
        }

        if(!sharedListOfKeywords.isEmpty()) {
            for (Keyword keywordCell : sharedListOfKeywords) {
                if (keywordCell.getLongName() != null && !keywordCell.getLongName().trim().isEmpty()) {
                    String keywordName;
                    String keywordValue = keywordCell.getDataValue();
                    fname.append(delimiter);
                    try {
                        keywordName = KeywordManager.getInstance().getKeywordByName("long", keywordCell.getLongName()).getShortName();
                        String affix = KeywordManager.getInstance().getKeywordByName("long", keywordCell.getLongName()).getAffix();
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