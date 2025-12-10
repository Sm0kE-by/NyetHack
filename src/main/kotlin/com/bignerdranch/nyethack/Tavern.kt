package org.example.com.bignerdranch.nyethack

import kotlin.random.Random
import kotlin.random.nextInt
import java.io.File

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuDataFoePrint = File("data/tavern-menu-items.txt").readText().split("\n")
private val menuData = File("data/tavern-menu-items.txt")
    .readText()
    .split("\n")
    .map { it.split(",") }

private val menuItems = menuData.map { (_, name, _) -> name }
private val menuItemPrices = menuData.associate { (_, name, price) -> name to price.toDouble() }
private val menuItemTypes = menuData.associate { (type, name, _) -> name to type }


fun visitTavern() {

    narrate("${player.name} enters $TAVERN_NAME")
    narrate("\nThere are several items for sale:")
    //joinToString выполняет конкатенацию всех элементов в коллекции и разделяет их запятыми
    narrate(menuItems.joinToString())

    //создаем случайных посетителей, при объединении списков через ЗИП - берется список длинной наименьшего входящего списка,
    // т.к. мы возвращаем коллекцию из 4 элементов а в СЕТ елементы не могут повторяться, все значения будут уникальными
    val patrons: MutableSet<String> =
        firstNames.shuffled().zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName" }.toMutableSet()

    //Вместо этого необходимо распаковать значения из коллекции в отдельные аргументы. Для этого мы воспользовались оператором распаковки (*). С оператором распаковки элементы коллекции
    //рассматриваются как отдельные параметры функции получающей переменное количество аргументов. Одно из ограничений оператора распаковки заключается в том, что он
    //работает только с Array, этим и объясняется необходимость также вызывать toTypedArray. И хотя применение оператора распаковки ограничивается очень узкой нишей, он весьма
    //удобен, когда возникает необходимость строить коллекции подобным способом.
    val patronGold = mutableMapOf(TAVERN_MASTER to 86.00, player.name to 4.50, *patrons.map {
        it to 6.00
    }.toTypedArray())

    narrate("\n${player.name} sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    //Выбираем случайное блюдо дня
    val itemOfDay = patrons.flatMap { getFavoriteMenuItems(it) }.random()

    narrate("The item of the day is the \"$itemOfDay\"\n")
    println(patronGold)

    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }

    displayPatronBalances(patronGold)

    //Выгоняем посетителей у которых баланс составляет меньше 4 монет
    patrons.filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0 }
        .also { departingPatrons ->
            patrons -= departingPatrons.toSet()
            patronGold -= departingPatrons.toSet()
        }
        .forEach { patron ->
            narrate("\n${player.name} sees $patron departing the tavern")
        }
    narrate("There are still some patrons in the tavern")
    narrate(patrons.joinToString())

    printMenu(menuDataFoePrint, menuItems.maxOf { it.length })
}

/**
 * Возвращаем список случайных любимых блюд для каждого посетителя
 */
private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
//Для Alex Ironfoot любимые блюда - все десерты
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }
//для остальных - перемешиваем список блюд и берем случайное количество 1 или 2 первых блюда
        else ->
            menuItems.shuffled().take(Random.nextInt(1..2))
    }
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
    narrate("${player.name} intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}

