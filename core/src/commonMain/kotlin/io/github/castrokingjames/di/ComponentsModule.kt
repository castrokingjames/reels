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
package io.github.castrokingjames.di

import io.github.castrokingjames.di.annotation.Dispatcher
import io.github.castrokingjames.reels.feature.stories.FeedComponent
import io.github.castrokingjames.reels.feature.stories.FeedComponentFactory
import io.github.castrokingjames.reels.feature.stories.story.StoryComponent
import io.github.castrokingjames.reels.feature.stories.story.StoryComponentFactory
import io.github.castrokingjames.ui.ReelsComponent
import io.github.castrokingjames.ui.ReelsViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val componentsModule = module {
  includes(componentContextModule)

  single {
    ReelsViewModel(get(), get())
  } bind ReelsComponent::class

  single {
    FeedComponentFactory(get(), get(), get(), get(named<Dispatcher.Main>()))
  } bind FeedComponent.Factory::class

  single {
    StoryComponentFactory(get(), get(named<Dispatcher.Main>()))
  } bind StoryComponent.Factory::class
}
