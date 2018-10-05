package Types;

import Types.Keyword;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class LogEntry {

    private String experimentDate;
    private String experimentTime;
    private String researcherName;
    private String experimentName;
    private String trialNumber;
    private String sampleNumber;
    private String filename;
    private String comment;
    private ObservableList<Keyword> listOfKeywords;
    public LogEntry(){
        this.experimentDate = "";
        this.experimentTime = "";
        this.researcherName = "";
        this.experimentName = "";
        this.trialNumber = "";
        this.sampleNumber = "";
        this.filename = "";
        this.listOfKeywords = null;
        this.comment = "";
    }
    public LogEntry(LocalDate experimentDate,
                    String researcherName,
                    String experimentName,
                    String trialNumber,
                    String sampleNumber,
                    String filename,
                    ObservableList<Keyword> listOfKeywords,
                    String comment){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd LLL yy");

        this.experimentDate = dateFormatter.format(experimentDate);
        this.experimentTime = ISO_LOCAL_TIME.format(LocalTime.now());
        this.researcherName = researcherName;
        this.experimentName = experimentName;
        this.trialNumber = trialNumber;
        this.sampleNumber = sampleNumber;
        this.filename = filename;
        this.listOfKeywords = listOfKeywords;
        this.comment = comment;
    }

    public String getExperimentDate() {
        return experimentDate;
    }

    public String getExperimentTime() {
        return experimentTime;
    }

    public String getResearcherName() {
        return researcherName;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public String getTrialNumber() {
        return trialNumber;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public String getFilename() {
        return filename;
    }

    public ObservableList<Keyword> getListOfKeywords() {
        return listOfKeywords;
    }

    public String getComment() {
        return comment;
    }

    public void setExperimentDate(String experimentDate) {
        this.experimentDate = experimentDate;
    }

    public void setExperimentTime(String experimentTime) {
        this.experimentTime = experimentTime;
    }

    public void setResearcherName(String researcherName) {
        this.researcherName = researcherName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public void setTrialNumber(String trialNumber) {
        this.trialNumber = trialNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setListOfKeywords(ObservableList<Keyword> listOfKeywords) {
        this.listOfKeywords = listOfKeywords;
    }
}
