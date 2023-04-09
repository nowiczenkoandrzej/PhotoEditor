package com.nowiczenkoandrzej.imagecropper.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class PictureItem(
    val picture: String? = null,
    val originalPicture: String? = null,
    val title: String = "",
    val laseEdit: LocalDate = LocalDate.now(),
): Parcelable
