package ru.vladsaybulin.model

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image

interface Entry {
    val id: Long
    val type: EntryType
    val name: String
    val russianName: String?
    val poster: Image?
}