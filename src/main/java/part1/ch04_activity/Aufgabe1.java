package part1.ch04_activity;

import java.util.stream.IntStream;


public class Aufgabe1 {

	public static void main(String[] args) 
	{
		System.out.println("Zahlen die durch 3 und 7 teilbar sind:");
		IntStream.range(1, 1_000)
				 .parallel()
				 .filter( i -> i%7 == 0 )
				 .filter( i -> i%3 == 0 )
				 .forEach( i -> System.out.println(i) );

	}

}
