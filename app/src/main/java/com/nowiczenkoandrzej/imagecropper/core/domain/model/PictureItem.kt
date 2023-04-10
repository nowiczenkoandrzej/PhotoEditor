package com.nowiczenkoandrzej.imagecropper.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class PictureItem(
    var id: Int? = null,
    val picture: String? = null,
    val originalPicture: String? = null,
    val title: String = "",
    val lastEdit: LocalDate = LocalDate.now(),
): Parcelable
