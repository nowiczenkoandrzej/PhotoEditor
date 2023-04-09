package com.nowiczenkoandrzej.imagecropper.core.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class PictureModel(
    var editedBitmap: Bitmap? = null,
    var originalBitmap: Bitmap? = null,
    var title: String = "",
    var lastEdit: LocalDate = LocalDate.now()
): Parcelable