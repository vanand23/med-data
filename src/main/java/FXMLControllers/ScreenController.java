package FXMLControllers;

import Singletons.FXMLManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;

public abstract class ScreenController {
    //define your offsets here
    private double xOffset = 0;
    private double yOffset = 0;

    public static boolean inHierarchy(Node node, Node potentialParent){
            if (potentialParent == null) {
                return true;
            }
            while (node != null) {
                if (node == potentialParent) {
                    return true;
                }
                node = node.getParent();
            }
            return false;
    }

    /**
     * handles when a new screen is needed as a popup window
     *
     * @param fxml the location what needs to pop up in a new window
     * @param ownerWindow the current window that is open
     * @param title the name that will appear at the top of the popup window
     * @throws IOException exception thrown
     */
    public FXMLLoader popupScreen(String fxml, Window ownerWindow, String title) throws IOException{
        Stage stage = new Stage();
        //Parent root = FXMLManager.getInstance().getOrLoadFXMLNode(fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL); //prevents main window from receiving input
        stage.initOwner(ownerWindow); //sets the main window as this screens owner
        stage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX()-xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.setResizable(false);
        stage.show();
        return fxmlLoader;

    }

    public void setScene(String fxml, Stage stage) throws IOException{
        Parent root = FXMLManager.getInstance().getOrLoadFXMLNode(fxml);
            System.out.println("root has parent");
            System.out.println(root.getScene());
            if(root.getScene() != null) {
                System.out.println("in the loops");
                stage.setScene(root.getScene());
                stage.setFullScreen(true);
            }
            else {
                //Group group = (Group)stage.getScene().getRoot();
                //group.getChildren().clear();
                //group.getChildren().add(root);
                stage.setScene(new Scene(root));
                stage.show();
                stage.setFullScreen(true);
            }

        //View all types of service requests
        //New Screen for types of service requests
    }

    protected FXMLLoader switchScreen(Stage stage, String fxmlFile){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));

        try{
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.setWidth(GiftServiceRequest.getWindowWidth());
            //stage.setHeight(GiftServiceRequest.getWindowLength());
        } catch(IOException e){
            e.printStackTrace();
        }

        return fxmlLoader;
    }


}
