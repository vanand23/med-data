package Animation;

import FXMLControllers.ZoomableScrollPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.ArrayList;

public class ScrollPaneAnimator {
    ZoomableScrollPane scrollPane;

    public ScrollPaneAnimator(ZoomableScrollPane scrollPane){
        this.scrollPane = scrollPane;
    }

    private void updateScale(double scale) {
        if(scale > 2.0) {
            scrollPane.getTarget().setScaleX(2.0);
            scrollPane.getTarget().setScaleY(2.0);
        }else if(scale < .5 ){
            scrollPane.getTarget().setScaleX(0.5);
            scrollPane.getTarget().setScaleY(0.5);
        }
    }


    /**
     * This is the function that will be used to zoom to a certain path - currently it is called in the click point method for testing
     * animation is currently not smooth and doesn't adapt to the new scale properly.
     * @param x1 pointX in pixels inside the scrollpane - one of the bounding box corners
     * @param y1 pointY in pixels inside the scrollpane
     * @param x2 - other bounding box corner
     * @param y2 - bounding box y corner
     */


    public void zoomToPoints(double x1, double y1, double x2, double y2){
//        SequentialTransition sequentialTransition = new SequentialTransition();
//        //then zoom into new point
//        double centerX = (x1+x2)/2;
//        double centerY = (y1+y2)/2;
//        double scrollWidth = scrollPane.getWidth();
//        double scrollHeight = scrollPane.getHeight();
//        double scrollDiag = Math.sqrt(scrollWidth* scrollWidth + scrollHeight*scrollHeight);
//        double newScale = 2;//scrollDiag/Math.sqrt((x1*x1 + y1*y1));
//        sequentialTransition.getChildren().add(setZoom(newScale,centerX,centerY,1000));
//        //sequentialTransition.getChildren().add(setZoom(1,500,500,1000));


        //First: the scale must be the height ratio of the height of endpoints/windowHeight or the width of endpoints/windowWidth, whichever is greater
        double endpointsHeight = Math.abs(y1-y2); //height distance between two points, with some wiggle room
        double endpointsWidth = Math.abs(x1-x2); //width distance between two points, with some wiggle room

        double centerX = (x1+x2)/2;
        double centerY = (y1+y2)/2;

        Point2D centerPoint = new Point2D(centerX,centerY);

        double scrollWidth = scrollPane.getWidth() - 500; //Account for the directions panel blocking out the right and the floor buttons blocking out the left
        double scrollHeight = scrollPane.getHeight() - 300;

        double endpointsCurrentWidth = endpointsWidth / (5000/scrollPane.getZoomNode().getLayoutBounds().getWidth());//*scrollPane.getTarget().getScaleX());
        double endpointsCurrentHeight = endpointsHeight / (5000/scrollPane.getZoomNode().getLayoutBounds().getWidth());//*scrollPane.getTarget().getScaleX());
        //Map is 5000 pixels wide, and is scaled down to 1920.
        //This means that the relative distance of the points is equal to the normal distance/(5000/1920)
        double scale;
        if(endpointsHeight/scrollHeight >= endpointsWidth/scrollWidth){
            scale = scrollHeight / endpointsCurrentHeight;
        }else{
            scale = scrollWidth / endpointsCurrentWidth;
        }
        scrollPane.twoPointZoom(scale, centerPoint);

        /*
        Bounds innerBounds = scrollPane.getZoomNode().getLayoutBounds();
        Bounds viewportBounds = scrollPane.getViewportBounds();

        double valX = scrollPane.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = scrollPane.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double previousScaleValue = scrollPane.getTarget().getScaleX();
        double tmpScaleValue = scrollPane.getTarget().getScaleX() * scale;

        scrollPane.layout();

        Point2D posInZoomTarget = scrollPane.getTarget().parentToLocal(scrollPane.getZoomNode().parentToLocal(centerPoint));
        System.out.println("PosInZoomTarget: " + posInZoomTarget);

        updateScale(previousScaleValue);
        updateScale(tmpScaleValue);

        System.out.println("Current Hval: " + scrollPane.getHvalue());
        System.out.println("Current Vval: " + scrollPane.getVvalue());
        System.out.println("Current centerX: " + centerX);
        System.out.println("Current centerY: " + centerY);

        scrollPane.setHvalue((centerX)/5000);
        scrollPane.setVvalue((centerY)/3400);





        /*
        double scrollDiag = Math.sqrt(scrollWidth* scrollWidth + scrollHeight*scrollHeight);
        double newScale = scrollDiag/Math.sqrt((x1*x1 + y1*y1));    //get the new final scale of the

        //find values for centering centerpoint
        double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
        double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        double viewPortWidth = scrollPane.getViewportBounds().getWidth();
        double vValue = scrollPane.getVmax() * ((centerY - 0.5*viewportHeight)/(contentHeight - viewportHeight));
        double hValue = scrollPane.getHmax() * ((centerX - 0.5*viewPortWidth)/(contentWidth - viewPortWidth));

        if(newScale < 1.2) newScale = 1.2;
        if(newScale > 5) newScale = 5;

        //ScaleTransition st = new ScaleTransition(Duration.millis(1000),scrollPane.getTarget());
        //st.setToX(newScale);
        //st.setToY(newScale);

        //ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition();

        KeyValue kvY = new KeyValue(scrollPane.vvalueProperty(), vValue);
        KeyValue kvX = new KeyValue(scrollPane.hvalueProperty(), hValue);
        KeyValue xScale = new KeyValue(scrollPane.getTarget().scaleXProperty(),newScale);
        KeyValue yScale = new KeyValue(scrollPane.getTarget().scaleYProperty(),newScale);
        KeyFrame kf = new KeyFrame(Duration.millis(2000),xScale,yScale,kvY,kvX);
        Timeline t = new Timeline();
        t.getKeyFrames().add(kf);
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().add(t);
        pt.getChildren().add(st);

        pt.play();

*/
        }

    public void setScroll(double hValue, double vValue, double milliseconds){
        Timeline t = new Timeline();
        KeyValue kvY = new KeyValue(scrollPane.vvalueProperty(), vValue);
        KeyValue kvX = new KeyValue(scrollPane.hvalueProperty(), hValue);
        KeyFrame kf = new KeyFrame(Duration.millis(milliseconds),kvX,kvY);
        t.getKeyFrames().add(kf);
        t.play();
    }

    public Timeline setZoom(double scale, double x, double y, double milliseconds){
        System.out.println("Zooming to " + scale);
        ArrayList<KeyValue> kvs = scrollPane.getZoomValues(scale, new Point2D(x,y));
        KeyFrame kf = new KeyFrame(Duration.millis(milliseconds),null,null,kvs);
        Timeline t = new Timeline();
        t.getKeyFrames().add(kf);
        return t;
    }


}
