package examples.stream.collection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingByExample05
{
   private static final String vowels = "aeiouAEIOU";
   static long vowelCount(String word)
   {
      return word.chars()
          .filter( c -> vowels.indexOf(c) >= 0 )
          .count();
   }

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get("odyssey.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+|;|\\.|,");
      System.out.println("Number of words " + words.length);

      
      // Group words by length and group by the number of vowels
      Map<Integer, Map<Long,Long> > map = 
      Arrays.stream( words )
            .filter(str -> str.matches("[a-zA-Z'\\-´]*"))
            .collect( Collectors.groupingBy( s -> s.length(), Collectors.groupingBy( s -> vowelCount(s), Collectors.counting()) ));
      
      map.forEach( (k,v) -> System.out.println( k + " : " + v) );
   }


}
