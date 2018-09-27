package Types;


import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

/*outputText.setText(updateName(
                                experimentType.getText(),
                                trialNumber.getText(),
                                sampleNumber.getText(),
                                researcherName.getText(),
                                experimentDate.getValue(),
                                data));*/
public class Filename {
    private LocalDate date;
    private String experiment;
    private String researcher;
    private int trialNumber;
    private int sampleNumber;
    private ObservableList<Keyword> keywords;

    public Filename(LocalDate date, String experiment, String researcher, int trialNumber, int sampleNumber, ObservableList<Keyword> keywords) {
        this.date = date;
        this.experiment = experiment;
        this.researcher = researcher;
        this.trialNumber = trialNumber;
        this.sampleNumber = sampleNumber;
        this.keywords = keywords;
    }

    public Filename() {
        this.date = LocalDate.now();
        this.experiment = "";
        this.researcher = "";
        this.trialNumber = -1;
        this.sampleNumber = -1;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    public int getTrialNumber() {
        return trialNumber;
    }

    public void setTrialNumber(int trialNumber) {
        this.trialNumber = trialNumber;
    }

    public int getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(int sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public ObservableList<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(ObservableList<Keyword> keywords) {
        this.keywords = keywords;
    }
}
