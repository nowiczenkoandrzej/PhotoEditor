package com.nowiczenkoandrzej.imagecropper.domain.repository

import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.util.Resource
import kotlinx.coroutines.flow.Flow

interface PictureRepository {

    suspend fun getPictures(): Flow<Resource<List<PictureModel>>>

    suspend fun insertPicture(picture: PictureModel)

    suspend fun deletePicture(picture: PictureModel)

}