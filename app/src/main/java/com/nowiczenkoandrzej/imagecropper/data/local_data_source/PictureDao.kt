package com.nowiczenkoandrzej.imagecropper.data.local_data_source

import androidx.room.*

@Dao
interface PictureDao {

    @Query("SELECT * FROM pictureentity")
    suspend fun getPictures(): List<PictureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity)

    @Delete
    suspend fun deletePicture(picture: PictureEntity)

}