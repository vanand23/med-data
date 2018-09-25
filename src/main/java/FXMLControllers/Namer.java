package FXMLControllers;

import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.Config;
import javafx.collections.ObservableList;

import javax.naming.NameNotFoundException;
import java.time.LocalDate;

class Namer extends ScreenController{

    String updateName(String experimentTypeText,
                      String trialNumberText,
                      String sampleNumberText,
                      String researcherNameText,
                      LocalDate experimentDate,
                      ObservableList<Keyword> sharedListOfKeywords)
    {
        Config config = new Config();
        String delimiter = config.getProperty("delimiter");
        if(delimiter == null){
            delimiter = "_";
        }

        StringBuilder fname = new StringBuilder();

        if(experimentDate != null)
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

        if(trialNumberText != null && !trialNumberText.trim().isEmpty())
        {
            fname.append(delimiter);
            fname.append("T");
            fname.append(trialNumberText);
        }

        if(sampleNumberText != null && !sampleNumberText.trim().isEmpty())
        {
            fname.append(delimiter);
            fname.append("S");
            fname.append(sampleNumberText);
        }

        StringBuilder initial = new StringBuilder();
        if(researcherNameText != null && researcherNameText.length() != 0)
        {
            String name = researcherNameText.toUpperCase();

            String[] parts = name.split(" ");
            StringBuilder finalInitial = new StringBuilder();

            String sepIni;

            for(int i=0; i<parts.length; i++) {

                sepIni = parts[i].substring(0,1);
                finalInitial = initial.append(sepIni);

            }

            fname.append(delimiter);
            fname.append(finalInitial);

        }
        for(Keyword keywordCell : sharedListOfKeywords)
        {
            if(keywordCell.getKeywordName() != null && !keywordCell.getKeywordName().trim().isEmpty())
            {
                String keywordName;
                String keywordValue = keywordCell.getDataValue();
                fname.append(delimiter);
                try {
                    keywordName = KeywordManager.getInstance().getKeywordByName("long",keywordCell.getKeywordName()).getShortName();
                    String affix = KeywordManager.getInstance().getKeywordByName("long",keywordCell.getKeywordName()).getAffix();
                    switch (affix){
                        case "prefix":
                            fname.append(keywordName);
                            if(keywordValue != null && !keywordValue.trim().isEmpty())
                            {
                                fname.append(keywordValue);
                            }
                            break;
                        case "suffix":
                            if(keywordValue != null && !keywordValue.trim().isEmpty())
                            {
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
        return fname.toString();
    }
}