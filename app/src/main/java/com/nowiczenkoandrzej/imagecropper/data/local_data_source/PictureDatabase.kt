package com.nowiczenkoandrzej.imagecropper.data.local_data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PictureEntity::class],
    version = 1
)
@TypeConverters(PictureConverter::class)
abstract class PictureDatabase: RoomDatabase() {

    abstract val pictureDao: PictureDao

    companion object {
        const val DATABASE_NAME = "pictures_db"
    }

}