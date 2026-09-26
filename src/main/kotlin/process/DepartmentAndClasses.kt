package org.example.process

import org.jsoup.nodes.Document

fun getSections(document: Document): List<String> = document
    .getElementsByTag("script")
    .firstOrNull()
    ?.let { script ->
        script
            .toString()
            .lineSequence()
            .map { it.trim() }
            .firstOrNull { line ->
                line.startsWith("const all = [")
            }
            ?.let { line ->
                line
                    .removePrefix("const all = [")
                    .removeSuffix("];")
                    .replace("\"", "")
                    .split(",")
            }
    } ?: emptyList()

fun getDepartments(document: Document): List<String> =
    document
        .getElementsByTag("option")
        .filter { option ->
            option.hasAttr("value") && ! (
                    option.attr("value") == "" || option.hasAttr("selected")
                    )
        }.map { option ->
            option.`val`()
        }