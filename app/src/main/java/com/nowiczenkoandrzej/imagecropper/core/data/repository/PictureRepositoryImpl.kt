package com.nowiczenkoandrzej.imagecropper.core.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.PictureDao
import com.nowiczenkoandrzej.imagecropper.core.data.mappers.*
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.core.util.Resource
import kotlinx.coroutines.flow.*

class PictureRepositoryImpl(
    private val dao: PictureDao
): PictureRepository {
    override fun getPictures(): Flow<List<PictureModel>> {

        return dao.getPictures().map { list ->
            list.map {  pictureEntity ->
                pictureEntity.toPictureModel()
            }
        }
    }


    override suspend fun insertPicture(picture: PictureModel) {

        var editedBitmapByteArray = picture.editedBitmap?.toByteArray()
        var originalBitmapByteArray = picture.originalBitmap?.toByteArray()

        while(editedBitmapByteArray!!.size > 500000)
            editedBitmapByteArray = editedBitmapByteArray.resizePicture()

        while(originalBitmapByteArray!!.size > 500000)
            originalBitmapByteArray = originalBitmapByteArray.resizePicture()

        val result = PictureModel(
            editedBitmap = editedBitmapByteArray.toBitmap(),
            originalBitmap = originalBitmapByteArray.toBitmap()
        )

        dao.insertPicture(result.toPictureEntity())
    }

    override suspend fun deletePicture(picture: PictureModel) {
        dao.deletePicture(picture.toPictureEntity())
    }

    private fun ByteArray.resizePicture(): ByteArray {
        val bitmap = this.toBitmap()
        val resized = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * 0.8).toInt(),
            (bitmap.height *0.8).toInt(),
            true
        )
        return resized.toByteArray()
    }

}