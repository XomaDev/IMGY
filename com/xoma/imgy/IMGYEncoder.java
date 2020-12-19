package com.xoma.imgy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.StringJoiner;
import java.util.zip.DeflaterOutputStream;

import com.xoma.tool.Tool;

public class IMGYEncoder {
    private File inputFilePath, outputFilePath;

    public IMGYEncoder(File inputFile, File outputFile)  {
        this.inputFilePath = inputFile;
        this.outputFilePath = outputFile;
    }

    public void startProcess() throws IOException{
        processToImage();
    }

    private void processToImage() throws IOException {
        ByteArrayOutputStream compressedData = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(compressedData);

        dos.write(Files.readAllBytes(inputFilePath.toPath()));
        dos.close();

        ArrayList<String> pixels = new ArrayList<>();
        StringJoiner builder = new StringJoiner(" ");

        byte[] base64 = Base64.getEncoder().encode(compressedData.toByteArray());
        compressedData.close();

        long index = 0, length = 1;
        for(Byte byt : base64) {
            builder.add(byt.toString());
            if(index != 2) {
                if(length == base64.length) pixels.add(builder.toString());
                index++;
            } else {
                pixels.add(builder.toString());
                builder = new StringJoiner(" ");
                index = 0;
            }
            length++;
        }

        int[] widthHeight = new Tool().imageArea(pixels.size());

        BufferedImage image = new BufferedImage(widthHeight[0], widthHeight[1], BufferedImage.TYPE_INT_ARGB);

        long pixelIndex = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if(pixelIndex == pixels.size()) break;

                String[] pixelValues = pixels.get(Math.toIntExact(pixelIndex)).split(" ");
                int[] RGBa = new int[] {Integer.parseInt(pixelValues[0]), 0, 0};

                if(pixelValues.length != 1) if (pixelValues.length == 2) {
                    RGBa[1] = Integer.parseInt(pixelValues[1]);
                } else {
                    RGBa[1] = Integer.parseInt(pixelValues[1]);
                    RGBa[2] = Integer.parseInt(pixelValues[2]);
                }

                image.setRGB(x, y, new Color(RGBa[0], RGBa[1], RGBa[2]).getRGB());
                pixelIndex++;
            }
        }

        ImageIO.write(image, "png", outputFilePath);
    }
}
