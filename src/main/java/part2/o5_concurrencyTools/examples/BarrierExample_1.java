package part2.o5_concurrencyTools.examples;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BarrierExample_1
{

   public static void main(String[] args)
   {
      CyclicBarrier barrier = new CyclicBarrier(5, () -> {
         System.out.println("Barriere schaltet von " + Thread.currentThread().getName());
      });

      Runnable work = () -> {
         System.out.println("Prepare " + Thread.currentThread().getName());
         for (int i = 0; i < 10; i++) {

            try {
               // Sync Point
               barrier.await();
               System.out.println("Work " + Thread.currentThread().getName() + " i= " + i);
               doSomething();

            } catch (InterruptedException e) {
               e.printStackTrace();
            } catch (BrokenBarrierException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

         }

      };

      new Thread(work).start();
      new Thread(work).start();
      new Thread(work).start();
      new Thread(work).start();
      new Thread(work).start();

      System.out.println("main done");
   }

   private static void doSomething()
   {
      try {
         TimeUnit.MILLISECONDS.sleep(500 + ThreadLocalRandom.current().nextInt(2000));
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

}
