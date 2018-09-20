package FXMLControllers;

import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.Config;
import Utilities.KeywordAutocompleteTextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import javax.naming.NameNotFoundException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class LoggerMenu extends ScreenController implements Initializable{

    @FXML
    private JFXTextField priorLogFile;

    @FXML
    private JFXTextField newLogFileName;

    @FXML
    private JFXTextField newLogFilePath;

    @FXML
    private JFXButton cancelLoggerButton;

    @FXML
    private JFXButton startLoggerButton;

    @FXML
    private JFXButton saveLoggerButton;

    @FXML
    private JFXButton resetLoggerButton;

    @FXML
    private JFXButton priorLogHelpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        final JFXButton priorLogHelp = priorLogHelpButton;
        final Tooltip priorLogTooltip = new Tooltip();
        priorLogTooltip.setText("Enter the file path of an existing log file.");
        priorLogHelp.setTooltip(priorLogTooltip);

    }

    @FXML
    public void closeLogger(ActionEvent e) {
        Stage primaryStage = (Stage) cancelLoggerButton.getScene().getWindow();
        primaryStage.close();
    }

}

