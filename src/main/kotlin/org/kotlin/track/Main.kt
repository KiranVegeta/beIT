package org.kotlin.track

import javafx.application.Application
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.lang.Exception
import java.net.URI

class Main: Application(), CoroutineScope {
    lateinit var stage: Stage
    override val coroutineContext = Dispatchers.Main + Job()
    var jsonManager : JsonManager = JsonManager()

    val jsonString = jsonManager.readJson()
    override fun start(primaryStage: Stage?) {
        stage = MainStage(this@Main)
        stage.show()
    }

    fun showDocument(uri: URI) {
        launch(Dispatchers.IO){
            try {
                Desktop.getDesktop().browse(uri)
            } catch (ex: Exception) {
                println("Could not show $uri")
            }
        }
    }
}