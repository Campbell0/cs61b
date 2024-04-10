import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;


public class Percolation {
    // TODO: Add any necessary instance variables.
    boolean[][] grid;
    int size;
    int openSite;
    // create two set, one for check percolation, another for judge is a site is full.
    WeightedQuickUnionUF setForFlow;
    WeightedQuickUnionUF set;
    int[][] neighborSite = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        size = N;
        grid = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }
        openSite = 0;
        set = new WeightedQuickUnionUF(size * size + 2);
        setForFlow = new WeightedQuickUnionUF(size * size + 1);
    }

    public void open(int row, int col) {
        if (!checkValidSite(row, col)) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        grid[row][col] = true;
        openSite++;
        // connect current site with neighbor site.
        connectNeighborSites(row, col);
        // key: don't add bottom site to setForFlow, so that won't happen backwash.
        if (row == 0) {
            set.union(0, xyTo1D(row, col));
            setForFlow.union(0, xyTo1D(row, col));
        }
        if (row == size - 1) {
            set.union(size * size + 1, xyTo1D(row, col));
        }
        connectNeighborSites(row, col);
    }

    public boolean isOpen(int row, int col) {
        if (!checkValidSite(row, col)) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (!checkValidSite(row, col)) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return isOpen(row, col) && setForFlow.connected(xyTo1D(row, col), 0);
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public void connectNeighborSites(int row, int col) {
        int neighborRow;
        int neighborCol;
        for (int[] ints : neighborSite) {
            neighborRow = row + ints[0];
            neighborCol = col + ints[1];
            if (checkValidSite(neighborRow, neighborCol) && isOpen(neighborRow, neighborCol)) {
                int currentSite = xyTo1D(row, col);
                int neighborSite = xyTo1D(neighborRow, neighborCol);
                set.union(currentSite, neighborSite);
                setForFlow.union(currentSite, neighborSite);
            }
        }

    }

    public boolean percolates() {
        return set.connected(0, size * size + 1);
    }

    public boolean checkValidSite(int row, int col) {
        return (row >= 0 && row < grid.length && col >= 0 && col < grid.length);
    }
    public int xyTo1D(int row, int col) {
        return row * grid.length + col;
    }


}
