package com.kareem.domain.repo

import com.kareem.domain.entity.Song

interface SongsRepo {
   suspend fun getSongsFromRemote() : List<Song>
   suspend fun getSongDetails(songId: String) : Song

}