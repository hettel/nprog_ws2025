package kmeans;


import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChatGPT_Variante {
    public static void main(String[] args) throws IOException {
       
    	System.out.println("Start");

        String inputPath = "bilder/Eisvogel.jpg";
        String outputPath = "bilder/chatGPTreduced_Eisvogel.jpg";
        int k = 5;

        BufferedImage inputImage = ImageIO.read(new File(inputPath));
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // Alle Pixel als Vektoren extrahieren
        List<int[]> pixels = IntStream.range(0, width * height)
                .parallel()
                .mapToObj(i -> {
                    int x = i % width;
                    int y = i / width;
                    Color c = new Color(inputImage.getRGB(x, y));
                    return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
                })
                .collect(Collectors.toList());

        // Initialisierung der Cluster-Zentren
        List<int[]> centroids = initializeCentroids(pixels, k);

        System.out.println("Start k-Means-Schleife");
        long start = System.currentTimeMillis();
        
        
        // k-Means-Schleife
        boolean changed;
        int iteration = 0;
        do {
            iteration++;
            Map<int[], List<int[]>> clusters = assignToClusters(pixels, centroids);
            List<int[]> newCentroids = recomputeCentroids(clusters, centroids);
            changed = !centroidsEqual(centroids, newCentroids);
            
            // JHE Compile-Fehler behobenbbb
            centroids.clear();
            centroids.addAll(newCentroids);
            //centroids = newCentroids;
            System.out.println("Iteration " + iteration + " abgeschlossen.");
        } while (changed && iteration < 20);
        
        long end = System.currentTimeMillis();
        System.out.println("Iterationen abgeschlossen");
        System.out.println("Dauer: " + (end-start) + " [ms]");

        // Bild mit reduzierter Farbpalette erzeugen
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        IntStream.range(0, width * height).parallel().forEach(i -> {
            int x = i % width;
            int y = i / width;
            Color c = new Color(inputImage.getRGB(x, y));
            int[] rgb = {c.getRed(), c.getGreen(), c.getBlue()};
            int[] nearest = nearestCentroid(rgb, centroids);
            Color newColor = new Color(nearest[0], nearest[1], nearest[2]);
            synchronized (outputImage) {
                outputImage.setRGB(x, y, newColor.getRGB());
            }
        });

        
        ImageIO.write(outputImage, "jpg", new File(outputPath));
        System.out.println("Farbquantisierung abgeschlossen. Ergebnis gespeichert in: " + outputPath);
    }

    private static List<int[]> initializeCentroids(List<int[]> pixels, int k) {
        List<int[]> centroids = new ArrayList<>();
        Set<Integer> chosen = new HashSet<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (centroids.size() < k) {
            int idx = random.nextInt(pixels.size());
            if (chosen.add(idx)) centroids.add(pixels.get(idx));
        }
        return centroids;
    }

    private static Map<int[], List<int[]>> assignToClusters(List<int[]> pixels, List<int[]> centroids) {
        return pixels.parallelStream()
                .collect(Collectors.groupingByConcurrent(p -> nearestCentroid(p, centroids)));
    }

    private static List<int[]> recomputeCentroids(Map<int[], List<int[]>> clusters, List<int[]> oldCentroids) {
        return clusters.entrySet().parallelStream()
                .map(e -> {
                    List<int[]> points = e.getValue();
                    int n = points.size();
                    if (n == 0) return e.getKey();
                    int r = (int) points.stream().mapToInt(p -> p[0]).average().orElse(0);
                    int g = (int) points.stream().mapToInt(p -> p[1]).average().orElse(0);
                    int b = (int) points.stream().mapToInt(p -> p[2]).average().orElse(0);
                    return new int[]{r, g, b};
                })
                .collect(Collectors.toList());
    }

    private static int[] nearestCentroid(int[] pixel, List<int[]> centroids) {
        return centroids.stream()
                .min(Comparator.comparingDouble(c -> colorDistanceSquared(pixel, c)))
                .orElse(centroids.get(0));
    }

    private static double colorDistanceSquared(int[] a, int[] b) {
        int dr = a[0] - b[0];
        int dg = a[1] - b[1];
        int db = a[2] - b[2];
        return dr * dr + dg * dg + db * db;
    }

    private static boolean centroidsEqual(List<int[]> c1, List<int[]> c2) {
        if (c1.size() != c2.size()) return false;
        for (int i = 0; i < c1.size(); i++) {
            if (!Arrays.equals(c1.get(i), c2.get(i))) return false;
        }
        return true;
    }
}
