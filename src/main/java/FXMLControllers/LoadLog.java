package FXMLControllers;

import Types.LogEntry;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static FXMLControllers.FullNamer.getLogEntryArrayList;

public class LoadLog implements Initializable {
    @FXML
    private TextField priorLogPath;

    @FXML
    private JFXButton priorLogHelpButton;

    @FXML
    private JFXButton openLogButton;

    @FXML
    private JFXButton logFileBrowser;

    @FXML
    private JFXButton closeLoggerButton;

    private Desktop desktop = Desktop.getDesktop();

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
    public void openLog(ActionEvent e){
        File file = new File(priorLogPath.getText());
        openFile(file);
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

    @FXML
    public void handleClose(ActionEvent e) {
        Stage primaryStage = (Stage) closeLoggerButton.getScene().getWindow();
        primaryStage.close();
    }
}
