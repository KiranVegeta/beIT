package org.kotlin.track

import com.jfoenix.controls.JFXDecorator
import com.jfoenix.controls.JFXListCell
import com.jfoenix.controls.JFXListView
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.BorderPane
import javafx.scene.text.Font
import javafx.stage.StageStyle


class ActivityListController : VBox() {
    @FXML lateinit var overallHealth: VBox
    @FXML lateinit var activityHolder: VBox
    @FXML lateinit var activityItems: JFXListView<ActivityItems>
    @FXML lateinit var rootPane: VBox

    private val activityList = FXCollections.observableArrayList<ActivityItems>()
    lateinit var app: Main
    lateinit var tasksController: TasksController

    fun init(app: Main) {
        this.app = app
        app.jsonString?.activities?.forEach {
            activityList += it
        }
        activityItems.items = activityList
        activityList.addListener { out: ListChangeListener.Change<out ActivityItems> ->
            while (out.next()) {
                if (out.wasAdded() || out.wasUpdated()) {
                    val activityPageDetails = ActivityPageDetails(overallHealth = 1.0, activities = activityList.toList())
                    app.jsonManager.writeJson(activityPageDetails)
                }
            }
        }
        activityItems.setCellFactory { _ ->
            object : JFXListCell<ActivityItems>() {
                override fun updateItem(item: ActivityItems?, empty: Boolean) {
                    super.updateItem(item, empty)

                    if (empty || item == null || item.activityName == null) {
                        text = null
                    } else {
                        text = ""
                        graphic = HBox().apply {
                            spacing = 10.0
                            prefHeight = 100.0
                            val progressIndicator = ProgressIndicator(item.health).apply {
                                style = " -fx-progress-color: ${colorDictionary[item.health]};"
                            }
                            val vboxContainer = VBox().apply {
                                children.addAll(
                                    Hyperlink(item.activityName).apply {
                                        font = Font.font(20.0)
                                        setOnAction {
                                            val loader = FXMLLoader(TasksController::class.java.getResource("/tasks_activity.fxml"))
                                            val task = loader.load() as VBox
                                            tasksController = loader.getController()
                                            rootPane.children.setAll(task)
                                            tasksController.init(app, activityList, text)
                                        }
                                    },
                                    Label(item.activityDescription).apply {
                                        font = Font.font(15.0)
                                    }
                                )
                            }
                            children.addAll(progressIndicator, vboxContainer)
                        }
                    }
                }
            }
        }
    }

    @FXML fun createActivity(e: ActionEvent) {
        AddActivityDialog(app) {name , desc, health ->
            val activityItems = ActivityItems(health, name, desc,null)
            activityList.add(activityItems)
        }
    }

    companion object {
        val colorDictionary = mapOf(0.4 to "#ff0000", 0.5 to "#9B870C", 0.9 to "#00ff00")
    }
}
