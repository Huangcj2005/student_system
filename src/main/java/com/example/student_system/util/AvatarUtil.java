package com.example.student_system.util;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Data
public class AvatarUtil {
    public static void compressToJpeg(BufferedImage image, File output, float quality) throws IOException {
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(quality);

        try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(output)) {
            jpgWriter.setOutput(outputStream);
            jpgWriter.write(null, new javax.imageio.IIOImage(image, null, null), jpgWriteParam);
        }
        jpgWriter.dispose();
    }

    public static BufferedImage cropSquare(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        int size = Math.min(width, height);
        int x = (width - size) / 2;
        int y = (height - size) / 2;
        return src.getSubimage(x, y, size, size);
    }
}
