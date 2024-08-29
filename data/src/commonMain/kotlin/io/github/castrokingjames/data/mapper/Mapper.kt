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
package io.github.castrokingjames.data.mapper

import io.github.castrokingjames.datasource.local.database.Feeds
import io.github.castrokingjames.datasource.local.database.Stories
import io.github.castrokingjames.datasource.remote.response.FeedResponse
import io.github.castrokingjames.model.Feed
import io.github.castrokingjames.model.Story
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun FeedResponse.toModel(): Feed {
  val id = title.lowercase().replace(" ", "-")
  val thumbnail = thumbnail ?: ""
  return Feed(
    id,
    title,
    thumbnail,
  )
}

fun Feed.toEntity(): Feeds {
  return Feeds(
    id,
    title,
    thumbnail,
  )
}

fun Feeds.toModel(): Feed {
  return Feed(
    id,
    title,
    thumbnail,
  )
}

fun FeedResponse.Story.toModel(): Story {
  return Story(
    id,
    description ?: "",
    thumbnail,
    url,
    likes,
    shares,
    LocalDateTime.parse(createdAt).toInstant(TimeZone.UTC).epochSeconds,
  )
}

fun Story.toEntity(): Stories {
  return Stories(
    id,
    description,
    thumbnail,
    url,
    likes.toLong(),
    shares.toLong(),
    createdAt,
  )
}

fun Stories.toModel(): Story {
  return Story(
    id,
    description ?: "",
    thumbnail,
    url,
    likes.toInt(),
    shares.toInt(),
    createdAt,
  )
}
