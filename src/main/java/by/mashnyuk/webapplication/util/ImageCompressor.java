package by.mashnyuk.webapplication.util;
// ImageCompressor.java
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageCompressor {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    private static final float COMPRESSION_QUALITY = 0.7f;

    public static byte[] compressImage(byte[] originalData) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalData));


        int newWidth = originalImage.getWidth();
        int newHeight = originalImage.getHeight();

        if (newWidth > MAX_WIDTH || newHeight > MAX_HEIGHT) {
            double ratio = Math.min((double) MAX_WIDTH / newWidth,
                    (double) MAX_HEIGHT / newHeight);
            newWidth = (int) (newWidth * ratio);
            newHeight = (int) (newHeight * ratio);
        }

        BufferedImage compressedImage = new BufferedImage(newWidth, newHeight,
                originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType());

        compressedImage.createGraphics().drawImage(
                originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH),
                0, 0, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(compressedImage, "jpg", baos);

        return baos.toByteArray();
    }
}
