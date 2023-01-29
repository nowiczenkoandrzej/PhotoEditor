package com.nowiczenkoandrzej.imagecropper.data.local_data_source

import android.graphics.Bitmap

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PictureEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val picture: Bitmap,
    val title: String,

)

class InvalidPictureException(message: String): Exception(message)