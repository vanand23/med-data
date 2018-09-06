package FXMLControllers;

import Animation.ScrollPaneAnimator;
import javafx.animation.KeyValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

import java.util.ArrayList;

public class ZoomableScrollPane extends ScrollPane {
   // private double scaleValue = 1;
    private double zoomIntensity = 0.02;
    private Node target;
    private Node zoomNode;
    private ScrollPaneAnimator scrollPaneAnimator = new ScrollPaneAnimator(this);
    private double screenPadding = 0;
    private Node outer;

    public ZoomableScrollPane(Node target) {
        super();
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));


        setPannable(true);
        //setFitToHeight(true);
        //setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHvalue(0.9);
        //setHmax(2.0);
        //setVmax(2.0);
        updateScale(1d);
    }

    public ScrollPaneAnimator getScrollPaneAnimator() {
        return scrollPaneAnimator;
    }
    public Node getTarget(){
        return this.target;
    }
    public Node getZoomNode(){
        return this.zoomNode;
    }
    public double getScale(){
        return  target.getScaleX();
    }

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getTextDeltaY(), new Point2D(e.getX() - screenPadding, e.getY() - screenPadding));
        });
        outerNode.setOnZoom(e-> {
        	e.consume();
        	onZoom(e.getZoomFactor(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(screenPadding, screenPadding,screenPadding,screenPadding));
        return vBox;
    }
    
    public void setScale(double scaleFactor, Point2D pivot) {
    	target.getTransforms().clear();
    	Scale scale = new Scale();
    	scale.setX(scaleFactor);
    	scale.setY(scaleFactor);
    	scale.setPivotX(pivot.getX());
    	scale.setPivotY(pivot.getY());
    	target.getTransforms().add(scale);
    }

    public boolean updateScale(double scale) {
    	if(scale < .7 || scale > 3.0) {
    		return false;
    	}
        target.setScaleX(scale);
        target.setScaleY(scale);
        this.layout();
        return true;
    }

    private void guaranteedUpdateScale(double scale){
        if(scale < .7){
            scale = 0.7;
        } else if(scale > 2.0) {
            scale = 2.0;
        }
        target.setScaleX(scale);
        target.setScaleY(scale);
    }

    public ArrayList<KeyValue> getZoomValues(double newScale, Point2D sourcePoint){
        if(newScale < 1.2){
            newScale = 1.2;
        }
        ArrayList<KeyValue> values = new ArrayList<>();
        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());
        double previousScaleValue = target.getScaleX();
        double tmpScaleValue = newScale;

        boolean res = updateScale(tmpScaleValue);
        this.layout();

        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(sourcePoint));

        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(newScale - 1));

        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        double hVal = (valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth());
        double vVal = (valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight());
        updateScale(previousScaleValue);

        values.add(new KeyValue(this.vvalueProperty(),vVal));
        values.add(new KeyValue(this.hvalueProperty(),hVal));
        values.add(new KeyValue(target.scaleXProperty(),tmpScaleValue));
        values.add(new KeyValue(target.scaleYProperty(),tmpScaleValue));
        return values;
    }

    public void setZoom(double zoomFactor){
        double prevHValue =this.getHvalue();
        double prevVValue = this.getVvalue();
        this.updateScale(zoomFactor);
        this.setHvalue(prevHValue);
        this.setVvalue(prevVValue);
        this.layout();
    }
    
    public void handleZoom(double zoomFactor, Point2D sourcePoint) {
        System.out.println("\n");
        System.out.println("Zooming on " + sourcePoint);

        Bounds innerBounds = zoomNode.getLayoutBounds();
        System.out.println("innerBounds " +innerBounds);
        Bounds viewportBounds = getViewportBounds();
        System.out.println("viewPortBounds " +viewportBounds);

        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double previousScaleValue = target.getScaleX();
        double tmpScaleValue = target.getScaleX() * zoomFactor;
        
        boolean res = updateScale(tmpScaleValue);
        if(!res)
        	return;

        this.layout(); 

        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(sourcePoint));
        System.out.println("PosInZoomTarget: " + posInZoomTarget);

        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        updateScale(previousScaleValue);
        updateScale(tmpScaleValue);

        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }

    public void twoPointZoom(double zoomFactor, Point2D sourcePoint) {
        Bounds innerBounds = zoomNode.getLayoutBounds(); //Current resolution (dimension) of the inner map
        System.out.println("innerBounds " +innerBounds);
        Bounds viewportBounds = getViewportBounds();    //Returns the scrollPane bounds, always 1918 by 1078
        System.out.println("viewPortBounds " +viewportBounds);

        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double tmpScaleValue = target.getScaleX() * zoomFactor;



        this.layout();
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(sourcePoint));
        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        System.out.println("PosInZoomTarget: " + posInZoomTarget);

        guaranteedUpdateScale(tmpScaleValue);
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal(); //Resolution (dimension) of the inner map after zooming
        System.out.println("updatedInnerBounds " +updatedInnerBounds);




        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        System.out.println((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        System.out.println((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));


    }


    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);
        handleZoom(zoomFactor, mousePoint);
    }
    
    private void onZoom(double zoom, Point2D touchPoint) {
    	handleZoom(zoom, touchPoint);
    }
}