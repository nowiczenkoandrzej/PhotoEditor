package com.nowiczenkoandrzej.imagecropper.feature_picture_details.presentation

import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel

sealed class PictureDetailEvent {
    data class EnterScreen(val picture: PictureModel): PictureDetailEvent()
    data class EditTitle(val title: String): PictureDetailEvent()
}
