# IMGY
## Introduction

An algorithm made by Kumaraswamy BG (Xoma Devs) which has the ability to encode files into images and back again to its original form without losing any data

At first, the input file is read and compressed the data to reduce the size. Then the bytes are encoded as base64 which to be shown as a pixel. As we know, the pixel represented is of RGB, Red, Green and Blue. Bytes of the base64 are sorted into every three values and then encoded as a pixel in an image.

Now we have the file encoded as an image! HEHE!

Now to decode, we should go through every pixel of the image to get the RGB values which were encoded. The values parsed to bytes to obtain the base64. Then the base64 is decoded and uncompressed to its original form as bytes, finally, it is written back into file.

## HOW TO

Initialize the IMGYEncoder with the parameters input file and output image

```
IMGYEncoder encoder = new IMGYEncoder(originalFile, processedFile);

// NOTE : Make sure you do not use the Encoder and Decoder because it may lead to FileNotFoundException
```
