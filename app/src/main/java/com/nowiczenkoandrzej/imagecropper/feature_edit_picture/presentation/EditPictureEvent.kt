package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation

import android.content.Context
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.FilterType

sealed class EditPictureEvent {

    data class AddFilter(val filterType: FilterType, val context: Context): EditPictureEvent()
    data class SavePicture(val picture: PictureItem): EditPictureEvent()
    data class SetPictureToEdit(val picture: PictureItem, val context: Context): EditPictureEvent()

}