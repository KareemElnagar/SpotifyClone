package com.kareem.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kareem.data.remote.Constants.SONG_COLLECTION
import com.kareem.domain.entity.Song
import kotlinx.coroutines.tasks.await

class SongsDbFirebase {
    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getSongsFromFirebase() : List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

object Constants {
    const val SONG_COLLECTION = "songs"
}