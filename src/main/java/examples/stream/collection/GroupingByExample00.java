package examples.stream.collection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingByExample00
{

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get("odyssey.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+|;|\\.|,|:");
      System.out.println("Number of words " + words.length);

      // Group words by length
      Map<Integer, List<String> > resultMap =
      Arrays.stream(words)
            .collect( Collectors.groupingBy( str-> str.length() ) );

      resultMap.forEach( (key, value) -> System.out.println( key + " : " + value ) );
   }

}
