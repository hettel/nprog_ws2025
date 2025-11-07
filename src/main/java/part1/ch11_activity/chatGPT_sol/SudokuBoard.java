package part1.ch11_activity.chatGPT_sol;

import java.io.*;
import java.util.*;

public class SudokuBoard {
    private final int[][] board = new int[9][9];

    // Lädt Sudoku aus einer Textdatei
    public void loadFromFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 9) {
                String[] tokens = line.trim().split("\\s+");
                for (int col = 0; col < 9; col++) {
                    if (tokens[col].equals("-")) {
                        board[row][col] = 0;
                    } else {
                        board[row][col] = Integer.parseInt(tokens[col]);
                    }
                }
                row++;
            }
        }
    }

    public int get(int row, int col) {
        return board[row][col];
    }

    public void set(int row, int col, int value) {
        board[row][col] = value;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == 0;
    }

    public boolean isValid(int row, int col, int num) {
        // Zeile prüfen
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num) return false;
        }

        // Spalte prüfen
        for (int x = 0; x < 9; x++) {
            if (board[x][col] == num) return false;
        }

        // 3x3 Block prüfen
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }

        return true;
    }

    public SudokuBoard copy() {
        SudokuBoard copy = new SudokuBoard();
        for (int i = 0; i < 9; i++) {
            System.arraycopy(this.board[i], 0, copy.board[i], 0, 9);
        }
        return copy;
    }

    public void printBoard() {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                System.out.print((board[i][j] == 0 ? "-" : board[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0) return false;
        return true;
    }
}
