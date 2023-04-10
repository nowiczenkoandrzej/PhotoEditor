package com.nowiczenkoandrzej.imagecropper.core.data.local_data_source

import android.graphics.Bitmap

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class PictureEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val picture: String,
    val originalPicture: String,
    val title: String,
    val date: LocalDate

)