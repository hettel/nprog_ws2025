package part1.ch01_threads;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class CountTest

{
    private static class CountTask implements Runnable
    {
    	public static AtomicInteger summe = new AtomicInteger(0);
    	
        private int start;
        private int end;

        public CountTask(int start, int end)
        {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run()
        {
           

            //System.out.println("Start serach " + Thread.currentThread().getName());
            //long startTime = System.currentTimeMillis();
            for ( long candidate = start; candidate < end; candidate++ ) {
                BigInteger bInt = BigInteger.valueOf(candidate);
                if ( bInt.isProbablePrime(1000) ) {
                    summe.incrementAndGet();
                }
            }
            //long endTime = System.currentTimeMillis();

            
            //System.out.println("Elapsed time " + ( endTime - startTime ) + " [ms] from " + Thread.currentThread().getName());
            //System.out.println("Number of prims " + count + " " + Thread.currentThread().getName());
        }
    }


    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Num of cores : " + Runtime.getRuntime().availableProcessors());

        long start = System.currentTimeMillis();

        Thread t1 = new Thread(new CountTask(1_000_000, 1_250_000));
        Thread t2 = new Thread(new CountTask(1_250_000, 1_500_000));
        Thread t3 = new Thread(new CountTask(1_500_000, 1_750_000));
        Thread t4 = new Thread(new CountTask(1_750_000, 2_000_000));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        
        System.out.println("Summer " + CountTask.summe.get() );

        long end = System.currentTimeMillis();
        System.out.println("Elapsed time " + ( end - start ) + " [ms] from " + Thread.currentThread().getName());
    }

}




