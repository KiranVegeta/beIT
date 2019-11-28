package org.kotlin.track

import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.concurrent.Worker
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.layout.Region
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import netscape.javascript.JSObject
import org.w3c.dom.html.HTMLAnchorElement
import org.w3c.dom.events.EventTarget
import java.net.URI

class MarkdownView: Region() {
    private val webView =   WebView()
    private val engine = webView.engine

    private var app: Main? = null

    val markdownProperty: ObjectProperty<String> = SimpleObjectProperty("")
    var markdown: String
    get() = markdownProperty.value
    set(value) {markdownProperty.set(value)}

    val backgroundColorProperty = SimpleStringProperty("#f7f7f7")

    val dimensionsAssignedProperty = SimpleBooleanProperty(false)
    var dimensionsAssigned: Boolean
    get() = dimensionsAssignedProperty.value
    set(value) {dimensionsAssignedProperty.value = value}

    init {
        children += webView
        engine.apply {
            isJavaScriptEnabled = true
            load(this@MarkdownView.javaClass.getResource("/markdown.html").toExternalForm())

            loadWorker.stateProperty().addListener { _, _, newState ->
                if (newState === Worker.State.SUCCEEDED) {
                    (executeScript("window") as JSObject).setMember("view", this@MarkdownView)
                    insertMarkdown()
                }
            }
        }

        markdownProperty.addListener { _, _, _ ->
            dimensionsAssigned = false
            engine.insertMarkdown()
        }
    }

    fun init(app: Main) {
        this.app = app
        webView.prefHeight = 0.0
        webView.prefWidth = 0.0
    }

    private val linkActions = mutableMapOf<String, () -> Unit>()

    fun addLinkAction(link: String, action: () -> Unit) {
        linkActions[link] = action
    }

    private fun WebEngine.insertMarkdown() {
        if (app == null || document == null || loadWorker.state != Worker.State.SUCCEEDED) { return }

        executeScript("document.body.style.backgroundColor ='${backgroundColorProperty.value}'")
        executeScript("document.getElementById('content').innerHTML ='${markdown.renderMarkdown().escape()}'")

        document.apply {
            getElementsByTagName("a").toIterable().forEach {
                (it as? EventTarget)?.addLinkClickListener { url ->
                    linkActions[url.split("!/").last()]?.invoke()
                        ?: app!!.showDocument(URI.create(url))
                }
            }
        }
        adjustDimensions()
    }

    override fun layoutChildren() {
        layoutInArea(webView, 0.0, 0.0, width, height, 0.0, HPos.CENTER, VPos.CENTER)
    }

    private fun adjustDimensions() {
        Platform.runLater {
            val offsetHeight = engine.executeScript("document.getElementById('content').offsetHeight") as Int
            val offsetWidth = engine.executeScript("document.getElementById('content').offsetWidth") as Int
            webView.prefHeight = offsetHeight + WEBVIEW_HEIGHT_PADDING
            webView.prefWidth = offsetWidth + WEBVIEW_WIDTH_PADDING
            parent.layout()
            dimensionsAssigned = false
        }
    }

    companion object {
        const val WEBVIEW_HEIGHT_PADDING = 40.0
        const val WEBVIEW_WIDTH_PADDING = 6.0
        const val DEFAULT_BACKGROUND_COLOR = "#F7F7F7"
        /** Invoke the [clicked] callback when this node is clicked. */
        fun EventTarget.addLinkClickListener(clicked: (url: String) -> Unit) {
            addEventListener("click", { event ->
                (event.currentTarget as? HTMLAnchorElement)?.also { anchor ->
                    event.preventDefault()
                    clicked(anchor.href)
                }
            }, false)
        }

        /** Escape a string for inclusion in javascript. */
        fun String.escape() =
            replace("\n", " ").replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"")
    }
}