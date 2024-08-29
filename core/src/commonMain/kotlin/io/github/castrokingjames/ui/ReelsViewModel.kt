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
package io.github.castrokingjames.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.github.castrokingjames.reels.feature.stories.FeedComponent
import kotlinx.serialization.Serializable

class ReelsViewModel internal constructor(
  private val feedComponentFactory: FeedComponent.Factory,
  private val componentContext: ComponentContext,
) : ReelsComponent, ComponentContext by componentContext {

  private val nav = StackNavigation<Config>()

  override val stack: Value<ChildStack<*, ReelsComponent.Child>> =
    childStack(
      source = nav,
      initialConfiguration = Config.Feed,
      serializer = Config.serializer(),
      handleBackButton = true,
      childFactory = ::child,
    )

  private fun child(config: Config, context: ComponentContext): ReelsComponent.Child =
    when (config) {
      is Config.Feed -> ReelsComponent.Child.Feed(feedComponentFactory(context))
    }

  @Serializable
  private sealed interface Config {

    @Serializable
    data object Feed : Config
  }
}
