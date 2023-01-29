package com.nowiczenkoandrzej.imagecropper.data.repository

import com.nowiczenkoandrzej.imagecropper.data.local_data_source.PictureDao
import com.nowiczenkoandrzej.imagecropper.data.mappers.toPictureEntity
import com.nowiczenkoandrzej.imagecropper.data.mappers.toPictureModel
import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class PictureRepositoryImpl(
    private val dao: PictureDao
): PictureRepository {
    override suspend fun getPictures(): Flow<Resource<List<PictureModel>>> {
        return flow {
            emit(Resource.Loading(true))

            TODO()

        }
    }

    override suspend fun insertPicture(picture: PictureModel) {
        dao.insertPicture(picture.toPictureEntity())
    }

    override suspend fun deletePicture(picture: PictureModel) {
        dao.deletePicture(picture.toPictureEntity())
    }

}