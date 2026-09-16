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

plugins {
    id("seanime.android.feature.impl")
}

android {
    namespace = "ru.vladsaybulin.feature.title.details.impl"
}

dependencies {
    implementation(projects.core.ui2.strings)
    implementation(projects.core.ui2.entry)
    implementation(projects.feature.title.details.api)

    implementation(libs.primeTransformer)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt.compose)

    implementation(libs.androidx.activity.compose)

    implementation(projects.feature.title.authors.api)
    implementation(projects.feature.title.characters.api)
    implementation(projects.feature.title.related.api)
    implementation(projects.feature.title.screenshots.api)
    implementation(projects.feature.title.videos.api)
    implementation(projects.feature.character.api)
    implementation(projects.feature.imageview.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.rate.editor.api)
}