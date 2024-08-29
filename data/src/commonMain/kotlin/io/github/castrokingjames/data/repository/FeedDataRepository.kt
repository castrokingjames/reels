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
package io.github.castrokingjames.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import io.github.castrokingjames.data.mapper.toEntity
import io.github.castrokingjames.data.mapper.toModel
import io.github.castrokingjames.datasource.local.database.FeedStories
import io.github.castrokingjames.datasource.local.database.FeedStoriesQueries
import io.github.castrokingjames.datasource.local.database.FeedsQueries
import io.github.castrokingjames.datasource.local.database.StoriesQueries
import io.github.castrokingjames.datasource.remote.service.FeedService
import io.github.castrokingjames.model.Feed
import io.github.castrokingjames.repository.FeedRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class FeedDataRepository constructor(
  private val feedService: FeedService,
  private val feedsQueries: FeedsQueries,
  private val storyQueries: StoriesQueries,
  private val feedStoriesQueries: FeedStoriesQueries,
  private val io: CoroutineDispatcher,
) : FeedRepository {

  override suspend fun loadFeeds(platform: String, version: String): Flow<Feed> {
    return channelFlow {
      val response = feedService.getFeed(platform, version)
      val feed = response.toModel()
      val entity = feed.toEntity()
      feedsQueries.upsert(entity)

      response
        .stories
        .map { story ->
          story
            .toModel()
            .toEntity()
        }
        .forEach { story ->
          storyQueries.upsert(story)
          val feedStories = FeedStories(feed.id, story.id)
          feedStoriesQueries.upsert(feedStories)
        }

      feedsQueries
        .selectAll()
        .asFlow()
        .mapToOne(io)
        .map { entity ->
          entity.toModel()
        }
        .collectLatest { feed ->
          send(feed)
        }
    }
  }
}
