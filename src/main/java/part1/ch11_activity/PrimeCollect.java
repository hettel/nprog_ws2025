package part1.ch11_activity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimeCollect 
{
	@SuppressWarnings("serial")
	private static class PrimeCountTask extends RecursiveTask<  List<Integer> >
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
		protected List<Integer> compute() 
		{
			if( end - start < 50  )
			{
				List<Integer> primes = new ArrayList<>();
				for( int i=start; i < end; i++)
				{
					if( BigInteger.valueOf(i).isProbablePrime(100) )
					{
						primes.add(i);
					}
				}
				
				return primes;
				
			}
			else
			{
				int mid = (start + end)/2;
				PrimeCountTask leftTask = new PrimeCountTask(start, mid);
				PrimeCountTask rightTask = new PrimeCountTask(mid, end);
				
				invokeAll( leftTask, rightTask );
				
				List<Integer> primes = leftTask.join();
				primes.addAll( rightTask.join() );
				
				return primes;
				
			}
		}
		
	}

	public static void main(String[] args) 
	{
		ForkJoinPool executor = new ForkJoinPool(8);
		
		PrimeCountTask rootTask = new PrimeCountTask(1_000_000, 2_000_000);
		executor.invoke(rootTask);
		
		System.out.println("Anzahl der Primzahlen: " + rootTask.join().size() );
		System.out.println("Anzahl der Taskobjekte: " + PrimeCountTask.counter.get() );

	}

}
