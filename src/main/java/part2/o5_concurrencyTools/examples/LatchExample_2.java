package part2.o5_concurrencyTools.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class LatchExample_2 {

	public static void main(String[] args) 
	{
		CountDownLatch latch = new CountDownLatch(3);
		
		Runnable work = () -> { 
			
			System.out.println("Prepare " + Thread.currentThread().getName());
			doSomething();
			
			System.out.println("Waiting " + Thread.currentThread().getName());
			latch.countDown();
			try {
				latch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Work "  + Thread.currentThread().getName());
			
		};
		
		
		new Thread( work ).start();
		new Thread( work ).start();
		new Thread( work ).start();
		new Thread( work ).start();
		new Thread( work ).start();
		
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
