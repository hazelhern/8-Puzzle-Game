package Model;
import Model.PuzzleState;
import java.util.*;

public class PuzzleSolver {
    public static List<int[]> solve(int[] initialBoard) {
        PriorityQueue<PuzzleState> open = new PriorityQueue<>(Comparator.comparingInt(PuzzleState::f));
        Set<PuzzleState> closed = new HashSet<>();

        PuzzleState start = new PuzzleState(initialBoard, 0, null);
        open.add(start);

        while (!open.isEmpty()) {
            PuzzleState current = open.poll();

            if (current.isGoal()) {
                return reconstructPath(current);
            }

            closed.add(current);

            for (PuzzleState neighbor : current.getSuccessors()) {
                if (closed.contains(neighbor)) continue;
                open.add(neighbor);
            }
        }

        return null;
    }

    private static List<int[]> reconstructPath(PuzzleState state) {
        List<int[]> path = new ArrayList<>();
        while (state != null) {
            path.add(state.board);
            state = state.parent;
        }
        Collections.reverse(path);
        return path;
    }
}

