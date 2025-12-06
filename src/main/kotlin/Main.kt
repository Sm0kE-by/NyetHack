package org.example

fun main() {

    narrate("A hero enters the town of Kronstadt. What is their name?",
        //ссылка на ф-ию
        ::makeYellow)

    val heroName = readLine()
    require(heroName != null && heroName.isNotEmpty()) {
        "The hero must have a name."
    }

    changeNarratorMood()
    narrate("$heroName, ${createTitle(heroName)}, heads to the town square")
}

// Выводит сообщение желтым цветом
private fun makeYellow(message: String) =
    "\u001b[33;1m$message\u001b[0m"

private fun createTitle(name: String): String {
    return when {
        //имя состоит только из цифр
        name.all { it.isDigit() } -> "The Identifiable"
        //имя не содержит ни одной буквы
        name.none { it.isLetter() } -> "The Witness Protection Member"
        //имя содержит много гласных
        name.count { it.lowercase() in "aeiou" } > 4 -> "The Master of Vowel"
        else -> "The Renowned Hero"
    }
}