package part1.ch08_activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import kmeans.util.ColorCentroid;
import kmeans.util.DistanceTools;
import kmeans.util.IOTools;
import kmeans.util.Image;
import kmeans.util.Pixel;

public class KMeans 
{
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Start processing");
		final String inputFileName = "Eisvogel.jpg";
		final String outputFileName = "reduced_" + inputFileName;
		
		// Number of clusters (fix value)
		final int k = 5;

		System.out.println("Color k-Means");
		System.out.println("Load and create image: " + inputFileName);
		long startLoading = System.currentTimeMillis();
		Image image = IOTools.load("bilder/" + inputFileName);
		long endLoading = System.currentTimeMillis();
		System.out.println("Loading image : " + (endLoading - startLoading) + "[ms]");
		System.out.println("Num of pixels : " + image.getPixelCount());
		System.out.println("Num of colors : " + image.getColorCount());

		System.out.println("Reduce image (k = " + k + ")");
		long startTime = System.currentTimeMillis();
		
		// inplace reduction
		reduce_v2(image, k);
		

		long endTime = System.currentTimeMillis();
		System.out.println("Duration: " + (endTime - startTime) + "[ms]");

		System.out.println("Store image: " + outputFileName);
		long startStore = System.currentTimeMillis();
		IOTools.store(image, "bilder/" + outputFileName);
		long endStore = System.currentTimeMillis();
		System.out.println("Store image : " + (endStore - startStore) + "[ms]");
		System.out.println("done");

	}
	
	
	private static void reduce(Image image, final int maxCluster)
	{
		// random assignment of pixels to a cluster
		Random rand = new Random();
		for(Pixel pixel : image.pixels )
		{
			pixel.centroidId = rand.nextInt(maxCluster);
		}

		ColorCentroid[] centroids = new ColorCentroid[maxCluster];

		// counts reallocations
		long accum = 0;
		while (true) {
			// reset counter
			accum = 0;

			// grouping pixels according to centroids
			Map<Integer, List<Pixel>> clusterMapList = new HashMap<Integer, List<Pixel>>();
			for( int i=0; i < maxCluster; i++ )
			{
				clusterMapList.put(i, new ArrayList<Pixel>() );
			}
			for( Pixel pixel : image.pixels )
			{
				clusterMapList.get( pixel.centroidId ).add(pixel);
			}
			

			// calculate new centroids
			for (int id = 0; id < maxCluster; id++ ) {
				double sumRed = 0;
				double sumGreen = 0;
				double sumBlue = 0;
				
				for(Pixel pixel : clusterMapList.get(id) )
				{
					sumRed += pixel.red;
					sumGreen += pixel.green;
					sumBlue += pixel.blue;
				}
				
				double len = clusterMapList.get(id).size();

				centroids[id] = new ColorCentroid(sumRed / len, sumGreen / len, sumBlue / len);
			}

			// assign new centroids
			for(Pixel pixel : image.pixels)
			{
				int newClusterId = DistanceTools.getNearestCentroidId(pixel, centroids);
				if (newClusterId != pixel.centroidId) {
					pixel.centroidId = newClusterId;
					// count cluster changes
					accum++;
				}
			}
			

			// if there are no reallocation
			// the cluster is stable
			if (accum == 0) {
				// reassign the new color (centroid) to the pixel
				for(Pixel pixel : image.pixels)
				{
					pixel.red   = (int) centroids[pixel.centroidId].red;
					pixel.green = (int) centroids[pixel.centroidId].green;
					pixel.blue  = (int) centroids[pixel.centroidId].blue;
				}
				
				// finish
				break;
			}
		}
	}
	
	private static void reduce_v1(Image image, final int maxCluster)
	{
		// random assignment of pixels to a cluster
		image.pixels.parallelStream().forEach( p -> p.centroidId = ThreadLocalRandom.current().nextInt(maxCluster) );
		

		ColorCentroid[] centroids = new ColorCentroid[maxCluster];


		while (true) {
			
			// grouping pixels according to centroids			
			Map<Integer, List<Pixel>> clusterMapList = image.pixels.parallelStream()
					                                        .collect( Collectors.groupingBy( pixel -> pixel.centroidId ));
			

			// calculate new centroids			
			for (Integer id : clusterMapList.keySet() ) {
				
				// use double[3]
				// a[0] - sum red
				// a[1] - sum green
				// a[2] - sum blue
				double[] colorSum =   
				clusterMapList.get(id).parallelStream()
				   .collect( () -> new double[3], 
						   (acc, pixel) -> {  
							   acc[0] += pixel.red;
							   acc[1] += pixel.green;
							   acc[2] += pixel.blue;
						   },
						   (left, right) -> {  
								  left[0] += right[0];
								  left[1] += right[1];
								  left[2] += right[2];
						   } );
				   
				
				double len = clusterMapList.get(id).size();
				
				centroids[id] = new ColorCentroid(colorSum[0] / len, colorSum[1] / len, colorSum[2] / len);
			}

			// assign new centroids
			long accum = 
			image.pixels.parallelStream().filter( pixel -> {
				int newClusterId = DistanceTools.getNearestCentroidId(pixel, centroids);
				if (newClusterId != pixel.centroidId) {
					pixel.centroidId = newClusterId;
					return true;
				}
				else
				{
					return false;
					}
			} ).count();
			

			// if there are no reallocation
			// the cluster is stable
			if (accum == 0) {
				// reassign the new color (centroid) to the pixel
				image.pixels.parallelStream().forEach( pixel -> {
					pixel.red   = (int) centroids[pixel.centroidId].red;
					pixel.green = (int) centroids[pixel.centroidId].green;
					pixel.blue  = (int) centroids[pixel.centroidId].blue;
				} );
				
				// finish
				break;
			}
		}
	}
	
	
	
	private static class CentroidCollector implements Collector<Pixel, double[], ColorCentroid>
	{
		// use double[4]
		// a[0] - sum red
		// a[1] - sum green
		// a[2] - sum blue
		// a[3] - count

		@Override
		public Supplier<double[]> supplier() {
			return () -> new double[4];
		}

		@Override
		public BiConsumer<double[], Pixel> accumulator() 
		{
			return (acc, pixel) -> {  
				   acc[0] += pixel.red;
				   acc[1] += pixel.green;
				   acc[2] += pixel.blue;
				   acc[3] += 1;
			   };
		}

		@Override
		public BinaryOperator<double[]> combiner() {
			return (left, right) -> {  
				  left[0] += right[0];
				  left[1] += right[1];
				  left[2] += right[2];
				  left[3] += right[3];
				  return left;
		   };
		}

		@Override
		public Function<double[], ColorCentroid> finisher() {
			
			return colorSum -> new ColorCentroid(colorSum[0] / colorSum[3], colorSum[1] / colorSum[3], colorSum[2] / colorSum[3]); 
		}

		@Override
		public Set<Characteristics> characteristics() {
			return Set.of( Characteristics.UNORDERED );
		}
		
	}
	
	private static void reduce_v2(Image image, final int maxCluster)
	{
		// random assignment of pixels to a cluster
		image.pixels.parallelStream().forEach( p -> p.centroidId = ThreadLocalRandom.current().nextInt(maxCluster) );
		

		ColorCentroid[] centroids = new ColorCentroid[maxCluster];


		while (true) {
			
			// calculte ColorCentroids		
			Map<Integer, ColorCentroid> clusterMap
			      = image.pixels.parallelStream()
					     .collect( Collectors.groupingBy( pixel -> pixel.centroidId, new CentroidCollector()));
					                                        		
			 // transfer result into array
	         for(Integer id : clusterMap.keySet())
	         {
	            centroids[id] = clusterMap.get(id);
	         }

			// assign new centroids
			long accum = 
			image.pixels.parallelStream().filter( pixel -> {
				int newClusterId = DistanceTools.getNearestCentroidId(pixel, centroids);
				if (newClusterId != pixel.centroidId) {
					pixel.centroidId = newClusterId;
					return true;
				}
				else
				{
					return false;
					}
			} ).count();
			

			// if there are no reallocation
			// the cluster is stable
			if (accum == 0) {
				// reassign the new color (centroid) to the pixel
				image.pixels.parallelStream().forEach( pixel -> {
					pixel.red   = (int) centroids[pixel.centroidId].red;
					pixel.green = (int) centroids[pixel.centroidId].green;
					pixel.blue  = (int) centroids[pixel.centroidId].blue;
				} );
				
				// finish
				break;
			}
		}
	}

}
