package game2048;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    private final Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;


    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore) {
        board = new Board(rawValues);
        this.score = score;
        this.maxScore = maxScore;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists(board) || !atLeastOneMoveExists(board);
    }

    /** Checks if the game is over and sets the maxScore variable
     *  appropriately.
     */
    private void checkGameOver() {
        if (gameOver()) {
            maxScore = Math.max(score, maxScore);
        }
    }
    
    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        Integer size = b.size();
        for (int col = 0; col < size; ++col) {
            for (int row = 0; row < size; ++row) {
                Tile tile = b.tile(col, row);
                if (tile == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        Integer size = b.size();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Tile tile = b.tile(i, j);
                if (tile != null && tile.value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        // the case that exists empty space.
        if (emptySpaceExists(b)) {
            return true;
        }

        // the case that there are two adjacent tiles with the same value.
        Integer size = b.size();
        int[][] adjacents = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Tile tile = b.tile(i, j);

                for (int[] adjacent : adjacents) {
                    if (i - adjacent[0] > 0 && i - adjacent[0] < size
                        && j - adjacent[1] > 0 && j - adjacent[1] < size) {
                        Tile adjacentTile = b.tile(i - adjacent[0], j - adjacent[1]);
                        if (adjacentTile != null) {
                            if (adjacentTile.value() == tile.value()) {
                                return true;
                            }
                        }
                    }
                }

            }
        }
        return false;
    }

    /** Tilt the board toward SIDE.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public void tilt(Side side) {
        // TODO: Modify this.board (and if applicable, this.score) to account
        // for the tilt to the Side SIDE.
        Integer size = this.board.size();
        Board t = this.board;
        t.setViewingPerspective(side);

        for (int col = 0; col < size; ++col) {
            Integer maxRowCanMove = size - 1;
            for (int row = size - 2; row >=0; row--) {
                Tile tile = t.tile(col, row);
                if (tile != null) {
                    Integer rowToMove = findRowToMove(col, row, maxRowCanMove);
                    if (t.move(col, rowToMove, tile)) {
                        this.score += tile.value() * 2;
                        maxRowCanMove = rowToMove - 1;
                    }
                }
            }
        }
        t.setViewingPerspective(Side.NORTH);

        checkGameOver();
    }

    /** find the row of current tile need to move with the maxRowCanMove
     *
     * @param col
     * @param row
     * @param maxRowCanMove
     * @return rowToMove
     */
    public Integer findRowToMove(Integer col, Integer row, Integer maxRowCanMove) {
        Board t = this.board;
        Tile tileToMove = t.tile(col, row);
        Integer size = t.size();
        Integer rowToMove = row;
        for (int r = row + 1; r <= maxRowCanMove; ++r) {
            Tile tile = t.tile(col, r);
            if (tile == null || tile.value() == tileToMove.value()) {
                rowToMove = r;
            }
            else {
                return rowToMove;
            }
        }
        return rowToMove;
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

