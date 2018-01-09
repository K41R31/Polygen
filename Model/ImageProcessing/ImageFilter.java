package Polygen.Model.ImageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.BORDER_DEFAULT;

public class ImageFilter {

    private boolean[] states; //Der Zustand der einzelnen Filter (Augen)
    private int blurFilter = -1; //Der Ausgewählter Blur Filter (-1 wenn keiner ausgewählt wurde)
    private int edgeExtraction0 = -1;
    private int edgeExtraction1 = -1;
    private int edgeExtraction2 = -1;
    private float[] values; //Die Werte der Filter

    public ImageFilter() {
    }

    public Mat processMat(Mat originalMat) {
        Mat processMat = originalMat;
        if (states[0]) processMat = greyscale(processMat);
        if (states[1]) processMat = brightnessContrast(processMat, values[0], values[1]);
        if (states[2]) {
            switch (blurFilter) {
                case 0: processMat = gaussianBlur(processMat, values[2]);
                        break;
                case 1: processMat = medianBlur(processMat, values[2]);
                        break;
                case 2: processMat = billateralFilter(processMat, values[2]);
            }
        }
        if (states[3]) {
            switch (edgeExtraction0) {
                case 0: processMat = cannyEdge(processMat, values[3], values[4]);
                        break;
                case 1: processMat = sobelFilter(processMat, values[5], values[6]);
                        break;
                case 2: processMat = scharrFilter(processMat, values[7], values[8]);
            }
            if (states[4]) {
                switch (edgeExtraction1) {
                    case 0: processMat = cannyEdge(processMat, values[3], values[4]);
                        break;
                    case 1: processMat = sobelFilter(processMat, values[5], values[6]);
                        break;
                    case 2: processMat = scharrFilter(processMat, values[7], values[8]);
                }
                if (states[5]) {
                    switch (edgeExtraction2) {
                        case 0: processMat = cannyEdge(processMat, values[3], values[4]);
                            break;
                        case 1: processMat = sobelFilter(processMat, values[5], values[6]);
                            break;
                        case 2: processMat = scharrFilter(processMat, values[7], values[8]);
                    }
                }
            }
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

    private Mat cannyEdge(Mat inputMat, float lowThreshold, float ratio) {
        Mat outputMat = new Mat();
        Imgproc.Canny(inputMat, outputMat, lowThreshold, lowThreshold * ratio);
        return outputMat;
    }

    private Mat sobelFilter(Mat inputMat, float scale, float delta) {
        Mat outputMat = new Mat();
        Imgproc.Sobel(inputMat, outputMat, inputMat.depth(), 1, 0, 3, scale, delta, BORDER_DEFAULT);
		Imgproc.Sobel(inputMat, outputMat, inputMat.depth(), 0, 1, 3, scale, delta, BORDER_DEFAULT);
        return outputMat;
    }

    private Mat scharrFilter(Mat inputMat, float scaleInt, float deltaInt) {
        Mat outputMat = new Mat();
        Imgproc.Scharr(inputMat, outputMat, inputMat.depth(), 1, 0, scaleInt, deltaInt, BORDER_DEFAULT);
		Imgproc.Scharr(inputMat, outputMat, inputMat.depth(), 0, 1, scaleInt, deltaInt, BORDER_DEFAULT);
        return outputMat;
    }

    void setValues(float[] values) { this.values = values; }
    void setBlurFilter(int blurFilter) { this.blurFilter = blurFilter; }
    void setEdgeExtraction0(int edgeExtraction0) { this.edgeExtraction0 = edgeExtraction0; }
    void setEdgeExtraction1(int edgeExtraction1) { this.edgeExtraction1 = edgeExtraction1; }
    void setEdgeExtraction2(int edgeExtraction2) { this.edgeExtraction2 = edgeExtraction2; }
    void setStates(boolean[] states) { this.states = states; }
}