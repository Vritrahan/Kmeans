import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Arrays;
import java.util.Comparator;


public class Kmeans {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Scanner in, in1;
        int[][] points = new int[100000][2];
        try {
            in = new Scanner(new File("birch1.csv"));
            for (int i = 0; i < 100000; i++) {
                points[i][0] = in.nextInt();
                points[i][1] = in.nextInt();
            }

            double[][] centres = new double[1000][2];
            double diff = 0;
            for (int i = 0; i < 100000; i += 1000) {
                centres[i / 1000][0] = points[i][0];
                centres[i / 1000][1] = points[i][1];
            }
            /*for (int i = 0; i < 100; i++) {
                System.out.println(centres[i][0] + ", " + centres[i][1]);
            }*/

            int itrs = 0;
            while (itrs < 41) {
                Cluster[] clusters = new Cluster[100];
                for (int i = 0; i < 100; i++) {
                    clusters[i] = new Cluster();
                }

                for (int i = 0; i < 100000; i++) {
                    int min = minicent(centres, points[i]);
                    clusters[min].add(points[i]);
                }

                diff = 0;
                for (int i = 0; i < 100; i++) {
                    double[] c = clusters[i].centroid();
                    diff += Math.abs(centres[i][0] - c[0] + centres[i][1] - c[1]);
                    centres[i] = c;
                }

                //System.out.println(diff);
                itrs++;
            }
            long elapsedTime = System.currentTimeMillis() - startTime;

            Arrays.sort(centres, 0, 100, new Comparator<double[]>() {
                public int compare(double[] a, double[] b) {
                    int d = Double.compare(a[0], b[0]);
                    if(d == 0){
                        return Double.compare(a[1], b[1]);
                    }
                    return d;
                }
            });
            for (int i = 0; i < 100; i++) {
                System.out.println(centres[i][0] + ", " + centres[i][1]);
            }
            System.out.println(elapsedTime + " ms");
            System.out.println(itrs + " itrs");

            long sse = 0;
            in1 = new Scanner(new File("b1-gt.csv"));
            for (int i = 0; i < 100; i++) {
                //System.out.println(i);
                sse += Math.pow(centres[i][0] - in1.nextInt(), 2);
                sse += Math.pow(centres[i][1] - in1.nextInt(), 2);
            }
            System.out.println("sse: " + sse);

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

    }

    private static int minicent(double[][] centres, int[] point) {
        double min = Double.MAX_VALUE;
        int j = -1;
        for (int i = 0; i < 100; i++) {
            double d = dist(centres[i], point);
            //System.out.println(d);
            if (d < min) {
                min = d;
                j = i;
            }
        }

        //System.out.println(j);
        return j;
    }

    private static double dist(double[] a, int[] b) {
        double d = Math.pow((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]), 0.5);
        //System.out.println(d);
        return d;
    }

}

class Cluster {
    ArrayList<Integer[]> points;

    public Cluster() {
        points = new ArrayList<Integer[]>();
    }

    public void add(int[] p) {
        Integer[] point = new Integer[2];
        point[0] = p[0];
        point[1] = p[1];
        points.add(point);
        //System.out.println(p[0] + ", " + p[1]);
    }

    public double[] centroid() {
        double[] c = {0, 0};
        for (int i = 0; i < points.size(); i++) {
            Integer[] p = points.get(i);
            c[0] += p[0] / points.size();
            c[1] += p[1] / points.size();
        }

        //System.out.println(c[0] + ", " + c[1]);
        return c;
    }
}