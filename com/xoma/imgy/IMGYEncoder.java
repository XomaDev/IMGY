/*

  Project: IMGY Encoder & Decoder
  Author: Kumaraswamy B G / Xoma Devs
  Date finished : 22 / 12 / 2020

  UPDATE 2.0.1

  DO NOT REDISTRIBUTE | NOT FOR COMMERCIAL USE

*/

package com.xoma.imgy;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.zip.DeflaterInputStream;

public class IMGYEncoder {

    private final File inputFile, outputImage;

    private final String imageFormat;

    private long imageSize = 0;

    public IMGYEncoder(File inputFile, File outputImage, String imageFormat)  {
        this.inputFile = inputFile;
        this.outputImage = outputImage;
        this.imageFormat = imageFormat;
    }

    public IMGYEncoder(String inputFileName, String outputImageName, String imageFormat) {
        this.inputFile = new File(inputFileName);
        this.outputImage = new File(outputImageName);
        this.imageFormat = imageFormat;
    }

    public void processToImage()  {
        encodeToImage();
    }

    private void encodeToImage()  {
        try {
            if(this.inputFile.length() == 0) throw new InvalidInputException("Empty input file!");

            ByteArrayInputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(this.inputFile.toPath()));
            DeflaterInputStream compressorInputStream = new DeflaterInputStream(inputStream);
            DeflaterInputStream compressorInputStream1 = new DeflaterInputStream(compressorInputStream);
            ByteArrayOutputStream hexadecimalEncoded = new ByteArrayOutputStream();

            for(Byte data : compressorInputStream1.readAllBytes())
                hexadecimalEncoded.write(String.format("%02X", data).getBytes());

            hexadecimalEncoded.write(("%" + inputFile.getName()).getBytes());

            ArrayList pixelsData = new ArrayList<Integer>();
            StringJoiner pixelBuilder = new StringJoiner(" ");

            long pixelIndex = 0, pixelsSorted = 1;
            for(Byte byt : hexadecimalEncoded.toByteArray()) {
                pixelBuilder.add(byt.toString());
                if(pixelIndex != 2) {
                    if(pixelsSorted == hexadecimalEncoded.size()) pixelsData.add(pixelBuilder.toString());
                    pixelIndex++;
                } else {
                    pixelsData.add(pixelBuilder.toString());
                    pixelBuilder = new StringJoiner(" ");
                    pixelIndex = 0;
                }
                pixelsSorted++;
            }

            inputStream.close();
            compressorInputStream.close();
            compressorInputStream1.close();
            hexadecimalEncoded.close();

            BufferedImage image = new BufferedImage(calculateArea(pixelsData.size())[0], calculateArea(pixelsData.size())[1], BufferedImage.TYPE_INT_ARGB);

            int pixelToGet = 0;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if(pixelToGet == pixelsData.size()) break;

                    String[] pixelData = pixelsData.get(pixelToGet).toString().split(" ");
                    int[] redGreenBlue = new int[] {Integer.parseInt(pixelData[0]), 0, 0};

                    if(pixelData.length != 1) if (pixelData.length == 2) redGreenBlue[1] = Integer.parseInt(pixelData[1]);
                    else {
                        redGreenBlue[1] = Integer.parseInt(pixelData[1]);
                        redGreenBlue[2] = Integer.parseInt(pixelData[2]);
                    }

                    image.setRGB(x, y, new Color(redGreenBlue[0], redGreenBlue[1], redGreenBlue[2]).getRGB());
                    pixelToGet++;
                }
            }

            OutputStream outputStream = new FileOutputStream(this.outputImage);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(this.imageFormat);
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.07f);

            writer.write(null, new IIOImage(image, null, null), param);
            outputStream.close();
            ios.close();
            writer.dispose();
        } catch (InvalidInputException | IOException e) {
            e.printStackTrace();
        }

        this.imageSize = this.outputImage.length();
    }


    public long size() {
        return this.imageSize;
    }


    private static int[] calculateArea(int pixels) {
        int height = optimumHeight(pixels);
        int width = pixels / height;
        return new int[] {width, height};
    }

    // VERY THANKS to : https://stackoverflow.com/a/64609729/14461795

    private static int optimumHeight(int pixels) {
        for (int h = (int) Math.sqrt(pixels); h > 1; h--) {
            if (pixels % h == 0) {
                return h;
            }
        }
        return 1;
    }
}
