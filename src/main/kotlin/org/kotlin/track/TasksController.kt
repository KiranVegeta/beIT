package org.kotlin.track

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTreeView
import javafx.fxml.FXML
import javafx.scene.control.CheckBoxTreeItem
import javafx.scene.layout.VBox
import javafx.scene.control.cell.CheckBoxTreeCell


// https://stackoverflow.com/questions/28844503/why-im-getting-javafx-fxml-loadexception-even-the-path-of-the-fxml-file-is-corr

class TasksController: VBox() {
    @FXML lateinit var tasksTree: JFXTreeView<String>
    @FXML lateinit var settingsIcon: JFXButton
    lateinit var app: Main

    fun init(app: Main) {
        this.app = app
        settingsIcon.setOnAction {
            TaskSettingsDialog(app) { name, subNames ->
                val rootChild = CheckBoxTreeItem(name)
                subNames.forEach {
                    rootChild.children += CheckBoxTreeItem(it)
                }
                tasksTree.root.children += rootChild.also {
                    it.isExpanded = true
                }
            }
        }
        tasksTree.apply {
            root = CheckBoxTreeItem("Diet").also {
                it.isExpanded = true
            }
            setCellFactory(CheckBoxTreeCell.forTreeView())
        }

        val protein = CheckBoxTreeItem("Protein")
        protein.children.addAll(
            CheckBoxTreeItem("Egg"),
            CheckBoxTreeItem("Chicken Breast")
        )
        val carbohydrate = CheckBoxTreeItem("Carbohydrate")
        carbohydrate.children.addAll(
            CheckBoxTreeItem("Rice"),
            CheckBoxTreeItem("Apple")
        )

        tasksTree.root.children.addAll(protein, carbohydrate)
        tasksTree.root.children.forEach {
            it.isExpanded = true
        }

    }

}