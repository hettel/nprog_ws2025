package part1.ch08_activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import kmeans.util.ColorCentroid;
import kmeans.util.DistanceTools;
import kmeans.util.IOTools;
import kmeans.util.Image;
import kmeans.util.Pixel;

public class KMeans {

	public static void main(String[] args) throws IOException 
	{
		final String inputFileName = "Eisvogel.jpg";
		final String outputFileName = "reduced_" + inputFileName;
		
		// Number of clusters (fix value)
		final int k = 5;

		System.out.println("Color k-Means");
		System.out.println("Load image: " + inputFileName);
		long startLoading = System.currentTimeMillis();
		Image image = IOTools.load("bilder/" + inputFileName);
		long endLoading = System.currentTimeMillis();
		System.out.println("Loading image : " + (endLoading - startLoading) + "[ms]");
		System.out.println("Num of pixels : " + image.getPixelCount());
		System.out.println("Num of colors : " + image.getColorCount());

		System.out.println("Reduce image (k = " + k + ")");
		long startTime = System.currentTimeMillis();
		
		
		reduce(image, k);
		

		long endTime = System.currentTimeMillis();
		System.out.println("Duration: " + (endTime - startTime) + "[ms]");

		System.out.println("Store image: " + outputFileName);
		IOTools.store(image, "bilder/" + outputFileName);
		System.out.println("done");

	}
	
	
	private static void reduce(Image image, final int maxCluster)
	{
		// random assignment of color pixels to a cluster
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

}
