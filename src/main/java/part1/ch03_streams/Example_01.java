package part1.ch03_streams;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class Example_01
{
   public static void main(String[] args)
   {
	  System.out.println("Start");
	  long start = System.currentTimeMillis();
      long count = IntStream.range(1_000_000, 2_000_000)
    		  				.parallel()
                            .mapToObj( BigInteger::valueOf ) 
                            .filter( bInt -> bInt.isProbablePrime(1000) )
                            .count();
      long end = System.currentTimeMillis();               
      
      System.out.println("Duration: " +(end-start) + " [ms]");
      System.out.println("Count : " + count );
   }
}
