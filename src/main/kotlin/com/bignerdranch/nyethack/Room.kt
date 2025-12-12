package com.bignerdranch.nyethack

import org.example.com.bignerdranch.nyethack.narrate

open class Room(val name: String) {
    protected open val status = "Calm"
    open val lootBox: LootBox<Loot> = LootBox.random()

    open fun description() = "$name (Currently: $status)"
    open fun enterRoom() {
        narrate("There is nothing to do here")
    }
}

open class MonsterRoom(
    name: String,
    var monster: Monster? = Goblin()
) : Room(name) {
    override fun description() =
        super.description() + " (Creature: ${monster?.description ?: "None"})"

    override fun enterRoom() {
        if (monster == null) {
            super.enterRoom()
        } else {
            narrate("Danger is lurking in this room")
        }
    }
}

open class TownSquare : Room("The Town Square") {
    override val status = "Bustling"
    private var bellSound = "GWONG"
    val hatDropOffBox = DropOffBox<Hat>()
    val gemDropOffBox = DropOffBox<Gemstones>()

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

    fun <T> sellLoot(loot: T): Int where T : Loot, T : Sellable {
        return when (loot) {
            is Hat -> hatDropOffBox.sellLoot(loot)
            is Gemstones -> gemDropOffBox.sellLoot(loot)

            else -> 0
        }
    }
}