package com.nowiczenkoandrzej.imagecropper.core.data.mappers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.PictureEntity
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import java.io.ByteArrayOutputStream

fun PictureEntity.toPictureItem(): PictureItem {

    val result = PictureItem(
        id = this.id,
        picture = this.picture,
        originalPicture = this.originalPicture,
        title = this.title,
        lastEdit = this.date
    )
    return result
}

fun PictureItem.toPictureEntity(): PictureEntity {
    return PictureEntity(
        id = this.id,
        picture = this.picture!!,
        originalPicture = this.originalPicture!!,
        title = this.title,
        date = this.lastEdit
    )
}

fun List<PictureEntity>.toPictureItemList(): List<PictureItem> {
    val result = ArrayList<PictureItem>()
    this.forEach {
        result.add(it.toPictureItem())
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

fun Uri.toBitmap(context: Context): Bitmap {
    val inputStream = context.contentResolver.openInputStream(this)
    return BitmapFactory.decodeStream(inputStream)
}
