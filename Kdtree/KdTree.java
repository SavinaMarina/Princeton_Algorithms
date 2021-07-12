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

public class KdTree {

    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;

    private class KdTreeNode {
        private final Point2D p;
        private KdTreeNode left, right;
        private final boolean axis;
        private final RectHV rect;
        public KdTreeNode(Point2D p, boolean axis, RectHV rect) {
            this.p = p;
            this.axis = axis;
            this.rect = rect;
        }
    }

    private KdTreeNode root;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private void insertIntoNode(KdTreeNode parent, Point2D p, boolean axis) {
        boolean isLess = (axis == HORIZONTAL) ? p.x() <= parent.p.x() : p.y() <= parent.p.y();
        if (parent.p.equals(p)) return;
        RectHV rect;

        if (isLess) {
            if (parent.left == null) {
                if (axis == HORIZONTAL)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                          parent.p.x(), parent.rect.ymax());
                else
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.p.y());
                parent.left = new KdTreeNode(p, axis, rect);
                size++;
            } else insertIntoNode(parent.left, p, !axis);
        }
        else {
            if (parent.right == null) {
                if (axis == HORIZONTAL)
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(),
                                  parent.rect.xmax(), parent.rect.ymax());
                else
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(),
                                      parent.rect.xmax(), parent.rect.ymax());
                parent.right = new KdTreeNode(p, axis, rect);
                size++;
            } else insertIntoNode(parent.right, p, !axis);
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("The argument must not be null");
        if (root == null) {
            root = new KdTreeNode(p, VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
        }
        else insertIntoNode(root, p, HORIZONTAL);
    }

    private boolean contains(KdTreeNode n, Point2D p) {
        if (n == null) return false;
        if (n.p.equals(p)) return true;
        if ((n.left != null) && (n.left.rect.contains(p))) {
            return contains(n.left, p);
        }
        else if ((n.right != null) && (n.right.rect.contains(p))) {
            return contains(n.right, p);
        }
        else return false;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("The argument must not be null");
        if (root == null) return false;
        return contains(root, p);
    }

    private void draw(KdTreeNode parent, KdTreeNode n) {
        if (n == null) return;
        n.p.draw();
        StdDraw.text(n.p.x(), n.p.y(), n.p.toString());
        if (n.axis == HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.01);
            n.p.drawTo(new Point2D(parent.p.x(), n.p.y()));
            if (n.p.x() < parent.p.x())
                // draw to the left
                n.p.drawTo(new Point2D(parent.rect.xmin(), n.p.y()));
            else
                // draw to the right
                n.p.drawTo(new Point2D(parent.rect.xmax(), n.p.y()));
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);

            if (parent == null) {
                // draw root
                n.p.drawTo(new Point2D(n.p.x(), 0));
                n.p.drawTo(new Point2D(n.p.x(), 1));
            }
            else {
                n.p.drawTo(new Point2D(n.p.x(), parent.p.y()));
                if (n.p.y() < parent.p.y())
                    // draw down
                    n.p.drawTo(new Point2D(n.p.x(), parent.rect.ymin()));
                else
                    // draw up
                    n.p.drawTo(new Point2D(n.p.x(), parent.rect.ymax()));
            }
        }
        draw(n, n.left);
        draw(n, n.right);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        draw(null, root);
        StdDraw.show();
    }

    private void addToList(KdTreeNode n, RectHV rect, ArrayList<Point2D> list) {
        if (n.left != null) {
            if (n.left.rect.intersects(rect)) {
                if (rect.contains(n.left.p)) {
                    list.add(n.left.p);
                }
                addToList(n.left, rect, list);
            }
        }
        if (n.right != null) {
            if (n.right.rect.intersects(rect)) {
                if (rect.contains(n.right.p)) {
                    list.add(n.right.p);
                }
                addToList(n.right, rect, list);
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("The argument must not be null");
        ArrayList<Point2D> list = new ArrayList<>();
        if (root != null) {
            if (rect.contains(root.p)) list.add(root.p);
            addToList(root, rect, list);
        }
        return list;
    }

    private Point2D findNearest(KdTreeNode n, Point2D p,
                                Point2D nearestPoint, double minDistance) {
        if (n == null) return nearestPoint;
        // StdOut.println("findNearest: "+ n.p + " nearestPoint = " + nearestPoint + " minDistance = " + minDistance);
        if (minDistance == 0) return nearestPoint;
        // StdOut.println("rect dist: " + n.rect.distanceSquaredTo(p));
        if (n.rect.distanceSquaredTo(p) > minDistance) return nearestPoint;
        if (n != root) {
            double dist = n.p.distanceSquaredTo(p);
            // StdOut.println("distance = " + dist);
            if (dist < minDistance) {
                nearestPoint = n.p;
                minDistance = dist;
            }
        }

        if ((n.axis == VERTICAL) && (p.x() < n.p.x()) || ((n.axis == HORIZONTAL) && (p.y() < n.p.y()))) {
            nearestPoint = findNearest(n.left, p, nearestPoint, minDistance);
            nearestPoint = findNearest(n.right, p, nearestPoint, nearestPoint.distanceSquaredTo(p));
        }
        else {
            nearestPoint = findNearest(n.right, p, nearestPoint, minDistance);
            nearestPoint = findNearest(n.left, p, nearestPoint, nearestPoint.distanceSquaredTo(p));
        }

        return nearestPoint;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("The argument must not be null");
        if (root == null) return null;
        return findNearest(root, p, root.p, root.p.distanceSquaredTo(p));
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kd = new KdTree();

        Point2D p = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);

        kd.insert(p);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);

        StdOut.println("Is Empty?");
        StdOut.println(kd.isEmpty());
        StdOut.println("Contains 2,2?");
        StdOut.println(kd.contains(p2));
        StdOut.println("Size");
        StdOut.println(kd.size());

        StdOut.println("Draw");
        kd.draw();

        Point2D pn = new Point2D(0.45, 0.22);
        StdOut.println("Nearest to " + pn.toString());
        StdOut.println(kd.nearest(pn));
        pn.draw();
        StdDraw.text(pn.x(), pn.y(), pn.toString());

        StdOut.println("In Rect:");
        RectHV rect = new RectHV(0.65, 0.65, 0.85, 0.85);
        for (Point2D px:kd.range(rect)) {
            StdOut.println(px);
        }
        StdDraw.setPenColor(StdDraw.GREEN);
        rect.draw();
    }
}
