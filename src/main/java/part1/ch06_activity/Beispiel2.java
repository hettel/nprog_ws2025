package part1.ch06_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public class Beispiel2 {

	public static void main(String[] args) throws IOException
	{
		Path path = Paths.get("odyssey.txt") ;
		String content = Files.readString( path , Charset.forName ("UTF8") ) ;
		long count = content.chars ( ).filter( Character::isLetter ).count ( ) ;
		
		
		long countVokale = 
		content.chars()
		       .filter( c -> "aeiouAEIOU".indexOf(c) >= 0 )
		       .count();
		       //.reduce(0, (links,oben) -> links+1 );
		
		
		System.out.println("Anzahl der Buchstaben " + count );
		System.out.println("Anzahl der Vokale: " + countVokale);
		
	}

}

