package org.kotlin.track

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension
import org.commonmark.ext.ins.InsExtension
import java.io.IOException
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import kotlin.system.exitProcess

private val markdownExtensions = listOf(
    TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(),
    HeadingAnchorExtension.create(), InsExtension.create(), YamlFrontMatterExtension.create()
)

fun javafx.scene.Node.load(fxmlResource: String, view: Any = this) {
    val resource = javaClass.getResource(fxmlResource) ?: javaClass.classLoader.getResource(fxmlResource)
    val loader = FXMLLoader(resource)
    loader.setRoot(this)
    loader.setController(view)
    try {
        loader.load<Parent>()
    } catch (e: IOException) {
        e.printStackTrace()
        exitProcess(1)
    }
}

fun String.renderMarkdown(): String =
    Parser.builder().extensions(markdownExtensions).build().parse(this).let { document ->
        HtmlRenderer.builder().extensions(markdownExtensions).build().render(document)
    }

fun NodeList.toIterable() = object : Iterable<Node> {
    override fun iterator() = object : Iterator<Node> {
        private var i = 0
        override fun hasNext() = i < length
        override fun next() = item(i++)
    }
}