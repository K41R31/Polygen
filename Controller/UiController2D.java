package Polygen.Controller;

import Polygen.Model.ImageProcessing.ImageProcessing;
import Polygen.Model.Polygons.DetectionAlg;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private StackPane pane_processWindow;
    @FXML
    private ImageView view_processView;
    @FXML
    private ImageView imageView_buttonGenPoly;
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
    private Button button_buttonGenPoly;
    @FXML
    private CheckBox checkBox_0;
    @FXML
    private CheckBox checkBox_1;
    @FXML
    private CheckBox checkBox_2;
    @FXML
    private Image image_genPoly;
    @FXML
    private Image image_genPolyHover;
    @FXML
    private Line line_close1;
    @FXML
    private Line line_close2;
    @FXML
    private Slider slider_alpha;
    @FXML
    private Slider slider_beta;
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
    private VBox vBox_preProcessingBackground;
    @FXML
    private VBox vBox_blurBackground;
    @FXML
    private VBox vBox_edgeExtractionBackground;
    @FXML
    private HBox hBox_blurFilter;
    @FXML
    private HBox pane_sliderPreProcessing;
    @FXML
    private HBox pane_sliderBlur;
    @FXML
    private HBox pane_sliderEdgeExtraction;

    private ImageProcessing imageProcessing;
    private Glow glow = new Glow();
    private ArrayList<HBox> list_sliderPanes = new ArrayList<>();
    private ArrayList<VBox> list_fiterBackgounds = new ArrayList<>();


    private void resizeObjectsRelative() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double windowWidth = screenSize.getWidth();
        double windowHeight = screenSize.getHeight();
        anchorPane_uiMain.setPrefWidth(windowWidth);
        anchorPane_uiMain.setPrefHeight(windowHeight);
        imageView_logo.setFitWidth(windowWidth*0.021);
        imageView_logo.setFitHeight(windowHeight* 0.037);
        Font resizeTitleFont = new Font("Walkway Black", windowHeight*0.0241);
        Font resizeFilterFont = new Font("Walkway SemiBold", windowHeight*0.0278);
        text_title0.setFont(resizeTitleFont);
        text_title1.setFont(resizeTitleFont);
        text_title2.setFont(resizeTitleFont);
        text_greyscaleFilter.setFont(resizeFilterFont);
        text_brightnessFilter.setFont(resizeFilterFont);
        text_blurFilter.setFont(resizeFilterFont);
        text_edgeExtractionFilter0.setFont(resizeFilterFont);
        text_edgeExtractionFilter1.setFont(resizeFilterFont);
        text_edgeExtractionFilter2.setFont(resizeFilterFont);
        double buttonGenWidth = windowWidth*0.1458;
        double buttonGenHeight = windowHeight*0.1134;
        image_genPoly = new Image("Polygen/resources/Ui/ui_generatePolysButton.png", buttonGenWidth, buttonGenHeight, false, false);
        image_genPolyHover = new Image("Polygen/resources/Ui/ui_generatePolysButtonHover.png");
        imageView_buttonGenPoly.setFitWidth(buttonGenWidth);
        imageView_buttonGenPoly.setFitHeight(buttonGenHeight);
        button_buttonGenPoly.setPrefWidth(buttonGenWidth);
        button_buttonGenPoly.setPrefHeight(buttonGenHeight);
    }

    private void updateStates() {
        boolean[] states = {checkBox_0.isSelected(), checkBox_1.isSelected(), checkBox_2.isSelected()};
        imageProcessing.setStates(states);
    }

    private void updateValues() {
        float[] values = {(float)slider_alpha.getValue(), (float)slider_beta.getValue()};
        imageProcessing.setValues(values);
    }

    @FXML
    private void selectBlurFilter() {
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
                button_buttonGenPoly.setDisable(false); //TODO Später beim hinzufügen eines Edge Detection Filters aktivieren
                closeFilterSelector();
                updatePicture();
            });
            VBox_filterSelector.getChildren().add(t);
        }
        Text empty1 = new Text(); //Platzhalter1
        VBox_filterSelector.getChildren().add(empty1);
        Text back = new Text("BACK");
        back.getStyleClass().add("text-filterBack");
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

    private void sliderManager(int activePane) {
        for(int i = 0; i < 3; i++) {
            if(i == activePane) {
                list_sliderPanes.get(i).setVisible(true);
                list_sliderPanes.get(i).setDisable(false);
                list_fiterBackgounds.get(i).setStyle("-fx-background-color: #3a3a3a");
                continue;
            }
            list_sliderPanes.get(i).setVisible(false);
            list_sliderPanes.get(i).setDisable(true);
            list_fiterBackgounds.get(i).setStyle("-fx-background-color: #2b2b2b");
        }
    }
    @FXML
    private void testAlgorithm() { //TODO Algorithmus anders einbinden
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", new DetectionAlg(imageProcessing.getOriginalMat(), imageProcessing.getProcessedImgMat(), 100).getMat(), byteMat); //imgMat = Mat die gezeichnet werden soll
        view_processView.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
    }
    @FXML
    private void removeBlurFilter() {
        text_blurFilter.setText("");
        imageProcessing.setBlurFilter(-1);
        button_addBlurFilter.setDisable(false);
        button_addBlurFilter.setVisible(true);
        hBox_blurFilter.setDisable(true);
        hBox_blurFilter.setVisible(false);
        updatePicture();
    }
    @FXML
    private void updatePicture() {
        updateStates();
        updateValues();
        view_processView.setImage(imageProcessing.drawImage());
    }
    @FXML
    private void loadImage() {
        imageProcessing.loadImage();
        view_processView.setFitWidth(pane_processWindow.getWidth());
        view_processView.setFitHeight(pane_processWindow.getHeight());
        updatePicture();
    }
    @FXML
    private void closeFilterSelector() {
        toggleFilterSelector(false, null);
        VBox_filterSelector.getChildren().clear();
    }
    @FXML
    private void buttonGenPolys_entered() { imageView_buttonGenPoly.setImage(image_genPolyHover); }
    @FXML
    private void buttonGenPolys_exited() { imageView_buttonGenPoly.setImage(image_genPoly); }
    @FXML
    private void buttonGenPolys_action() { testAlgorithm(); }
    @FXML
    private void title0_entered() {
        text_title0.setEffect(glow);
    }
    @FXML
    private void title0_exited() {
        text_title0.setEffect(null);
    }
    @FXML
    private void title0_active() {
        sliderManager(0);
    }
    @FXML
    private void title1_entered() { //text_title1
        text_title1.setEffect(glow);
    }
    @FXML
    private void title1_exited() { text_title1.setEffect(null); }
    @FXML
    private void title1_active() {
        sliderManager(1);
    }
    @FXML
    private void title2_entered() {
        text_title2.setEffect(glow);
    }
    @FXML
    private void title2_exited() {
        text_title2.setEffect(null);
    }
    @FXML
    private void title2_active() {
        sliderManager(2);
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
        glow.setLevel(0.5);
        resizeObjectsRelative();
        imageView_buttonGenPoly.setImage(image_genPoly);
        button_buttonGenPoly.setDisable(true);
        list_sliderPanes.add(pane_sliderPreProcessing);
        list_sliderPanes.add(pane_sliderBlur);
        list_sliderPanes.add(pane_sliderEdgeExtraction);
        list_fiterBackgounds.add(vBox_preProcessingBackground);
        list_fiterBackgounds.add(vBox_blurBackground);
        list_fiterBackgounds.add(vBox_edgeExtractionBackground);
        try {
            imageProcessing = new ImageProcessing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}