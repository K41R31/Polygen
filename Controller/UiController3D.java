package Polygen.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.ResourceBundle;

public class UiController3D implements Initializable {

    @FXML
    private Rectangle rectangle_closeBackg;
    @FXML
    private Line line_close1;
    @FXML
    private Line line_close2;
    @FXML
    private StackPane stackPane_renderer;


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
        new Polygen.Model.ThreeDProcessing.StartCodeMainWindowPP();
    }
}