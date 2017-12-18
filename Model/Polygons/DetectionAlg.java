package Polygen.Model.Polygons;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import java.awt.*;
import java.util.ArrayList;

import static org.opencv.core.CvType.CV_8UC3;
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
        Mat mask = new Mat(imageMat.rows(),imageMat.cols(),CV_8UC3, new Scalar(0,0,0));
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

    private ArrayList<Point> getPoly0(ArrayList<Point> arrayList_vertices) {

        Point point_middle = null;
        Point point_first = null;
        Point point_second = null;
        Point point_zero = new Point(0,0);
        arrayList_vertices.add(point_zero); //Immer der Anfang, erster Vertex liegt auf 0,0
        float distance1 = 600;
        while (distance1 > 500) {
            point_first = new Point(randomLength(), randomLength());
            point_middle = new Point(point_first.x / 2, point_first.y / 2);
            distance1 = (float) Math.sqrt(point_first.x * point_first.x + point_first.y * point_first.y);
        }
        while (true) {
            point_second = new Point(point_middle.x + randomLength(), point_middle.y + randomLength());
            float distance2 = (float) Math.sqrt((point_second.x-point_first.x)*(point_second.x-point_first.x)+(point_second.y-point_first.y)*(point_second.y-point_first.y));
            Point vektor1 = new Point(point_first.x,point_first.y);
            Point vektor2 = new Point(point_second.x-point_first.x,point_second.y-point_first.y);
            double double_skalar = vektor1.x*vektor2.x + vektor1.y*vektor2.y;
            float skalar = (float) double_skalar;
            double alpha = Math.acos(skalar/(Math.abs(distance1)*Math.abs(distance2)));
            double degrees_double_alpha = 180*alpha/Math.PI;
            float degrees_alpha = (float) degrees_double_alpha;
            System.out.println(" Distanz1: " +distance1 + " Distanz2: " +distance2 + " Bogenmaß: " + alpha + " Alpha: " + degrees_alpha);
            if (distance2>0.6*distance1 && degrees_alpha>=30) break;
        }
        arrayList_vertices.add(point_first);
        arrayList_vertices.add(point_second);
        System.out.println("" + arrayList_vertices.get(1) + "" + arrayList_vertices.get(2));

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