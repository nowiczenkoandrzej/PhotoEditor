package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util

import android.content.Context
import android.graphics.*
import android.net.Uri

class PictureFilter(uri: Uri, context: Context) {

    private val inputStream = context.contentResolver.openInputStream(uri)
    private val bitmap = BitmapFactory.decodeStream(inputStream)

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