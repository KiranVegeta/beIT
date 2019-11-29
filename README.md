# beIT
A daily activity tracker application which help to increase the quality of life.

Notes: 
``` package org.practice

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.util.*


fun main() {
    println("Hello World")
    val menu: MutableList<RestaurantMenuItem> = ArrayList()
    menu.add(RestaurantMenuItem("Spaghetti", 7.99f))
    menu.add(RestaurantMenuItem("Steak", 12.99f))
    menu.add(RestaurantMenuItem("Papad", 5.99f))
    val restaurant = RestaurantWithMenu("Future Studio Steak House", menu)
    writeJson(restaurant)
    readJson()
}

fun writeJson(restaurant: RestaurantWithMenu) {

    val gson = Gson()
    val restaurantJson = gson.toJson(restaurant)
    //Initialize the File Writer and write into file
    val fileFolder = System.getenv("APPDATA") + "\\" + "Maintain"
    val directory = File(fileFolder)

    if (directory.exists()) {
        println("Found folder")
    }

    if (!directory.exists()) {
        directory.mkdir()
        println("Could not find folder so created it")
    }
    val file= File(fileFolder, "details.json")
    file.writeText(restaurantJson)
}

fun readJson() {
    val gson = Gson()
    val fileFolder = System.getenv("APPDATA") + "\\" + "Maintain"
    val bufferedReader: BufferedReader = File(fileFolder, "details.json").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val generalInfoObject: RestaurantWithMenu = gson.fromJson(inputString, RestaurantWithMenu::class.java)
    generalInfoObject.menu?.forEach {
        println(it)
    }
}

class RestaurantWithMenu (
    var name: String? = null,
    var menu: List<RestaurantMenuItem>? = null
)

data class RestaurantMenuItem (
    var description: String? = null,
    var price: Float = 0f
) 
```
