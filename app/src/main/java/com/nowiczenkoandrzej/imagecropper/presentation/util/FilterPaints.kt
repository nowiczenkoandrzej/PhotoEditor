package com.nowiczenkoandrzej.imagecropper.presentation.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

object FilterPaints {

    val NEGATIVE = ColorMatrixColorFilter(
        ColorMatrix(floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            -0f, -1f, 0f, 0f, 255f,
            -0f, 0f, -1f, 0f, 255f,
            -0f, 0f, 0f, 1f, 0f

        ))
    )

    val BLACK_AND_WHITE = ColorMatrixColorFilter(
        ColorMatrix(floatArrayOf(
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f

        ))
    )

}