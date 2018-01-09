package Polygen.Model.Polygons;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgproc.Imgproc.fillConvexPoly;


public class DetectionAlg {

    private ArrayList<Point> arrayList_firstPolyVertices = new ArrayList<>();
    private ArrayList<Point> arrayList_mainVertices = new ArrayList<>();
    private float scale, newScale; //TODO Scale minValue = 0,0007% || 10; maxValue = 0,01% || 10
    private int polyCounter = 0;
    private Mat imageMat;
    private Mat edgeMat;
    private Mat mask;
    private Point temporaryPoint, greenPoint, whitePoint = null;
    private boolean isGreen, isWhite = false;

    public DetectionAlg(Mat imageMat, Mat edgeMat, float scale) { //edge Mat = KantenBild; scale = eingabe vom Nutzer
        this.scale = scale;
        this.imageMat = imageMat;
        this.edgeMat = edgeMat;
        this.mask = new Mat(imageMat.rows(),imageMat.cols(),CV_8UC3, new Scalar(0,0,0));
        algorithm();
    }

    private void algorithm() {
        while (true) {
            if (polyCounter == 0) firstPoly();
            else if (polyCounter < 2) getFirstAlgPolys(); //TODO Muss später 4 sein
            else break;
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
        int middleX = (int)(arrayList_mainVertices.get((polyCounter*3)-3).x + arrayList_mainVertices.get((polyCounter*3)-2).x)/2; //X Mittelwert der letzten hinzugefügten Punkte //-2 und -1 weil der Index auf 0 beginnt
        int middleY = (int)(arrayList_mainVertices.get((polyCounter*3)-3).y + arrayList_mainVertices.get((polyCounter*3)-2).y)/2; //Y Mittelwert der letzten hinzugefügten Punkte
        System.out.println(Arrays.toString(sideDetection(new Point(middleX, middleY)))); //TODO Gibt erstmal nur Text aus
        arrayList_mainVertices.set(arrayList_mainVertices.size()-1, new Point(middleX + randomLength(), middleY + randomLength())); //TODO Muss mit Side Detection erstellt werden
        /* TODO Für die späteren Polys
        for(int i = 0; i < 2; i++) {
            arrayList_firstPolyVertices.get(polyCounter * 2 + i);
        }
        */
    }

/*    private ArrayList<Point> randomPolys(Mat mask) { //Berechnet die anderen Polygone um das Bild auszufüllen
        for(int i = 1; i<6; i++) {
            Point point_Poly1 = arrayList_vertices.get(0);
            Point point_Poly2 = arrayList_vertices.get(1);
            Point point_middle = new Point((point_Poly1.x+point_Poly2.x)/2,(point_Poly1.y+point_Poly2.y)/2);
            Point point_check1 = new Point(point_middle.x+1,point_middle.y+1);
            Point point_check2 = new Point(point_middle.x-1,point_middle.y+1);
            Point point_check3 = new Point(point_middle.x+1,point_middle.y-1);
            Point point_check4 = new Point(point_middle.x-1,point_middle.y-1);
            Color color_check1 = new Color(255);
            //System.out.println(Arrays.toString(mask.get((int) point_check1.y, (int) point_check1.x)));
            //Test um Color Werte einer Matrix zu erhalten, klappt leider nicht ohne Umwandlung
            //Vorhaben: Vom Mittelpunkt ausgehend die Checkpoints auf weiß überprüfen, demnach ist x bzw. y für das nächste Polygon positiv bzw negativ
        }
        return arrayList_vertices;
    }
*/
    private Point pointForSearch(){
        int result = 0;
        Random r = new Random();
        int low = (int) arrayList_mainVertices.get((polyCounter*3)-3).x;
        int high = (int) arrayList_mainVertices.get((polyCounter*3)-2).x;
        if(high >= low)
            result = r.nextInt(high-low) + low;
        else //Falls X-Wert vom ersten Vertex größer ist
            result = r.nextInt(low-high) + high;
        int pitch = ((int) (arrayList_mainVertices.get((polyCounter*3)-2).y - arrayList_mainVertices.get((polyCounter*3)-3).y))/(int) ((arrayList_mainVertices.get((polyCounter*3)-2).x - arrayList_mainVertices.get((polyCounter*3)-3).x));
        int pitch_Y = result * pitch;
        temporaryPoint = new Point(result, pitch_Y);
    return temporaryPoint;
    }

    private Point verticeDetection() {
        double[] green = {0.0, 255.0, 0.0};
        int range_X = (int) (temporaryPoint.x + scale);
        int range_Y = (int) (temporaryPoint.y + scale);
        while((int) temporaryPoint.x < range_X || isGreen) {
            for (int x = (int) temporaryPoint.x; x <= range_X; x++) { //Suche nach einem Vertex in diesem Bereich
                for (int y = (int) temporaryPoint.y; y <= range_Y; y++) {
                    if (Arrays.equals(mask.get(y, x), green)) { //Falls ein grüner Vertex gefunden wird muss geprüft werden, ob einen theoretische Linie mit einem vorhandenen Polygon interferiert
                        greenPoint = new Point(mask.get(y,x));
                        isGreen = true;
                    }
                    else temporaryPoint.x++;
                }
            }
        }
        if(isGreen)
            return greenPoint;
        else
            return null;
    }

    private float interferenceDetection() {
        double[] white = {255.0, 255.0, 255.0};
        int x = 0;
        int y = 0;
        int scaledX = (int) (temporaryPoint.x+scale);
        int scaledY = (int) (temporaryPoint.y+scale);
        if(isGreen) {//Falls ein Vertex vorliegt muss noch geprüft werden ob ein Objekt dazwischen liegt
            while(isWhite || x < greenPoint.x) {
                int pitch_White = (int) ((greenPoint.y - temporaryPoint.y) / (greenPoint.x - temporaryPoint.x));//Strecke zwischen Ausgangspunkt und Vertex
                for (int runX = x ; runX <= greenPoint.x; runX++) {
                    int runY = pitch_White * runX;
                    if (Arrays.equals(mask.get(runY, runX), white)) {
                        whitePoint = new Point(mask.get(runY, runX));
                        newScale = (float) Math.sqrt((runX-temporaryPoint.x)*(runX-temporaryPoint.x)+(runY-temporaryPoint.y)*(runY-temporaryPoint.y));
                        isWhite = true;
                    }
                    else x++;
                }
            }
        }
        else{
            while(isWhite || x <= scaledX) {
                for (x = (int) temporaryPoint.x; x <= scaledX; x++) { //Suche nach einem weißen Punkt in diesem Bereich
                    for (y = (int) temporaryPoint.y; y <= scaledY; y++) {
                        if (Arrays.equals(mask.get(y, x), white)) { //Falls ein weißer Punkt gefunden wurde muss eine neue maximale Länge betrachtet werden
                            whitePoint = new Point(mask.get(y,x));
                            newScale = (float) Math.sqrt((x-temporaryPoint.x)*(x-temporaryPoint.x)+(y-temporaryPoint.y)*(y-temporaryPoint.y));
                            isWhite = true;
                        }
                    }
                }
            }

        }
        if(isWhite)
            return newScale;
        else 
            return 0;
    }

    private boolean[] sideDetection(Point searchpoint) {

//        System.out.println("Punkt0 Position: "+arrayList_firstPolyVertices.get(0));
//        System.out.println("Punkt1 Position: "+arrayList_firstPolyVertices.get(1));
//        System.out.println("Punkt2 Position: "+arrayList_firstPolyVertices.get(2));

        boolean[] toSearch = {true, true};
        for (double i = 1; i < 50 && (toSearch[0] || toSearch[1]); i++) {

            if (toSearch[0]) {
                if (((int) (searchpoint.x - i)) < 0) {
                    System.out.println("x-: " + i + " weniger als 0");
                    toSearch[0] = false;
                }
                else if (mask.get((int)(searchpoint.y), (int)(searchpoint.x + i))[2] >= 100 && mask.get((int)(searchpoint.y), ((int)(searchpoint.x - i)))[2] < 100) {
                    System.out.println("x+ " + i);
                    toSearch[0] = false;
                }
                else if (mask.get(((int)(searchpoint.y)), ((int)(searchpoint.x - i)))[2] >= 100 && mask.get((int)(searchpoint.y), ((int)(searchpoint.x + i)))[2] < 100) {
                    System.out.println("x- " + i);
                    toSearch[0] = false;
                }
            }
            if (toSearch[1]) {
                if (((int) (searchpoint.y - i)) < 0) {
                    System.out.println("y-: " + i + " weniger als 0");
                    toSearch[1] = false;
                }
                else if (mask.get(((int)(searchpoint.y + i)), (int)(searchpoint.x))[2] >= 100 && mask.get(((int)(searchpoint.y - i)), (int)(searchpoint.x))[2] < 100) {
                    System.out.println("y+ " + i);
                    toSearch[1] = false;
                }
                else if (mask.get(((int)(searchpoint.y - i)), (int)(searchpoint.x))[2] >= 100 && mask.get(((int)(searchpoint.y + i)), (int)(searchpoint.x))[2] < 100) {
                    System.out.println("y- " + i);
                    toSearch[1] = false;
                }
            }
        }
/*
        double[] black = {0.0, 0.0, 0.0};
        boolean[] mathOperators = new boolean[2];
        for (int x = 1, y = 1; x <= 20; x++, y++) {
            if ((int)searchpoint.x - x < 0) {System.out.println("Error: sideDetection konnte für x nichts finden"); break;}
            System.out.println((searchpoint.x + x)+Arrays.toString(mask.get((int) searchpoint.x + x, (int) searchpoint.y)));
            System.out.println((searchpoint.x - x)+Arrays.toString(mask.get((int) searchpoint.x - x, (int) searchpoint.y)));
            if (Arrays.equals(mask.get((int)searchpoint.x + x, (int)searchpoint.y), black) && !(Arrays.equals(mask.get((int)searchpoint.x - x, (int)searchpoint.y), black)))
                {mathOperators[0] = true; break;}
            else if (Arrays.equals(mask.get((int)searchpoint.x - x, (int)searchpoint.y), black) && !(Arrays.equals(mask.get((int)searchpoint.x + x, (int)searchpoint.y), black)))
                {mathOperators[0] = false; break;}
            if (x == 20) System.out.println("Setze x auf 0");
        }
        for (int x = 1, y = 1; x <= 20; x++, y++) { //TODO x und y in einer Schleife erstellen
            if ((int)searchpoint.y - y < 0) break;
            if (Arrays.equals(mask.get((int)searchpoint.x, (int)searchpoint.y + y), black) && !(Arrays.equals(mask.get((int)searchpoint.x, (int)searchpoint.y - y), black)))
            {mathOperators[1] = true; break;}
            else if (Arrays.equals(mask.get((int)searchpoint.x, (int)searchpoint.y - y), black) && !(Arrays.equals(mask.get((int)searchpoint.x, (int)searchpoint.y + y), black)))
            {mathOperators[1] = false; break;}
            if (y == 20) System.out.println("Error: sideDetection konnte für y nichts finden");
            }
*/
        //TODO Gibt nur Werte aus, wenn (!)(+ true && - false) gegeben ist
        return(null); //TODO Später mathOperators
    }

    private void drawMask() {
        if (polyCounter == 0) {
            fillConvexPoly(mask, new MatOfPoint(arrayList_firstPolyVertices.get(polyCounter), arrayList_firstPolyVertices.get(polyCounter+1), arrayList_firstPolyVertices.get(polyCounter+2)), new Scalar(240,240, 240));
//            System.out.println("first: "+arrayList_firstPolyVertices);
        }
        else {
            fillConvexPoly(mask, new MatOfPoint(arrayList_mainVertices.get(polyCounter*3-3), arrayList_mainVertices.get(polyCounter*3-2), arrayList_mainVertices.get(polyCounter*3-1)), new Scalar(255,255, 255));
//            System.out.println("main: "+arrayList_mainVertices);
        }
    }

    private void drawPoly(Polygon poly) {

    }

    private int randomLength() { return (int)(scale * Math.random()); }

    public Mat getMask() { return mask; }
}