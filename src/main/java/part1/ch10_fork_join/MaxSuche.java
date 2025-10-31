package part1.ch10_fork_join;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MaxSuche {
	
	@SuppressWarnings("serial")
	public static class SearchTask extends RecursiveTask<Integer>
	{
		private final int[] values;
		private final int start;
		private final int end;
		

		public SearchTask(int[] values, int start, int end) {
			super();
			this.values = values;
			this.start = start;
			this.end = end;
		}



		@Override
		protected Integer compute() 
		{
			// Work phase
			if( end - start <= 2)
			{
				if( end- start == 1)
					return values[start];
				else
					return Math.max(values[start], values[start+1]);
			}
			
			
			
			int mid = (start+end)/2;
			
			// Divide phase
			SearchTask leftTasl = new SearchTask(values, start, mid);
			SearchTask rightTask = new SearchTask(values, mid, end );
			invokeAll( leftTasl, rightTask );
			
			
			return Math.max( leftTasl.join(), rightTask.join() );
		}
		
	}
	
	public static void main(String[] args) 
	{
		int[] values = new int[100];
		Random rand = new Random();
		
		for (int i=0; i<values.length; i++) 
		{
			values[i] = rand.nextInt(1000);
		}
		
		System.out.println("Ieratirv : " + max_itertiv(values));
		System.out.println("Rekursiv : " + max_rekursiv(values, 0, values.length));
		
		
		SearchTask rootTask = new SearchTask(values, 0, values.length);
		
		ForkJoinPool.commonPool().invoke( rootTask );
		
		System.out.println( "Max " + rootTask.join() );
		
	}

	public static int max_itertiv(int[] values )
	{
		int max = values[0];
		for(int i=1; i< values.length; i++)
		{
			if( values[i] > max )
				max = values[i];
		}
		
		return max;
	}
	
	
	public static int max_rekursiv(int[] values, int start, int end )
	{
		if( end - start <= 2)
		{
			if( end- start == 1)
				return values[start];
			else
				return Math.max(values[start], values[start+1]);
		}
		
		int mid = (start+end)/2;
		
		int valLeft = max_rekursiv( values, start, mid );
		int valRight = max_rekursiv( values, mid, end );
		return Math.max(valLeft, valRight);
		
	}
	
}
