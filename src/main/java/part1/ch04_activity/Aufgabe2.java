package part1.ch04_activity;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;


public class Aufgabe2 {

	public static void main(String[] args) 
	{
		IntConsumer out = (i) -> System.out.println(i);
		IntPredicate filter5Teiler = i -> Util.numOfDivisors(i) == 5;
		
		
		System.out.println("Zahlen die 5 Teiler besitzen:");
		IntStream.range(1, 1_000)
				 .parallel()
				 .filter( filter5Teiler )
				 .forEach( out );

	}

}
