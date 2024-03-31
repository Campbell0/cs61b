import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;


public class Percolation {
    // TODO: Add any necessary instance variables.
    int[][] grid;
    int openSite;
    WeightedQuickUnionUF set;
    List<Integer> virtualTopSite = new ArrayList<>();
    List<Integer> virtualBottomSite = new ArrayList<>();
    int[][] neighborSite = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = -1;
            }
        }
        openSite = 0;
        set = new WeightedQuickUnionUF(grid.length * grid.length);
    }

    public void open(int row, int col) {
        grid[row][col] = 0;
        openSite++;
        connectNeighborSites(row, col);
        if (row == 0) {
            setFull(row, col);
            virtualTopSite.add(xyTo1D(row, col));
        } else if (row == grid.length - 1) {
            virtualBottomSite.add(xyTo1D(row, col));
        }
        liquidFlow(row, col);
    }

    public boolean isOpen(int row, int col) {
        return grid[row][col] >= 0;
    }

    public boolean isFull(int row, int col) {
        return grid[row][col] >= 1;
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public void setFull(int row, int col) {
        if (checkValidSite(row, col) && isOpen(row, col)) {
            grid[row][col] = 1;
        }
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
            }
        }

    }
    public void liquidFlow(int r, int c) {
        int neighborRow;
        int neighborCol;
        for (int[] ints : neighborSite) {
            neighborRow = r + ints[0];
            neighborCol = c + ints[1];
            if (checkValidSite(neighborRow, neighborCol) && isFull(neighborRow, neighborCol)) {
                setFull(r, c);
                break;
            }
        }
        if (isFull(r, c)) {
            int root = set.find(xyTo1D(r, c));
            for (int row = 1; row < grid.length; ++row) {
                for (int col = 0; col < grid.length; ++col) {
                    if (set.find(xyTo1D(row, col)) == root) {
                        setFull(row, col);
                    }
                }
            }
        }
    }

    public boolean percolates() {
        for (int top : virtualTopSite) {
            for (int bottom : virtualBottomSite) {
                if (set.connected(top, bottom)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkValidSite(int row, int col) {
        return (row >= 0 && row < grid.length && col >= 0 && col < grid.length);
    }
    public int xyTo1D(int row, int col) {
        return row * grid.length + col;
    }


}
