package com.nowiczenkoandrzej.imagecropper.core.domain.repository

import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface PictureRepository {

    fun getPictures(): Flow<List<PictureModel>>

    suspend fun insertPicture(picture: PictureModel)

    suspend fun deletePicture(picture: PictureModel)

}