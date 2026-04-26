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

package ru.vladsaybulin.core.textprocessor

import org.junit.Assert.assertEquals
import org.junit.Test
import org.primeframework.transformer.domain.TagNode

class ListOfTagTransformersToMap {

    private class StubTagTransformer(
        override val tagNames: Set<String>,
        override val priority: Int = 0
    ) : TagTransformer<StringBuilder> {
        override fun TagTransformerScope<StringBuilder>.transform(
            tagNode: TagNode,
            builder: StringBuilder
        ): TagTransformerResult {
            return TagTransformerResult.Success
        }
    }

    @Test
    fun list_of_tag_transformers_can_be_converted_to_map_given_priority() {
        val a = StubTagTransformer(setOf("a"))
        val ab = StubTagTransformer(setOf("a", "b"))
        val b = StubTagTransformer(setOf("b"))
        val bPriority = StubTagTransformer(setOf("b"), 1)
        val bc = StubTagTransformer(setOf("b", "c"))

        val listOfTransformers = listOf(a, b, ab, bPriority, bc).shuffled()

        val map = listOfTransformers.toMapOfTagTransformers()

        //Number of tags. ("a", "b" and "c")
        assertEquals(3, map.size)

        //Contains "a", "b" and "c" tags
        assert(map.containsKey("a"))
        assert(map.containsKey("b"))
        assert(map.containsKey("c"))

        //Number of tag transformers
        assertEquals(2, map["a"]!!.size)
        assertEquals(4, map["b"]!!.size)
        assertEquals(1, map["c"]!!.size)

        //bPriority must be first of "b" transformers
        assert(map["b"]!!.first() === bPriority)
    }
}