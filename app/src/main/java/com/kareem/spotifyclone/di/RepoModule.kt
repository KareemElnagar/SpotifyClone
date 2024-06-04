package com.kareem.spotifyclone.di

import com.kareem.data.remote.SongsDbFirebase
import com.kareem.data.repo.SongsRepoImpl
import com.kareem.domain.repo.SongsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRepo(songsDbFirebase: SongsDbFirebase): SongsRepo {
        return SongsRepoImpl(songsDbFirebase)
    }
}