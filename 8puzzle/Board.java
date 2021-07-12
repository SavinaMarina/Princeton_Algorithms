import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {

    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = deepCopy(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + System.lineSeparator());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(tiles[i][j] + " ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = tiles[i][j];
                if (x == 0) continue;
                if (x != i*n + j + 1) c++;
            }
        }
        return c;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int s = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = tiles[i][j];
                if (x == 0) continue;
                x--;
                if (x != i*n + j) {
                    s = s + Math.abs(i - x / n) + Math.abs(j - x % n);
                }
            }
        }
        return s;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board by = (Board) y;
        if (this.dimension() != by.dimension()) return false;
        return Arrays.deepEquals(this.tiles, by.tiles);
    }

    private int[][] deepCopy(int[][] t) {
        int[][] ntiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ntiles[i][j] = t[i][j];
            }
        }
        return ntiles;
    }

    private Board getClone() {
        return new Board(deepCopy(this.tiles));
    }

    private void addNeighbor(Queue<Board> boards,
                             int blankSqRow, int blankSqCol,
                             int tileRow, int tileCol) {
        if ((tileRow >= 0) && (tileRow < n)
                && (tileCol >= 0) && (tileCol < n)) {
            Board c = getClone();
            c.tiles[blankSqRow][blankSqCol] = c.tiles[tileRow][tileCol];
            c.tiles[tileRow][tileCol] = 0;
            boards.enqueue(c);
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int blankRow = -1, blankCol = -1;
        outerloop:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = tiles[i][j];
                if (x == 0) {
                    blankRow = i;
                    blankCol = j;
                    break outerloop;
                }
            }
        }

        Queue<Board> boards = new Queue<>();
        addNeighbor(boards, blankRow, blankCol, blankRow + 1, blankCol);
        addNeighbor(boards, blankRow, blankCol, blankRow - 1, blankCol);
        addNeighbor(boards, blankRow, blankCol, blankRow, blankCol - 1);
        addNeighbor(boards, blankRow, blankCol, blankRow, blankCol + 1);
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board c = getClone();
        int firstTileRow = -1, firstTileCol = -1, secTileRow = -1, secTileCol = -1;
        outerloop:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    firstTileRow = i;
                    firstTileCol = j;
                    break outerloop;
                }
            }
        }

        outerloop:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((tiles[i][j] != 0) && ((i != firstTileRow) || (j != firstTileCol))) {
                    secTileRow = i;
                    secTileCol = j;
                    break outerloop;
                }
            }
        }

        int t = c.tiles[firstTileRow][firstTileCol];
        c.tiles[firstTileRow][firstTileCol] = c.tiles[secTileRow][secTileCol];
        c.tiles[secTileRow][secTileCol] = t;
        return c;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] arr = {{1, 2, 3}, {4, 0, 5}, {6, 7, 8}};
        int[][] arr2 = {{1, 2, 3}, {5, 0, 4}, {6, 7, 8}};
        Board b = new Board(arr);
        Board b2 = new Board(arr2);
        System.out.println(b.toString());
        System.out.println(b.hamming());
        System.out.println(b.equals(b2));
        int[][] arr3 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board b3 = new Board(arr3);
        System.out.println(b3.manhattan());
        System.out.println(b3.hamming());
        System.out.println("----------original-------------------");
        System.out.println(b3);
        System.out.println("----------neighbors-------------------");


        for (Board x: b3.neighbors()) {
            System.out.println(x.toString());
        }

        System.out.println("----------twin-------------------");
        int[][] arr4 = {{1, 0}, {3, 2}};
        Board b4 = new Board(arr4);
        System.out.println(b4.twin());


    }

}