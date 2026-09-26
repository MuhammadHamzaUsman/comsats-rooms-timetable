package org.example.model.section

import kotlinx.serialization.Serializable

@Serializable
data class SectionDay(
    val day: String,
    val classes: List<SectionClass>,
    val freePeriods: List<Int>
)
