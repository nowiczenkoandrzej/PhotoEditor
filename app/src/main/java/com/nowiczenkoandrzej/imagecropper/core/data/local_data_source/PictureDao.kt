package com.nowiczenkoandrzej.imagecropper.core.data.local_data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Query("SELECT * FROM pictureentity")
    fun getPictures(): Flow<List<PictureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity)

    @Delete
    suspend fun deletePicture(picture: PictureEntity)

}