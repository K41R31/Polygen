package Polygen.Controller;

import Polygen.Model.ImageProcessing.ImageProcessing;
import Polygen.Model.Polygons.DetectionAlg;
import Polygen.Model.LastOpenedFiles;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UiController2D implements Initializable {

    @FXML
    private SplitPane splitPane_paneSize;
    @FXML
    private StackPane pane_processWindow;
    @FXML
    private ImageView view_processView;
    @FXML
    private ImageView imageView_buttonGenPoly;
    @FXML
    private Text text_zoomText;
    @FXML
    private Text text_zoomFator;
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
    private Text text_edgeExtraction0;
    @FXML
    private Text text_edgeExtraction1;
    @FXML
    private Text text_edgeExtraction2;
    @FXML
    private Button button_addBlurFilter;
    @FXML
    private Button button_buttonGenPoly;
    @FXML
    private Button button_addEdgeExtraction0;
    @FXML
    private Button button_addEdgeExtraction1;
    @FXML
    private Button button_addEdgeExtraction2;
    @FXML
    private CheckBox checkBox_0;
    @FXML
    private CheckBox checkBox_1;
    @FXML
    private CheckBox checkBox_2;
    @FXML
    private CheckBox checkBox_3;
    @FXML
    private CheckBox checkBox_4;
    @FXML
    private CheckBox checkBox_5;
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
    private Slider slider_kernelsize;
    @FXML
    private Slider slider_lowThreshold;
    @FXML
    private Slider slider_ratio;
    @FXML
    private Slider slider_scale0;
    @FXML
    private Slider slider_delta0;
    @FXML
    private Slider slider_scale1;
    @FXML
    private Slider slider_delta1;
    @FXML
    private AnchorPane anchorPane_uiMain;
    @FXML
    private ImageView imageView_logo;
    @FXML
    private Rectangle rectangle_closeBackg;
    @FXML
    private AnchorPane AnchorPane_filterSelector;
    @FXML
    private VBox vBox_fileChooser;
    @FXML
    private VBox VBox_filterSelector;
    @FXML
    private VBox vBox_preProcessingBackground;
    @FXML
    private VBox vBox_blurBackground;
    @FXML
    private VBox vBox_edgeExtractionBackground;
    @FXML
    private HBox hBox_zoomTextContainer;
    @FXML
    private HBox hBox_blurFilter;
    @FXML
    private HBox pane_sliderPreProcessing;
    @FXML
    private HBox pane_sliderBlur;
    @FXML
    private HBox pane_sliderEdgeExtraction;
    @FXML
    private HBox hBox_edgeExtraction0;
    @FXML
    private HBox hBox_edgeExtraction1;
    @FXML
    private HBox hBox_edgeExtraction2;

    private File quickLoadFile;
    private LastOpenedFiles lastOpenedFiles;
    private ImageProcessing imageProcessing;
    private Glow glow = new Glow();
    private ArrayList<HBox> list_sliderPanes = new ArrayList<>();
    private ArrayList<VBox> list_fiterBackgounds = new ArrayList<>();
    private ArrayList<ImageView> list_queueImages = new ArrayList<>();
    private double windowWidth = Polygen.Root.screenSize.getWidth();
    private double windowHeight = Polygen.Root.screenSize.getHeight();
    private int int_dataQueueController = 0;


    private void resizeObjectsRelative() {
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
        text_edgeExtraction0.setFont(resizeFilterFont);
        text_edgeExtraction1.setFont(resizeFilterFont);
        text_edgeExtraction2.setFont(resizeFilterFont);
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
        boolean[] states = {
                checkBox_0.isSelected(),
                checkBox_1.isSelected(),
                checkBox_2.isSelected(),
                checkBox_3.isSelected(),
                checkBox_4.isSelected(),
                checkBox_5.isSelected()
        };
        imageProcessing.setStates(states);
    }

    private void updateValues() {
        int kernel = (int)Math.round(slider_kernelsize.getValue());
        if (kernel % 2 == 0) kernel++;
        float[] values = {
                (float)slider_alpha.getValue(),
                (float)slider_beta.getValue(),
                kernel,
                (float)slider_lowThreshold.getValue(),
                (float)slider_ratio.getValue(),
                (float)slider_scale0.getValue(),
                (float)slider_delta0.getValue(),
                (float)slider_scale1.getValue(),
                (float)slider_delta1.getValue(),
        };
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

    private void initializeDataQueue() {  //TODO Möglichkeit einzelne Images raus zu löschen
        ArrayList<String> files = lastOpenedFiles.getOpenedFiles();
        if (files.size() == 0) {
            Text text = new Text("No recently opened files");
            text.setFont(new Font("Walkway Bold",windowWidth*0.0104));
            vBox_fileChooser.getChildren().add(0, text);
        } else {
            for (String fileString : files) {
                if (new File(fileString).exists()) {
                    Image image = new Image(new File(fileString).toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setOnMousePressed(e -> {
                        quickLoadFile = new File(fileString);
                        loadImage();
                    });
                    imageView.setFitWidth(windowWidth*0.0781 * 1.5);
                    imageView.setFitHeight(windowWidth*0.0781);
                    imageView.setViewport(new Rectangle2D((image.getWidth() - image.getHeight() * 1.5) / 2, 0, image.getHeight() * 1.5, image.getHeight()));
                    imageView.setEffect(new DropShadow(windowWidth*0.0156, Color.BLACK));
                    imageView.setOnMouseEntered(e -> imageView.setEffect(new Glow()));
                    imageView.setOnMouseExited(e -> imageView.setEffect(new DropShadow(windowWidth*0.0156, Color.BLACK)));
                    imageView.getStyleClass().add("images-Queue");
                    list_queueImages.add(imageView);
                }
                else {
                    try {
                        lastOpenedFiles.deleteStringInFile(fileString);
                    } catch (IOException e) {
                        System.out.println("ERROR");
                    }
                }
            }
        }
       updateDataQueue();
    }

    private void addEdgeExtraction(int pane) {
        toggleFilterSelector(true, "EDGE EXTRACTION");
        Text empty0 = new Text(); //Platzhalter0
        VBox_filterSelector.getChildren().add(empty0);
        String[] edgeText = {"Canny","Scharr Filter","Sobel Filter"};
        for(int i = 0; i < 3; i++) {
            Text t = new Text(edgeText[i]);
            t.getStyleClass().add("text-blurSelector");
            t.setFill(Color.WHITE);
            int finalI = i;
            t.setOnMouseClicked(event -> {
                switch (pane) {
                    case 0: text_edgeExtraction0.setText(edgeText[finalI]);
                            break;
                    case 1: text_edgeExtraction1.setText(edgeText[finalI]);
                            break;
                    case 2: text_edgeExtraction2.setText(edgeText[finalI]);
                }
                switch (pane) {
                    case 0: button_addEdgeExtraction0.setDisable(true);
                            button_addEdgeExtraction0.setVisible(false);
                            hBox_edgeExtraction0.setDisable(false);
                            hBox_edgeExtraction0.setVisible(true);
                            button_buttonGenPoly.setDisable(false);
                            imageProcessing.setEdgeExtraction0(finalI);
                            closeFilterSelector();
                            updatePicture();
                            break;
                    case 1: button_addEdgeExtraction1.setDisable(true);
                            button_addEdgeExtraction1.setVisible(false);
                            hBox_edgeExtraction1.setDisable(false);
                            hBox_edgeExtraction1.setVisible(true);
                            button_buttonGenPoly.setDisable(false);
                            imageProcessing.setEdgeExtraction1(finalI);
                            closeFilterSelector();
                            updatePicture();
                            break;
                    case 2: button_addEdgeExtraction2.setDisable(true);
                            button_addEdgeExtraction2.setVisible(false);
                            hBox_edgeExtraction2.setDisable(false);
                            hBox_edgeExtraction2.setVisible(true);
                            button_buttonGenPoly.setDisable(false);
                            imageProcessing.setEdgeExtraction2(finalI);
                            closeFilterSelector();
                            updatePicture();
                }

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

    private void updateDataQueue() {
        vBox_fileChooser.getChildren().clear();
        if (list_queueImages.size() > 0) {
            for (int i = int_dataQueueController; i < int_dataQueueController +4 && i < int_dataQueueController +list_queueImages.size(); i++) {
                vBox_fileChooser.getChildren().add(0, list_queueImages.get(i));
            }
        }
        if (list_queueImages.size() > 4) {
            //Button_up
            Button button_up = new Button();
            button_up.setPrefSize(windowWidth*0.0270, 0.0125);
            button_up.getStyleClass().add("button-arrowChooser");
            button_up.setOnAction(e -> {
                if (int_dataQueueController+4 < list_queueImages.size()) {
                    int_dataQueueController++;
                    updateDataQueue();
                }
            });
            if (int_dataQueueController+4 == list_queueImages.size()) button_up.setVisible(false);
            //Button_Down
            Button button_down = new Button();
            button_down.setPrefSize(windowWidth*0.0270, 0.0125);
            button_down.setRotate(180);
            button_down.getStyleClass().add("button-arrowChooser");
            button_down.setOnAction(e -> {
                if (int_dataQueueController > 0) {
                    int_dataQueueController--;
                    updateDataQueue();
                }
            });
            if (int_dataQueueController == 0) button_down.setVisible(false);
            vBox_fileChooser.getChildren().add(0, button_up);
            vBox_fileChooser.getChildren().add(5, button_down);
        }
    }

    @FXML
    private void testAlgorithm() { //TODO Algorithmus anders einbinden
        MatOfByte byteMat = new MatOfByte();
        DetectionAlg detecttionAlg = new DetectionAlg(imageProcessing.getOriginalMat(), imageProcessing.getProcessedImgMat(), 100);
        Imgcodecs.imencode(".png", detecttionAlg.getMask() , byteMat); //imgMat = Mat die gezeichnet werden soll
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
        imageProcessing.loadImage(quickLoadFile);
        quickLoadFile = null;
        lastOpenedFiles.setOpenedFiles(imageProcessing.getFile().toString());
        view_processView.setFitWidth(pane_processWindow.getWidth());
        view_processView.setFitHeight(pane_processWindow.getHeight());
        text_zoomText.setFont(new Font("Walkway Bold", (hBox_zoomTextContainer.getHeight()*0.6)-1)); //Schriftgröße relativ zur Fenstergröße
        text_zoomFator.setFont(new Font("Walkway Bold", hBox_zoomTextContainer.getHeight()*0.6)); //Schriftgröße relativ zur Fenstergröße
        text_zoomText.setVisible(true);
        text_zoomFator.setVisible(true);
        vBox_fileChooser.setVisible(false);
        vBox_fileChooser.setDisable(true);
        splitPane_paneSize.setVisible(true);
        splitPane_paneSize.setDisable(false);
        text_zoomFator.setText("100%");
        updatePicture();
        new Polygen.Model.ZoomHandler(view_processView);
    }
    @FXML
    private void closeFilterSelector() {
        toggleFilterSelector(false, null);
        VBox_filterSelector.getChildren().clear();
    }
    @FXML
    private void selectEdgeExtraction0() { addEdgeExtraction(0); }
    @FXML
    private void selectEdgeExtraction1() { addEdgeExtraction(1); }
    @FXML
    private void selectEdgeExtraction2() { addEdgeExtraction(2); }
    @FXML
    private void removeEdgeExtraction0() {}
    @FXML
    private void removeEdgeExtraction1() {}
    @FXML
    private void removeEdgeExtraction2() {}
    @FXML
    private void textZoom_action() { System.out.println("ZoomFaktorauswahl anzeigen"); }
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
            lastOpenedFiles = new LastOpenedFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeDataQueue();
    }
}
