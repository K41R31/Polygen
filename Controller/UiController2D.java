package Polygen.Controller;

import Polygen.Model.ImageProcessing.ImageProcessing;
import Polygen.Model.Polygons.DetectionAlg;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UiController2D implements Initializable {

    @FXML
    private Text text_title0;
    @FXML
    private Text text_title1;
    @FXML
    private Text text_title2;
    @FXML
    private Text text_greyscaleFilter;
    @FXML
    private Text text_brightnessFilter;
    @FXML
    private Text text_blurFilter;
    @FXML
    private Text text_edgeExtractionFilter0;
    @FXML
    private Text text_edgeExtractionFilter1;
    @FXML
    private Text text_edgeExtractionFilter2;
    @FXML
    private CheckBox button_0;
    @FXML
    private CheckBox button_1;
    @FXML
    private CheckBox button_2;
    @FXML
    private Button button_3;
    @FXML
    private Button button_4;
    @FXML
    private Button button_5;
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
    @FXML
    private VBox vBox_filterSelector;
    private ImageProcessing imageProcessing;


    private void resizeObjectsRelative() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double windowWidth = screenSize.getWidth();
        double windowHeight = screenSize.getHeight();
        anchorPane_uiMain.setPrefWidth(windowWidth);
        anchorPane_uiMain.setPrefHeight(windowHeight);
        imageView_logo.setFitWidth(windowWidth*0.021);
        imageView_logo.setFitHeight(windowHeight* 0.037);
        Font resizeTitleFont = new Font("Walkway Black", windowHeight*0.023);
        Font resizeFilterFont = new Font("Walkway SemiBold", windowHeight*0.028);
        text_title0.setFont(resizeTitleFont);
        text_title1.setFont(resizeTitleFont);
        text_title2.setFont(resizeTitleFont);
        text_greyscaleFilter.setFont(resizeFilterFont);
        text_brightnessFilter.setFont(resizeFilterFont);
        text_blurFilter.setFont(resizeFilterFont);
        text_edgeExtractionFilter0.setFont(resizeFilterFont);
        text_edgeExtractionFilter1.setFont(resizeFilterFont);
        text_edgeExtractionFilter2.setFont(resizeFilterFont);
    }

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
    private void selectBlurFilter() {
        vBox_filterSelector.setDisable(false);
        vBox_filterSelector.setVisible(true);
        anchorPane_uiMain.setEffect(new GaussianBlur(10)); //TODO add Blur Auswahlmöglichkeiten

    }
    @FXML
    private void updatePicture() {
        processView.setImage(imageProcessing.drawImage(getAllStates(), 2, 2));
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
        resizeObjectsRelative();
    }
}