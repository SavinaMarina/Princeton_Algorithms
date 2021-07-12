/**
Monte Carlo simulation.

To estimate the percolation threshold, consider the following computational experiment:
    Initialize all sites to be blocked.
    Repeat the following until the system percolates:
        Choose a site uniformly at random among all blocked sites.
        Open the site.

The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.

main() method takes two command-line arguments n and T, performs T independent computational experiments (discussed above) on an n-by-n grid, and prints the sample mean, sample standard deviation, and the 95% confidence interval for the percolation threshold. Use StdRandom to generate random numbers;
use StdStats to compute the sample mean and sample standard deviation.

 */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double[] t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) throw new IllegalArgumentException("arguments must be positive");
        t = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation q = new Percolation(n);
            while (!q.percolates()) {
                q.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            double threshold = (double) q.numberOfOpenSites()/(n*n);
            t[i] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(t);

    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(t);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean()-CONFIDENCE_95*stddev()/ Math.sqrt(t.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean()+CONFIDENCE_95*stddev()/Math.sqrt(t.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) return;
        PercolationStats p = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + p.mean());
        System.out.println("stddev                  = " + p.stddev());
        System.out.println("95% confidence interval = [" + p.confidenceLo()+","+p.confidenceHi()+"]");
    }

}