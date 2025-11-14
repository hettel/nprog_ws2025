package part1.ch14_activity;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelSearch {
	
	private static class Task implements Callable<String> {

		@Override
		public String call() throws Exception {
			System.out.println("Start searching");
			long startTime = System.currentTimeMillis();

			long counter = 0;
			String str = "";
			while (true) {
				str = Util.getRandomString(100);
				int hashValue = str.hashCode();
				counter++;
				
				if( Thread.currentThread().isInterrupted() )
				{
					System.out.println("Bye");
					return null;
				}

				if (0 < hashValue && hashValue < 200) {
					break;
				}
			}

			long endTime = System.currentTimeMillis();
			System.out.println("Found hash value : " + str.hashCode());
			System.out.println("Number of tries  : " + counter);

			System.out.println("Time elapsed     : " + (endTime - startTime) + " [ms]");
			return str;
		}
	}

	public static void main(String[] args) throws Exception
	{
		int numOfProc = Runtime.getRuntime().availableProcessors();
		
		ExecutorService executor = Executors.newFixedThreadPool(numOfProc);
		CompletionService<String> service = new ExecutorCompletionService<>(executor);
		
		for(int i=0; i< numOfProc; i++)
		{
			Task task = new Task();
			service.submit(task);
		}
		
		String str = service.take().get();
		System.out.println("String " + str);
		System.out.println("hash " + str.hashCode() );
		
		
		executor.shutdownNow();
	}
}
