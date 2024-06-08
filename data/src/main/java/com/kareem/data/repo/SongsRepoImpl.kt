package com.kareem.data.repo

import com.kareem.data.remote.SongsDbFirebase
import com.kareem.domain.entity.Song
import com.kareem.domain.repo.SongsRepo

class SongsRepoImpl(private val songsDbFirebase: SongsDbFirebase) : SongsRepo {
    override suspend fun getSongsFromRemote(): List<Song> {
        return songsDbFirebase.getSongsFromFirebase()
    }

    override suspend fun getSongDetails(songId: String): Song {
        TODO("Not yet implemented")


    }
}