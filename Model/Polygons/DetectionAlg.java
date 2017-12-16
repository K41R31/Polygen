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

    public DetectionAlg() { //edge Mat = KantenBild; scale = eingabe vom Nutzer
    }

    public Mat getMat(Mat imageMat, Mat edgeMat, float scale) {
        this.scale = scale;
        Mat mask = new Mat(imageMat.rows(),imageMat.cols(),3, new Scalar(0,0,0));
        ArrayList<Point> arrayList_vertices = new ArrayList<>();
        Mat processedMat = algorithm(arrayList_vertices, edgeMat, mask);
        return processedMat;
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

    private ArrayList<Point> getPoly0(ArrayList<Point> arrayList_vertices) {

        arrayList_vertices.add(new Point(0,0)); //Immer der Anfang, erster Vertex liegt auf 0,0
        arrayList_vertices.add(new Point(randomLength(),randomLength()));
        arrayList_vertices.add(new Point(randomLength(),randomLength()));
//        float relationship1 = (float)((arrayList_vertices.get(1).x)/(arrayList_vertices.get(1).y));
//        float relationship2 = (float)((arrayList_vertices.get(2).x)/(arrayList_vertices.get(2).y));
//        if (relationship1>relationship2-100 && relationship1<relationship2+100) arrayList_vertices.set(2,new Point(randomLength(),randomLength()));
        return arrayList_vertices;
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