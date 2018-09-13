package FXMLControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class SimpleNamer extends ScreenController {

    @FXML
    private JFXTextField experimentDate;

    @FXML
    private JFXTextField trialNumber;

    @FXML
    private JFXButton copyButton;

    @FXML
    private JFXToggleButton switchNamers;


    @FXML
    public void copy(ActionEvent e) throws IOException {
        String myString = "test";
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }




}


