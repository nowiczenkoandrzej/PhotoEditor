package com.nowiczenkoandrzej.imagecropper.presentation.util

import android.graphics.*

class PictureFilter(bitmap: Bitmap) {

    private val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    private val paint = Paint()
    private val canvas = Canvas(mutableBitmap)

    fun negativePicture(): Bitmap {
        paint.colorFilter = FilterPaints.NEGATIVE
        canvas.drawBitmap(
            mutableBitmap,
            0f,
            0f,
            paint
        )
        return mutableBitmap
    }

    fun blackAndWhitePicture(): Bitmap {
        paint.colorFilter = FilterPaints.BLACK_AND_WHITE
        canvas.drawBitmap(
            mutableBitmap,
            0f,
            0f,
            paint
        )
        return mutableBitmap
    }

    fun sepiaPicture(): Bitmap {
        val sepiaMatrix = ColorMatrix()
        sepiaMatrix.setSaturation(0f)
        val colorScale = ColorMatrix()
        colorScale.setScale(1f,1f,0.8f, 1f)
        sepiaMatrix.postConcat(colorScale)

        val filter = ColorMatrixColorFilter(sepiaMatrix)

        paint.colorFilter = filter
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)

        return mutableBitmap
    }

}