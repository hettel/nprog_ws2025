package part1.ch11_activity;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Fibo 
{
	private static class FiboTask extends RecursiveTask<Long>
	{
		private final int n;
	
		public FiboTask(int n) {
			super();
			this.n = n;
		}


		@Override
		protected Long compute() 
		{
			if(n == 1 || n == 2 )
				return 1L;
			
			
			FiboTask left = new FiboTask(n-1);
			FiboTask right = new FiboTask(n-2);
			invokeAll( left, right);
			
			return left.join() + right.join();
		}}
	


	public static long fibo(int n)
	{
		assert( n > 0);
		
		if(n == 1 || n == 2 )
			return 1;
		
		return fibo(n-1) + fibo(n-2);
	}
	
	public static void main(String[] args) 
	{
		int n = 48;
		System.out.println("Berechne Fibo von " + n);
		
		long startTime = System.currentTimeMillis();
		
		//long result = fibo(n);
		
		FiboTask root = new FiboTask(n);
		ForkJoinPool.commonPool().invoke(root);
		long result = root.join();
		
		long endTime = System.currentTimeMillis();

		System.out.println("Ergebnis : " + result);
		System.out.println("Dauer    : " + (endTime-startTime) + "[ms]");
		
	}

}
