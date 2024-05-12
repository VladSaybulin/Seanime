package ru.vladsaybulin.core.ui.annotatedtext

private fun contains(baseStart: Int, baseEnd: Int, targetStart: Int, targetEnd: Int) =
    (baseStart <= targetStart && targetEnd <= baseEnd) &&
            (baseEnd != targetEnd || (targetStart == targetEnd) == (baseStart == baseEnd))

internal fun intersect(lStart: Int, lEnd: Int, rStart: Int, rEnd: Int) =
    maxOf(lStart, rStart) < minOf(lEnd, rEnd) ||
            contains(lStart, lEnd, rStart, rEnd) || contains(rStart, rEnd, lStart, lEnd)