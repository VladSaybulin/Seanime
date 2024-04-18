package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.related.RelationType


@Composable
@ReadOnlyComposable
fun relationTypeString(relationType: RelationType) =
    stringResource(id = relationTypeStringResId(relationType))

fun relationTypeStringResId(relationType: RelationType) = when (relationType) {
    RelationType.Adaptation -> R.string.relation_type_adaptation
    RelationType.AltHistory -> R.string.relation_type_alt_history
    RelationType.SideStory -> R.string.relation_type_side_story
    RelationType.SpinOff -> R.string.relation_type_spin_off
    RelationType.Sequel -> R.string.relation_type_sequel
    RelationType.Prequel -> R.string.relation_type_prequel
    RelationType.Summary -> R.string.relation_type_summary
    RelationType.Character -> R.string.relation_type_character
    RelationType.Other -> R.string.relation_type_other
}