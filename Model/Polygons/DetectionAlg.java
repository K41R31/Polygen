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

        Point point_second = null;
        Point point_zero = new Point(0,0);
        arrayList_vertices.add(point_zero); //Immer der Anfang, erster Vertex liegt auf 0,0
        Point point_first = new Point(randomLength(), randomLength());
        arrayList_vertices.add(point_first);
        Point point_middle = new Point(point_first.x/2,point_first.y/2);
        float distance = (float) Math.sqrt(point_first.x*point_first.x+point_first.y*point_first.y);
        while (true) {
            point_second = new Point(point_middle.x + randomLength(), point_middle.y + randomLength());
            System.out.println("First "+point_first + "Second "+point_second);
            if ((float) Math.sqrt((point_second.x-point_first.x)*(point_second.x-point_first.x)+(point_second.y-point_first.y)*(point_second.y-point_first.y))>0.7*distance) break;
        }
        arrayList_vertices.add(point_second);
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