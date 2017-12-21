package Polygen.Model.Polygons;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import java.awt.*;
import java.util.ArrayList;

import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.core.CvType.CV_8UC4;
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
        for(int i = 0; i < 2; i++) { //Später so oft, wie Polygone gezeichnet werden
            if (polyCounter == 0) {
                getFirstPoly(arrayList_vertices);
            } else {
                getSecondPoly(arrayList_vertices);
                System.out.println(arrayList_vertices);
            }

            drawMask(mask, arrayList_vertices);
            //drawPoly(poly);
            polyCounter++;
        }
        if (polyCounter == 0) {
            getFirstPoly(arrayList_vertices);
            polyCounter = polyCounter++;
        }
        else {
            arrayList_vertices.addAll(randomPolys(arrayList_vertices, mask));
        }

        drawMask(mask, arrayList_vertices);
        //drawPoly(poly);
        polyCounter++;
        return mask;
    }

    private void getFirstPoly(ArrayList<Point> arrayList_vertices) { //Berechnet das erste Polygon so, dass es nicht zu groß und dünn wird

        Point point_0 = new Point(0,0);
        Point point_middle = null;
        Point point_1 = null;
        Point point_2;
        float distance1 = 0;
        while (distance1 > scale || distance1 == 0) {
            point_1 = new Point(randomLength(), randomLength());
            point_middle = new Point(point_1.x / 2, point_1.y / 2);
            distance1 = (float) Math.sqrt(point_1.x * point_1.x + point_1.y * point_1.y);
        }
        while (true) {
            point_2 = new Point(point_middle.x + randomLength(), point_middle.y + randomLength());
            float distance2 = (float) Math.sqrt((point_2.x-point_1.x)*(point_2.x-point_1.x)+(point_2.y-point_1.y)*(point_2.y-point_1.y));
            Point vektor1 = new Point(point_1.x,point_1.y);
            Point vektor2 = new Point(point_2.x-point_1.x,point_2.y-point_1.y);
            double double_skalar = vektor1.x*vektor2.x + vektor1.y*vektor2.y;
            float skalar = (float) double_skalar;
            double alpha = Math.acos(skalar/(Math.abs(distance1)*Math.abs(distance2)));
            double degrees_double_alpha = 180*alpha/Math.PI;
            float degrees_alpha = (float) degrees_double_alpha;
            System.out.println("Distanz1: " +distance1 + " Distanz2: " +distance2 + " Bogenmaß: " + alpha + " Alpha: " + degrees_alpha);
            if (distance2>0.6*distance1 && degrees_alpha>=30) break;
        }
        arrayList_vertices.add(point_0); //Immer der Anfang, erster Vertex liegt auf 0,0
        arrayList_vertices.add(point_1); //Wird so lange neu berechnet, bis distance1(die Distanz zum point_zero) kleiner als der scale ist
        arrayList_vertices.add(point_2); //Wird so lange neu berechnet, bis die Distanz und die Winkel zu einander groß genug sind
        System.out.println("" + arrayList_vertices.get(1) + "" + arrayList_vertices.get(2));
    }

    private void getSecondPoly(ArrayList<Point> arrayList_vertices) {
    }

    private ArrayList<Point> randomPolys(ArrayList<Point> arrayList_vertices, Mat mask) { //Berechnet die anderen Polygone um das Bild auszufüllen
        for(polyCounter=1; polyCounter>6; polyCounter++) {
            Point point_Poly1 = arrayList_vertices.get(0);
            Point point_Poly2 = arrayList_vertices.get(1);
            Point point_middle = new Point((point_Poly1.x+point_Poly2.x)/2,(point_Poly1.y+point_Poly2.y)/2);
            Point point_check1 = new Point(point_middle.x+1,point_middle.y+1);
            Point point_check2 = new Point(point_middle.x-1,point_middle.y+1);
            Point point_check3 = new Point(point_middle.x+1,point_middle.y-1);
            Point point_check4 = new Point(point_middle.x-1,point_middle.y-1);
            Color color_check1 = new Color(255);
            System.out.println(mask.get((int) point_check1.y,(int) point_check1.x));
            //Test um Color Werte einer Matrix zu erhalten, klappt leider nicht ohne Umwandlung
            //Vorhaben: Vom Mittelpunkt ausgehend die Checkpoints auf weiß überprüfen, demnach ist x bzw. y für das nächste Polygon positiv bzw negativ
        }

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