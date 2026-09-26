package org.example.process

import org.example.model.section.SectionTimeTable

val specialCase: List<(SectionTimeTable) -> SectionTimeTable?> = listOf(
    /*FA23-BCS-A Case*/ { timeTable ->
        if(timeTable.section == "FA23-BCE-A"){
            val updatedDays = timeTable.days
                .map { sectionDay ->
                    if(sectionDay.day == "We") {
                        val updatedClasses = sectionDay.classes.map { sectionClass ->
                            if (sectionClass.slots.min() >= 6) {
                                sectionClass.copy(
                                    slots = sectionClass.slots.map { it + 1 }.toSet()
                                )
                            } else sectionClass
                        }

                        sectionDay.copy(
                            freePeriods = (sectionDay.freePeriods - 6) + 18,
                            classes = updatedClasses
                        )
                    } else sectionDay
                }

            timeTable.copy(
                days = updatedDays
            )
        } else null
    }
)

fun correctIfSpecialCase(sectionTimeTable: SectionTimeTable): SectionTimeTable{
    specialCase.forEach {
        it(sectionTimeTable)?.let { return it }
    }

    return sectionTimeTable
}