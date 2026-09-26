package org.example.pdfgeneration

import org.openpdf.text.FontFactory
import java.awt.Color

val BackGround = Color(18, 18, 18)
val Outline = Color(61, 61, 61)
val Text = Color(210, 210, 210)

val Colors = listOf(
    Color(69, 91, 85),
    Color(43, 71, 82),
    Color(21, 39, 41),
    Color(30, 69, 62),
    Color(46, 71, 58),
    Color(48, 104, 68),
    Color(24, 44, 37),
    Color(0, 88, 89),
    Color(66, 104, 103),
    Color(5, 36, 46),
)

val RoomFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20f, Text)
val HeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f, Text)
val CellHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f, Text)
val CellBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9f, Text)
val CellFooterFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8f, Text)
val DayFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14f, Text)

val Columns = 25
val Rows = 8
val PagePadding = 20f
val CellHeight = 60f

fun String.getDayIndex() = when(this) {
    "Mo" -> 1
    "Tu" -> 2
    "We" -> 3
    "Th" -> 4
    "Fr" -> 5
    "Sa" -> 6
    "Su" -> 7
    else -> error("Unknown Day")
}