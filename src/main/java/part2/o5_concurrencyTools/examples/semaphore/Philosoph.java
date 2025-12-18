package part2.o5_concurrencyTools.examples.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Philosoph implements Runnable 
{
	private final String name;
	private final Gabel linkeGabel;
	private final Gabel rechteGabel;
	private final Semaphore semaphore;
	
	public Philosoph(String name, Gabel links, Gabel rechts, Semaphore semaphore)
	{
		this.name = name;
		this.linkeGabel = links;
		this.rechteGabel = rechts;
		this.semaphore = semaphore;
	}

	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				// denken
				int sleepDenken = ThreadLocalRandom.current().nextInt(10);
				System.out.println( name + " denkt");
				TimeUnit.MILLISECONDS.sleep(sleepDenken);
				
				// essen
				// 1. Hole Gabeln
				semaphore.acquire();
				
				linkeGabel.nehmen();
				rechteGabel.nehmen();
				// 2. essen
				System.out.println( name + " isst");
				int sleepEssen = ThreadLocalRandom.current().nextInt(10);
				TimeUnit.MILLISECONDS.sleep(sleepEssen );
				// 3. Gabeln zurücklegen
				rechteGabel.zuruecklegen();
				linkeGabel.zuruecklegen();
				
				semaphore.release();
			}
		}
		catch(Exception exce)
		{
			exce.printStackTrace();
		}
		
	}

}
