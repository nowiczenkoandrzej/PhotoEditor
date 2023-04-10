package com.nowiczenkoandrzej.imagecropper.di

import android.app.Application
import androidx.room.Room
import com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.PictureDatabase
import com.nowiczenkoandrzej.imagecropper.core.data.repository.PictureRepositoryImpl
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.PositionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePictureDataBase(app: Application): PictureDatabase {
        return Room.databaseBuilder(
            app,
            PictureDatabase::class.java,
            PictureDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePictureRepository(db: PictureDatabase): PictureRepository {
        return PictureRepositoryImpl(db.pictureDao)
    }


}