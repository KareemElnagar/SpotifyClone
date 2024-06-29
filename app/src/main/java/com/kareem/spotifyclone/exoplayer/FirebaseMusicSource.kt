package com.kareem.spotifyclone.exoplayer

import android.media.MediaDescription
import android.media.MediaMetadata
import android.media.MediaMetadata.METADATA_KEY_ALBUM_ART_URI
import android.media.MediaMetadata.METADATA_KEY_ARTIST
import android.media.MediaMetadata.METADATA_KEY_DISPLAY_DESCRIPTION
import android.media.MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI
import android.media.MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE
import android.media.MediaMetadata.METADATA_KEY_DISPLAY_TITLE
import android.media.MediaMetadata.METADATA_KEY_MEDIA_ID
import android.media.MediaMetadata.METADATA_KEY_MEDIA_URI
import android.media.MediaMetadata.METADATA_KEY_TITLE
import android.media.browse.MediaBrowser
import android.media.browse.MediaBrowser.MediaItem.FLAG_PLAYABLE
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.kareem.data.remote.SongsDbFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val musicDatabase: SongsDbFirebase
) {
    var songs = emptyList<MediaItem>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        val allSongs = musicDatabase.getSongsFromFirebase()
        songs = allSongs.map { song ->
            MediaItem.Builder()
                .setUri(song.songUrl)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.subtitle)
                        .setArtworkUri(song.imageUrl.toUri())
                        .build()
                )
                .setMediaId(song.mediaId)
                .build()
        }
        state = State.STATE_INITIALIZED
        }
//        fun asMediaItems() = songs.map { song ->
//            val desc = MediaDescription.Builder()
//                .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
//                .setTitle(song.description.title)
//                .setSubtitle(song.description.subtitle)
//                .setMediaId(song.description.mediaId)
//                .setIconUri(song.description.iconUri)
//                .build()
//            MediaBrowser.MediaItem(desc, FLAG_PLAYABLE)
//        }

    private val onReadyListener = mutableListOf<(Boolean) -> Unit>()

//    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
//    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
//        val concatenatingMediaSource = ConcatenatingMediaSource()
//
//        songs.forEach { song ->
//            val mediaItem = MediaItem.Builder()
//                .setUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
//                .build()
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(mediaItem)
//            concatenatingMediaSource.addMediaSource(mediaSource)
//
//        }
//        return concatenatingMediaSource
//    }

    private var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListener) {
                    field = value
                    onReadyListener.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            } else {
                field = value

            }


        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListener += action
            return false
        } else {
            action(state == State.STATE_INITIALIZED)
            return true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,


}