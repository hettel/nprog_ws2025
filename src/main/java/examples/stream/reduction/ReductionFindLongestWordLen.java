package examples.stream.reduction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.OptionalInt;


public class ReductionFindLongestWordLen
{
   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get("odyssey.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+|;|\\.|,|-");
      System.out.println("Number of words " + words.length);

      System.out.println("With for-loop");
      int longestWordLen = words[0].length();
      for(int i = 1; i < words.length; i++)
      {
         if(words[i].matches("[a-zA-Z'\\-´]*"))
         {
            if(words[i].length() > longestWordLen)
            {
               longestWordLen = words[i].length();
            }
         }
      }

      System.out.println("Longest word length " + longestWordLen);

      System.out.println("With stream");
      OptionalInt max = Arrays.stream(words)
                              .filter(str -> str.matches("[a-zA-Z'\\-´]*"))
                              .mapToInt( s -> s.length())
                              .reduce( (left, right) -> left > right ? left : right);
      
      System.out.println("Longest String with streams : " + max.getAsInt() );   }
}
