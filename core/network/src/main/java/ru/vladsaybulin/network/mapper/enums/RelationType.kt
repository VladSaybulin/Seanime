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

package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.RelationKindEnum
import ru.vladsaybulin.model.related.RelationType

fun RelationKindEnum.asRelationType() = when (this) {
    RelationKindEnum.adaptation -> RelationType.Adaptation
    RelationKindEnum.alternative_setting -> RelationType.AltSetting
    RelationKindEnum.alternative_version -> RelationType.AltHistory
    RelationKindEnum.character -> RelationType.Character
    RelationKindEnum.full_story -> RelationType.FullStory
    RelationKindEnum.parent_story -> RelationType.ParentStory
    RelationKindEnum.prequel -> RelationType.Prequel
    RelationKindEnum.sequel -> RelationType.Sequel
    RelationKindEnum.side_story -> RelationType.SideStory
    RelationKindEnum.spin_off -> RelationType.SpinOff
    RelationKindEnum.summary -> RelationType.Summary
    else -> RelationType.Other
}