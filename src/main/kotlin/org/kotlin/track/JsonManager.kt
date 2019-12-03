package org.kotlin.track

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File


class JsonManager {

    private val gson = Gson()

    private val fileFolder = System.getProperty("os.name").toUpperCase().run {
        when {
            contains("WIN") -> System.getenv("APPDATA") + File.pathSeparator + "Maintain"
            else -> System.getProperty("user.home") + "/.maintain"
        }
    }

    private val directory = File(fileFolder)

    private val detailsJson = File(fileFolder, "details.json")
    fun readJson(): ActivityPageDetails? {
        if (!directory.exists()) {
            println("No Directory found")
            return null
        } else {
            println(fileFolder)
            val bufferedReader: BufferedReader = detailsJson.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            val activityPageDetailsObject: ActivityPageDetails = gson.fromJson(inputString, ActivityPageDetails::class.java)
            activityPageDetailsObject.activities.forEach {
                println(it.activityName)
            }

            return activityPageDetailsObject
        }
    }

    fun writeJson(activityPageDetails: ActivityPageDetails?) {
        val activityJson = gson.toJson(activityPageDetails)

        if (directory.exists()) {
            println("Found folder")
        }

        if (!directory.exists()) {
            directory.mkdir()
            println("Could not find folder so created it")
        }
        val file= File(fileFolder, "details.json")
        file.writeText(activityJson)
    }
}