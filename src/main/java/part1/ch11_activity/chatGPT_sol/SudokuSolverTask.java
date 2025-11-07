package part1.ch11_activity.chatGPT_sol;

import java.util.concurrent.RecursiveTask;

public class SudokuSolverTask extends RecursiveTask<SudokuBoard> {
    private final SudokuBoard board;

    public SudokuSolverTask(SudokuBoard board) {
        this.board = board;
    }

    @Override
    protected SudokuBoard compute() {
        // Nächste leere Zelle finden
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.isEmpty(row, col)) {
                    // Für jede mögliche Zahl versuchen
                    for (int num = 1; num <= 9; num++) {
                        if (board.isValid(row, col, num)) {
                            SudokuBoard newBoard = board.copy();
                            newBoard.set(row, col, num);

                            SudokuSolverTask task = new SudokuSolverTask(newBoard);
                            task.fork();
                            SudokuBoard result = task.join();
                            if (result != null && result.isComplete()) {
                                return result;
                            }
                        }
                    }
                    // Keine Lösung möglich
                    return null;
                }
            }
        }
        // Kein leeres Feld mehr: Lösung gefunden
        return board;
    }
}

