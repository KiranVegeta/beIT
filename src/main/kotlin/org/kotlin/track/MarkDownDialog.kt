package org.kotlin.track

import com.jfoenix.controls.JFXAlert
import com.jfoenix.controls.JFXDialogLayout
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import javafx.stage.Modality
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import java.net.URI

class MarkDownDialog(
    app: Main,
    title: String
    ): JFXDialogLayout() {
    @FXML private lateinit var headingLabel: Label
    @FXML private lateinit var textArea: TextArea
    @FXML private lateinit var markdownView: MarkdownView
    @FXML private lateinit var markdownHelpLink: Hyperlink

    private val dialog = JFXAlert<Unit>(app.stage)

    init {
        load("/markdown_dialog.fxml")
        dialog.isOverlayClose = false
        dialog.initModality(Modality.WINDOW_MODAL)
        dialog.setContent(this)
        toFront()
        dialog.show()

        headingLabel.text = title
        markdownView.init(app)
        markdownView.markdownProperty.bind(textArea.textProperty())

        textArea.text = "## Heading"
        textArea.requestFocus()

        markdownView.prefHeight = textArea.prefHeight
        markdownView.prefWidth = textArea.prefWidth

        markdownHelpLink.setOnAction {
            app.showDocument(MARKDOWN_HELP_PAGE)
        }
    }

    @FXML
    private fun onOK() {
        dialog.close()
    }

    @FXML fun onCancel() {
        dialog.close()
    }
    companion object {
        private val MARKDOWN_HELP_PAGE = URI.create("https://commonmark.org/help/")
    }
}