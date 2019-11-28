package org.kotlin.track

import com.jfoenix.controls.JFXAlert
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDialogLayout
import com.jfoenix.controls.JFXTextField
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.stage.Modality

class TaskSettingsDialog(
    app: Main,
    onDone: (String, List<String>) -> Unit
): JFXDialogLayout(), AutoCloseable {
    @FXML
    lateinit var headerLabel: Label
    @FXML
    lateinit var addItemsField: JFXTextField
    @FXML
    lateinit var addSubItemsField: JFXTextField
    @FXML
    lateinit var expectedCalculation: Hyperlink
    @FXML
    lateinit var actualCalculation: Hyperlink
    @FXML
    lateinit var specialWidget: Hyperlink
    @FXML
    lateinit var okButton: JFXButton
    @FXML
    lateinit var cancelButton: JFXButton
    private val dialog = JFXAlert<Unit>(app.stage)

    init {
        load("/task_settings_dialog.fxml")
        headerLabel.text = "Task Settings"
        okButton.disableProperty().bind(addItemsField.textProperty().isEmpty)

        dialog.isOverlayClose = false
        dialog.initModality(Modality.WINDOW_MODAL)
        dialog.setContent(this)
        toFront()
        dialog.show()

        expectedCalculation.setOnAction {
            MarkDownDialog(app, "Expected Calculation")
        }

        okButton.setOnAction {
            onDone(addItemsField.text, addSubItemsField.text.split(";").map { it })
            dialog.close()
        }

        cancelButton.setOnAction {
            dialog.close()
        }
    }
    override fun close() {
        dialog.close()
    }
}