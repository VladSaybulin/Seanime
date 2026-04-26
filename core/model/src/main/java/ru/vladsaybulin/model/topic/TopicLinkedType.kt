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

package ru.vladsaybulin.model.topic

enum class TopicLinkedType(val serializedValue: String) {
    Anime("Anime"),
    Manga("Manga"),
    Ranobe("Ranobe"),
    Character("Character"),
    Person("Person"),
    Club("Club"),
    ClubPage("ClubPage"),
    Critique("Critique"),
    Review("Review"),
    Contest("Contest"),
    CosplayGallery("CosplayGallery"),
    Collection("Collection"),
    Article("Article"),
    Unknown("")
}

fun String.asTopicLinkedType() =
    TopicLinkedType.entries.first { it.serializedValue == this }