package com.xoma.imgy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.zip.InflaterOutputStream;

public class IMGYDecoder {
    private File inputFilePath, outputFilePath;

    public IMGYDecoder(File inputFile, File outputFile) {
        this.inputFilePath = inputFile;
        this.outputFilePath = outputFile;
    }

    public void startProcess() throws IOException {
        processToFile();
    }

    private void processToFile() throws IOException {
        BufferedImage image = ImageIO.read(this.inputFilePath);
        ByteArrayOutputStream base64 = new ByteArrayOutputStream();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color values = new Color(image.getRGB(x, y));
                if(values.getRed() != 0) base64.write(values.getRed());
                if(values.getGreen() != 0) base64.write(values.getGreen());
                if(values.getBlue() != 0) base64.write(values.getBlue());
            }
        }

        OutputStream data = new FileOutputStream(this.outputFilePath);
        OutputStream outputStream = new InflaterOutputStream(data);
        outputStream.write(Base64.getDecoder().decode(base64.toByteArray()));

        outputStream.close();
        data.close();
        base64.close();
    }
}
