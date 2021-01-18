package com.xoma.imgy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.InflaterOutputStream;

public class IMGYDecoder {
    private final File inputImage, outputFile;

    private long fileSize = 0;

    public IMGYDecoder(File inputImage, File outputFile) {
        this.inputImage = inputImage;
        this.outputFile = outputFile;
    }

    public IMGYDecoder(String inputImageName, String outputFile) {
        this.inputImage = new File(inputImageName);
        this.outputFile = new File(outputFile);
    }


    public void processToFile() {
        decodeToFile();
    }

    private void decodeToFile() {
        try {
            BufferedImage image = ImageIO.read(this.inputImage);
            ByteArrayOutputStream hexadecimalEncoded = new ByteArrayOutputStream();

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color values = new Color(image.getRGB(x, y));
                    if(values.getRed() != 0) hexadecimalEncoded.write(values.getRed());
                    if(values.getGreen() != 0) hexadecimalEncoded.write(values.getGreen());
                    if(values.getBlue() != 0) hexadecimalEncoded.write(values.getBlue());
                }
            }

            String data = hexadecimalEncoded.toString();
            int index = data.indexOf('%');

            String fileName = data.substring(index + 1);
            this.outputFile = new File(this.outputFile + "\\" + fileName);
            hexadecimalEncoded = new ByteArrayOutputStream();
            hexadecimalEncoded.write(data.substring(0, index).getBytes());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(hexadecimalEncoded.toByteArray());
            hexadecimalEncoded.close();

            byte[] buffer = new byte[2];
            while (inputStream.read(buffer) != -1) {
                outputStream.write((Character.digit(buffer[0], 16) << 4)
                        + Character.digit(buffer[1], 16));
            }

            FileOutputStream byteArrayOutputStream = new FileOutputStream(this.outputFile);
            InflaterOutputStream inflaterOutputStream = new InflaterOutputStream(byteArrayOutputStream);
            InflaterOutputStream inflaterOutputStream1 = new InflaterOutputStream(inflaterOutputStream);
            inflaterOutputStream1.write(outputStream.toByteArray());

            outputStream.close();
            inputStream.close();
            byteArrayOutputStream.close();
            inflaterOutputStream.close();
            inflaterOutputStream1.close();

            this.fileSize = this.outputFile.length();
        } catch (Exception error) {
            try {
                throw new ImageCorruptException("Image is corrupted!");
            } catch (ImageCorruptException e) {
                e.printStackTrace();
            }
        }
    }

    public long size() {
        return this.fileSize;
    }
}
