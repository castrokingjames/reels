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
package io.github.castrokingjames.reels.feature.stories

import com.arkivanov.decompose.ComponentContext
import io.github.castrokingjames.model.Feed
import io.github.castrokingjames.model.Story
import io.github.castrokingjames.reels.feature.stories.story.StoryComponent
import kotlinx.coroutines.flow.StateFlow

interface FeedComponent {

  val feed: StateFlow<FeedUiState>

  fun story(id: String): StoryComponent

  sealed interface FeedUiState {
    data class Success(val feed: Feed, val stories: List<Story>) : FeedUiState
    data class Error(val exception: Throwable) : FeedUiState
    data object Loading : FeedUiState
  }

  interface Factory {

    operator fun invoke(componentContext: ComponentContext): FeedComponent
  }
}
