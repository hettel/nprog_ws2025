package part1.ch06_activity;


import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Aufgabe1 {

	public static void main(String[] args)
	{
		
		int sum = 1;
		for (int i=1; i <= 10; i++) 
		{
			sum = sum * i;
		}
		System.out.println("Summe 1 : " + sum );
		
		int result =  IntStream.rangeClosed(1, 10)
				               .reduce(0, (i,j) -> i * j);

		System.out.println("Summe 2 : " + result );
	}

}
