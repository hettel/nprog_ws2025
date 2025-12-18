package part2.o5_concurrencyTools.examples.semaphore;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Main {

	public static void main(String[] args) 
	{
		int anzahlPhilisophen = 2;
		
		Semaphore semaphore = new Semaphore(anzahlPhilisophen-1, true);
		
		Philosoph[] philosophen = new Philosoph[ anzahlPhilisophen ];
		Gabel[] gabeln = new Gabel[ anzahlPhilisophen ];
		
		Arrays.setAll( gabeln, (_) -> new Gabel() );
		
		for(int i=0; i < anzahlPhilisophen; i++)
		{
			philosophen[i] = new Philosoph("P" + i, gabeln[i], gabeln[(i+1)%anzahlPhilisophen], semaphore);
			new Thread(philosophen[i]).start(); 
		}
	}

}
