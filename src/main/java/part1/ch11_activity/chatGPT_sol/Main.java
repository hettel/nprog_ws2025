package part1.ch11_activity.chatGPT_sol;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) 
    {
        //if (args.length != 1) {
        //    System.out.println("Verwendung: java Main <pfad/zur/sudoku_datei.txt>");
        //    return;
        //}

        SudokuBoard board = new SudokuBoard();
        try {
        	// Jhe
        	String path = "/Users/joerg/Seafile/Vorlesungskripte/Bachelor/NProg/Working/nprog_ws2025/board01.txt";
            board.loadFromFile( path );
            System.out.println("Eingelesenes Sudoku:");
            board.printBoard();

            //Jhe
            long start = System.currentTimeMillis();
            
            ForkJoinPool pool = new ForkJoinPool();
            SudokuSolverTask solverTask = new SudokuSolverTask(board);

            System.out.println("\nLöse Sudoku...");
            SudokuBoard solved = pool.invoke(solverTask);
            
            //Jhe
            long end = System.currentTimeMillis();
            System.out.println("Dauer: " + (end-start) + "[ms]");

            if (solved != null && solved.isComplete()) {
                System.out.println("\n✅ Lösung gefunden:");
                solved.printBoard();
            } else {
                System.out.println("\n❌ Keine Lösung gefunden.");
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Einlesen der Datei: " + e.getMessage());
        }
    }
}

