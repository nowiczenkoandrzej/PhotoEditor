package com.nowiczenkoandrzej.imagecropper.core.data.mappers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.PictureEntity
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import java.io.ByteArrayOutputStream

fun PictureEntity.toPictureModel(): PictureModel {
    return PictureModel(
        editedBitmap = this.picture,
        originalBitmap = this.originalBitmap,
        title = this.title,
        lastEdit = this.date
    )
}

fun PictureModel.toPictureEntity(): PictureEntity {
    return PictureEntity(
        picture = this.editedBitmap!!,
        originalBitmap = this.originalBitmap!!,
        title = this.title,
        date = this.lastEdit
    )
}

fun List<PictureEntity>.toPictureModelList(): List<PictureModel> {
    val result = ArrayList<PictureModel>()
    this.forEach {
        result.add(it.toPictureModel())
    }
    return result
}

fun Bitmap.toByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}