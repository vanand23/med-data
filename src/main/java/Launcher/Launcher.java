package Launcher;

import Singletons.Database;
import Singletons.FXMLManager;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

//This is used to run the whole program and launch the app, particularly the full namer window
public class Launcher extends Application {

    //define your offsets here
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception{
        Database.initDatabase();
        FXMLManager fxmlManager = FXMLManager.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("FXML/fullNamer.fxml"));

        fxmlManager.loadFXML();

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.setScene(scene);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWING, window -> {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
            ft.setFromValue(0.8);
            ft.setToValue(1.0);
            ft.play();
        });

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
