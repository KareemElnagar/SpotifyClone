package com.kareem.spotifyclone.exoplayer

import com.kareem.spotifyclone.exoplayer.callbacks.MusicPlaybackHandler
import android.app.PendingIntent
import android.content.Intent
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import com.kareem.spotifyclone.exoplayer.callbacks.MusicPlayerNotificationListener
import com.kareem.spotifyclone.other.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject


private const val SERVICE_TAG = "Music Service"

@UnstableApi
@AndroidEntryPoint
class MusicService : MediaBrowserService() {
    @Inject
    lateinit var dataSourceFactoryFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    private lateinit var musicNotificationManager: MusicNotificationManager


    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSession
    private lateinit var mediaController: MediaController

    var isForegroundService = false

    private var curPlayingSong: MediaMetadata? = null
    override fun onCreate() {
        super.onCreate()
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        exoPlayer = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, exoPlayer).apply {
            activityIntent?.let { setSessionActivity(it) }


        }.build()
        val sessionToken = mediaSession.token

        musicNotificationManager = MusicNotificationManager(
            this,
            sessionToken,
            MusicPlayerNotificationListener(this)
        ) {

        }
        val musicPlaybackPreparer =
            MusicPlaybackHandler(firebaseMusicSource, mediaController, exoPlayer)

        val mediaControllerFuture = MediaController.Builder(this, sessionToken)
            .buildAsync()


    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO()
    }


    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowser.MediaItem>>
    ) {

    }
}