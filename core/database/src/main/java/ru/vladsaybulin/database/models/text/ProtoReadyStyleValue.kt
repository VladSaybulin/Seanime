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

import ru.vladsaybulin.model.annotatedtext.SeanimeText

enum class ProtoReadyStyleValue {
    H1,
    H2,
    H3,
    H4,
    H5,
    H6,
    UNDERLINE,
    STRIKETHROUGH,
    BOLD,
    ITALIC
}

fun ProtoReadyStyleValue.asExternalModel() = when (this) {
    ProtoReadyStyleValue.H1 -> SeanimeText.ReadyStyleValue.H1
    ProtoReadyStyleValue.H2 -> SeanimeText.ReadyStyleValue.H2
    ProtoReadyStyleValue.H3 -> SeanimeText.ReadyStyleValue.H3
    ProtoReadyStyleValue.H4 -> SeanimeText.ReadyStyleValue.H4
    ProtoReadyStyleValue.H5 -> SeanimeText.ReadyStyleValue.H5
    ProtoReadyStyleValue.H6 -> SeanimeText.ReadyStyleValue.H6
    ProtoReadyStyleValue.UNDERLINE -> SeanimeText.ReadyStyleValue.Underline
    ProtoReadyStyleValue.STRIKETHROUGH -> SeanimeText.ReadyStyleValue.Strikethrough
    ProtoReadyStyleValue.BOLD -> SeanimeText.ReadyStyleValue.Bold
    ProtoReadyStyleValue.ITALIC -> SeanimeText.ReadyStyleValue.Italic
}