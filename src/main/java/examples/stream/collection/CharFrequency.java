package examples.stream.collection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CharFrequency
{

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get( "Homer-Odyssey-UTF8-Coding.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      
      long count = content.chars().filter( Character::isLetter ).count();
      System.out.println("Number of characters " + count );
      
      // Insert code for a frequency analysis
      Map<Integer, Long> map =
      content.chars().filter( Character::isLetter )
                     .map( Character::toLowerCase )
                     .filter( c -> c >= 'a' && c <= 'z')
                     .mapToObj( c -> Integer.valueOf(c))
                     .collect( Collectors.groupingBy(  Function.identity(), Collectors.counting() ));
      
      // print map
      map.forEach( (key,value) -> System.out.println( ((char) key.intValue()) + " : " + value ) );
   }

}
