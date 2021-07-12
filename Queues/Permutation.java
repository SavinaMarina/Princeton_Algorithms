import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        if (args.length == 0) return;
        int k = Integer.parseInt(args[0]);
        if (k == 0) return;
        int m = 0;

        RandomizedQueue<String> q = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            // StdOut.println("string = " + s);
            int size = q.size();
            if (size == 0) {
                q.enqueue(s);
            }
            else {
                int x = (int) (Math.random()*(size+1)); //StdRandom.uniform(size + 1);//(int) (Math.random()*(size+1));
                 StdOut.println("x = " + x);
                if (x <= size) {
                    if (size >= k) q.dequeue();
                    q.enqueue(s);
                }
            }
            for (String s1 : q)
                StdOut.println(s1);
            m++;
        }

        // StdOut.println(q.size());

        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }

    }

    /* public static void main(String[] args) {
        if (args.length == 0) return;
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            q.enqueue(s);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }
    } */
}
