package de.volumesliderfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class VolumeIcon {
    public static final String SVG_ICON = "M22.485 25.985c-0.384 0-0.768-0.146-1.061-0.439-0.586-0.586-0.586-1.535 0-2.121 4.094-4.094 4.094-10.755 0-14.849-0.586-0.586-0.586-1.536 0-2.121s1.536-0.586 2.121 0c2.55 2.55 3.954 5.94 3.954 9.546s-1.404 6.996-3.954 9.546c-0.293 0.293-0.677 0.439-1.061 0.439v0zM17.157 23.157c-0.384 0-0.768-0.146-1.061-0.439-0.586-0.586-0.586-1.535 0-2.121 2.534-2.534 2.534-6.658 0-9.192-0.586-0.586-0.586-1.536 0-2.121s1.535-0.586 2.121 0c3.704 3.704 3.704 9.731 0 13.435-0.293 0.293-0.677 0.439-1.061 0.439zM13 30c-0.26 0-0.516-0.102-0.707-0.293l-7.707-7.707h-3.586c-0.552 0-1-0.448-1-1v-10c0-0.552 0.448-1 1-1h3.586l7.707-7.707c0.286-0.286 0.716-0.372 1.090-0.217s0.617 0.519 0.617 0.924v26c0 0.404-0.244 0.769-0.617 0.924-0.124 0.051-0.254 0.076-0.383 0.076z";

    private final Color backgroundColor;
    private final Timeline fillAnimation;
    private Circle circle;
    private SVGPath background;
    private final SVGPath clip;
    private RotateTransition rotateTransition;
    private final Duration duration=Duration.millis(2000);
    private final Group iconGroup = new Group();

    public VolumeIcon(Color backgroundColor){
        this.backgroundColor=backgroundColor;

        clip = new SVGPath();
        clip.setFill(Color.WHITE);
        clip.setContent(SVG_ICON);
        double height = clip.getBoundsInLocal().getHeight();
        double width = clip.getBoundsInLocal().getWidth();
        clip.setTranslateY(-height /2);


        circle = new Circle(0);
        RadialGradient gradient = new RadialGradient(0,
                .1,
                0,
                0,
                20,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, backgroundColor),
                new Stop(1, Color.web("#6e33a5")));
        circle.setFill( gradient);
        circle.setClip(clip);

        fillAnimation = new Timeline();
        KeyValue radiusStart = new KeyValue(circle.radiusProperty(), 0);

        KeyValue radiusEnd = new KeyValue(circle.radiusProperty(), width);
        KeyFrame keyFrame = new KeyFrame(duration, radiusStart, radiusEnd);
        fillAnimation.getKeyFrames().add(keyFrame);

        background = new SVGPath();
        background.setFill(backgroundColor);
        background.setContent(SVG_ICON);
        background.setTranslateY(-height /2);


        iconGroup.getChildren().add(background);
        movePivot(iconGroup,-width/2,0);
        rotateTransition = new RotateTransition(duration, iconGroup);
        rotateTransition.setToAngle(-45);
        iconGroup.getChildren().add(circle);
    }

    public Group getNode(){

        return iconGroup;
    }

    private void movePivot(Node node, double x, double y){
        node.getTransforms().add(new Translate(-x,-y));
        node.setTranslateX(x); node.setTranslateY(y);
    }

    public void startFill(){
        circle.setRadius(0);
        fillAnimation.playFromStart();
    }

    public void stopFill(){
        fillAnimation.stop();
        circle.setRadius(0);
    }

    public void highLight() {
        background.setFill(Color.web("#B6AEBF"));
    }

    public void unHighLight() {
        background.setFill(backgroundColor);
    }

    public Duration getDuartion(){
        return duration;
    }

    public void startRotate() {
        iconGroup.setRotate(0);
        rotateTransition.playFromStart();
    }

    public void stopRotate() {
        rotateTransition.stop();
        RotateTransition deRotateTransition = new RotateTransition(Duration.millis(200), iconGroup);
        deRotateTransition.setToAngle(0);
        deRotateTransition.play();
    }

    public void addMouseHandling(Runnable pressedAction, Runnable releaseAction) {
        iconGroup.setOnMousePressed(event -> {
            startFill();
            startRotate();
            pressedAction.run();
        });
        iconGroup.setOnMouseReleased(event -> {
            stopFill();
            stopRotate();
            releaseAction.run();
        });
        iconGroup.setCursor(Cursor.HAND);

        iconGroup.setOnMouseEntered(event -> {
            if (!event.isPrimaryButtonDown()){
                highLight();
            }
        });
        iconGroup.setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()){
                unHighLight();
            }
        });
    }
}
