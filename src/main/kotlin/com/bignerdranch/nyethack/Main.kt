package org.example.com.bignerdranch.nyethack

import com.bignerdranch.nyethack.Room
import com.bignerdranch.nyethack.TownSquare

lateinit var player : Player

fun main() {
    narrate("Welcome to NyetHack")
    val playerName = promptHeroName()
    player = Player(playerName)
// changeNarratorMood()
    player.prophesize()

    var currentRoom: Room = Tavern()

    val mortality = if (player.isImmortal) "an immortal" else "a mortal"
    println()
    narrate("${player.name}, ${player.title} is in ${currentRoom.description()}")
    narrate("${player.name}, $mortality, has ${player.healthPoints} health points")

    currentRoom.enterRoom()


    println()
    player.castFireball()
    player.prophesize()

}

private fun promptHeroName(): String {
    narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
// Выводит message желтым цветом
        "\u001b[33;1m$message\u001b[0m"
    }
    /*val input = readLine()
    require(input != null && input.isNotEmpty()) {
    "The hero must have a name."
    }
    return input*/
    println("Madrigal")
    return "Madrigal"
}

// Выводит сообщение желтым цветом
private fun makeYellow(message: String) =
    "\u001b[33;1m$message\u001b[0m"

