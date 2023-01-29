package com.nowiczenkoandrzej.imagecropper.presentation.add_picture

import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel

sealed class AddPictureEvent {
    object ChoosePicture: AddPictureEvent()
    object BackFromChoosingPicture: AddPictureEvent()
    data class SavePicture(val picture: PictureModel): AddPictureEvent()
    data class PictureCropped(val picture: PictureModel): AddPictureEvent()
}
