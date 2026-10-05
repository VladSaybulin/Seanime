/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.common

import kotlinx.coroutines.CancellationException

/**
 * Executes a suspending [block] and wraps its result into [Result].
 *
 * Behavior:
 * - returns [Result.success] when [block] completes successfully;
 * - rethrows [CancellationException] to preserve structured concurrency cancellation semantics;
 * - invokes [catch] for any other [Throwable] and returns [Result.failure].
 *
 * This function does not perform logging, keeping it platform-agnostic.
 *
 * @param catch Callback for non-cancellation failures. The default implementation rethrows the error.
 * @param block Suspended operation to execute.
 * @return [Result.success] with the value from [block] or [Result.failure] with the thrown exception.
 */
suspend inline fun <T> runCatchingCancellable(
    noinline catch: suspend (Throwable) -> Unit = { throw it },
    block: suspend () -> T
): Result<T> {
    try {
        return Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        catch(e)
        return Result.failure(e)
    }
}