package com.nowiczenkoandrzej.imagecropper.core.data.local_data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Query(
        """
            SELECT * FROM pictureentity 
            WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
            """)
    fun getPicturesByName(query: String): Flow<List<PictureEntity>>

    @Query("SELECT * FROM pictureentity ORDER BY id ASC")
    fun getPictures(): Flow<List<PictureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity)


}