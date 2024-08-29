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
package io.github.castrokingjames.reels.desktop

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.LocalWindowSizeClassProvider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass.Companion.calculateFromSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.castrokingjames.AppTheme
import io.github.castrokingjames.BASE_URL
import io.github.castrokingjames.DEBUG
import io.github.castrokingjames.STORYTELLER_API_KEY
import io.github.castrokingjames.di.initKoin
import io.github.castrokingjames.initializer.StartupInitializer
import io.github.castrokingjames.ui.ReelsApp
import io.github.castrokingjames.ui.ReelsComponent

private val koin =
  initKoin {
    properties(
      mapOf(
        DEBUG to (System.getenv(DEBUG).toBoolean() ?: false),
        BASE_URL to (System.getenv(BASE_URL) ?: ""),
        STORYTELLER_API_KEY to (System.getenv(STORYTELLER_API_KEY) ?: ""),
      ),
    )
  }.koin

fun main() {
  val initializer = koin.get<StartupInitializer>()
  initializer()

  application {
    val component = koin.get<ReelsComponent>()
    // Emulate android phone size
    val windowState = rememberWindowState(size = DpSize(400.dp, 820.dp))
    Window(
      title = "Reels",
      onCloseRequest = ::exitApplication,
      state = windowState,
    ) {
      BoxWithConstraints {
        val windowSizeClass: WindowSizeClass = calculateFromSize(DpSize(maxWidth, maxHeight))
        CompositionLocalProvider(LocalWindowSizeClassProvider provides windowSizeClass) {
          AppTheme {
            ReelsApp(
              component = component,
            )
          }
        }
      }
    }
  }
}
