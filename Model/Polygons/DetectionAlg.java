package Polygen.Model.Polygons;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgproc.Imgproc.fillConvexPoly;


public class DetectionAlg {

    private ArrayList<Point> arrayList_firstPolyVertices = new ArrayList<>();
    private ArrayList<Point> arrayList_mainVertices = new ArrayList<>();
    private float scale; //TODO Scale minValue = 0,0007% || 10; maxValue = 0,01% || 10
    private int polyCounter = 0;
    private Mat imageMat;
    private Mat edgeMat;
    private Mat mask;
    private Point whitePoint = null;
    private boolean isGreen, isWhite;

    public DetectionAlg(Mat imageMat, Mat edgeMat, float scale) { //edge Mat = KantenBild; scale = eingabe vom Nutzer
        this.scale = scale;
        this.imageMat = imageMat;
        this.edgeMat = edgeMat;
        this.mask = new Mat(imageMat.rows(),imageMat.cols(),CV_8UC3, new Scalar(0,0,0));
        algorithm();
    }

    private void algorithm() {
        while (true) {
            isGreen = false;
            isWhite = false;

            if (polyCounter == 0) { firstPoly(); drawMask(); polyCounter++; continue; }
            else if (polyCounter < 4) {
                getFirstAlgPolys();
                if (arrayList_mainVertices.get(arrayList_mainVertices.size()-1) == null) { System.out.println("null hinzugefügt"); polyCounter++; continue; } //Falls kein Polygon gezeichnet werden muss
            }
            else break;
            Point temporaryPoint = pointForSearch();
//            System.out.println("temporaryPoint: "+temporaryPoint);
            Point greenPoint = verticeDetection(temporaryPoint); //TODO
//            System.out.println("greenPoint: "+greenPoint);
            System.out.println("temporaryPoint: "+temporaryPoint);
            int[] signs = sideDetection(temporaryPoint);
            System.out.println("signs: "+signs[0] + " "+signs[1]);
            Point greenPoint = verticeDetection(temporaryPoint, signs); //TODO
            System.out.println("greenPoint: "+greenPoint);
            float newScale = interferenceDetection(temporaryPoint, greenPoint);
//            System.out.println("newScale: "+newScale);
            drawMask();
            //drawPoly(poly);
            polyCounter++;
        }
    }

    private void firstPoly() { //Berechnet das erste Polygon so, dass es nicht zu groß und dünn wird

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
            //System.out.println("Distanz1: " +distance1 + " Distanz2: " +distance2 + " Bogenmaß: " + alpha + " Alpha: " + degrees_alpha);
            if (distance2>0.6*distance1 && degrees_alpha>=30) break;
        }
        arrayList_firstPolyVertices.add(point_0); //Immer der Anfang, erster Vertex liegt auf 0,0
        arrayList_firstPolyVertices.add(point_1); //Wird so lange neu berechnet, bis distance1(die Distanz zum point_zero) kleiner als der scale ist
        arrayList_firstPolyVertices.add(point_2); //Wird so lange neu berechnet, bis die Distanz und die Winkel zu einander groß genug sind
        //System.out.println("" + arrayList_vertices.get(1) + "" + arrayList_vertices.get(2));
    }

    private void getFirstAlgPolys() {

        switch (polyCounter) {
            case 1: for(int i = 0; i < 2; i++) arrayList_mainVertices.add(arrayList_firstPolyVertices.get(i)); //Point 0 + 1
                    break;
            case 2: for(int i = 1; i < 3; i++) arrayList_mainVertices.add(arrayList_firstPolyVertices.get(i)); //Point 1 + 2
                    break;
            case 3: for(int i = 0; i < 3; i=i+2) arrayList_mainVertices.add(arrayList_firstPolyVertices.get(i)); //Point 0 + 2
        }
        arrayList_mainVertices.add(new Point(0,0)); //Damit man immer mit einer Multiplikation mit 3 die Punkte finden kann
        System.out.println("polyCounter: "+polyCounter); //TODO immer bei polyCounter == 3
        System.out.println("-3: "+arrayList_mainVertices.get((polyCounter*3)-3).x);
        System.out.println("-2: "+arrayList_mainVertices.get((polyCounter*3)-2).x);
        int middleX = (int)(arrayList_mainVertices.get((polyCounter*3)-3).x + arrayList_mainVertices.get((polyCounter*3)-2).x)/2; //X Mittelwert der letzten hinzugefügten Punkte //-2 und -1 weil der Index auf 0 beginnt
        int middleY = (int)(arrayList_mainVertices.get((polyCounter*3)-3).y + arrayList_mainVertices.get((polyCounter*3)-2).y)/2; //Y Mittelwert der letzten hinzugefügten Punkte
        int[] mathOperators = sideDetection(new Point(middleX, middleY));
        if (mathOperators[0] == 0 || mathOperators[1] == 0) { arrayList_mainVertices.add(null); return; } //Falls kein Polygon gezeichnet werden muss
        int randomX = middleX + randomLength() * mathOperators[0];
        int randomY = middleY + randomLength() * mathOperators[1];
        if (randomX < 0) randomX = 0;
        if (randomY < 0) randomY = 0;
        arrayList_mainVertices.set(arrayList_mainVertices.size()-1, new Point(randomX, randomY));
        /* TODO Für die späteren Polys
        for(int i = 0; i < 2; i++) {
            arrayList_firstPolyVertices.get(polyCounter * 2 + i);
        }
        */
    }

    private int[] sideDetection(Point searchpoint) {

        int[] mathOperators = new int[2];
        boolean[] toSearch = {true, true};
        for (double i = 1; i < 20 && (toSearch[0] || toSearch[1]); i++) {

            if (toSearch[0]) {
                if (((int) (searchpoint.x - i)) < 0) {
                    mathOperators[0] = 0;
                    toSearch[0] = false;
                }
                else if (mask.get((int)(searchpoint.y), (int)(searchpoint.x + i))[2] >= 100 && mask.get((int)(searchpoint.y), ((int)(searchpoint.x - i)))[2] < 100) {
                    mathOperators[0] = -1;
                    toSearch[0] = false;
                }
                else if (mask.get(((int)(searchpoint.y)), ((int)(searchpoint.x - i)))[2] >= 100 && mask.get((int)(searchpoint.y), ((int)(searchpoint.x + i)))[2] < 100) {
                    mathOperators[0] = 1;
                    toSearch[0] = false;
                }
            }
            if (toSearch[1]) {
                if (((int) (searchpoint.y - i)) < 0) {
                    mathOperators[1] = 0;
                    toSearch[1] = false;
                }
                else if (mask.get(((int)(searchpoint.y + i)), (int)(searchpoint.x))[2] >= 100 && mask.get(((int)(searchpoint.y - i)), (int)(searchpoint.x))[2] < 100) {
                    mathOperators[1] = -1;
                    toSearch[1] = false;
                }
                else if (mask.get(((int)(searchpoint.y - i)), (int)(searchpoint.x))[2] >= 100 && mask.get(((int)(searchpoint.y + i)), (int)(searchpoint.x))[2] < 100) {
                    mathOperators[1] = 1;
                    toSearch[1] = false;
                }
            }
        }
        return(mathOperators);
    }

    private Point pointForSearch() {
        int result = 0;
        Random r = new Random();
        int low = (int) arrayList_mainVertices.get((polyCounter*3)-3).y;
        int high = (int) arrayList_mainVertices.get((polyCounter*3)-2).y;
        if (high > low) result = r.nextInt(high-low) + low; //TODO Wirft exeptions (bound must be positive) (Anscheinend weil r 0 ist)
        else if (high < low) result = r.nextInt(low-high) + high; //Falls Y-Wert vom ersten Vertex größer ist
        else result = low; //Wenn die beiden Y-Werte gleich sind -> result = low
        int pitch = ((int)(arrayList_mainVertices.get((polyCounter*3)-2).x - arrayList_mainVertices.get((polyCounter*3)-3).x))/(int)((arrayList_mainVertices.get((polyCounter*3)-2).y - arrayList_mainVertices.get((polyCounter*3)-3).y));
        int pitch_X = result * pitch;
        return new Point(pitch_X, result);
    }

    private Point verticeDetection(Point temporaryPoint, int[] signs) {
        Point greenPoint = new Point();
        double[] green = {0.0, 255.0, 0.0};
        int range_X = (int) (temporaryPoint.x + signs[0]*scale);
        int range_Y = (int) (temporaryPoint.y + signs[1]*scale);
        for (int x = (int) temporaryPoint.x; x <= range_X && !isGreen; x++) { //Suche nach einem Vertex in diesem Bereich
            for (int y = (int) temporaryPoint.y; y <= range_Y; y++) {
            //System.out.println("rangex: "+range_X);
            for (int y = (int) temporaryPoint.y; y <= range_Y; y++) {
                //System.out.println("x: "+x+", y: "+y);
                if (Arrays.equals(mask.get(y, x), green)) { //Falls ein grüner Vertex gefunden wird muss geprüft werden, ob einen theoretische Linie mit einem vorhandenen Polygon interferiert //TODO Wirft unknown exception (Passiert wenn (x || y) < 0)
                    greenPoint = new Point(x, y);
                    isGreen = true;
                }
            }
        }
        if(isGreen)
            return greenPoint;
        else
            return null;
    }

    private float interferenceDetection(Point temporaryPoint, Point greenPoint) {
        double[] white = {100.0, 100.0, 100.0}; //TODO muss wieder in weiß [255,255,255] geändert werden
        float newScale = 0;
        int runX= 0;
        int runY = 0;
        int y = 0;
        int pitch_White = 0;
        int scaledX = (int) (temporaryPoint.x+scale);
        int scaledY = (int) (temporaryPoint.y+scale);
        if(isGreen) { //Falls ein Vertex vorliegt muss noch geprüft werden ob ein Objekt dazwischen liegt
            while(!isWhite && runX < greenPoint.x) {
                pitch_White = (int) ((greenPoint.x - temporaryPoint.x) / (greenPoint.y - temporaryPoint.y)); //Strecke zwischen Ausgangspunkt und Vertex
                for (runX = 0 ; runX <= greenPoint.x; runX++) {
                    runY = pitch_White * runX;
                    if (Arrays.equals(mask.get(runY, runX), white)) {
                        whitePoint = new Point(runX, runY);
                        newScale = (float) Math.min((runX-temporaryPoint.x),(runY-temporaryPoint.y));
                        isWhite = true;
                    }
                }
            }
        }
        else {
            while(!isWhite && runX <= scaledX) {
                for (runX = (int) temporaryPoint.x; runX <= scaledX; runX++) { //Suche nach einem weißen Punkt in diesem Bereich
                    for (y = (int) temporaryPoint.y; y <= scaledY; y++) {
                        if (Arrays.equals(mask.get(y, runX), white)) { //Falls ein weißer Punkt gefunden wurde muss eine neue maximale Länge betrachtet werden
                            whitePoint = new Point(runX, y);
                            newScale = (float) Math.min((runX-temporaryPoint.x),(y-temporaryPoint.y));
                            isWhite = true;
                        }
                    }
                }
            }

        }
        if(isWhite) {
            return newScale;
        }
        else 
            return 0;
    }

    private void drawMask() {
        if (polyCounter == 0) {
            fillConvexPoly(mask, new MatOfPoint(arrayList_firstPolyVertices.get(polyCounter), arrayList_firstPolyVertices.get(polyCounter+1), arrayList_firstPolyVertices.get(polyCounter+2)), new Scalar(255,255, 255));
        }
        else {
            fillConvexPoly(mask, new MatOfPoint(arrayList_mainVertices.get(polyCounter*3-3), arrayList_mainVertices.get(polyCounter*3-2), arrayList_mainVertices.get(polyCounter*3-1)), new Scalar(100,100, 100)); // TODO - polyCounter um die Polygone zu unterscheiden
            Imgproc.line(mask, arrayList_mainVertices.get(polyCounter*3-3), arrayList_mainVertices.get(polyCounter*3-3), new Scalar(0.0, 255.0, 0.0));
            Imgproc.line(mask, arrayList_mainVertices.get(polyCounter*3-2), arrayList_mainVertices.get(polyCounter*3-2), new Scalar(0.0, 255.0, 0.0));
            Imgproc.line(mask, arrayList_mainVertices.get(polyCounter*3-1), arrayList_mainVertices.get(polyCounter*3-1), new Scalar(0.0, 255.0, 0.0));
        }
    }

    private void drawPoly(Polygon poly) {

    }

    private int randomLength() { return (int)(scale * (Math.random() + Math.random() / 2)); } //Für Werte die näher am Durchschnitt liegen

    public Mat getMask() { return mask; }
}