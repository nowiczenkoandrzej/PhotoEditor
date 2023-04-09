package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util

import android.graphics.*

class PictureFilter(bitmap: Bitmap) {

    private val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    private val paint = Paint()
    private val canvas = Canvas(mutableBitmap)

    fun invertPicture(): Bitmap {
        paint.colorFilter = FilterPaints.INVERT
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)
        return mutableBitmap
    }

    fun blackAndWhitePicture(): Bitmap {
        paint.colorFilter = FilterPaints.BLACK_AND_WHITE
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)
        return mutableBitmap
    }

    fun sepiaPicture(): Bitmap {
        paint.colorFilter = FilterPaints.SEPIA
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)
        return mutableBitmap
    }

}