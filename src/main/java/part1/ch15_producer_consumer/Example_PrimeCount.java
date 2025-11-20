package part1.ch15_producer_consumer;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example_PrimeCount 
{
	public static class Producer implements Runnable 
	{
		private final BlockingQueue<Optional<BigInteger>> outputQueue;
		private final int numOfEmpties;

		public Producer(BlockingQueue<Optional<BigInteger>> outputQueue, int empties) {
			super();
			this.outputQueue = outputQueue;
			this.numOfEmpties = empties;
		}

		@Override
		public void run() {
			try {
				for (int i = 1_000_000; i < 2_000_000; i++) {
					BigInteger bInt = BigInteger.valueOf(i);
					outputQueue.put( Optional.of( bInt ));
				}
				
				for(int i=0; i< numOfEmpties; i++)
				{	
					outputQueue.put( Optional.empty() );
				}
				
				System.out.println("producer done");
			} catch (Exception exce) {
				exce.printStackTrace();
			}
		}

	}

	public static class PrimeTester implements Runnable {

		private final BlockingQueue<Optional<BigInteger>> inputQueue;
		private final BlockingQueue<Optional<Boolean>> outputQueue;
		
		public PrimeTester(BlockingQueue<Optional<BigInteger>> inputQueue, BlockingQueue<Optional<Boolean>> outputQueue) {
			super();
			this.inputQueue = inputQueue;
			this.outputQueue = outputQueue;
		}

		@Override
		public void run() {

			try {
				
				while(true)
				{
					Optional<BigInteger> oBigInt = inputQueue.take();
					if( oBigInt.isEmpty() )
					{
						outputQueue.put( Optional.empty() );
						break;
					}
					
					BigInteger bigInt = oBigInt.get();
					boolean test = bigInt.isProbablePrime(1000);
					outputQueue.put( Optional.of(test) );
					
				}
				System.out.println("tester done");
				
			}catch(Exception exce)
			{
				exce.printStackTrace();
			}
		}

	}

	public static class PrimeCounter implements Runnable 
	{
		private final BlockingQueue<Optional<Boolean>> inputQueue;
		private final int numofEmpties;
		
		
		public PrimeCounter(BlockingQueue<Optional<Boolean>> inputQueue, int numOfEmties) {
			super();
			this.inputQueue = inputQueue;
			this.numofEmpties = numOfEmties;
		}



		@Override
		public void run() 
		{
			try {
				int count = 0;
				int emtpyCount = 0;
				while( true )
				{
					Optional<Boolean> oBool = inputQueue.take();
					if( oBool.isEmpty() )
					{
						emtpyCount++;
						if( emtpyCount == numofEmpties )
							break;
						
					}
					else
					{
						if( oBool.get() == true )
						{
							count++;
						}
					}
				}
				
				System.out.println("Anzahl : " + count );
				
			}catch( Exception exce)
			{
				exce.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		BlockingQueue<Optional<BigInteger>> queueProd2Filter = new ArrayBlockingQueue<>(100);
		BlockingQueue<Optional<Boolean>>    queueFilter2Consumer = new ArrayBlockingQueue<>(100);
		
		Producer producer = new Producer(queueProd2Filter, 3);
		PrimeTester tester1 = new PrimeTester(queueProd2Filter, queueFilter2Consumer);
		PrimeTester tester2 = new PrimeTester(queueProd2Filter, queueFilter2Consumer);
		PrimeTester tester3 = new PrimeTester(queueProd2Filter, queueFilter2Consumer);
		PrimeCounter counter = new PrimeCounter(queueFilter2Consumer, 3);
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.execute(producer);
		executor.execute(tester1);
		executor.execute(tester2);
		executor.execute(tester3);
		executor.execute(counter);
		
		executor.shutdown();
		System.out.println("main done");

	}
}
