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

package ru.vladsaybulin.data.request

/**
 * Scope exposed to repository refresh blocks.
 *
 * Implementations define how writes are executed (for example in a DB transaction)
 * and may append additional side effects like updating last refresh timestamp.
 */
fun interface UpdateScope {
    /**
     * Executes write operations inside synchronization-provided context.
     */
    suspend fun write(block: suspend () -> Unit)
}