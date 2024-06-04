package com.kareem.spotifyclone.di

import com.kareem.domain.repo.SongsRepo
import com.kareem.domain.usecase.GetSongDetails
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SongDetailUseCase {
    @Provides
    fun provideSongDetailUseCase(songsRepo: SongsRepo) : GetSongDetails {
        return GetSongDetails(songsRepo)
    }
}