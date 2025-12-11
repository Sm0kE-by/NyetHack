package com.bignerdranch.nyethack

import org.example.com.bignerdranch.nyethack.narrate

open class Room (val name: String) {
    protected open val status = "Calm"
    fun description() = "$name (Currently: $status)"
    open fun enterRoom() {
        narrate("There is nothing to do here")
    }
}

open class TownSquare : Room("The Town Square"){
    override val status = "Bustling"
    private var bellSound = "GWONG"

    //Когда вы переопределяете функцию в Kotlin, переопределяющая функция в подклассе по умолчанию
    //открыта для переопределения (если сам подкласс помечен ключевым словом open).
    //Добавьте ключевое слово final, чтобы запретить возможность переопределения функции.
    final override fun enterRoom() {
        narrate("The villagers rally and cheer as the hero enters")
        ringBell()
    }
    fun ringBell() {
        narrate("The bell tower announces the hero's presence: $bellSound")
    }
}