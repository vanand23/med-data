package Animation;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class IconTransitions {
/*
    public static void startTransition(Node icon, double amount){
        TranslateTransition start = new TranslateTransition(Duration.millis(0.1),icon);
        start.setByY(amount);
        start.play();
    }
    */
    /**
     * Fades in an icon from top to bottom, 1 means icon slides top to bottom, 2 means icon slides bottom-up
     */
    public static SequentialTransition fadeIn(Node icon, int direction){
        //boolean isPlaying = false;
        double original = icon.localToParent(icon.getBoundsInLocal()).getMinY();
        icon.setOpacity(0.0);
        icon.setTranslateY(0.0);
        TranslateTransition start = new TranslateTransition(Duration.millis(0.1),icon);
        TranslateTransition t1 = new TranslateTransition(Duration.millis(100),icon);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(75),icon);
        TranslateTransition t3 = new TranslateTransition(Duration.millis(50),icon);
        TranslateTransition t4 = new TranslateTransition(Duration.millis(50),icon);

        FadeTransition f1 = new FadeTransition(Duration.millis(100),icon);
        FadeTransition f2 = new FadeTransition(Duration.millis(75),icon);
        FadeTransition f3 = new FadeTransition(Duration.millis(50),icon);
        FadeTransition f4 = new FadeTransition(Duration.millis(50),icon);


        f1.setFromValue(0.0);
        f1.setToValue(0.5);

        f2.setFromValue(0.5);
        f2.setToValue(0.8);

        f3.setFromValue(0.8);
        f3.setToValue(0.9);

        f4.setToValue(0.9);
        f4.setToValue(1.0);


        start.setByY(-20 * direction);
        t1.setByY(10 * direction);
        t2.setByY(5 * direction);
        t3.setByY(3 * direction);
        t4.setByY(2 * direction);


        ParallelTransition pt1 = new ParallelTransition(t1, f1);
        ParallelTransition pt2 = new ParallelTransition(t2, f2);
        ParallelTransition pt3 = new ParallelTransition(t3, f3);
        ParallelTransition pt4 = new ParallelTransition(t4, f4);
        SequentialTransition st = new SequentialTransition(start, pt1, pt2, pt3, pt4);

        return st;
    }

    /**
     * Fades out an icon from bottom to top
     */
    public static SequentialTransition bounceTransition(Node icon, int direction){
        double original = icon.localToParent(icon.getBoundsInLocal()).getMinY();
        icon.setOpacity(0.0);
        icon.setTranslateY(0.0);
        TranslateTransition start = new TranslateTransition(Duration.millis(0.1),icon);
        TranslateTransition t1 = new TranslateTransition(Duration.millis(100),icon);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(75),icon);
        TranslateTransition t3 = new TranslateTransition(Duration.millis(50),icon);
        TranslateTransition t4 = new TranslateTransition(Duration.millis(50),icon);

        FadeTransition f1 = new FadeTransition(Duration.millis(100),icon);
        FadeTransition f2 = new FadeTransition(Duration.millis(75),icon);
        FadeTransition f3 = new FadeTransition(Duration.millis(50),icon);
        FadeTransition f4 = new FadeTransition(Duration.millis(50),icon);


        f1.setFromValue(0.0);
        f1.setToValue(0.4);

        f2.setFromValue(0.4);
        f2.setToValue(0.7);

        f3.setFromValue(0.7);
        f3.setToValue(0.87);

        f4.setToValue(0.87);
        f4.setToValue(1.0);


        start.setByY(-50 * direction);
        t1.setByY(27 * direction);
        t2.setByY(13 * direction);
        t3.setByY(7 * direction);
        t4.setByY(3 * direction);


        ParallelTransition pt1 = new ParallelTransition(t1, f1);
        ParallelTransition pt2 = new ParallelTransition(t2, f2);
        ParallelTransition pt3 = new ParallelTransition(t3, f3);
        ParallelTransition pt4 = new ParallelTransition(t4, f4);
        SequentialTransition st = new SequentialTransition(start, pt1, pt2, pt3, pt4);

        return st;

    }

    public static void fadeOut(Node icon){

    }

    /**
     * Oscillates a node back and forth in the given direction
     * @param icon
     * @param direction
     */
    public static SequentialTransition oscillate(Node icon, String direction){
        TranslateTransition t1 = new TranslateTransition(Duration.millis(150),icon);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(150),icon);
        TranslateTransition t3 = new TranslateTransition(Duration.millis(150),icon);
        TranslateTransition t4 = new TranslateTransition(Duration.millis(150),icon);
        TranslateTransition t5 = new TranslateTransition(Duration.millis(300),icon);

        switch (direction){
            case "up":
                t1.setByY(12);
                t2.setByY(9);
                t3.setByY(6);
                t4.setByY(3);
                t5.setByY(-30);
                break;
            case "down":
                t1.setByY(-12);
                t2.setByY(-9);
                t3.setByY(-6);
                t4.setByY(-3);
                t5.setByY(30);
                break;
            case "left":
                t1.setByX(-12);
                t2.setByX(-9);
                t3.setByX(-6);
                t4.setByX(-3);
                t5.setByX(30);
                break;
            case "right":
                t1.setByX(12);
                t2.setByX(9);
                t3.setByX(6);
                t4.setByX(3);
                t5.setByX(-30);
                break;
        }
        SequentialTransition s = new SequentialTransition(t1,t2,t3,t4,t5);
        s.setCycleCount(Animation.INDEFINITE);
        s.setOnFinished(event -> icon.setTranslateY(0));

        return s;
    }

    public static SequentialTransition errorOscillate(Node node){
        double width = node.getBoundsInParent().getWidth();
        TranslateTransition t1 = new TranslateTransition(Duration.millis(100),node);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(100),node);
        TranslateTransition t3 = new TranslateTransition(Duration.millis(100),node);
        TranslateTransition t4 = new TranslateTransition(Duration.millis(100),node);
        TranslateTransition t5 = new TranslateTransition(Duration.millis(100),node);

        t1.setByX(6);
        t2.setByX(-12);
        t3.setByX(10);
        t4.setByX(-6);
        t5.setByX(2);

        SequentialTransition s = new SequentialTransition(t1,t2,t3,t4,t5);
        return s;
    }

    public static void flash(Label node){

        KeyValue kv1 = new KeyValue(node.backgroundProperty(), node.getBackground());
        KeyValue kv2 = new KeyValue(node.backgroundProperty(), new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, kv1);
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(200), kv2);

        Timeline timeline = new Timeline(keyFrame1, keyFrame2);

        //timeline.setAutoReverse(true);
        //timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
}