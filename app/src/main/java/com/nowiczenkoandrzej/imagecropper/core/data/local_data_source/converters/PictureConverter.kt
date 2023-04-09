package com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.nowiczenkoandrzej.imagecropper.core.data.mappers.toBitmap
import com.nowiczenkoandrzej.imagecropper.core.data.mappers.toByteArray
import java.io.ByteArrayOutputStream

class PictureConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        return bitmap.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return byteArray.toBitmap()
    }


}