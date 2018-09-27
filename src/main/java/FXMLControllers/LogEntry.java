package FXMLControllers;

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
    private String experimentType;
    private String trialNumber;
    private String sampleNumber;
    private String fileName;
    private String comment;
    private ObservableList<Keyword> listOfKeywords;
    LogEntry(LocalDate experimentDate,
             String researcherName,
             String experimentType,
             String trialNumber,
             String sampleNumber,
             String fileName,
             ObservableList<Keyword> listOfKeywords,
             String comment){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd LLL yy");
        DateTimeFormatter timeFormatter = ISO_LOCAL_TIME;

        this.experimentDate = dateFormatter.format(experimentDate);
        this.experimentTime = timeFormatter.format(LocalTime.now());
        this.researcherName = researcherName;
        this.experimentType = experimentType;
        this.trialNumber = trialNumber;
        this.sampleNumber = sampleNumber;
        this.fileName = fileName;
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

    public String getExperimentType() {
        return experimentType;
    }

    public String getTrialNumber() {
        return trialNumber;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public ObservableList<Keyword> getListOfKeywords() {
        return listOfKeywords;
    }

    public String getComment() {
        return comment;
    }
}
