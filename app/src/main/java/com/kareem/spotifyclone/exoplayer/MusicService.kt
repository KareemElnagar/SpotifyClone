package com.kareem.spotifyclone.exoplayer

import android.app.PendingIntent
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
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

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSession

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