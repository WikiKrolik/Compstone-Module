package com.example.phonelocalization

object WifiHeatmap {
    private const val IMG_WIDTH = 531;
    private const val IMG_HEIGHT = 449;
    private const val MEASUREMENTS_WIDTH = 12;
    private const val MEASUREMENTS_HEIGHT = 11;

    private var heatmap = arrayOf(
        arrayOf(-84, -84, -84, 1, 1, 1, 1, 1, -65, -61, -64, -65),
        arrayOf(-82, -84, -84, 1, 1, 1, 1, 1, -65, -61, -64, -65),
        arrayOf(-82, -84, -76, -73, -70, -68, -65, -65, -58, -56, -54, -56),
        arrayOf(-80, -79, -76, -73, -70, -68, -65, -65, -60, -57, -48, -54),
        arrayOf(-79, -83, -80, -73, -70, -68, -65, -65, -60, -50, -50, -40),
        arrayOf(-80, -82, -76, 1, 1, 1, 1, 1, 1, -48, -48, -43),
        arrayOf(-9999, -9999, -9999, 1, 1, 1, 1, 1, 1, -56, -56, -53),
        arrayOf(-9999, -9999, -9999, 1, 1, 1, 1, 1, 1, -62, -62, -58),
        arrayOf(-9999, -9999, -9999, -84, -80, -76, -76, -73, -65, -65, -60),
        arrayOf(-9999, -9999, -9999, -9999, -84, -80, -76, -76, -73, -70, -68, -67),
        arrayOf(-9999, -9999, -9999, -9999, -84, -81, -80, -76, -73, -70, -68, -66),
    );

    fun getWifiStrength(x: Int, y: Int): Int {
        val calculatedYIndex = y * MEASUREMENTS_HEIGHT / IMG_HEIGHT;
        val calculatedXIndex = x * MEASUREMENTS_WIDTH / IMG_WIDTH;

        if (calculatedYIndex > MEASUREMENTS_HEIGHT - 1
            || calculatedYIndex < 0
            || calculatedXIndex > MEASUREMENTS_WIDTH - 1
            || calculatedXIndex < 0) {
            return 1;
        }

        return heatmap[y * MEASUREMENTS_HEIGHT / IMG_HEIGHT][x * MEASUREMENTS_WIDTH / IMG_WIDTH];
    }
}