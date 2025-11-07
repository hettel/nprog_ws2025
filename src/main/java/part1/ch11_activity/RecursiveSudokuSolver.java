package part1.ch11_activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import part1.ch11_activity.SudokuBoard.FieldPosition;
import part1.ch11_activity.SudokuBoard.FieldValue;


public class RecursiveSudokuSolver
{

  public static void main(String[] args) throws Exception
  {
    File file = new File("board02.txt");
    SudokuBoard board = SudokuBoard.load(file);
    board.print();

    System.out.println("Solve game: ");

    long startTime = System.currentTimeMillis();
    
    SudokuSolveTask root = new SudokuSolveTask(board);
    ForkJoinPool.commonPool().invoke(root);
    
    long endTime = System.currentTimeMillis();
    System.out.println("Elapsed time " + (endTime - startTime) + " [ms]");
    System.out.println("Found " + root.join().size() + " solutions");
    root.join().get(0).print();

    System.out.println("done sequential");
  }
  
  @SuppressWarnings("serial")
  private static class SudokuSolveTask extends RecursiveTask<List<SudokuBoard>>
  {
	  
	 private final SudokuBoard board;
	 
	 
	public SudokuSolveTask(SudokuBoard board) {
		super();
		this.board = board;
	}



	@Override
	protected List<SudokuBoard> compute() 
	{
		if (board.isComplete())
	    {
	      return List.of(board);
	    }

	    FieldPosition nextFreeField = board.getFreePosition();
	    //FieldPosition nextFreeField = board.getFreePositionWithLeastCandidates();
	    Set<FieldValue> candidates = board.getValueCandidates(nextFreeField.row, nextFreeField.col);

	    if (candidates.isEmpty())
	    {
	      return Collections.emptyList();
	    }
	    
	    
	    List<SudokuBoard> resultList = new ArrayList<>();
	    List<SudokuSolveTask> tasks = new ArrayList<>();
	    for (FieldValue field : candidates)
	    {
	      SudokuBoard newBoard = SudokuBoard.copy(board);
	      newBoard.setValue(nextFreeField.row, nextFreeField.col, field);
	      newBoard.pack();
	      
	      SudokuSolveTask task = new SudokuSolveTask(newBoard);
	      tasks.add(task);  
	    }
	    
	    invokeAll( tasks );
	    
	    for( SudokuSolveTask task: tasks)
	    {
	    	resultList.addAll(task.join());
	    }

	    return resultList;
	    
	}
	  
  }

}
