package part1.ch06_activity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public class Main {

	public static void main(String[] args) throws IOException
	{
		Path path = Paths.get("odyssey.txt") ;
		String content = Files.readString( path , Charset.forName ("UTF8") ) ;
		content = content.replace("-", "");
		String words[ ] = content.split( "\\s+" ) ;
		
		long count = content.chars ( ).filter( Character::isLetter ).count ( ) ;
		
		BinaryOperator<String> maxlenSuche = (left, right) -> left.length() > right.length() ? left: right;
		
		
		String maxLenString =
		Arrays.stream(words)
		      .parallel()
		      .map( a -> new String(a) )
		      .reduce("", maxlenSuche );
		
		
		System.out.println(" Number of characters " + count );
		System.out.println(" Number of words " + words.length) ;
		
		System.out.println("MaxLen String" + maxLenString);

	}

}
