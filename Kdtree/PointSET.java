/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> ts;
    // construct an empty set of points
    public PointSET() {
        ts  = new TreeSet<>();
    }
    // is the set empty?
    public boolean isEmpty() {
        return ts.isEmpty();
    }

    // number of points in the set
    public int size() {
        return ts.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("The argument must not be null");
        ts.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("The argument must not be null");
        return ts.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p:ts) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("The argument must not be null");
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D p:ts) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("The argument must not be null");
        if (ts.isEmpty()) return null;
        Point2D nearestPoint = ts.first();
        double minDist = ts.first().distanceSquaredTo(p);
        for (Point2D pts:ts) {
            if (pts.equals(p)) return pts;
            double dist = pts.distanceSquaredTo(p);
            if (dist < minDist) {
                nearestPoint = pts;
                minDist = dist;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET ps = new PointSET();
        Point2D p = new Point2D(1, 1);
        Point2D p2 = new Point2D(2, 2);
        Point2D p3 = new Point2D(3, 3);

        ps.insert(p);
        ps.insert(p2);
        ps.insert(p3);

        StdOut.println("Is Empty?");
        StdOut.println(ps.isEmpty());
        StdOut.println("Contains 2,2?");
        StdOut.println(ps.contains(p2));
        StdOut.println("Size");
        StdOut.println(ps.size());
        StdOut.println("Nearest to 1,1");
        StdOut.println(ps.nearest(p));

        StdOut.println("In Rect:");
        for (Point2D px:ps.range(new RectHV(1, 1, 2, 2))) {
            StdOut.println(px);
        }

        StdOut.println("Draw");
        StdDraw.setPenRadius(0.01);
        ps.draw();
    }
}
