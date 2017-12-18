package Polygen.Controller;

import Polygen.Root;
import Polygen.Model.ImageProcessing.ImageProcessing;
import Polygen.Model.Polygons.DetectionAlg;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UiController3D implements Initializable {

    @FXML
    private CheckBox checkBox_0;
    @FXML
    private CheckBox checkBox_1;
    @FXML
    private ImageView processView;
    @FXML
    private Line line_close1;
    @FXML
    private Line line_close2;
    @FXML
    private AnchorPane anchorPane_uiMain;
    @FXML
    private ImageView imageView_logo;
    @FXML
    private Rectangle rectangle_closeBackg;
    private ImageProcessing imageProcessing;

/*
    private void resizeObjectsRelative() {
        double windowWidth = Root.primaryStage.getWidth();
        double windowHeight = Root.primaryStage.getHeight();
        anchorPane_uiMain.setPrefWidth(windowWidth);
        anchorPane_uiMain.setPrefHeight(windowHeight);
        imageView_logo.setFitWidth(windowWidth*0.021);
        imageView_logo.setFitHeight(windowHeight* 0.037);
    }
*/
    private ArrayList<Boolean> getAllStates() {
        ArrayList<Boolean> states = new ArrayList<>();
        states.add(checkBox_0.isSelected());
        states.add(checkBox_1.isSelected());
        return states;
    }
    /*
        private ArrayList<Float> getAllValues() {
            ArrayList<Float> states = new ArrayList<>();
            states.add();
            states.add();
            return states;
        }
    */
    @FXML
    private void updatePicture() {
        processView.setImage(imageProcessing.drawImage());
    }
    @FXML
    private void testAlgorithm() {
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", new DetectionAlg().getMat(imageProcessing.getOriginalMat(), imageProcessing.getProcessedImgMat(), 500), byteMat); //imgMat = Mat die gezeichnet werden soll
        processView.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
    }
    @FXML
    private void loadImage() {
        try {
            imageProcessing = new ImageProcessing();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatePicture();
    }
    @FXML
    private void btnClose_entered() {
        rectangle_closeBackg.setFill(Color.web("#c43838"));
        line_close1.setStroke(Color.WHITE);
        line_close2.setStroke(Color.WHITE);
    }
    @FXML
    private void btnClose_exited() {
        rectangle_closeBackg.setFill(Color.web("#c4383800"));
        line_close1.setStroke(Color.BLACK);
        line_close2.setStroke(Color.BLACK);
    }
    @FXML
    private void btnClose_action() {
        System.exit(0);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //resizeObjectsRelative();
    }
}