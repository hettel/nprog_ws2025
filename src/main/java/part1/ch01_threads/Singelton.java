package part1.ch01_threads;

public class Singelton 
{
	private static Singelton instance = null;
	
	public static Singelton getInstance()
	{
		if( instance == null )
		{
			synchronized (instance) {
				if( instance == null)
				{
					instance = new Singelton();
				}
			}
			
		}
		
		return instance;
	}
	
	private Singelton()
	{
		super();
		
	}
}
