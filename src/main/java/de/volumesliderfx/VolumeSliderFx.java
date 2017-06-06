package de.volumesliderfx;

import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class VolumeSliderFx {

    private static final Color BACKGROUND_COLOR = Color.web("#CBC8CE");
    public final SimpleDoubleProperty volume = new SimpleDoubleProperty();
    private VolumeIcon volumeIcon;

    public Node createContent(){
        volumeIcon = new VolumeIcon(BACKGROUND_COLOR);

        Group volumeSlider = new Group();
        volumeSlider.setTranslateX(5);
        volumeSlider.setTranslateY(50);
        volumeSlider.getChildren().add(volumeIcon.getNode());

        Rectangle track = new Rectangle();
        track.setFill(BACKGROUND_COLOR);
        track.setX(40);
        track.setWidth(125);
        track.setHeight(4);
        track.setArcWidth(3);
        track.setArcHeight(3);
        volumeSlider.getChildren().add(track);
        double trackMin=track.getX();
        double trackMax=track.getX()+track.getWidth();

        Circle ball = new Circle(5);
        ball.setTranslateX((trackMax-trackMin)*volume.get()+trackMin);
        ball.setTranslateY(2.4);
        ball.setFill(Color.web("#6e33a5"));
        volumeSlider.getChildren().add(ball);


        Timeline volumeTimeline = new Timeline();
        volumeTimeline.getKeyFrames().add(new KeyFrame(volumeIcon.getDuartion(), new KeyValue(volume, 1)));

        volumeIcon.addMouseHandling(()->{
            volume.set(0);
            volumeTimeline.playFromStart();
        }, ()->{
            volumeTimeline.stop();
            playThrowTransition(ball, trackMin, trackMax,volume.get());
        });

        Group result = new Group();
        Rectangle background = new Rectangle(1, 60);//to get a constant boundingbox
        background.setFill(Color.TRANSPARENT);
        result.getChildren().add(background);

        result.getChildren().add(volumeSlider);
        return result;
    }

    public void playThrowTransition(Circle ball, double trackMin, double trackMax, double volume){
        Path path = new Path();
        path.getElements().add (new MoveTo (0f, 0f));
        double trackWidth=trackMax-trackMin;
        double xTarget=trackWidth*volume+trackMin;

        float maxHeight = -50f;
        path.getElements().add(new CubicCurveTo(xTarget/3, maxHeight *volume, (xTarget/3)*2, maxHeight *volume, xTarget, ball.getTranslateY()));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(200+400*volume));
        pathTransition.setNode(ball);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }
}
