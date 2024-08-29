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
package io.github.castrokingjames.datasource.remote.error

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

suspend inline fun <reified T> handle(
  context: CoroutineContext,
  crossinline response: suspend () -> HttpResponse,
): T = withContext(context) {
  val result = try {
    response()
  } catch (e: IOException) {
    throw Exception("${Error.ServiceUnavailable}: ${e.message}")
  }

  val code = result.status.value
  when (code) {
    in 200..299 -> Unit
    in 400..499 -> throw Exception("${Error.ClientError}: HTTP:$code")
    500 -> throw Exception("${Error.ServerError} HTTP:$code")
    else -> throw Exception("${Error.UnknownError}")
  }

  return@withContext try {
    result.body()
  } catch (e: Exception) {
    throw Exception("${Error.ServerError}: ${e.message}")
  }
}
