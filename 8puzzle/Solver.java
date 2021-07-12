/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private SearchNode solution;
    private boolean isSolvable = false;

    private class SearchNode {
        private final Board board;
        private final int moves;
        private final SearchNode prevSearchNode;
        private final int manhattan;

        public SearchNode(Board board, int moves, SearchNode prevSearchNode) {
            this.board = board;
            this.moves = moves;
            this.prevSearchNode = prevSearchNode;
            this.manhattan = board.manhattan();
        }
    }

    private class ManhattanOrder implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            int m1 = o1.manhattan + o1.moves;
            int m2 = o2.manhattan + o2.moves;
            if (m1 == m2) return 0;
            if (m1 < m2) return -1;
            return 1;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("The argument is null!");
        SearchNode s = new SearchNode(initial, 0, null);
        if (initial.isGoal()) {
            solution = s;
            isSolvable = true;
            return;
        }
        SearchNode st = new SearchNode(initial.twin(), 0, null);
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new ManhattanOrder());
        MinPQ<SearchNode> pqt = new MinPQ<SearchNode>(new ManhattanOrder());
        pq.insert(s);
        pqt.insert(st);
        while (true) {
            SearchNode min = pq.delMin();
            // System.out.println("remove " + min.board);
            s = min;
            if (min.board.isGoal()) {
                solution = min;
                isSolvable = true;
                return;
            }

            SearchNode mint = pqt.delMin();
            st = mint;
            if (mint.board.isGoal()) {
                // if the twin is solvable then there is no solution for the initial board
                return;
            }

            for (Board x: s.board.neighbors()) {
                /* if ((s.prevSearchNode != null) && ((x.equals(s.prevSearchNode.board)))) {
                     System.out.println("skip " + x);
                }*/
                if ((s.prevSearchNode == null) || ((!x.equals(s.prevSearchNode.board)))) {
                    SearchNode sn = new SearchNode(x, s.moves + 1, s);
                    pq.insert(sn);
                    // System.out.println("insert" + x);
                }
            }
            for (Board x: st.board.neighbors()) {
                if ((st.prevSearchNode == null) || ((!x.equals(st.prevSearchNode.board)))) {
                    SearchNode snt = new SearchNode(x, st.moves + 1, st);
                    pqt.insert(snt);
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        if (solution != null) {
            return solution.moves;
        }
        return 0;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> boards = new Stack<>();
        boards.push(solution.board);
        SearchNode n = solution.prevSearchNode;
        while (n != null) {
            boards.push(n.board);
            n = n.prevSearchNode;
        }
        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
