package com.kareem.spotifyclone.exoplayer.callbacks

import android.media.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.kareem.spotifyclone.exoplayer.FirebaseMusicSource

class MusicPlaybackHandler(
    private val firebaseMusicSource: FirebaseMusicSource,
    private val mediaController: MediaController,
    private val player: Player
) {

    init {
        // Listen for playback state changes
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        // Player is prepared and ready to play
                    }
                    // Handle other playback states as needed
                }
            }
        })
    }

    fun prepareFromMediaId(mediaId: String, playWhenReady: Boolean) {
        // Fetch the media item based on mediaId (replace with your actual logic)
        val itemToPlay = firebaseMusicSource.songs.find {
            mediaId == it.mediaId
        }

        if (itemToPlay != null) {
            // Set the media item and prepare the player


            mediaController.setMediaItem(itemToPlay)
            mediaController.prepare()

            // Set playWhenReady if needed
            mediaController.playWhenReady = playWhenReady
        } else {
            // Handle the case where the media item is not found
        }
    }

    // ... other methods to handle different preparation scenarios (if needed)
}