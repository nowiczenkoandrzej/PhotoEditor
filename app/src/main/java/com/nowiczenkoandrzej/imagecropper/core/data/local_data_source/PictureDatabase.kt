package com.nowiczenkoandrzej.imagecropper.core.data.local_data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.converters.DateConverter
import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.converters.PictureConverter

@Database(
    entities = [PictureEntity::class],
    version = 8
)
@TypeConverters(PictureConverter::class, DateConverter::class)
abstract class PictureDatabase: RoomDatabase() {

    abstract val pictureDao: PictureDao

    companion object {
        const val DATABASE_NAME = "pictures_db"
    }

}