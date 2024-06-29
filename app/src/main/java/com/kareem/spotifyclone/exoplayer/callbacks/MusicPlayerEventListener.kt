package com.kareem.spotifyclone.exoplayer.callbacks

import android.app.Service.STOP_FOREGROUND_DETACH
import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.kareem.spotifyclone.exoplayer.MusicService

class MusicPlayerEventListener @OptIn(UnstableApi::class) constructor
    (
    private val musicService: MusicService
) : Player.Listener { // Use Player.Listener in Media3

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
         when (playbackState) {
            Player.STATE_READY -> {
                // Player is ready, but check if it's actually playing
                if (musicService.exoPlayer.playWhenReady) {
                    //musicService.startForegroundService()// Start foreground if playing
                } else {
                    musicService.stopForeground(STOP_FOREGROUND_DETACH) // Stop foreground (keep notification)
                }
            }
            Player.STATE_ENDED, Player.STATE_IDLE -> {
                musicService.stopForeground(STOP_FOREGROUND_REMOVE) // Stop foreground and remove notification
            }
            // Handle other states as needed (e.g., BUFFERING)
        }
    }

    override fun onPlayerError(error: PlaybackException) { // Use PlaybackException in Media3
        super.onPlayerError(error)
        val message = when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_UNSPECIFIED -> "Network error occurred"
            PlaybackException.ERROR_CODE_DECODING_FAILED -> "Media decoding failed"
            // ... handle other specific error codes
            else -> "An unknown error occurred"
        }
        Toast.makeText(musicService, message, Toast.LENGTH_LONG).show()
    }
}