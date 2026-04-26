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

package ru.vladsaybulin.core.ui.text

import ru.vladsaybulin.model.annotatedtext.SeanimeText

interface ClickableRange <Item> {
    val range: SeanimeText.Range<Item>
}

class RangeWrapper <Item>(
    override val range: SeanimeText.Range<Item>
) : ClickableRange<Item> {
    val start: Int = range.start
    val end: Int = range.end
    val tag: String = range.tag
    val item: Item = range.item
}