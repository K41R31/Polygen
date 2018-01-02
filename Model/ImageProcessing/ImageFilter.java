package Polygen.Model.ImageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class ImageFilter {

    private boolean[] states; //Der Zustand der einzelnen Filter (Augen)
    private int blurFilter = -1; //Der Ausgewählter Blur Filter (-1 wenn keiner ausgewählt wurde)
    private float[] values; //Die Werte der Filter

    public ImageFilter() {
    }

    public Mat processMat(Mat originalMat) {
        Mat processMat = originalMat;
        if (states[0]) processMat = greyscale(processMat);
        if (states[1]) processMat = brightnessContrast(processMat, values[0], values[1]);
        if (states[2]) {
            if (blurFilter == 0) processMat = gaussianBlur(processMat, values[2]);
            else if (blurFilter == 1) processMat = medianBlur(processMat, values[2]);
            else if (blurFilter == 2) processMat = billateralFilter(processMat, values[2]);
        }
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

    private Mat gaussianBlur(Mat inputMat, float kernelsize) {
        Mat outputMat = new Mat();
        Imgproc.GaussianBlur(inputMat, outputMat, new Size(kernelsize, kernelsize), kernelsize, kernelsize);
        return outputMat;
    }

    private Mat medianBlur(Mat inputMat, float medianKernel) {
        Mat outputMat = new Mat();
        Imgproc.medianBlur(inputMat, outputMat, (int)medianKernel);
        return outputMat;
    }

    private Mat billateralFilter(Mat inputMat, float billKernel) {
        Mat outputMat = new Mat();
        Imgproc.bilateralFilter(inputMat, outputMat, (int)billKernel, billKernel * 2, billKernel / 2);
        return outputMat;
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

    void setValues(float[] values) { this.values = values; }
    void setBlurFilter(int blurFilter) { this.blurFilter = blurFilter; }
    void setStates(boolean[] states) { this.states = states; }
}