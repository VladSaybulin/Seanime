package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R

fun personRoleStringResId(personRole: String): Int? =
    PersonRoleStringResIds[personRole]

@Composable
fun personRoleString(personRole: String): String =
    personRoleStringResId(personRole)
        ?.let { stringResource(id = it) }
        ?: personRole

private val PersonRoleStringResIds = mapOf(
    "Story & Art" to R.string.core_ui_person_role_story_and_art,
    "Art" to R.string.core_ui_person_role_art,
    "Story" to R.string.core_ui_person_role_story,
    "Producer" to R.string.core_ui_person_role_producer,
    "Assistant Producer" to R.string.core_ui_person_role_assistant_producer,
    "Director" to R.string.core_ui_person_role_director,
    "Episode Director" to R.string.core_ui_person_role_episode_director,
    "Storyboard" to R.string.core_ui_person_role_storyboard,
    "Assistant Director" to R.string.core_ui_person_role_assistant_director,
    "Key Animation" to R.string.core_ui_person_role_key_animation,
    "Animation Director" to R.string.core_ui_person_role_animation_director,
    "Setting Manager" to R.string.core_ui_person_role_setting_manager,
    "2nd Key Animation" to R.string.core_ui_person_role_2nd_key_animation,
    "Assistant Animation Director" to R.string.core_ui_person_role_assistant_animation_director,
    "Script" to R.string.core_ui_person_role_script,
    "Series Composition" to R.string.core_ui_person_role_series_composition,
    "ADR Director" to R.string.core_ui_person_role_adr_director,
    "Screenplay" to R.string.core_ui_person_role_screenplay,
    "Chief Animation Director" to R.string.core_ui_person_role_chief_animation_director,
    "Theme Song Performance" to R.string.core_ui_person_role_theme_song_performance,
    "Theme Song Arrangement" to R.string.core_ui_person_role_theme_song_arrangement,
    "Theme Song Lyrics" to R.string.core_ui_person_role_theme_song_lyrics,
    "Theme Song Composition" to R.string.core_ui_person_role_theme_song_composition,
    "Inserted Song Performance" to R.string.core_ui_person_role_inserted_song_performance,
    "Sound Effects" to R.string.core_ui_person_role_sound_effects,
    "Music" to R.string.core_ui_person_role_music,
    "Executive Producer" to R.string.core_ui_person_role_executive_producer,
    "Editing" to R.string.core_ui_person_role_editing,
    "Recording Engineer" to R.string.core_ui_person_role_recording_engineer,
    "Character Design" to R.string.core_ui_person_role_character_design,
    "Background Art" to R.string.core_ui_person_role_background_art,
    "Layout" to R.string.core_ui_person_role_layout,
    "Original Creator" to R.string.core_ui_person_role_original_creator,
    "Planning" to R.string.core_ui_person_role_planning
)