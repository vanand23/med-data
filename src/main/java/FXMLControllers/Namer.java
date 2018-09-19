package FXMLControllers;

import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.Config;
import Utilities.KeywordAutocompleteTextField;
import com.jfoenix.controls.JFXTextField;

import javax.naming.NameNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Namer extends ScreenController{

    String updateName(String experimentTypeText,
                      String trialNumberText,
                      String sampleNumberText,
                      String researcherNameText,
                      LocalDate experimentDate,
                      ArrayList<KeywordAutocompleteTextField> sharedListOfKeywords)
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

        for(KeywordAutocompleteTextField autocompleteTextField : sharedListOfKeywords)
        {
            if(autocompleteTextField.getText() != null && !autocompleteTextField.getText().trim().isEmpty())
            {
                String keyword;
                fname.append(delimiter);
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
            }
        }
        return fname.toString();
    }
}