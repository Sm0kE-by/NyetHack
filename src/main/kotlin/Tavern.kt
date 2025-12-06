package org.example

import java.io.File

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-items.txt")
    .readText()
    .split("\n")

private val menuItems = List(menuData.size) { index ->
    val (_, name, _) = menuData[index].split(",")
    name
}

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    //joinToString выполняет конкатенацию всех элементов в коллекции и разделяет их запятыми
    narrate(menuItems.joinToString())

    val patrons: MutableSet<String> = mutableSetOf()
    while (patrons.size < 10) {
        patrons += "${firstNames.random()} ${lastNames.random()}"
    }

    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    repeat(3) {
        placeOrder(
            patrons.random(),
            menuItems.random()
        )
    }

    println()
    menuData.forEachIndexed { index, data ->
        println("$index : $data")
    }

    printMenu(menuData, menuItems.maxOf { it.length })
}

private fun placeOrder(patronName: String, menuItemName: String) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    narrate("$TAVERN_MASTER hands $patronName a $menuItemName")
}

private fun printMenu(menu: List<String>, maximumDrinkLength: Int) {



}