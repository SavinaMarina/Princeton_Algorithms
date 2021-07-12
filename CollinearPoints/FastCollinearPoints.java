import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The argument is null");

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("The point is null");
        }

        Point[] ipoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            ipoints[i] = points[i];
        }

        Arrays.sort(ipoints);

        for (int i = 0; i < ipoints.length-1; i++) {
            if (ipoints[i].compareTo(ipoints[i+1]) == 0) throw new IllegalArgumentException("The argument contains repeated point");
        }

        if (ipoints.length < 4) return;

        for (int i = 0; i < ipoints.length-3; i++) {
            Point maxPoint = ipoints[i];
            Point[] arr = new Point[ipoints.length-1-i];
            int a = 0;
            for (int j = i + 1; j < ipoints.length; j++) {
                arr[a] = ipoints[j];
                a++;
            }

            Arrays.sort(arr, ipoints[i].slopeOrder());
            int c = 1;
            for (int k = 0; k < arr.length - 1; k++) {
                double s = ipoints[i].slopeTo(arr[k]);
                double s2 = ipoints[i].slopeTo(arr[k+1]);
                if ((s == s2)) {
                    maxPoint = arr[k+1];
                    c++;
                }
                else {
                    maxPoint = arr[k];
                    if (c >= 3) {
                        boolean segmentExists = false;
                        for (int x = 0; x < i; x++) {
                            if (ipoints[x].slopeTo(maxPoint) == ipoints[i].slopeTo(maxPoint)) {
                                segmentExists = true;
                            }
                        }
                        if (!segmentExists) {
                            segments.add(new LineSegment(ipoints[i], maxPoint));
                        }
                    }
                    c = 1;
                }
            }
            if (c >= 3) {
                boolean segmentExists = false;
                for (int x = 0; x < i; x++) {
                    if (ipoints[x].slopeTo(maxPoint) == ipoints[i].slopeTo(maxPoint)) {
                        segmentExists = true;
                    }
                }
                if (!segmentExists) {
                    segments.add(new LineSegment(ipoints[i], maxPoint));
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();

    }
    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }
}
