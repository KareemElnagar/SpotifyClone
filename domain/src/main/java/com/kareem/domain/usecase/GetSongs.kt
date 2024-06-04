package com.kareem.domain.usecase

import com.kareem.domain.repo.SongsRepo

class GetSongs(private val songsRepo: SongsRepo) {
    suspend operator fun invoke() = songsRepo.getSongsFromRemote()
}