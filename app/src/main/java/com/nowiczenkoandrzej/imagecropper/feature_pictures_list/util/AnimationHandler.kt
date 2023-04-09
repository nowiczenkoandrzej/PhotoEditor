package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util

import android.content.Context
import android.view.animation.AnimationUtils
import com.nowiczenkoandrzej.imagecropper.R

class AnimationHandler (context: Context) {

    val rotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim)
    val rotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim)
    val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
    val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)

}