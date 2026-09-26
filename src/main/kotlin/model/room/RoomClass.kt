package org.example.model.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomClass(
    val sections: MutableList<String>,
    val departments: MutableList<String>,
    val courseCodes: MutableList<String>,
    val teacher: String,
    val slots: Set<Int>
)

