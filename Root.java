package Polygen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class Root extends Application {

    private final int playIntro = 3; //1 = intro; 2 = 2D; 3 = 3D

    static public Stage openerStage;
    static public Stage primaryStage;
    public static Dimension screenSize;

    @Override
    public void start(Stage primaryStage) {
        Root.primaryStage = primaryStage;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            opener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void opener() throws IOException {
        if (playIntro == 1) {
            openerStage = new Stage();
            openerStage.setWidth(screenSize.getWidth());
            openerStage.setHeight(screenSize.getHeight()-40);
            AnchorPane root = FXMLLoader.load(getClass().getResource("View/Intro.fxml"));
            StackPane stackPane = new StackPane();
            root.getChildren().add(stackPane);
            AnchorPane.setBottomAnchor(stackPane, 0d);
            AnchorPane.setTopAnchor(stackPane, 0d);
            AnchorPane.setLeftAnchor(stackPane, 0d);
            AnchorPane.setRightAnchor(stackPane, 0d);
            stackPane.setAlignment(Pos.CENTER);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(100);
            stackPane.getChildren().add(hBox);
            Scene openerScene = new Scene(root);
            openerScene.getStylesheets().add(getClass().getResource("View/css/styleIntro.css").toExternalForm());
            Button btn2D = new Button();
            Button btn3D = new Button();
            btn2D.setText("");
            btn3D.setText("");
            btn2D.setStyle("-fx-background-color: TRANSPARENT");
            btn3D.setStyle("-fx-background-color: TRANSPARENT");
            btn2D.setPrefSize(350,350);
            btn3D.setPrefSize(350,350);
            hBox.getChildren().add(btn2D);
            hBox.getChildren().add(btn3D);
            btn2D.setOnAction(event -> {
                openerStage.close();
                try {
                    createMainView(FXMLLoader.load(getClass().getResource("View/ui2D.fxml")));
                } catch (IOException e) { e.printStackTrace(); }
            });
            btn3D.setOnAction(event -> {
                openerStage.close();
                try {
                    createMainView(FXMLLoader.load(getClass().getResource("View/ui3D.fxml")));
                } catch (IOException e) { e.printStackTrace(); }
            });
            openerStage.setScene(openerScene);
            openerStage.initStyle(StageStyle.TRANSPARENT);
            openerStage.show();
        } else if (playIntro == 2){
            try {
                createMainView(FXMLLoader.load(getClass().getResource("View/ui2D.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (playIntro == 3){
            try {
                createMainView(FXMLLoader.load(getClass().getResource("View/ui3D.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMainView(Parent root) {
        primaryStage.setTitle("Polygen");
        primaryStage.setWidth(screenSize.getWidth());
        primaryStage.setHeight(screenSize.getHeight() - 40);
        Scene primaryScene = new Scene(root, Color.TRANSPARENT);
        primaryScene.getStylesheets().add(getClass().getResource("View/css/styleUi.css").toExternalForm());
        primaryStage.setScene(primaryScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
