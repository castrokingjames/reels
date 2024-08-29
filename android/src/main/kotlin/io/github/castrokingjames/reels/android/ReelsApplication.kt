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
package io.github.castrokingjames.reels.android

import android.app.Application
import io.github.castrokingjames.BASE_URL
import io.github.castrokingjames.DEBUG
import io.github.castrokingjames.STORYTELLER_API_KEY
import io.github.castrokingjames.di.initKoin
import io.github.castrokingjames.initializer.StartupInitializer
import org.koin.android.ext.koin.androidContext

class ReelsApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    val koin =
      initKoin {
        properties(mapOf(DEBUG to BuildConfig.DEBUG))
        properties(mapOf(BASE_URL to BuildConfig.BASE_URL))
        properties(mapOf(STORYTELLER_API_KEY to BuildConfig.STORYTELLER_API_KEY))
        androidContext(this@ReelsApplication)
      }.koin
    val initializer = koin.get<StartupInitializer>()
    initializer()
  }
}
