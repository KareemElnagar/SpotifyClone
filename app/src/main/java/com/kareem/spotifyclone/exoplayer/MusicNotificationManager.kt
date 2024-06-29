package com.kareem.spotifyclone.exoplayer
import android.content.Context
import android.app.PendingIntent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.kareem.spotifyclone.R
import com.kareem.spotifyclone.other.Constants.NOTIFICATION_CHANNEL_ID
import com.kareem.spotifyclone.other.Constants.NOTIFICATION_ID


@UnstableApi
class MusicNotificationManager @OptIn(UnstableApi::class) constructor
    (
    val context: Context,
    sessionToken: SessionToken,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: () -> Unit
) {

    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var mediaController: MediaController

    init {
        val mediaControllerFuture = MediaController.Builder(context, sessionToken)
            .buildAsync()

        Futures.addCallback(mediaControllerFuture, object : FutureCallback<MediaController> {
            override fun onSuccess(result: MediaController) {
                mediaController = result
                playerNotificationManager = PlayerNotificationManager.Builder(
                    context,
                    NOTIFICATION_ID,
                    NOTIFICATION_CHANNEL_ID
                )
                    .setChannelNameResourceId(R.string.notification_channel_name)
                    .setChannelDescriptionResourceId(R.string.notification_channel_description)
                    .setMediaDescriptionAdapter(
                        DescriptionAdapter(
                            mediaController,
                            newSongCallback
                        )
                    )
                    .setNotificationListener(notificationListener)
                    .setSmallIconResourceId(R.drawable.ic_music)
                    .build()

            }

            override fun onFailure(t: Throwable) {
                Log.e("MusicNotificationManager", "Error Creating Media Controller", t)

            }

        }, ContextCompat.getMainExecutor(context))
    }

    // Add a method to set the Player instance when available
    fun showNotification(player: Player) {
        playerNotificationManager.setPlayer(player)
    }

    // ... other methods to manage notifications (if needed)
    private inner class DescriptionAdapter(
        private val mediaController: MediaController,
        private val newSongCallback: () -> Unit
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        // Implement the required methods, using mediaController.currentMediaItem
        // to access metadata and trigger newSongCallback when appropriate
        // ...
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaController.mediaMetadata.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return mediaController.mediaMetadata.subtitle.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap()
                .load(mediaController.mediaMetadata.artworkUri)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback.onBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) = Unit

                })
            return null
        }
    }
}

