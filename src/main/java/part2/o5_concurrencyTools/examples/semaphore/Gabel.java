package part2.o5_concurrencyTools.examples.semaphore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Gabel {
	
	private final Lock lock = new ReentrantLock();
	
	public void nehmen()
	{
		lock.lock();
	}
	
	public void zuruecklegen()
	{
		lock.unlock();
	}

}
