package part1.ch06_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public class Beispiel3 {

	public static void main(String[] args) throws IOException
	{
		Path path = Paths.get("odyssey.txt") ;
		String content = Files.readString( path , Charset.forName ("UTF8") ) ;
		long count = content.chars ( ).filter( Character::isLetter ).count ( ) ;
		
		String words[ ] = content.split( "\\s+|;|\\.|,|-|/") ;
		
		long len =
		Arrays.stream(words)
		      .mapToInt( s -> s.length() )
		      .sum();
		      //.reduce(0, (links,oben) -> links + oben );
		
		System.out.println("len : " + len );
		System.out.println("count : " + count );

		long len2 = Arrays.stream(words) 
				          .parallel()
				          .reduce(0, (links, oben ) -> links+oben.length(), (a,b)-> a + b );
		
		System.out.println("len2 " + len2 );
		
	}

}
