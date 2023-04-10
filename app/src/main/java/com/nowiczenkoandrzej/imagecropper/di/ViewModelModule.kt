package com.nowiczenkoandrzej.imagecropper.di

import android.app.Application
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.AnimationHandler
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.PositionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun providePositionManager(app: Application): PositionManager {
        return PositionManager(app)
    }

}