package FXMLControllers;

import Types.Keyword;
import Types.LogEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static FXMLControllers.FullNamer.getLogEntryArrayList;

public class LoggerMenu extends ScreenController implements Initializable {

    @FXML
    private TableView<LogEntry> logTable;

    @FXML
    private TableColumn<LogEntry, String>  timeColumn;

    @FXML
    private TableColumn<LogEntry, String>  dateColumn;

    @FXML
    private TableColumn<LogEntry, String> researcherColumn;

    @FXML
    private TableColumn<LogEntry, String> trialNumberColumn;

    @FXML
    private TableColumn<LogEntry, String> sampleNumberColumn;

    @FXML
    private TableColumn<LogEntry, String> filenameColumn;

    @FXML
    private TableColumn<LogEntry, String> commentColumn;

    @FXML
    private JFXTextField priorLogPath;

    @FXML
    private JFXButton logFileBrowser;

    @FXML
    private JFXTextField newLogFileName;

    @FXML
    private JFXButton closeLoggerButton;

    @FXML
    private JFXButton priorLogHelpButton;

    @FXML
    private JFXButton openLogButton;

    private Desktop desktop = Desktop.getDesktop();

    ArrayList<Keyword> allKeywords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final JFXButton priorLogHelp = priorLogHelpButton;
        final Tooltip priorLogTooltip = new Tooltip();
        priorLogTooltip.setText("Click on the browse button to find a previous log file.");
        priorLogHelp.setTooltip(priorLogTooltip);

        priorLogPath.textProperty().addListener((obs,oldLogFilePath,newLogFilePath) -> {
            if(priorLogPath!= null && !priorLogPath.getText().trim().isEmpty()){
                openLogButton.setDisable(false);
            }
            else{
                openLogButton.setDisable(true);
            }
        });
        ObservableList<LogEntry> logEntryArrayList = getLogEntryArrayList();

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("experimentTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("experimentDate"));
        researcherColumn.setCellValueFactory(new PropertyValueFactory<>("researcher"));
        trialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trialNumber"));
        sampleNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sampleNumber"));
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        logTable.setEditable(true);
        logTable.setItems(logEntryArrayList);
    }

    @FXML
    private void browseLogs(ActionEvent e){
        final FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog((Stage)logFileBrowser.getScene().getWindow());
        if (file != null) {
            //  openFile(file);
            priorLogPath.setText(file.getAbsolutePath());
        }
    }


    private void openFile (File file){
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    LoggerMenu.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    @FXML
    public void closeLogger(ActionEvent e) {
        Stage primaryStage = (Stage) closeLoggerButton.getScene().getWindow();
        primaryStage.close();
    }

   @FXML
    public void resetLogger(ActionEvent e){
       ObservableList<LogEntry> logEntryArrayList = getLogEntryArrayList();
        logEntryArrayList.clear();
    }

    @FXML
    public void openLog(ActionEvent e){
       File file = new File(priorLogPath.getText());
       openFile(file);
    }

    @FXML
    public void handleWriteToNewLogButton(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/logFileWriter.fxml", closeLoggerButton.getScene().getWindow(),"Create a new log");
    }
}

