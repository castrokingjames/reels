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
package io.github.castrokingjames.datasource.remote.service

import io.github.castrokingjames.API_CLIPS
import io.github.castrokingjames.PARAM_CLIENT_PLATFORM
import io.github.castrokingjames.PARAM_CLIENT_VERSION
import io.github.castrokingjames.datasource.remote.error.handle
import io.github.castrokingjames.datasource.remote.response.FeedResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.coroutines.CoroutineContext

class FeedService constructor(
  private val httpClient: HttpClient,
  private val context: CoroutineContext,
) {
  suspend fun getFeed(platform: String, version: String): FeedResponse {
    return handle(context) {
      httpClient
        .get(API_CLIPS) {
          parameter(PARAM_CLIENT_PLATFORM, platform)
          parameter(PARAM_CLIENT_VERSION, version)
        }
    }
  }
}
