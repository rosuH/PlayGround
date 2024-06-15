import androidx.compose.ui.ExperimentalComposeUiApi
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLParagraphElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.setAttribute("tabindex", "0")

    document.body?.appendChild(canvas)

    traverseTree(document.body!!, HelloScreenDSL().dumpTree())
}

fun traverseTree(parent: HTMLElement, node: Node) {
    when (node.nodeName) {
        "Column" -> {
            val columnDiv = document.createElement("div") as HTMLDivElement
            columnDiv.style.display = "flex"
            columnDiv.style.flexDirection = "column"
            parent.appendChild(columnDiv)
            for (childNode in node.child) {
                traverseTree(columnDiv, childNode)
            }
        }

        "Row" -> {
            val rowDiv = document.createElement("div") as HTMLDivElement
            rowDiv.style.display = "flex"
            rowDiv.style.flexDirection = "row"
            parent.appendChild(rowDiv)
            for (childNode in node.child) {
                traverseTree(rowDiv, childNode)
            }
        }

        "Text" -> {
            val pElement = document.createElement("p") as HTMLParagraphElement
            pElement.textContent = node.attributes["text"]
            val textSize = node.attributes["textSize"]?.toFloatOrNull() ?: 16f
            pElement.style.fontSize = "${textSize}px"
            parent.appendChild(pElement)
        }

        "Image" -> {
            val imageUrl = node.attributes["src"]
            if (imageUrl != null) {
                val imageElement = document.createElement("img") as HTMLImageElement
                imageElement.src = imageUrl

                val width = node.attributes["width"]?.toIntOrNull()
                val height = node.attributes["height"]?.toIntOrNull()

                if (width != null) {
                    if (width == -1) {
                        imageElement.style.width = "100%" // match_parent
                    } else if (width != -2) {
                        imageElement.width = width // specific width
                    }
                }

                if (height != null) {
                    if (height == -1) {
                        imageElement.style.height = "100%" // match_parent
                    } else if (height != -2) {
                        imageElement.height = height // specific height
                    }
                }

                parent.appendChild(imageElement)
            }
        }
    }
}
