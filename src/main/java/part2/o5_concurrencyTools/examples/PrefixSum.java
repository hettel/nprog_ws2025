package part2.o5_concurrencyTools.examples;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class PrefixSum
{
   static class Task implements Runnable
   {
      private final CyclicBarrier barrier;
      
      private final int[] array;
      private final int start;
      private final int end;
      
      private final int[] offsetArray;
      private final int offsetIdx;
      
      public Task(int[] array, int start, int end, int[] offsetArray, int offsetIdx, CyclicBarrier barrier)
      {
         super();
         this.array = array;
         this.start = start;
         this.end = end;
         this.offsetArray = offsetArray;
         this.offsetIdx = offsetIdx;
         this.barrier = barrier;
      }

      @Override
      public void run()
      {
         try
         {
            // Inclusive Prefix Summe
            inclusivePrefixSum(array, start, end);
            barrier.await();
            
            // Offset-Berechnung <= Schalfunktion der Barriere
            
            // Addiditon des Offsets 
            int offset = offsetArray[ offsetIdx ];
            for(int i=start; i< end; i++)
            {
               array[i] += offset;
            }
            barrier.await();
            
            System.out.println("Ausgabe " + Arrays.toString(array));
         }
         catch(Exception exce)
         {
            exce.printStackTrace();
         }
      }
      
   }
   
   
   

   public static void main(String[] args)
   {
      int[] array = createRandomIntArray(30);
      int[] copy = Arrays.copyOf(array, array.length);
      System.out.println("Original " + Arrays.toString(array));
      
      
      int numOfChunks = 4;
      int[] offsetArray = new int[numOfChunks];
      
      
      ExecutorService executor = Executors.newFixedThreadPool(numOfChunks );
      
      
      int chunkLen = array.length/numOfChunks;
      CyclicBarrier barrier = new CyclicBarrier( numOfChunks, 
            () -> { 
               for(int i=0; i < numOfChunks; i++)
               {
                  offsetArray[i] = (i < numOfChunks-1) ? array[(i+1)*chunkLen - 1] : array.length-1;
               }
               exclusivePrefixSum(offsetArray); } ); // Schaltfunktion
      
      for(int i=0; i < numOfChunks; i++ )
      {
         int start = i*chunkLen;
         int end = (i < numOfChunks-1) ? (i+1)*chunkLen : array.length;
         int offsetIdx = i;
         
         Task task = new Task(array, start, end, offsetArray, offsetIdx, barrier);
         executor.execute(task);
      }
     
      System.out.println();
      System.out.println("Check:");
      Arrays.parallelPrefix(copy, (i,j) -> i+j);
      System.out.println( Arrays.toString(copy ));
   }
   
   private static void inclusivePrefixSum(int[] array, int start, int end)
   {
      for(int i= start+1; i < end; i++)
      {
         array[i] += array[i-1];
      }
   }
   
   private static void exclusivePrefixSum(int[] array)
   {
      int tmp = array[0];
      array[0] = 0;
      for(int i=1; i<array.length; i++)
      {
         int ai = array[i];
         array[i] = array[i-1] + tmp;
         tmp = ai;
      }
   }
   
   private static int[] createRandomIntArray(int len)
   {
      return ThreadLocalRandom.current().ints(len, 0, 10).toArray();
   }
}
