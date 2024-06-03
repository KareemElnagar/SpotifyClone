package com.kareem.spotifyclone.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kareem.spotifyclone.data.entities.Song
import com.kareem.spotifyclone.other.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await

class MusicDatabase {
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