package com.kareem.spotifyclone.di

import com.kareem.domain.repo.SongsRepo
import com.kareem.domain.usecase.GetSongs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SongsUseCase {
    @Provides
    fun provideSongUseCase(songsRepo: SongsRepo): GetSongs {
        return GetSongs(songsRepo)
    }

}