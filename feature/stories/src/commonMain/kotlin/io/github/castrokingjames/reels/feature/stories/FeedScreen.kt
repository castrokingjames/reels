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
@file:OptIn(ExperimentalMaterialApi::class)

package io.github.castrokingjames.reels.feature.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.castrokingjames.model.Feed
import io.github.castrokingjames.model.Story
import io.github.castrokingjames.ui.VideoPlayer
import io.github.castrokingjames.ui.shimmerBackground

@Composable
fun FeedScreen(
  modifier: Modifier = Modifier,
  component: FeedComponent,
) {
  val state = component
    .feed
    .collectAsState()
    .value

  when (state) {
    is FeedComponent.FeedUiState.Loading -> {
      LoadingView(modifier)
    }

    is FeedComponent.FeedUiState.Success -> {
      StoriesView(
        modifier = modifier,
        feed = state.feed,
        stories = state.stories,
      )
    }

    is FeedComponent.FeedUiState.Error -> {
      ErrorView(
        modifier = modifier,
        exception = state.exception,
      )
    }
  }
}

@Composable
internal fun LoadingView(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .background(MaterialTheme.colorScheme.primary)
      .fillMaxSize(),
  ) {
    Text(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(16.dp)
        .shimmerBackground(RoundedCornerShape(4.dp)),
      text = "Dummy feed title",
      style = MaterialTheme.typography.titleLarge,
      color = Color.Transparent,
    )

    Text(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(
          start = 16.dp,
          bottom = 32.dp,
        )
        .shimmerBackground(RoundedCornerShape(4.dp)),
      text = "Dummy story title",
      style = MaterialTheme.typography.titleMedium,
      color = Color.Transparent,
    )

    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(
          end = 16.dp,
          bottom = 32.dp,
        ),
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .shimmerBackground(RoundedCornerShape(4.dp)),
      )
      Spacer(
        modifier = Modifier.size(16.dp),
      )
      Box(
        modifier = Modifier
          .size(44.dp)
          .shimmerBackground(RoundedCornerShape(4.dp)),
      )
    }
  }
}

@Composable
internal fun StoriesView(
  modifier: Modifier = Modifier,
  feed: Feed,
  stories: List<Story>,
) {
  Box(
    modifier = modifier
      .background(MaterialTheme.colorScheme.primary)
      .fillMaxSize(),
  ) {
    val pageCount = stories.size
    val max = Short.MAX_VALUE.toInt()
    val half = max / 2
    val buffer = half % pageCount
    val pagerPositionIndex = 0 + half - buffer
    val pagerState = rememberPagerState(pageCount = { max }, initialPage = 0)

    LaunchedEffect(pagerPositionIndex, pageCount) {
      pagerState.scrollToPage(pagerPositionIndex)
    }

    VerticalPager(
      state = pagerState,
      userScrollEnabled = pageCount > 1,
      modifier = modifier,
    ) { index ->
      val page = index % pageCount
      val story = stories[page]
      val shouldPlayVideo = remember { mutableStateOf(false) }
      StoryView(story, shouldPlayVideo)
      shouldPlayVideo.value = pagerState.currentPage == index
    }

    Text(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(16.dp),
      text = "${feed.title}",
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onPrimary,
    )
  }
}

@Composable
internal fun StoryView(
  story: Story,
  shouldPlayVideo: MutableState<Boolean>,
) {
  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    val thumbnail = remember { mutableStateOf(true) }
    if (shouldPlayVideo.value) {
      VideoPlayer(
        story.url,
        onRenderedFirstFrame = {
          thumbnail.value = false
        },
        onReleased = {
          thumbnail.value = true
        },
      )
    }

    if (thumbnail.value) {
      AsyncImage(
        model = story.thumbnail,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillHeight,
        contentDescription = null,
      )
    }

    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(
          end = 16.dp,
          bottom = 32.dp,
        ),
    ) {
      ActionIcon(
        imageVector = Icons.Outlined.FavoriteBorder,
        text = "${story.likes}",
      )
      Spacer(
        modifier = Modifier.size(16.dp),
      )
      ActionIcon(
        imageVector = Icons.Outlined.Share,
        text = "${story.shares}",
      )
    }

    Text(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(
          start = 16.dp,
          bottom = 32.dp,
        ),
      text = story.description,
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onPrimary,
    )
  }
}

@Composable
internal fun ErrorView(
  modifier: Modifier = Modifier,
  exception: Throwable,
) {
  Box(
    modifier = modifier.fillMaxSize(),
  ) {
    Text(
      modifier = Modifier.align(Alignment.Center),
      text = "${exception.message}",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onPrimary,
    )
  }
}

@Composable
internal fun ActionIcon(
  imageVector: ImageVector,
  text: String,
) {
  Column {
    Box {
      Card(
        modifier = Modifier
          .alpha(0.4f)
          .size(44.dp)
          .align(Alignment.Center),
        shape = RoundedCornerShape(32.dp),
        backgroundColor = Color.Black,
        onClick = {
        },
      ) {
      }
      Icon(
        modifier = Modifier
          .padding(8.dp, 8.dp)
          .size(24.dp)
          .align(Alignment.Center),
        imageVector = imageVector,
        contentDescription = null,
        tint = Color.White,
      )
    }
    Spacer(
      modifier = Modifier.size(4.dp),
    )
    Text(
      modifier = Modifier.align(Alignment.CenterHorizontally),
      style = MaterialTheme.typography.titleSmall,
      text = text,
      color = Color.White,
    )
  }
}
