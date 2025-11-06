package part1.ch11_activity;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimeCount 
{
	@SuppressWarnings("serial")
	private static class PrimeCountTask extends RecursiveTask<Integer>
	{       
		private static AtomicInteger counter = new AtomicInteger(0);
		
		private final int start;
        private final int end;
            
		public PrimeCountTask(int start, int end) 
		{
			super();
			this.start = start;
			this.end = end;
			
			counter.incrementAndGet();
		}


		@Override
		protected Integer compute() 
		{
			if( end - start < 100  )
			{
				int count = 0;
				for( int i=start; i < end; i++)
				{
					if( BigInteger.valueOf(i).isProbablePrime(100) )
					{
						count++;
					}
				}
				
				return count;
				
			}
			else
			{
				int mid = (start + end)/2;
				PrimeCountTask leftTask = new PrimeCountTask(start, mid);
				PrimeCountTask rightTask = new PrimeCountTask(mid, end);
				
				invokeAll( leftTask, rightTask );
				
				return leftTask.join() + rightTask.join();
				
			}
		}
		
	}

	public static void main(String[] args) 
	{
		ForkJoinPool executor = new ForkJoinPool(8);
		
		PrimeCountTask rootTask = new PrimeCountTask(1_000_000, 2_000_000);
		executor.invoke(rootTask);
		
		System.out.println("Anzahl der Primzahlen: " + rootTask.join() );
		System.out.println("Anzahl der Taskobjekte: " + PrimeCountTask.counter.get() );

	}

}
