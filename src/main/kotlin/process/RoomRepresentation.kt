package org.example.process

import org.example.model.room.RoomClass
import org.example.model.room.RoomDay
import org.example.model.room.RoomTimeTable
import org.example.model.section.SectionTimeTable
import org.example.pdfgeneration.getDayIndex

fun convertToRoomRepresentation(sectionTimeTables: List<SectionTimeTable>): List<RoomTimeTable> {
    val roomDayClassMap: MutableMap<String, MutableMap<String, RoomDay>> = mutableMapOf()
    var periodTimingList: Map<Int, List<String>> = emptyMap()

    sectionTimeTables.forEach { sectionTimeTable ->
        val sectionTimeTable = correctIfSpecialCase(sectionTimeTable)
        periodTimingList = sectionTimeTable.periodsTiming

        sectionTimeTable.days.forEach { (day, classes, _) ->
            classes.forEach { clazz ->
                val daySlotMap = roomDayClassMap.computeIfAbsent(clazz.room) { mutableMapOf() }
                val roomDay = daySlotMap.computeIfAbsent(day) { day ->
                    RoomDay(
                        day = day,
                        classes = mutableListOf(),
                        freePeriods = (1..24).toMutableSet()
                    )
                }

                if(clazz.slots.min() !in roomDay.freePeriods){
                    roomDay.classes.firstOrNull { roomClass ->
                        clazz.slots.min() in roomClass.slots
                    }?.let { roomClass ->
                        roomClass.sections += sectionTimeTable.section
                        roomClass.departments += sectionTimeTable.department
                        roomClass.courseCodes += clazz.courseCode
                    }
                }
                else {
                    roomDay.freePeriods -= clazz.slots

                    roomDay.classes += RoomClass(
                        sections = mutableListOf(sectionTimeTable.section),
                        departments = mutableListOf(sectionTimeTable.department),
                        courseCodes = mutableListOf(clazz.courseCode),
                        teacher = clazz.teacher,
                        slots = clazz.slots
                    )
                }
            }
        }
    }

    val periodTiming = periodTimingList.mapValues { (_, timings) ->
        timings[0] to timings[1]
    }

    val roomTimeTables = roomDayClassMap.map { (room, dayMap) ->
        RoomTimeTable(
            room = room,
            days = dayMap.values
                .sortedBy { it.day.getDayIndex() }
                .onEach {
                    it.classes.sortBy { it.slots.min() }
                }
                .toList(),
            periodsTiming = periodTiming
        )
    }

    return roomTimeTables.sortedBy {
        it.room
    }
}