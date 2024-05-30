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