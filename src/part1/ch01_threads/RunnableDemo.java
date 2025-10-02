package part1.ch01_threads;

import java.util.concurrent.TimeUnit;

public class RunnableDemo
{
    private static class Task implements Runnable
    {
        @Override
        public void run()
        {
            System.out.println("Running Task: "  + Thread.currentThread().getName() );

            try{
                TimeUnit.MILLISECONDS.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            System.out.println("done: "  + Thread.currentThread().getName() );
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("start " + Thread.currentThread().getName());

        Thread t1 = new Thread(new Task(), "Task1");
        Thread t2 = new Thread(new Task(), "Task2");
        Thread t3 = new Thread(new Task());

        t1.start();
        t2.start();
        t3.start();

        // Warte auf Ende der Threads
        t1.join();
        t2.join();
        t3.join();

        System.out.println("done " + Thread.currentThread().getName());
    }
}
