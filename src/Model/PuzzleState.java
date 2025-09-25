package Model;

import java.util.*;

public class PuzzleState {
    public int[] board;
    public int blankIndex;
    public PuzzleState parent;
    public int g;
    public int h;

    public PuzzleState(int[] board, int g, PuzzleState parent) {
        this.board = board.clone();
        this.parent = parent;
        this.g = g;
        this.blankIndex = findBlank();
        this.h = calculateManhattanDistance();
    }

    private int findBlank() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }

    public int f() {
        return g + h;
    }

    public boolean isGoal() {
        for (int i = 0; i < 8; i++) {
            if (board[i] != i + 1) return false;
        }
        return board[8] == 0;
    }

    public List<PuzzleState> getSuccessors() {
        List<PuzzleState> successors = new ArrayList<>();
        int row = blankIndex / 3;
        int col = blankIndex % 3;

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int d = 0; d < 4; d++) {
            int newRow = row + dRow[d];
            int newCol = col + dCol[d];

            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
                int newIndex = newRow * 3 + newCol;
                int[] newBoard = board.clone();

                // swaps blank with new tile
                newBoard[blankIndex] = newBoard[newIndex];
                newBoard[newIndex] = 0;

                successors.add(new PuzzleState(newBoard, g + 1, this));
            }
        }

        return successors;
    }

    private int calculateManhattanDistance() {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0) {
                int targetRow = (board[i] - 1) / 3;
                int targetCol = (board[i] - 1) % 3;
                int currentRow = i / 3;
                int currentCol = i % 3;

                distance += Math.abs(targetRow - currentRow) + Math.abs(targetCol - currentCol);
            }
        }
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PuzzleState) {
            return Arrays.equals(board, ((PuzzleState) o).board);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}
