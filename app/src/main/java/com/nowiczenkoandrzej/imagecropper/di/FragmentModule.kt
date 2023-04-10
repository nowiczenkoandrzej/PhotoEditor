package com.nowiczenkoandrzej.imagecropper.di

import android.app.Application
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.AnimationHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped


@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @FragmentScoped
    @Provides
    fun provideAnimationHandler(app: Application): AnimationHandler {
        return AnimationHandler(app)
    }

}