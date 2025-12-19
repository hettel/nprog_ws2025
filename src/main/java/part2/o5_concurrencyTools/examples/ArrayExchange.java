package part2.o5_concurrencyTools.examples;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayExchange
{
   static class CollectSmallerElements implements Runnable
   {
      private final int[] array;
      private final Exchanger<OptionalInt> exchanger;
      
      public CollectSmallerElements(int[] array, Exchanger<OptionalInt> exchanger)
      {
         super();
         this.array = array;
         this.exchanger = exchanger;
         
         Arrays.sort( this.array );
      }

      @Override
      public void run()
      {
         try 
         {
            for(int i = array.length-1; i >= 0; i--)
            {
               OptionalInt myOptVal = OptionalInt.of( array[i] );
               OptionalInt otherOptVal = exchanger.exchange( myOptVal );
               
               if( otherOptVal.isEmpty() )
               {
                  Arrays.sort( this.array );
                  System.out.println("Smaller Elements: " + Arrays.toString(array));
                  return;
               }
               
               if( otherOptVal.getAsInt() < array[i] )
               {
                  array[i] = otherOptVal.getAsInt();
               }
               else
               {
                  Arrays.sort( this.array );
                  System.out.println("Smaller Elements: " + Arrays.toString(array));
                  return;
               }
            }
            
            exchanger.exchange( OptionalInt.empty() );
            Arrays.sort( this.array );
            System.out.println("Smaller Elements: " + Arrays.toString(array));
         }
         catch(Exception exce)
         {
            exce.printStackTrace();
         }
      }
      
   }
   
   static class CollectGreaterElements implements Runnable
   {
      private final int[] array;
      private final Exchanger<OptionalInt> exchanger;
      
      public CollectGreaterElements(int[] array, Exchanger<OptionalInt> exchanger)
      {
         super();
         this.array = array;
         this.exchanger = exchanger;
         
         Arrays.sort( this.array );
      }

      @Override
      public void run()
      {
         try 
         {
            for(int i = 0; i < array.length; i++)
            {
               OptionalInt myOptVal = OptionalInt.of( array[i] );
               OptionalInt otherOptVal = exchanger.exchange( myOptVal );
               
               if( otherOptVal.isEmpty() )
               {
                  Arrays.sort( this.array );
                  System.out.println("Greater Elements: " + Arrays.toString(array));
                  return;
               }
               
               if( otherOptVal.getAsInt() > array[i] )
               {
                  array[i] = otherOptVal.getAsInt();
               }
               else
               {
                  Arrays.sort( this.array );
                  System.out.println("Greater Elements: " + Arrays.toString(array));
                  return;
               }
            }
            
            exchanger.exchange( OptionalInt.empty() );
            Arrays.sort( this.array );
            System.out.println("Greater Elements " + Arrays.toString( this.array) );
         }
         catch(Exception exce)
         {
            exce.printStackTrace();
         }  
      }
   }
   
   public static void main(String[] args)
   {
      int[] array1 = createRandomIntArray(8);
      int[] array2 = createRandomIntArray(10);
      
      System.out.println("Array 1: " + Arrays.toString(array1));
      System.out.println("Array 2: " + Arrays.toString(array2));
      
      Exchanger<OptionalInt> exchanger = new Exchanger<OptionalInt>();
      
      
      CollectSmallerElements task1 = new CollectSmallerElements(array1,exchanger);
      CollectGreaterElements task2 = new CollectGreaterElements(array2,exchanger);
      
      new Thread( task1 ).start();
      new Thread( task2 ).start();
      
      System.out.println("main done");
   }
   
   private static int[] createRandomIntArray(int len)
   {
      return ThreadLocalRandom.current().ints(len, -100, 100).toArray();
   }
}
