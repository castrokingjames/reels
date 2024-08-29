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
import io.github.castrokingjames.reels.feature.stories.story.StoryComponent
import io.github.castrokingjames.usecase.LoadFeedUseCase
import io.github.castrokingjames.usecase.LoadStoriesByFeedIdUseCase
import kotlin.coroutines.CoroutineContext

class FeedComponentFactory(
  private val loadFeed: LoadFeedUseCase,
  private val loadStoriesByFeedId: LoadStoriesByFeedIdUseCase,
  private val storyComponentFactory: StoryComponent.Factory,
  private val coroutineContext: CoroutineContext,
) : FeedComponent.Factory {

  override fun invoke(componentContext: ComponentContext): FeedComponent {
    return FeedViewModel(
      loadFeed,
      loadStoriesByFeedId,
      storyComponentFactory,
      componentContext,
      coroutineContext,
    )
  }
}
