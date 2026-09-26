package org.example.pdfgeneration

import org.example.model.room.RoomDay
import org.example.model.room.RoomTimeTable
import org.openpdf.text.Document
import org.openpdf.text.Element
import org.openpdf.text.PageSize
import org.openpdf.text.Paragraph
import org.openpdf.text.pdf.PdfPCell
import org.openpdf.text.pdf.PdfPTable
import org.openpdf.text.pdf.PdfWriter
import java.io.FileOutputStream

fun generatePdf(roomTimeTables: List<RoomTimeTable>) {
    val document = Document(
        PageSize.A4.rotate().apply {
            backgroundColor = BackGround
        },
        PagePadding, PagePadding,
        PagePadding, PagePadding
    )

    val writer = PdfWriter.getInstance(
        document,
        FileOutputStream("room_timetable.pdf")
    )

    document.open()

    roomTimeTables.forEach { roomTimeTable ->
        document.addRoomName(roomTimeTable.room)
        document.addTimeTableOfRoom(roomTimeTable)
        document.newPage()
    }

    document.close()
}

fun Document.addTimeTableOfRoom(timeTable: RoomTimeTable){
    val table = PdfPTable(Columns)
    table.totalWidth = pageSize.width - PagePadding * 2
    table.isLockedWidth = true

    table.addPeriodsIndexAndTiming(
        timeTable.periodsTiming
    )

    timeTable.days
        .map {
            getDayIndex(it)
        }
        .sortedBy { (dayIndex, dayCells) -> dayIndex }
        .onEach { (dayIndex, dayCells) ->
            for (cell in dayCells) {
                table.addCell(cell)
            }
        }

    add(table)
}

fun getDayIndex(day: RoomDay): Pair<Int, List<PdfPCell>> {
    val dayCell = PdfPCell(Paragraph(day.day, DayFont)).apply {
        borderColor = Outline
        verticalAlignment = Element.ALIGN_MIDDLE
        horizontalAlignment = Element.ALIGN_CENTER
    }

    val freePeriods = day.freePeriods.map { slot ->
        slot to PdfPCell().apply {
            borderColor = Outline
        }
    }

    val classes = day.classes.map { roomClass ->
        val starting = roomClass.slots.min()

        val cell = PdfPCell().apply {
            backgroundColor = Colors.random()
            borderColor = Outline
            colspan = roomClass.slots.size
            fixedHeight = CellHeight
            setPadding(0f)

            val innerTable = PdfPTable(1).apply {
                paddingLeft = 2f
                horizontalAlignment = Element.ALIGN_LEFT
                widthPercentage = 100f
            }

            val sectionCell = PdfPCell(
                Paragraph(roomClass.sections.joinToString(), CellHeaderFont)
            ).apply {
                verticalAlignment = Element.ALIGN_TOP
                fixedHeight = CellHeight * 0.3f
                borderWidth = 0f
            }

            val departmentAndCourseCell= PdfPCell(
                Paragraph(roomClass.courseCodes.joinToString(), CellBodyFont)
            ).apply {
                verticalAlignment = Element.ALIGN_MIDDLE
                fixedHeight = CellHeight * 0.4f
                borderWidth = 0f
            }

            val teacherCell = PdfPCell(
                Paragraph(roomClass.teacher, CellFooterFont)
            ).apply {
                fixedHeight = CellHeight * 0.3f
                verticalAlignment = Element.ALIGN_BOTTOM
                paddingBottom = 5f
                borderWidth = 0f
            }

            innerTable.apply {
                addCell(sectionCell)
                addCell(departmentAndCourseCell)
                addCell(teacherCell)
            }

            addElement(innerTable)
        }

        starting to cell
    }

    return day.day.getDayIndex() to (listOf(Pair(0, dayCell)) + freePeriods + classes)
        .sortedBy { (index, cell) -> index }
        .map { (index, cell) -> cell }
}

fun PdfPTable.addPeriodsIndexAndTiming(
    periodsTiming: Map<Int, Pair<String, String>>
){
    val table = PdfPTable(Columns)

    val startingCell = PdfPCell().apply {
        borderColor = Outline
        fixedHeight = CellHeight
        rowspan = 2
    }

    table.addCell(startingCell)

    for (index in periodsTiming.keys) {
        val indexText = Paragraph(index.toString(), HeaderFont)

        val indexCell = PdfPCell(indexText).apply {
            borderColor = Outline
            fixedHeight = CellHeight / 3
            verticalAlignment = Element.ALIGN_MIDDLE
            horizontalAlignment = Element.ALIGN_CENTER
        }

        table.addCell(indexCell)
    }

    for ((starting, ending) in periodsTiming.values) {

        val timingCell = PdfPCell().apply {
            borderColor = Outline
            fixedHeight = CellHeight / 3 * 2
        }

        val timingText = Paragraph("$starting\n$ending", HeaderFont)
        timingText.alignment = Element.ALIGN_CENTER

        timingCell.addElement(timingText)
        table.addCell(timingCell)
    }

    val tableCell = PdfPCell(table)
    tableCell.colspan = Columns
    addCell(tableCell)
}

fun Document.addRoomName(roomName: String) {
    val room = Paragraph(roomName, RoomFont)
    room.alignment = Element.ALIGN_CENTER
    room.spacingAfter = 10f
    add(room)
}