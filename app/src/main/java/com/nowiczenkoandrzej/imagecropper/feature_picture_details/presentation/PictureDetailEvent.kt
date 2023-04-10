package com.nowiczenkoandrzej.imagecropper.feature_picture_details.presentation

import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem

sealed class PictureDetailEvent {
    data class EnterScreen(val picture: PictureItem): PictureDetailEvent()
    data class EditTitle(val title: String): PictureDetailEvent()
}
