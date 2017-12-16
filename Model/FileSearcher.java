package Polygen.Model;

import java.io.File;

import Polygen.Root;
import javafx.stage.FileChooser;

public class FileSearcher {

    public File openFile() throws Exception {

        final FileChooser fileChooser = new FileChooser();
        configuringFileChooser(fileChooser);
        //Das Fenster was bei ownerWindow angegeben ist, wird solange der FileChooser offen ist disabled (primaryStage)
        return fileChooser.showOpenDialog(Root.primaryStage);
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select a picture");

        // Set Initial Directory
        fileChooser.setInitialDirectory(new File("C:/Users"));

        // Add Extension Filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
    }
}