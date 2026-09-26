package org.example.model.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomTimeTable(
    val room: String,
    val days: List<RoomDay>,
    val periodsTiming: Map<Int, Pair<String, String>>
)
