package org.example

val BaseUrl = "https://sfs.cuilahore.edu.pk/schedule/Public/Timetable?Kind=class&Dept=&Who="

fun getDepartmentUrl(department: String) = "https://sfs.cuilahore.edu.pk/schedule/Public/Timetable?Kind=class&Dept=$department&Who="

fun getSectionUrl(
    section: String,
    department: String
) = "https://sfs.cuilahore.edu.pk/schedule/Public/Timetable?Kind=class&Dept=$department&Who=$section"