package examples.stream.reduction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;


public class ReductionFindLongestWord
{
   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get("odyssey.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+|;|\\.|,|-");
      System.out.println("Number of words " + words.length);

      System.out.println("With for-loop");
      String longestWord = words[0];
      for(int i = 1; i < words.length; i++)
      {
         if(words[i].matches("[a-zA-Z'\\-´]*"))
         {
            if(words[i].length() > longestWord.length())
            {
               longestWord = words[i];
            }
         }
      }

      System.out.println("Longest word " + longestWord);
      System.out.println("Longest word length " + longestWord.length());

      System.out.println("With stream");
      Optional<String> str1 = Arrays.stream(words)
                          .filter(str -> str.matches("[a-zA-Z'\\-´]*"))
                          .reduce( (left, str) -> left.length() > str.length() ? left : str);
      
      System.out.println("Longest String with streams : " + str1.get() );
      System.out.println("Len " + str1.get().length());

      System.out.println("With parallel stream");
      String str2 = Arrays.stream(words).parallel()
    		              .filter(str -> str.matches("[a-zA-Z'\\-´]*"))
    		              .reduce("", (left, str) -> left.length() > str.length() ? left : str);

      System.out.println("Longest String with parallel streams : " + str2);
      System.out.println("Len " + str2.length());

      System.out.println("Words with 19 characters");
      Arrays.stream(words).filter(str -> str.matches("[a-zA-Z'\\-´]*"))
                          .filter( word -> word.length() == 19 )
                          .forEach( str -> System.out.println(str) );
   }
}
