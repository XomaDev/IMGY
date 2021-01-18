package com.xoma;

import com.xoma.imgy.IMGYDecoder;
import com.xoma.imgy.IMGYEncoder;

import java.io.File;

public class Main {
    public static void main(String[] args) {
//        encode();
//        decode();
    }

    private static void encode() {
        File input = new File("\\Inputs and outputs\\input.png"),
               output = new File("\\Inputs and outputs\\output.png");

        IMGYEncoder imgyEncoder = new IMGYEncoder(input, output, "PNG");
        imgyEncoder.processToImage();
    }

    private static void decode() {
        File input = new File("\\Inputs and outputs\\output.png"),
                output = new File("\\Inputs and outputs");

        IMGYDecoder imgyDecoder = new IMGYDecoder(input, output);
        imgyDecoder.processToFile();
        System.out.println(imgyDecoder.size());
    }
}
