package part2.o5_concurrencyTools.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class LatchExample_1 {

	public static void main(String[] args) 
	{
		CountDownLatch latch = new CountDownLatch(2);
		
		Runnable work1 = () -> { 
			
			doSomething();
			System.out.println("Prepare " + Thread.currentThread().getName());
			doSomething();
			latch.countDown();
			System.out.println("Prepare done "  + Thread.currentThread().getName());
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		Runnable work2 = () -> { 
			
			try {
				latch.await();
				System.out.println("Work");
				doSomething();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		};
		
		new Thread( work2 ).start();
		new Thread( work2 ).start();
		new Thread( work2 ).start();
		new Thread( work1 ).start();
		new Thread( work1 ).start();
		
		System.out.println("main done");
	}
	
	private static void doSomething()
	{
		try {
			TimeUnit.MILLISECONDS.sleep( 500 + ThreadLocalRandom.current().nextInt(2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
