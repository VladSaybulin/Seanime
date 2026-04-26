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

package ru.vladsaybulin.database.models.text

import androidx.room.ColumnInfo
import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.model.annotatedtext.SeanimeText

data class SeanimeTextPOJO(

    @ColumnInfo("text")
    val text: String,

    @ColumnInfo("styles")
    val styles: List<ProtoSeanimeTextRange<ProtoSeanimeTextStyle>>,

    @ColumnInfo("inline_spoilers")
    val inlineSpoilers: List<ProtoSeanimeTextRange<Unit>>,

    @ColumnInfo("spoiler_blocks")
    val spoilerBlocks: List<ProtoSeanimeTextRange<ProtoSeanimeTextSpoilerBlockItem>>,

    @ColumnInfo("links")
    val links: List<ProtoSeanimeTextRange<String>>,
)

fun SeanimeTextPOJO.asExternalModel() = SeanimeText(
    text = text,
    styles = styles.map { style ->
        style.asExternalModel { styleItem -> styleItem.asExternalModel() }
    }.toImmutableList(),
    inlineSpoilers = inlineSpoilers.map { spoiler ->
        spoiler.asExternalModel { }
    }.toImmutableList(),
    spoilerBlocks = spoilerBlocks.map { spoiler ->
        spoiler.asExternalModel { it.asExternalModel() }
    }.toImmutableList(),
    links = links.map { link -> link.asExternalModel { it } }.toImmutableList()
)