package org.kotlin.track

import com.jfoenix.controls.JFXAlert
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDialogLayout
import com.jfoenix.controls.JFXTextField
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.stage.Modality

class AddActivityDialog(
    app: Main,
    onCreate: (String, String, Double) -> Unit
    ): JFXDialogLayout(), AutoCloseable {
    @FXML lateinit var headerLabel: Label
    @FXML lateinit var nameField: JFXTextField
    @FXML lateinit var descField: JFXTextField
    @FXML lateinit var okButton: JFXButton
    @FXML lateinit var cancelButton: JFXButton
    private val dialog = JFXAlert<Unit>(app.stage)
    init {
        load("/add_activity_dialog.fxml")
        headerLabel.text = "Add Activity"
        okButton.disableProperty().bind(nameField.textProperty().isEmpty)

        dialog.isOverlayClose = false
        dialog.initModality(Modality.WINDOW_MODAL)
        dialog.setContent(this)
        toFront()
        dialog.show()

        okButton.setOnAction {
            onCreate(nameField.text, descField.text, 0.0)
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