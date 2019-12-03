package org.kotlin.track

import com.jfoenix.controls.*
import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.value.ObservableStringValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.CheckBoxTreeItem
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.VBox
import javafx.scene.control.cell.CheckBoxTreeCell
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import kotlin.properties.ObservableProperty


// https://stackoverflow.com/questions/28844503/why-im-getting-javafx-fxml-loadexception-even-the-path-of-the-fxml-file-is-corr

class TasksController: VBox() {
    @FXML
    lateinit var taskItem: JFXListView<String>
    @FXML
    lateinit var settingsIcon: JFXButton
    lateinit var app: Main

    fun init(app: Main, activityList: ObservableList<ActivityItems>, activityName: String) {
        this.app = app

        taskItem.setCellFactory { _ ->
            object : JFXListCell<String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)

                    if (empty || item == null) {
                        text = null
                    } else {
                        text = ""
                        graphic = HBox().apply {
                            spacing = 10.0
                            prefHeight = 100.0
                            val checkBox = JFXCheckBox()
                            val vboxContainer = Label().apply {
                                text = item
                            }
                            children.addAll(checkBox, vboxContainer)
                        }
                    }
                }
            }
        }

        Platform.runLater {
            val task = app.jsonString?.activities?.first { it.activityName == activityName }?.tasks
            if (task != null) {
                taskItem.items.setAll(task)
            }
        }
        settingsIcon.setOnAction {
            TaskSettingsDialog(app) { name ->
                taskItem.items.add(name)
            }

            taskItem.items.addListener { out: ListChangeListener.Change<out String> ->
                while (out.next()) {
                    if (out.wasAdded() || out.wasUpdated()) {
                        println("Came 41 in TC")
                        val activityListItems = mutableListOf<ActivityItems>()
                        val activities = activityList.forEach {
                            if (it.activityName == activityName) {
                                activityListItems.add(ActivityItems(
                                    it.health, it.activityName, it.activityDescription,
                                    tasks = taskItem.items
                                ))
                                println("Having activity")

                            } else {
                                activityListItems.add(it)
                            }
                        }
                        activityListItems.forEach {
                            println(it)
                        }
                        val writeJsonString = app.jsonManager.readJson()?.copy(activities = activityListItems)
                        println(writeJsonString?.toString())
                        app.jsonManager.writeJson(writeJsonString)
                    }
                }
            }
        }
    }
}