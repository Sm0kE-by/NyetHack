package org.example.com.bignerdranch.nyethack

import com.bignerdranch.nyethack.Fightable
import com.bignerdranch.nyethack.Loot

class Player(
    initialName: String,
    val hometown: String = "Neversummer",
    override var healthPoints: Int,
    val isImmortal: Boolean
) : Fightable {

   override var name = initialName
        get() = field.replaceFirstChar { it.uppercase() }
        private set(value) {
            field = value.trim()
        }
    override val diceCount: Int = 3
    override val diceSides: Int=4

    val title: String
        get() = when {
            name.all { it.isDigit() } -> "The Identifiable"
            name.none { it.isLetter() } -> "The Witness Protection Member"
            name.count { it.lowercase() in "aeiou" } > 4 -> "The Master of Vowels"
            else -> "The Renowned Hero"
        }
    val prophecy by lazy {
        narrate("$name embarks on an arduous quest to locate a fortune teller")
        Thread.sleep(3000)
        narrate("The fortune teller bestows a prophecy upon $name")
        "An intrepid hero from $hometown shall some day " + listOf(
            "form an unlikely bond between two warring factions",
            "take possession of an otherworldly blade",
            "bring the gift of creation back to the world",
            "best the world-eater"
        ).random()
    }

    val inventory = mutableListOf<Loot>()
    var gold = 0

    init {
        require(healthPoints > 0) { "healthPoints must be greater than zero" }
        require(name.isNotBlank()) { "Player must have a name" }
    }

    constructor(name: String) : this(
        initialName = name,
        healthPoints = 100,
        isImmortal = false
    ) {
        if (name.equals("Jason", ignoreCase = true)) {
            healthPoints = 500
        }
    }

    override fun takeDamage(damage: Int) {
        if (!isImmortal) {
            healthPoints -= damage
        }
    }

    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence (x$numFireballs)")
    }

    fun changeName(newName: String) {
        narrate("$name legally changes their name to $newName")
        name = newName
    }
    fun prophesize() {
        narrate("$name thinks about their future")
        narrate("A fortune teller told Madrigal, \"$prophecy\"")
    }
}