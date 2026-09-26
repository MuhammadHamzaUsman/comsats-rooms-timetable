package org.example.process

import org.example.model.section.SectionDay
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.example.model.section.SectionClass

fun getTimeTableForWeek(document: Document): Pair<Map<Int, List<String>>, List<SectionDay>> {
    val table = document
        .getElementsByTag("table")
        .first() ?: return Pair(emptyMap(), emptyList())

    val periods = getPeriodHeader(table) ?: emptyMap()

    val body = table
        .getElementsByTag("tbody")
        .firstOrNull() ?: return Pair(emptyMap(), emptyList())

    return periods to body
        .getElementsByTag("tr")
        .map { day ->
            processDay(day)
        }
}

private fun processDay(day: Element): SectionDay {
    val columns = day
        .getElementsByTag("td")
        .groupBy { it.hasClass("daycol") }

    val day = columns[true]?.get(0)?.text() ?: "Unknown"

    var index = 1
    val slots = columns[false] ?: emptyList()
    val freePeriods = mutableListOf<Int>()
    val classes = mutableListOf<SectionClass>()

    for (element in slots) {
        if (element.hasAttr("colspan") && element.hasAttr("rowspan")) {
            val numberOfSlots = element
                .attr("colspan")
                .ifEmpty { "1" }
                .toInt()

            val slots = (index..<(index + numberOfSlots)).toSet()
            index += numberOfSlots

            val spans = element
                .getElementsByTag("span").associate {
                    val key = when (val className = it.className()) {
                        "code" -> "courseCode"
                        "mid2" -> "teacher"
                        else -> "room"
                    }

                    key to it.text()
                }

            classes += SectionClass(
                courseCode = spans["courseCode"] ?: continue,
                slots = slots,
                teacher = spans["teacher"] ?: continue,
                room = spans["room"] ?: continue,
            )
        } else {
            freePeriods += index++
        }
    }

    return SectionDay(
        day = day,
        classes = classes,
        freePeriods = freePeriods
    )
}

private fun getPeriodHeader(table: Element) = table
    .getElementsByTag("thead")
    .firstOrNull()
    ?.let { header ->
        val rows = header.getElementsByTag("tr")

        val periodIndex = rows[0]
            .getElementsByTag("th")
            .filter { it.hasClass("pnum") }
            .map { it.text().toInt() }

        val periodTiming = rows[1]
            .getElementsByTag("th")
            .map { timeHeader ->
                timeHeader
                    .getElementsByTag("div")
                    .map {
                        it.text()
                    }
            }

        periodIndex
            .zip(periodTiming)
            .toMap()
    }

