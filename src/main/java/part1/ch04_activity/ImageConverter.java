package part1.ch04_activity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageConverter 
{
	
	public static void main(String[] args) throws Exception 
	{
			
		String dateiname = "Eisvogel.jpg";
		System.out.println("Konvertiere " + dateiname );
		
        // 1. Originalbild laden
		long time_start = System.currentTimeMillis();
        BufferedImage colorImage = loadImage("bilder/" + dateiname);

        // 2. In Graustufenbild umwandeln
        long time_convert_start = System.currentTimeMillis();
        BufferedImage grayImage = toGrayscale(colorImage);
        long time_convert_end = System.currentTimeMillis();

        // 3. Ergebnis speichern
       saveImage(grayImage, "png", "bilder/grau_" + dateiname );
       long time_end = System.currentTimeMillis();

        System.out.println("Graustufenbild wurde  erzeugt!");
        
        System.out.println();
        System.out.printf("Gesamtzeit: %d [ms]%n", (time_end - time_start) );
        System.out.printf(" - Datei lesen:        %d [ms]%n", (time_convert_start - time_start) );
        System.out.printf(" - Datei konvertieren: %d [ms]%n", (time_convert_end - time_convert_start) );
        System.out.printf(" - Datei schreiben:    %d [ms]%n", (time_end - time_convert_end) );
		
	}
	
	
	 /**
     * Wandelt ein Farbbild in ein Graustufenbild um.
     *
     * @param colorImage Eingabebild (RGB)
     * @return Neues Graustufenbild
     */
    public static BufferedImage toGrayscale(BufferedImage colorImage) {
        int width = colorImage.getWidth();
        int height = colorImage.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(colorImage.getRGB(x, y));

                // Helligkeit nach Luminanzformel (ITU BT.601)
                int grayLevel = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                int gray = new Color(grayLevel, grayLevel, grayLevel).getRGB();

                grayImage.setRGB(x, y, gray);
            }
        }

        return grayImage;
    }	
	

    /**
     * Liest ein Bild von einer Datei.
     *
     * @param path Dateipfad (z. B. "bilder/test.jpg")
     * @return Das eingelesene Bild als BufferedImage
     * @throws IOException falls das Einlesen fehlschlägt
     */
    public static BufferedImage loadImage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Datei nicht gefunden: " + path);
        }
        return ImageIO.read(file);
    }
    
    /**
     * Speichert ein BufferedImage unter dem angegebenen Pfad.
     *
     * @param image  Das zu speichernde Bild
     * @param format Das Ausgabeformat (z. B. "png" oder "jpg")
     * @param path   Zielpfad (z. B. "output/result.png")
     * @throws IOException falls das Speichern fehlschlägt
     */
    public static void saveImage(BufferedImage image, String format, String path) throws IOException {
        File outputFile = new File(path);
        boolean success = ImageIO.write(image, format, outputFile);
        if (!success) {
            throw new IOException("Kein passender Writer für Format: " + format);
        }
    }
}
