package Polygen.Controller;

import Polygen.Model.ImageProcessing.ImageFilter;
import Polygen.Model.ImageProcessing.ImageProcessing;
import Polygen.Model.Polygons.DetectionAlg;
import javafx.scene.layout.HBox;
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
    private Button button_addBlurFilter;
    @FXML
    private CheckBox checkBox_0;
    @FXML
    private CheckBox checkBox_1;
    @FXML
    private CheckBox checkBox_2;
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
    private AnchorPane AnchorPane_filterSelector;
    @FXML
    private VBox VBox_filterSelector;
    @FXML
    private HBox hBox_blurFilter;
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

    private boolean[] getAllStates() {
        boolean[] states = {false, false, false, false, false, false};
        states[0] = (checkBox_0.isSelected());
        states[1] = (checkBox_1.isSelected());
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
    private void selectBlurFilter() { //TODO + durch Auge ersetzen wenn Filter hinzugefügt wurde
        toggleFilterSelector(true, "BLUR FILTER");
        Text empty0 = new Text(); //Platzhalter0
        VBox_filterSelector.getChildren().add(empty0);
        String[] blurText = {"Gaussian Blur","Median Blur","Billateral Filter"};
        for(int i = 0; i < 3; i++) {
            Text t = new Text(blurText[i]);
            t.getStyleClass().add("text-blurSelector");
            t.setFill(Color.WHITE);
            int finalI = i;
            t.setOnMouseClicked(event -> {
                text_blurFilter.setText(blurText[finalI]);
                imageProcessing.setBlurFilter(finalI);
                button_addBlurFilter.setDisable(true);
                button_addBlurFilter.setVisible(false);
                hBox_blurFilter.setDisable(false);
                hBox_blurFilter.setVisible(true);
                closeFilterSelector();
            });
            VBox_filterSelector.getChildren().add(t);
        }
        Text empty1 = new Text(); //Platzhalter1
        VBox_filterSelector.getChildren().add(empty1);
        Text back = new Text("BACK");
        back.getStyleClass().add("text_filterBack");
        back.setFill(Color.web("#ff3535"));
        back.setOnMouseClicked(event -> {
            closeFilterSelector();
        });
        VBox_filterSelector.getChildren().add(back);
    }
    private void toggleFilterSelector(boolean toggle, String headingText) {
        if(toggle) {
            Text header = new Text("ADD A "+headingText);
            header.setFont(new Font("Walkway Bold",40));
            header.setFill(Color.WHITE);
            VBox_filterSelector.getChildren().add(header);
        }
        AnchorPane_filterSelector.setDisable(!toggle);
        AnchorPane_filterSelector.setVisible(toggle);
    }
    @FXML
    private void removeBlurFilter() {
        text_blurFilter.setText("");
        imageProcessing.setBlurFilter(-1);
        button_addBlurFilter.setDisable(false);
        button_addBlurFilter.setVisible(true);
        hBox_blurFilter.setDisable(true);
        hBox_blurFilter.setVisible(false);
    }
    @FXML
    private void updatePicture() {
        boolean[] states = {checkBox_0.isSelected(), checkBox_1.isSelected(), checkBox_2.isSelected()};
        imageProcessing.setStates(states);
        processView.setImage(imageProcessing.drawImage(5,5));
    }
    @FXML
    private void testAlgorithm() {
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", new DetectionAlg().getMat(imageProcessing.getOriginalMat(), imageProcessing.getProcessedImgMat(), 500), byteMat); //imgMat = Mat die gezeichnet werden soll
        processView.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
    }
    @FXML
    private void loadImage() {
        imageProcessing.loadImage();
        updatePicture();
    }
    @FXML
    private void closeFilterSelector() {
        toggleFilterSelector(false, null);
        VBox_filterSelector.getChildren().clear();
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
        try {
            imageProcessing = new ImageProcessing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}