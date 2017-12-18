package Polygen.Model.Polygons;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import java.awt.*;
import java.util.ArrayList;
import static org.opencv.imgproc.Imgproc.fillConvexPoly;


public class DetectionAlg {

    private float scale; //TODO Scale minValue = 0,0007% || 10; maxValue = 0,01% || 10
    private int polyCounter = 0;
    private Mat imageMat;
    private Mat edgeMat;

    public DetectionAlg(Mat imageMat, Mat edgeMat, float scale) { //edge Mat = KantenBild; scale = eingabe vom Nutzer
        this.scale = scale;
        this.imageMat = imageMat;
        this.edgeMat = edgeMat;
    }

    public Mat getMat() {
        Mat mask = new Mat(imageMat.rows(),imageMat.cols(),3, new Scalar(0,0,0));
        ArrayList<Point> arrayList_vertices = new ArrayList<>();
        return algorithm(arrayList_vertices, edgeMat, mask);
    }

    private Mat algorithm(ArrayList<Point> arrayList_vertices, Mat progress, Mat mask) {
        if (polyCounter == 0) {
            arrayList_vertices = getPoly0(arrayList_vertices);
        }
        else {

        }

        drawMask(mask, arrayList_vertices);
        //drawPoly(poly);
        polyCounter++;
        return mask;
    }

    private void drawMask(Mat mask, ArrayList<Point> arrayList_vertices) {
        fillConvexPoly(mask, new MatOfPoint(arrayList_vertices.get(0), arrayList_vertices.get(1), arrayList_vertices.get(2)), new Scalar(255,255, 255));
    }

    private void drawPoly(Polygon poly) {

    }

    private Float randomLength() {
        return scale * (float)Math.random();
    }
}