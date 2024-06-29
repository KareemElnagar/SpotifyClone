package com.kareem.spotifyclone.exoplayer.callbacks

import android.app.Notification
import android.app.Service.STOP_FOREGROUND_DETACH
import android.content.Intent
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.kareem.spotifyclone.exoplayer.MusicService
import com.kareem.spotifyclone.other.Constants.NOTIFICATION_ID

@UnstableApi
class MusicPlayerNotificationListener @OptIn(UnstableApi::class) constructor
    (
    private val musicService: MusicService
) : PlayerNotificationManager.NotificationListener {
    @OptIn(UnstableApi::class)
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicService.apply {
            stopForeground(STOP_FOREGROUND_DETACH)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    this,
                    Intent(applicationContext, this::class.java)
                )
                startForeground(NOTIFICATION_ID,notification)
                isForegroundService = true
            }
        }
    }
}