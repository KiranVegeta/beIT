package org.kotlin.track

import com.jfoenix.controls.JFXDecorator
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class MainStage(private val app: Main): Stage() {

    private val activityListController: ActivityListController
    init {
        title = "Activity Tracker"
        val loader = FXMLLoader(javaClass.getResource("/activity_list.fxml"))
        val root = loader.load<Parent>()
        val decoraterObject = object : CustomDecorater {
            override fun decorate() = JFXDecorator(this@MainStage, root, false,true, true)
        }
        val decorator = decoraterObject.decorate()
        decorator.isCustomMaximize = true
        scene = Scene(decorator, 850.0, 600.0)
        activityListController = loader.getController()
        activityListController.init(app)
    }
}