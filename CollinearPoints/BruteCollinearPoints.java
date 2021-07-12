import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
            for (int j = i + 1; j < ipoints.length-2; j++) {
                for (int k = j + 1; k < ipoints.length-1; k++) {
                    for (int m = k + 1; m < ipoints.length; m++) {
                        double slope1 = ipoints[i].slopeTo(ipoints[j]);
                        double slope2 = ipoints[i].slopeTo(ipoints[k]);
                        double slope3 = ipoints[i].slopeTo(ipoints[m]);
                        if ((slope1 == slope2) && (slope2 == slope3)) {
                            segments.add(new LineSegment(ipoints[i], ipoints[m]));
                        }
                    }
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
