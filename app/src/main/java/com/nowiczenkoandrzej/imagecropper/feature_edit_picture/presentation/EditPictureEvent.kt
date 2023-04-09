package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation

import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.FilterType

sealed class EditPictureEvent {

    data class AddFilter(val filterType: FilterType): EditPictureEvent()
    data class SavePicture(val picture: PictureModel): EditPictureEvent()
    data class SetPictureToEdit(val picture: PictureModel): EditPictureEvent()

}