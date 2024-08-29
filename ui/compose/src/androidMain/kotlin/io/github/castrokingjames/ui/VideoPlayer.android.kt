/*
 * Copyright 2024, King James Castro and project contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.castrokingjames.ui

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import timber.log.Timber
import timber.log.error

@UnstableApi
@Composable
actual fun VideoPlayer(
  url: String,
  onRenderedFirstFrame: () -> Unit,
  onReleased: () -> Unit,
) {
  val context = LocalContext.current
  val player = remember(context) {
    val player = ExoPlayer
      .Builder(context)
      .build()
    player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    player.repeatMode = Player.REPEAT_MODE_ONE
    player.playWhenReady = true
    player.setMediaItem(MediaItem.fromUri(url))
    player.addListener(object : Player.Listener {
      override fun onRenderedFirstFrame() {
        onRenderedFirstFrame()
      }

      override fun onPlayerError(error: PlaybackException) {
        Timber.error { "Error: ${error.message}" }
        player.seekTo(0)
      }
    })
    player.prepare()
    player
  }
  val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
  DisposableEffect(key1 = lifecycleOwner) {
    val lifeCycleObserver = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_STOP -> {
          player.pause()
        }

        Lifecycle.Event.ON_START -> player.play()
        else -> {}
      }
    }
    lifecycleOwner.lifecycle.addObserver(lifeCycleObserver)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(lifeCycleObserver)
    }
  }

  val playerView = remember {
    val playerView = PlayerView(context)
    playerView.player = player
    playerView.useController = false
    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    playerView.layoutParams = ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT,
    )
    playerView
  }

  DisposableEffect(
    key1 = AndroidView(
      factory = {
        playerView
      },
    ),
    effect = {
      onDispose {
        player.release()
        onReleased()
      }
    },
  )
}
