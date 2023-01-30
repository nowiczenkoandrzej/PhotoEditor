package com.nowiczenkoandrzej.imagecropper.domain.model

import android.graphics.Bitmap

data class PictureModel(
    var title: String? = null,
    var bitmap: Bitmap? = null
)