package Polygen.Controller;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class IntroController implements Initializable {

    @FXML
    private ImageView imageView_titleBackground;
    @FXML
    private ImageView imageView_tiltleText;
    @FXML
    private ImageView imageView_tiltleLogo;
    @FXML
    private Button button_2D;
    @FXML
    private Button button_3D;

    private double windowWidth = Polygen.Root.screenSize.getWidth();
    private double windowHeight = Polygen.Root.screenSize.getHeight();

    public IntroController() {

        //Wartet darauf, dass das Programm sich zeigt. Wenn dies der Fall ist wird Animation() gestartet
        Polygen.Root.openerStage.addEventHandler(WindowEvent.WINDOW_SHOWN, window -> {
            Image image = new Image("Polygen/Resources/Intro/intro_background.png", windowWidth, windowHeight-40, false, true);
            imageView_titleBackground.setImage(image);
            animation();
        });
    }

    private void animation() {
        Timeline timelineAnimation = new Timeline();
        timelineAnimation.getKeyFrames().addAll(
                new KeyFrame(new Duration(4000), new KeyValue(imageView_tiltleText.opacityProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(imageView_tiltleLogo.opacityProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(button_2D.opacityProperty(), 0), new KeyValue(button_3D.opacityProperty(), 0)),
                new KeyFrame(new Duration(4300), new KeyValue(imageView_tiltleText.opacityProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(imageView_tiltleLogo.opacityProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(button_2D.opacityProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(button_3D.opacityProperty(), 1, Interpolator.EASE_BOTH))
        );
        timelineAnimation.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
