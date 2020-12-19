package com.xoma.tool;

public class Tool {
    public int[] imageArea(int pixels) {
        int height = optimumHeight(pixels);
        int width = pixels / height;
        return new int[] {width, height};
    }

    // Thanks : https://stackoverflow.com/a/64609729/14461795

    private static int optimumHeight(int areaSize) {
        for (int h = (int) Math.sqrt(areaSize); h > 1; h--)
            if (areaSize % h == 0) return h;
        return 1;
    }
}
