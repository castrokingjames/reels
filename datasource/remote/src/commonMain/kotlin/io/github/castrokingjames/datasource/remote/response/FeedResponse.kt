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
package io.github.castrokingjames.datasource.remote.response

import io.github.castrokingjames.FEED_STORIES
import io.github.castrokingjames.FEED_THUMBNAIL
import io.github.castrokingjames.FEED_TITLE
import io.github.castrokingjames.STORY_CREATED_AT
import io.github.castrokingjames.STORY_DESCRIPTION
import io.github.castrokingjames.STORY_ID
import io.github.castrokingjames.STORY_LIKES
import io.github.castrokingjames.STORY_SHARES
import io.github.castrokingjames.STORY_THUMBNAIL
import io.github.castrokingjames.STORY_URL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse(
  @SerialName(FEED_TITLE)
  val title: String,
  @SerialName(FEED_THUMBNAIL)
  val thumbnail: String? = "",
  @SerialName(FEED_STORIES)
  val stories: List<Story>,
) {

  @Serializable
  data class Story(
    @SerialName(STORY_ID)
    val id: String,
    @SerialName(STORY_DESCRIPTION)
    val description: String? = "",
    @SerialName(STORY_THUMBNAIL)
    val thumbnail: String,
    @SerialName(STORY_URL)
    val url: String,
    @SerialName(STORY_LIKES)
    val likes: Int,
    @SerialName(STORY_SHARES)
    val shares: Int,
    @SerialName(STORY_CREATED_AT)
    val createdAt: String,
  )
}
