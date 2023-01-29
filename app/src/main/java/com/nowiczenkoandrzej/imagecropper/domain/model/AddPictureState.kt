package com.nowiczenkoandrzej.imagecropper.domain.model

data class AddPictureState(
    val picture: PictureModel? = null,
    val chooseImageToCrop: Boolean = false,
    val textedPicture: String? = null
)
