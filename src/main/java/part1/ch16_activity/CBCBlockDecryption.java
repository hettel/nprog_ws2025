package part1.ch16_activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CBCBlockDecryption 
{
	private static class Producer implements Runnable
	{
		private final BlockingQueue<Optional<Integer>> outQueue;
		private final String inputFile;
		
		public Producer(BlockingQueue<Optional<Integer>> outQueue, String inputFile) {
			super();
			this.outQueue = outQueue;
			this.inputFile = inputFile;
		}

		@Override
		public void run() 
		{
			try (InputStream input = new FileInputStream(inputFile)){				
				// read buffer (reading an int, 4 bytes)
		         byte[] block = new byte[4];  

		         int bytesRead = 0;
		         while (bytesRead != -1)
		         {
		            bytesRead = input.read(block);
		            if (bytesRead > 0)
		            {
		               int intValue = fromByteArray(block);
		               outQueue.put( Optional.of(intValue));
		            }
		         }
				
				outQueue.put( Optional.empty() );
			}
			catch(Exception exce)
			{
				exce.printStackTrace();
			}
			System.out.println("Producer done");
		}
		
		public int fromByteArray(byte[] bytes)
		{
		      return ByteBuffer.wrap(bytes).getInt();
		}
	}
	
	
	private static class Decrypter implements Runnable
	{
		private final BlockingQueue<Optional<Integer>>  inQueue;
		private final BlockingQueue<Optional<Integer>>  outQueue;
		private final int initVector;
		private final int shift;
		
		public Decrypter(int initVector, int shift, BlockingQueue<Optional<Integer>> inQueue, BlockingQueue<Optional<Integer>> outQueue) {
			super();
			this.initVector = initVector;
			this.shift = shift;
			this.inQueue = inQueue;
			this.outQueue = outQueue;
		}

		@Override
		public void run() 
		{
			try {
				int salt = initVector;
				while( true )
				{
					Optional<Integer> oInt = inQueue.take( );
					if( oInt.isEmpty() )
					{
						outQueue.put( Optional.empty() );
						break;
					}
					
					int item = oInt.get();
					
					// decrypt value using a simple Caesar cipher (shift 42)
					int value = (item - shift);
									
					// XOR operation
			        value = (salt ^ value); //xor operation
					salt = item;
			
			        outQueue.put( Optional.of(value));
				}
			}
			catch(Exception exce)
			{
				exce.printStackTrace();
			}
			System.out.println("Encrypter done");
		}
	}
	
	private static class Consumer implements Runnable
	{
		private final BlockingQueue<Optional<Integer>>  inQueue;
		private final String outputFile;
		
		public Consumer(BlockingQueue<Optional<Integer>> inQueue, String outputFile) {
			super();
			this.inQueue = inQueue;
			this.outputFile = outputFile;
		}

		@Override
		public void run() 
		{
			try (OutputStream output = new FileOutputStream(outputFile)){
			
				while( true )
				{
					Optional<Integer> oInt = inQueue.take( );
					if( oInt.isEmpty() )
						break;
					
	
					byte[] block = toByteArray(oInt.get());
		            output.write(block, 0, block.length);
				}	
			}
			catch(Exception exce)
			{
				exce.printStackTrace();
			}
			
			System.out.println("Consumer done");
		}
		
		
		public byte[] toByteArray(int value)
		{
		   return ByteBuffer.allocate(4).putInt(value).array();
		}
	}
	

	public static void main(String[] args) throws Exception
	{
		BlockingQueue<Optional<Integer>> queueProd2Enrcpt = new ArrayBlockingQueue<>(10_000);
		BlockingQueue<Optional<Integer>> queueEncrypt2Consumer = new ArrayBlockingQueue<>(10_000);
		
		String inputFileName = "./bilder/Eisvogel_encrypted.jpg";
		Producer produce = new Producer(queueProd2Enrcpt, inputFileName);
		
		final int InitVector = 47011;
		final int CaesarShift = 42;
		Decrypter decrypter = new Decrypter(InitVector, CaesarShift,queueProd2Enrcpt, queueEncrypt2Consumer);
		
		String encryptedFileName = "./bilder/Eisvogel_decrypted.jpg";
		Consumer consumer = new Consumer(queueEncrypt2Consumer, encryptedFileName);
		
		long startTime = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(produce);
		executor.submit(decrypter);
		Future<?> f1 = executor.submit(consumer);
		f1.get();
		
		
		executor.shutdown();
		//executor.awaitTermination(10, TimeUnit.SECONDS );
		long endTime = System.currentTimeMillis();
		System.out.println("Elapsed time for decryption " + (endTime - startTime) + "[ms]");
		System.out.println("main done");
	}

}
