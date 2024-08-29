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
@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.castrokingjames.reels.feature.stories

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import io.github.castrokingjames.reels.feature.stories.story.StoryComponent
import io.github.castrokingjames.usecase.LoadFeedUseCase
import io.github.castrokingjames.usecase.LoadStoriesByFeedIdUseCase
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FeedViewModel constructor(
  private val loadFeed: LoadFeedUseCase,
  private val loadStoriesByFeedId: LoadStoriesByFeedIdUseCase,
  private val storyComponentFactory: StoryComponent.Factory,
  private val componentContext: ComponentContext,
  private val coroutineContext: CoroutineContext,
) : FeedComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(coroutineContext + SupervisorJob())

  override val feed: StateFlow<FeedComponent.FeedUiState> = loadFeed()
    .flatMapLatest { feed ->
      loadStoriesByFeedId(feed.id)
        .map { stories ->
          FeedComponent.FeedUiState.Success(feed, stories) as FeedComponent.FeedUiState
        }
    }
    .catch { e ->
      emit(FeedComponent.FeedUiState.Error(e))
    }
    .stateIn(
      scope,
      started = SharingStarted.WhileSubscribed(5000L),
      initialValue = FeedComponent.FeedUiState.Loading,
    )

  override fun story(id: String): StoryComponent {
    return storyComponentFactory(id, componentContext)
  }
}
