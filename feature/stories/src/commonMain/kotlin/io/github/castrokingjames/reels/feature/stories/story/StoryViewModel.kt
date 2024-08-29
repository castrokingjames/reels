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
package io.github.castrokingjames.reels.feature.stories.story

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import io.github.castrokingjames.usecase.LoadStoryByStoryIdUseCase
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StoryViewModel constructor(
  private val id: String,
  private val loadStoryByStoryId: LoadStoryByStoryIdUseCase,
  private val componentContext: ComponentContext,
  private val coroutineContext: CoroutineContext,
) : StoryComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(coroutineContext + SupervisorJob())

  override val story: StateFlow<StoryComponent.StoryUIState> = loadStoryByStoryId(id)
    .map { story ->
      StoryComponent.StoryUIState.Success(story)
    }
    .stateIn(
      scope,
      started = SharingStarted.WhileSubscribed(5000L),
      initialValue = StoryComponent.StoryUIState.Loading,
    )
}
