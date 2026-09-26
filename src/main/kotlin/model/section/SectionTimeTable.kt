package org.example.model.section

import kotlinx.serialization.Serializable

@Serializable
data class SectionTimeTable (
    val section: String,
    val department: String,
    val days: List<SectionDay>,
    val periodsTiming: Map<Int, List<String>> = emptyMap()
)
