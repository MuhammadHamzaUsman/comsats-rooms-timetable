package org.example.model.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomDay(
    val day: String,
    val classes: MutableList<RoomClass>,
    val freePeriods: MutableSet<Int>,
)
