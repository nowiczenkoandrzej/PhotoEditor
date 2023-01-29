package com.nowiczenkoandrzej.imagecropper.data.mappers

import android.graphics.Bitmap
import com.nowiczenkoandrzej.imagecropper.data.local_data_source.PictureEntity
import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel

fun PictureEntity.toPictureModel(): PictureModel {
    return PictureModel(
        title = this.title,
        bitmap = this.picture
    )
}

fun PictureModel.toPictureEntity(): PictureEntity {
    return PictureEntity(
        title = this.title,
        picture = this.bitmap
    )
}