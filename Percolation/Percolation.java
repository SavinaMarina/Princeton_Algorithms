/**
 *
 *  We model a percolation system using an n-by-n grid of sites.
 *  Each site is either open or blocked.
 *  A full site is an open site that can be connected to an open site in the top row
 *   via a chain of neighboring (left, right, up, down) open sites.
 *
 * We say the system percolates if there is a full site in the bottom row.
 * In other words, a system percolates if we fill all open sites connected to the top row
 * and that process fills some open site on the bottom row.
 *
 * The row and column indices are integers between 1 and n, where (1, 1) is the upper-left site
*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[] openedStatusArr;
    private boolean[] fullStatusArr;
    private final int gridSize;
    private final int virtTop;
    private final int virtBottom;
    private final WeightedQuickUnionUF q;
    private int openedCnt;
    private boolean isPercolating;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive: " + n);
        openedCnt = 0;
        gridSize = n;
        virtTop = n*n;
        virtBottom = n*n+1;
        openedStatusArr = new boolean[n*n];
        fullStatusArr = new boolean[n*n];
        isPercolating = false;
        // + 2 for virtual top and bottom sites
        q = new WeightedQuickUnionUF(n*n+2);
        for (int i = 0; i < openedStatusArr.length; i++) {
            // set all sites as blocked
            openedStatusArr[i] = true;
            // set all sites not full
            fullStatusArr[i] = false;
        }
    }

    private void checkParams(int row, int col) {
        if (row <= 0 || row > gridSize || col <= 0 || col > gridSize)
            throw new IllegalArgumentException("must be between 0 and "+gridSize+" : "+row+" "+col);
    }

    private int getIndx(int row, int col) {
        return (row-1)*gridSize+col-1;
    }

    private void setFull(int row, int col, int rowPrev, int colPrev) {
        int index = getIndx(row, col);
        if (fullStatusArr[index]) return;
        if (isOpen(row, col)) {
            fullStatusArr[index] = true;
            int upRow = row - 1;
            if ((row > 1) && (upRow != rowPrev)) {
                setFull(upRow, col, row, col);
            }
            int downRow = row + 1;
            if ((row < gridSize) && (downRow != rowPrev)) {
                setFull(downRow, col, row, col);
            }
            int leftCol = col - 1;
            if ((col > 1) && (leftCol != colPrev)) {
                setFull(row, leftCol, row, col);
            }
            int rightCol = col + 1;
            if ((col < gridSize) && (rightCol != colPrev)) {
                setFull(row, rightCol, row, col);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        checkParams(row, col);
        if (isOpen(row, col)) return;
        int indx = getIndx(row, col);
        // System.out.println("open"+indx);
        openedStatusArr[indx] = false;
        openedCnt++;
        // connect with neighbours
        // if in the first row - connect with virtual top
        if (row == 1) {
            q.union(indx, virtTop);
            setFull(row, col, 0, 0);
        }
        else {
            if (isOpen(row - 1, col)) {
                q.union(indx, indx - gridSize);
                if (isFull(row - 1, col)) {
                    setFull(row, col, 0, 0);
                }
            }
        }
        // if in the last row - connect with virtual bottom
        if (row == gridSize) {
            q.union(indx, virtBottom);
        }
        // else connect with bottom neighbour
        else {
            if (isOpen(row+1, col)) {
                q.union(indx, indx + gridSize);
                if (isFull(row + 1, col)) {
                    setFull(row, col, 0, 0);
                }
            }
        }
        // if not the first element in a row
        if (!(indx % gridSize == 0)) {
            if (isOpen(row, col - 1)) {
                q.union(indx, indx - 1);
                if (isFull(row, col - 1)) {
                    setFull(row, col, 0, 0);
                }
            }
        }
        // if not the last element in the row
        if (!((indx+1) % gridSize == 0)) {
            if (isOpen(row, col+1)) {
                q.union(indx, indx + 1);
                if (isFull(row, col + 1)) {
                    setFull(row, col, 0, 0);
                }
            }
        }

       isPercolating = q.find(virtTop) == q.find(virtBottom);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkParams(row, col);
        return !openedStatusArr[getIndx(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkParams(row, col);
        return fullStatusArr[getIndx(row, col)];
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolating;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation q = new Percolation(5);

        q.open(1, 1);
        q.open(2, 1);
        System.out.println(q.isOpen(2, 1));
        System.out.println(q.isFull(2, 1));

        q.open(1, 4);
        q.open(2, 4);
        q.open(3, 4);
        q.open(3, 5);
        q.open(4, 5);
        q.open(5, 5);
        System.out.println(q.percolates());

        q.open(5, 1);
        System.out.println(q.isFull(5, 1));

    }
}