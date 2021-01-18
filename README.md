<img src="https://imgy-android.vercel.app/iMGY.svg">

## Introduction

An algorithm made by Kumaraswamy BG (Xoma Devs) which has the ability to encode files into images and back again to its original form without losing any data

At first, the input file is read and compressed the data to reduce the size. Then the bytes are encoded as hexadecimal which to be shown as a pixel. As we know, the pixel represented is of RGB, Red, Green and Blue. Bytes of the hexadecimal are sorted into every three values and then encoded as a pixel in an image.

Now we have the file encoded as an image! HEHE!

Now to decode, we should go through every pixel of the image to get the RGB values which were encoded. The values parsed to bytes to obtain the hexadecimal. Then the hexadecimal is decoded and uncompressed to its original form as bytes, finally, it is written back into file.

## HOW TO

To encode, initialize the `IMGYEncoder` with the parameters input file, output image and the encode format.

```
IMGYEncoder encoder = new IMGYEncoder(originalFile, processedFile, "PNG");

// NOTE : Make sure you do not use the Encoder and Decoder because it may lead to FileNotFoundException
```
<br>

Once you've initialized the encoder, start the process through `processToImage()` method.

```
encoder.processToImage();
```

<br>

To decode, intialize `IMGYDecoder` with the parameters encoded file and the directory you need to put the decoded result.

```
IMGYDecoder decoder = new IMGYDecoder(encodedFile, outputDirectory);
```
<br>

As same as the encoder, use `processToFile()` method to decode.

```
decoder.processToFile();
```
<br>

To know the encoded and decoded results length use `size()` method.
