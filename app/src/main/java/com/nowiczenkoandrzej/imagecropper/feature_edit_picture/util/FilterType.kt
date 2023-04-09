package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util

sealed class FilterType {
    object Normal: FilterType()
    object Invert: FilterType()
    object BlackAndWhite: FilterType()
    object Sepia: FilterType()
}
