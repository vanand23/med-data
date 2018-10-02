package FXMLControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static FXMLControllers.KeywordsDBTable.setSelectedValue;
import static Utilities.DirectorySearcher.doesFileExist;

public class CreateNewKeywordDatabase {
    @FXML
    private JFXTextField newDatabaseName;
    @FXML
    private JFXButton closeButton;

    @FXML
    public void handleCloseButton(ActionEvent actionEvent) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    public void generateDatabase(ActionEvent actionEvent) throws IOException {
        String newDatabaseNameString = newDatabaseName.getText();
        if(!doesFileExist(newDatabaseNameString, "keywords")){
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();

            File file = new File(path.substring(0, path.length() - 1) + "/Libraries/keywords/" + newDatabaseNameString + ".csv");
            file.createNewFile();
        }
        setSelectedValue(newDatabaseNameString);
        handleCloseButton(actionEvent);
    }
}
