package com.kareem.domain.usecase

import com.kareem.domain.repo.SongsRepo

class GetSongDetails(private val songsRepo: SongsRepo) {
    suspend operator fun invoke(songId: String) = songsRepo.getSongDetails(songId)
}