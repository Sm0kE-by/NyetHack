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
private val menuItemPrices: Map<String, Double> = List(menuData.size) { index ->
    val (_, name, price) = menuData[index].split(",")
    name to price.toDouble()
}.toMap()
private val menuItemTypes: Map<String, String> = List(menuData.size) { index ->
    val (type, name, _) = menuData[index].split(",")
    name to type
}.toMap()

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("\nThere are several items for sale:")
    //joinToString выполняет конкатенацию всех элементов в коллекции и разделяет их запятыми
    narrate(menuItems.joinToString())

    val patrons: MutableSet<String> = mutableSetOf()
    val patronGold = mutableMapOf(TAVERN_MASTER to 86.00, heroName to 4.50)

    while (patrons.size < 5) {
        val patronName = "${firstNames.random()} ${lastNames.random()}"
        patrons += patronName
        patronGold += patronName to 6.0
    }


    narrate("\n$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    println(patronGold)

    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }

    displayPatronBalances(patronGold)
    printMenu(menuData, menuItems.maxOf { it.length })
}

private fun placeOrder(patronName: String, menuItemName: String, patronGold: MutableMap<String, Double>) {
    val itemPrice = menuItemPrices.getValue(menuItemName)

    narrate("$patronName speaks with $TAVERN_MASTER to place an order")

    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {

        val action = when (menuItemTypes[menuItemName]) {
            "shandy", "elixir" -> "pours"
            "meal" -> "serves"
            else -> "hands"
        }
        narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")

        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
    }
}

private fun printMenu(menu: List<String>, maximumDrinkLength: Int) {
    val title = "\n*** Welcome to Taernyl's Folly ***"
    val dot = "."
    val space = " "
    val lineSize = if (title.length > (maximumDrinkLength + 5 + 5)) title.length else (maximumDrinkLength + 5 + 5)
    val menuCategories = List(menu.size) { index ->
        val (categories, _, _) = menu[index].split(",")
        categories
    }.toSet().toList()
    println(title)
    menuCategories.forEach { category ->
        val spaceSize = (lineSize - category.length - 4) / 2
        println("${space.repeat(spaceSize)}~[$category]~")
        menu.forEach {
            if (it.startsWith(category)) {
                val (_, menuItem, _) = it.split(",")
                menuItem
                val (_, _, price) = it.split(",")
                price
                println("$menuItem${dot.repeat(lineSize - menuItem.length - price.length)}$price")
            }
        }
    }
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    println()
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}
