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

package ru.vladsaybulin.core.textprocessor.util

import org.primeframework.transformer.domain.Document
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.service.HTMLParser

fun String.toHtmlDocument(): Document =
    HTMLParser().buildDocument(this, emptyMap())

fun TagNode.htmlClasses() =
    attributes["class"]?.split(' ')

fun TagNode.containsHtmlClass(classValue: String) =
    htmlClasses()?.contains(classValue) ?: false