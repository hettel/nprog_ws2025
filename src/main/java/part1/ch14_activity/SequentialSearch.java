package part1.ch14_activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SequentialSearch {

	private static class SearchTask implements Callable<String> 
	{
		private boolean isRunning = true;
		
		@Override
		public String call() {

			String str = "";
			while (isRunning) {
				str = Util.getRandomString(100);
				int hashValue = str.hashCode();
				
				if (0 < hashValue && hashValue < 500) {
					break;
				}
			}

			System.out.println("end " + isRunning );
			
			return str;
		}
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println("start");
		
		int numOfProc = Runtime.getRuntime().availableProcessors();
		
		ExecutorService executor = Executors.newFixedThreadPool(numOfProc);
		CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
		
		List<SearchTask> tasks = new ArrayList<>();
		for(int i=0; i<numOfProc;i++)
		{
			SearchTask task = new SearchTask();
			tasks.add(task);
			Future<String> future =  completionService.submit(task);
		}
		
		for(int i=0; i<2;i++)
		{
			System.out.println( completionService.take().get().hashCode() );
		}
		
		for( SearchTask task: tasks )
		{
			task.isRunning = false;
		}
		
		
		executor.shutdownNow();
		System.out.println("main done");
		
	}
}
