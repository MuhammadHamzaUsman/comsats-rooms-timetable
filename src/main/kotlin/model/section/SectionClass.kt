package org.example.model.section

import kotlinx.serialization.Serializable

@Serializable
data class SectionClass (
    val courseCode: String,
    val slots: Set<Int>,
    val teacher: String,
    val room: String
)
