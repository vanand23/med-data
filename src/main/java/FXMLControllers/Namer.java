package FXMLControllers;

import Types.ExperimentManager;
import Types.Filename;
import Types.KeywordManager;
import Types.Keyword;
import Singletons.Config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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

public class Namer extends ScreenController{

    @FXML
    JFXCheckBox sampleNumberCheckbox;

    @FXML
    JFXCheckBox trialNumberCheckbox;

    public static Filename sharedFilename;

    String updateName(Filename filename)
    {
        String experimentTypeText = filename.getExperiment();
        int trialNumberText = filename.getTrialNumber();
        int sampleNumberText = filename.getSampleNumber();
        String researcherNameText = filename.getResearcher();
        LocalDate experimentDate =  filename.getDate();
        ObservableList<Keyword> sharedListOfKeywords = filename.getKeywords();


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

        StringBuilder initial = new StringBuilder();
        if(researcherNameText != null && researcherNameText.length() != 0)
        {
            String name = researcherNameText.toUpperCase();

            String[] parts = name.split(" ");
            StringBuilder finalInitial = new StringBuilder();

            String sepIni;

            for (String part : parts) {

                sepIni = part.substring(0, 1);
                finalInitial = initial.append(sepIni);
            }
            fname.append(delimiter);
            fname.append(finalInitial);
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