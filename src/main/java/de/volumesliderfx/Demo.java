package de.volumesliderfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sun.misc.Launcher;

public class Demo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        primaryStage.setScene(new Scene(root,300,200, Color.WHITE));
        primaryStage.show();
        VolumeSliderFx volumeSliderFx = new VolumeSliderFx();
        HBox hBox = new HBox();
        hBox.getChildren().add(volumeSliderFx.createContent());
        root.getChildren().add(hBox);
        primaryStage.titleProperty().bind(volumeSliderFx.volume.asString());
    }

    public static void main(String [] args){
        Application.launch();
    }
}
