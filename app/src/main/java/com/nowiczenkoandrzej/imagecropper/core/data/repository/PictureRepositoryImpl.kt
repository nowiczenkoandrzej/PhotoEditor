package com.nowiczenkoandrzej.imagecropper.core.data.repository

import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.PictureDao
import com.nowiczenkoandrzej.imagecropper.core.data.mappers.*
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import kotlinx.coroutines.flow.*

class PictureRepositoryImpl(
    private val dao: PictureDao
): PictureRepository {
    override fun getPictures(): Flow<List<PictureItem>> {

        return dao.getPictures().map { list ->
            list.map {  pictureEntity ->
                pictureEntity.toPictureItem()
            }
        }
    }


    override suspend fun insertPicture(picture: PictureItem) {
        dao.insertPicture(picture.toPictureEntity())
    }

    override suspend fun deletePicture(picture: PictureItem) {
        dao.deletePicture(picture.toPictureEntity())
    }

}