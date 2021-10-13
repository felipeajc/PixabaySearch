package com.challenge.felipeajc.pixabaysearch.di

import android.content.Context
import com.challenge.felipeajc.pixabaysearch.data.ImagesRepository
import com.challenge.felipeajc.pixabaysearch.data.ImagesRepositoryContract
import com.challenge.felipeajc.pixabaysearch.data.local.*
import com.challenge.felipeajc.pixabaysearch.data.remote.RemotePixabayContract
import com.challenge.felipeajc.pixabaysearch.data.remote.pixabayApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return createPixabayImageDB(context)
    }

    @Provides
    @Singleton
    fun providesImagesDao(appDatabase: AppDatabase): PixabayImageDao {
        return appDatabase.pixabayImagesDao()
    }

    @Provides
    @Singleton
    fun providesPixabayLocal(daoPixabay: PixabayImageDao): LocalPixabayImageStoreContract {
        return LocalPixabayImageStore(daoPixabay)
    }


    @Provides
    @Singleton
    fun providespixabaysearchRemote(): RemotePixabayContract {
        return pixabayApi()
    }

    @Provides
    fun providesImageRepository(repository: ImagesRepository): ImagesRepositoryContract {
        return repository
    }
}