package Polygen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class Root extends Application {

    static public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        Root.primaryStage = primaryStage;
        opener();
    }
    private void opener() {
        Stage openerStage = new Stage();
        HBox root = new HBox();
        Button btn2D = new Button();
        Button btn3D = new Button();
        btn2D.setPrefSize(250,290);
        btn3D.setPrefSize(250,290);
        Scene openerScene = new Scene(root, 900, 800);
        openerScene.getStylesheets().add(getClass().getResource("View/css/styleOpener.css").toExternalForm());
        root.getStyleClass().add("root-pane");
        btn2D.getStyleClass().add("button-2D");
        btn3D.getStyleClass().add("button-3D");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().add(btn2D);
        root.getChildren().add(btn3D);
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
    }

    //Penis

    private void createMainView(Parent root) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setTitle("Polygen");
        primaryStage.setWidth(screenSize.getWidth());
        primaryStage.setHeight(screenSize.getHeight() - 40);
        Scene primaryScene = new Scene(root, Color.TRANSPARENT);
        primaryScene.getStylesheets().add((getClass().getResource("View/css/styleUi.css")).toExternalForm());
        primaryStage.setScene(primaryScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}