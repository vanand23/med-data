package Launcher;

import Singletons.Database;
import Singletons.FXMLManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launcher extends Application {

    //define your offsets here
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        double width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        double height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        Font.loadFont(
                Launcher.class.getResource("/Fonts/ArialBlack.ttf").toExternalForm(),
                10
        );


        Database.initDatabase();
        FXMLManager fxmlManager = FXMLManager.getInstance();

        fxmlManager.loadFXML("FXML/fullNamer.fxml");

        Parent root = fxmlManager.getFXMLNode("FXML/fullNamer.fxml");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
