package com.nowiczenkoandrzej.imagecropper.core.domain.repository

import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import kotlinx.coroutines.flow.Flow

interface PictureRepository {

    fun getPictures(): Flow<List<PictureItem>>

    suspend fun insertPicture(picture: PictureItem)

    suspend fun deletePicture(picture: PictureItem)

}