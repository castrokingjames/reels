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
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import io.github.castrokingjames.data.mapper.toModel
import io.github.castrokingjames.datasource.local.database.StoriesQueries
import io.github.castrokingjames.model.Story
import io.github.castrokingjames.repository.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class StoryDataRepository constructor(
  private val storyQueries: StoriesQueries,
  private val io: CoroutineDispatcher,
) : StoryRepository {

  override suspend fun loadStoriesByFeedId(feedId: String): Flow<List<Story>> {
    return channelFlow {
      storyQueries
        .selectAllByFeedId(feedId)
        .asFlow()
        .mapToList(io)
        .map { stories ->
          stories
            .map { story ->
              story.toModel()
            }
        }
        .collectLatest { stories ->
          send(stories)
        }
    }
  }

  override suspend fun loadById(id: String): Flow<Story> {
    return channelFlow {
      storyQueries
        .selectByStoryId(id)
        .asFlow()
        .mapToOne(io)
        .map { story ->
          story.toModel()
        }
        .collect { story ->
          send(story)
        }
    }
  }
}
