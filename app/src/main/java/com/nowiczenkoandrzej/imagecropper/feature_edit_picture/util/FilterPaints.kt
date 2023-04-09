package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

object FilterPaints {

    val INVERT = ColorMatrixColorFilter(
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

    val SEPIA = ColorMatrixColorFilter(
        ColorMatrix().apply {
            setSaturation(0f)
            postConcat(
                ColorMatrix().apply {
                    setScale(1f, 1f, 0.8f, 1f)
                }
            )
        }
    )

}