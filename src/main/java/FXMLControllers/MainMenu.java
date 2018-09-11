package FXMLControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class MainMenu extends ScreenController{

    @FXML
    private JFXButton simpleNamerButton;

    @FXML
    private JFXButton fullNamerButton;

    /**
     * creates a popup window with the admin user manager screen
     *
     * @param e Action event given
     * @throws IOException exception
     */
    @FXML
    private void goToAdminOptionsScreen(ActionEvent e) throws IOException {
        //switchScreen(, "FXML/AdminOptions.fxml");
    }

}
