package Polygen.Model.ImageProcessing;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;


public class ImageProcessing {

    private Polygen.Model.FileSearcher fileSearcher = new Polygen.Model.FileSearcher();
    private ImageFilter imageFilter = new ImageFilter();
    private Mat originalMat;
    private Mat processedImgMat;


	public ImageProcessing() throws Exception {

	    System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        originalMat = loadImage();

	}

	private Mat loadImage() throws Exception {
        Mat imageMat;
        File file = fileSearcher.openFile();
        if (file == null) return null;
        else {
            imageMat = Imgcodecs.imread(file.toString());
            if (imageMat.empty() && file.toString() != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Loading File");
                alert.setHeaderText("Could not load the image");
                alert.setContentText(file.toString().substring(file.toString().lastIndexOf("\\") + 1) + " is not a supported file"); //Windows Path muss an alle Betriebsysteme angepasst werden
                alert.showAndWait();
                loadImage(); //Wenn eine nicht lesbare Datei geöffnet wurde, öffnet sich die Methode immer wieder selber
            }
        }
        return imageMat;
	}

	private void writeImage(Mat imgMat, String filename) {
		String filePathName = "Resources/" + filename;
	    Imgcodecs.imwrite(filePathName, imgMat,
	    		new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY,
	    					 Imgcodecs.CV_IMWRITE_PNG_STRATEGY_FIXED));
	}

    public Image drawImage(ArrayList states, float alpha, float beta) { //wird in UiController von EventHandlern ausgeführt (Variablen sollen nicht einzelnd übergeben werden)
        Mat processedImgMat = imageFilter.processMat(originalMat, states, alpha, beta);
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", processedImgMat, byteMat); //imgMat = Mat die gezeichnet werden soll
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public Mat getOriginalMat() { return originalMat; }
    public Mat getProcessedImgMat() { return processedImgMat; }
}