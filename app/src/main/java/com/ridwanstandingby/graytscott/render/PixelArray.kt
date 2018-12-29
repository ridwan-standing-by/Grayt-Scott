package com.ridwanstandingby.graytscott.render

class PixelArray(private val dimX: Int, private val dimY: Int) {

    private var aspectRatio = dimX.toDouble()/dimY.toDouble()

    private var time = 0
    private var pixelArray: IntArray = IntArray(dimX * dimY)


    private val colour = intArrayOf(
        -0xe6f8e6,
        -0xbde1f1,
        -0x95cbfd,
        -0x66a900,
        -0x338000,
        -0x5600,
        -0x736a1,
        -0xe1741,
        -0x2c1308,
        -0x794a1b,
        -0xc6822f,
        -0xe7ad4f,
        -0xf3dd76,
        -0xfff89c,
        -0xfbfbb7,
        -0xf6fed1
    )

    init {
        for (i in 0 until dimX * dimY) {
            pixelArray[i] = -0x1000000
        }
    }

    fun updateArray() {
        val pointRe = -1.76633656629364184
        val pointIm = -0.04180922088925528
        val thresholdBase = 64
        val thresholdTimeScale = 800

        val speed = 0.01
        val maxTime = 4000
        val zoomBase = 0.1

        if (time > maxTime) {
            time = 0
        }

        val zoom = zoomBase * java.lang.Math.exp(time.toDouble() * speed)
        val xMin = pointRe - aspectRatio / zoom
        val xMax = pointRe + aspectRatio / zoom
        val yMin = pointIm - 1 / zoom
        val yMax = pointIm + 1 / zoom
        val xStep = (xMax - xMin) / dimX.toDouble()
        val yStep = (yMax - yMin) / dimY.toDouble()
        val X = DoubleArray(dimX)
        val Y = DoubleArray(dimY)
        for (i in 0 until dimX) X[i] = xMin + xStep * i
        for (j in 0 until dimY) Y[j] = yMin + yStep * j
        time++
        var threshold = thresholdBase
        for (n in 0 until time / thresholdTimeScale)
            threshold *= 2

        for (j in 0 until dimY) {
            val jDimX = j * dimX
            for (i in 0 until dimX) {
                val index = jDimX + i
                var zRe = X[i]
                var zIm = Y[j]
                var k = 0
                while (k < threshold) {
                    val zReTemp = zRe
                    val zReTemp2 = zReTemp * zReTemp
                    val zIm2 = zIm * zIm
                    if (zReTemp2 + zIm2 > 4.0) break
                    zRe = zReTemp2 - zIm2 + X[i]
                    zIm = 2.0 * zReTemp * zIm + Y[j]
                    k++
                }
                if (k == threshold) {
                    pixelArray[index] = -0x1000000
                } else {
                    pixelArray[index] = colour[k % 16]
                }
            }
        }
    }

    fun getArray(): IntArray = pixelArray
}