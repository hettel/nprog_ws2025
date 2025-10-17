package examples.stream.collection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GroupingByExample03
{
   private static final String vowels = "aeiouAEIUO";
   private static long vowelCount(String word)
   {
      return word.chars()
                 .filter( c -> vowels.indexOf(c) >= 0 )
                 .count();
   }
   
   public static class VowelCollector implements Collector<String, List<String>, List<String>>
   {
     
     private final int vowels;

     VowelCollector(int vowels)
     {
       this.vowels = vowels;
     }
     
      @Override
      public Supplier<List<String>> supplier()
      {
         return () -> new ArrayList<>();
      }

      @Override
      public BiConsumer<List<String>, String> accumulator()
      {
         return (list, str) -> { 
            if(vowelCount(str) == vowels ) 
               list.add(str); 
            };
      }

      @Override
      public BinaryOperator<List<String>> combiner()
      {
         return (list1, list2) -> { list1.addAll(list2); return list1; };
      }

      @Override
      public Function<List<String>, List<String>> finisher()
      {
         return Function.identity();
      }

      @Override
      public Set<Characteristics> characteristics()
      {
         return Set.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT);
      }
      
   }

   public static void main(String[] args) throws IOException
   {
      Path path = Paths.get("odyssey.txt");
      String content = Files.readString(path, Charset.forName("UTF8"));
      String words[] = content.split("\\s+|;|\\.|,");
      System.out.println("Number of words " + words.length);

      List<String> result = Arrays.stream(words).parallel().collect( new VowelCollector(3) );
      
      System.out.println("Number " + result.size() );
      result.forEach( str -> System.out.println(str) );
      
      
      
      
      Map<Integer, Map<Long,List<String>> > map = 
      Arrays.stream( words )
            .filter(str -> str.matches("[a-zA-Z'\\-´]*"))
            .collect( Collectors.groupingBy( s -> s.length(), Collectors.groupingBy( s -> vowelCount(s)) ));
      
      map.forEach( (k,v) -> System.out.println( k + " : " + v) );
   }
}
