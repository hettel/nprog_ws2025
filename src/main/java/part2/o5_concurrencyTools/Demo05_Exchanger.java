package part2.o5_concurrencyTools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Demo05_Exchanger
{
  static class Task implements Runnable
  {
    private final Exchanger<Integer> exchanger;
    
    public Task(Exchanger<Integer> exchanger)
    {
      this.exchanger = exchanger;
    }
   
    @Override
    public void run()
    {
      try
      {
        for(int i=0; i<10; i++)
        {
          int produced = ThreadLocalRandom.current().nextInt(100);
          System.out.println("Waiting for exchange " + Thread.currentThread().getName());
          int received = exchanger.exchange(produced);
          System.out.printf(" %2d --> | <-- %2d \n", produced, received );
          sleep( ThreadLocalRandom.current().nextInt(500,1500), TimeUnit.MILLISECONDS);
        }
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      } 
    }
  }
  
  public static void main(String[] args) throws InterruptedException
  {
    Exchanger<Integer> exchanger = new Exchanger<>();
    
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.execute( new Task(exchanger) );
    executor.execute( new Task(exchanger) );

    executor.shutdown();
  }
  
  private static void sleep(int time, TimeUnit unit) 
  {
    try
    {
      unit.sleep(time);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
