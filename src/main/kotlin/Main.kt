package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.example.model.section.SectionTimeTable
import org.example.pdfgeneration.generatePdf
import org.example.process.convertToRoomRepresentation
import org.example.process.getDepartments
import org.example.process.getSections
import org.example.process.getTimeTableForWeek
import org.jsoup.Jsoup
import java.io.FileWriter
import kotlin.time.Duration.Companion.milliseconds

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

suspend fun main() {
    val client = HttpClient(CIO)

    val departmentsResponse = client.get(BaseUrl)
    val departmentsResponseBodyDocument = Jsoup.parse(departmentsResponse.body<String>())
    val departments = getDepartments(departmentsResponseBodyDocument)

    println("Departments: ${departments.size}")

    val departmentAndSections = if(departmentsResponse.status == HttpStatusCode.OK) {
        coroutineScope {
            var index = 0

            departments.associateWith { department ->
                delay(0.5.milliseconds)

                val sectionsResponse = client.get(getDepartmentUrl(department))
                val sectionResponseDocument = Jsoup.parse(sectionsResponse.body<String>())
                println("${index++}: $department")

                getSections(sectionResponseDocument)
            }
        }
    } else emptyMap()

    val sectionRepresentation = departmentAndSections.flatMap { (department, sections) ->
        println("\n$department: ${sections.size}")

        sections.mapIndexed { index, section ->
            delay(0.5.milliseconds)

            val timeTableResponse = client.get(getSectionUrl(section, department))
            val timeTableResponseDocument = Jsoup.parse(timeTableResponse.body<String>())
            val (periodsTiming, days) = getTimeTableForWeek(timeTableResponseDocument)
            println("$index: $section")

            SectionTimeTable(
                section = section,
                department = department,
                days = days,
                periodsTiming = periodsTiming
            )
        }
    }

    client.close()

    coroutineScope {
        launch {
            FileWriter("section_timetable.json").use {
                it.write(json.encodeToString(sectionRepresentation))
            }
        }

        val roomRepresentation = convertToRoomRepresentation(sectionRepresentation)

        launch {
            FileWriter("room_timetable.json").use {
                it.write(json.encodeToString(roomRepresentation))
            }
        }

        launch {
            generatePdf(roomRepresentation)
        }
    }
}
