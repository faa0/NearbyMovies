package com.fara.nearbymovies.di

import android.content.Context
import androidx.room.Room
import com.fara.nearbymovies.db.AppDatabase
import com.fara.nearbymovies.repo.LocalRepo
import com.fara.nearbymovies.repo.RemoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "cinema_db.db",
    ).build()

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase) = db.getCinemaDao()

    @Singleton
    @Provides
    fun provideLocalRepo(db: AppDatabase): LocalRepo = LocalRepo(db)

    @Singleton
    @Provides
    fun provideRemoteRepo(): RemoteRepo = RemoteRepo()
}