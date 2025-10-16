package part1.ch04_activity;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Util
{
   public static double[][] getRandomMatrix(final int row, final int col)
   {     
      return IntStream.range(0, row).parallel()
               .mapToObj( _ -> ThreadLocalRandom.current().doubles(col).toArray() )
               .toArray( double[][]::new );
   }
   
   public static int numOfDivisors(int n)
   {
      assert (n >= 1);

      int count = 1;

      int divisor = 2;
      while (n > 1)
      {
         if (n % divisor == 0)
         {
            count++;
            n = n / divisor;
         }
         else
         {
            divisor++;
         }
      }

      return count;
   }

   public static int[] getDivisors(int n)
   {
      assert (n >= 1);

      List<Integer> divisors = new ArrayList<>();
      divisors.add(1);

      int divisor = 2;
      while (n > 1)
      {
         if (n % divisor == 0)
         {
            divisors.add(divisor);
            n = n / divisor;
         }
         else
         {
            divisor++;
         }
      }

      return divisors.stream().mapToInt(Integer::intValue).toArray();
   }
}
