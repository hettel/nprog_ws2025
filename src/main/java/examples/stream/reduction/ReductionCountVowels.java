package examples.stream.reduction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ReductionCountVowels
{
   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get("odyssey.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+|;|\\.|,");
      System.out.println("Number of words " + words.length);

      long numVowels =
      content.chars()
             .filter( c -> "aeiouAEIOU".indexOf(c) >= 0)
             .count();

      System.out.println("Number of vowels " + numVowels);
   }
}
