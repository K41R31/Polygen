package Polygen.Model.ImageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ImageFilter {

    private Mat processMat;

    public ImageFilter() {
    }

    public Mat processMat(Mat originalMat, ArrayList<Boolean> states, float alpha, float beta) {
        processMat = originalMat;
        if (states.get(0)) processMat = greyscale(processMat);
        if (states.get(1)) processMat = brightnessContrast(processMat, alpha, beta);
        return processMat;
    }

    private Mat showOriginalImage() {
        Mat original = null;
        return original;
    }

    private Mat greyscale(Mat inputMat) {
        Mat outputMat = new Mat();
        Imgproc.cvtColor(inputMat, outputMat, Imgproc.COLOR_BGR2GRAY);
        return outputMat;
    }

    private Mat brightnessContrast(Mat inputMat, float alpha, float beta) {
        Mat outputMat = new Mat(inputMat.rows(), inputMat.cols(), inputMat.channels());
        inputMat.convertTo(outputMat, -1, alpha, beta);
        return outputMat;
    }

    private Mat gaussianBlur(int kernelsize_min, int kernelsize_max) {
        Mat gauss = null;
        return gauss;
    }

    private Mat medianBlur(int kernelsize) {
        Mat median = null;
        return median;
    }

    private Mat billateralFilter(int kernelsize) {
        Mat billateral = null;
        return billateral;
    }

    private Mat sobelFilter() {
        Mat sobel = null;
        return sobel;
    }

    private Mat scharrFilter() {
        Mat scharr = null;
        return scharr;
    }

    private Mat cannyEdge() {
        Mat canny = null;
        return canny;
    }
}