import java.awt.Color
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.Image

fun main() = SwingUtilities.invokeLater {
    val frame = JFrame("KMPUI")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    val kmpUIViewDSL = KMPUIViewDSL()
    frame.contentPane.add(kmpUIViewDSL)

    frame.pack()
    frame.isVisible = true
}

class KMPUIViewDSL : JPanel() {
    init {
        val tree = HelloScreenDSL().dumpTree()
        traverseTree(this, tree)
    }

    private fun traverseTree(parent: JPanel, node: Node) {
        when (node.nodeName) {
            "Column" -> {
                val columnPanel = JPanel()
                columnPanel.layout = BoxLayout(columnPanel, BoxLayout.Y_AXIS)
                parent.add(columnPanel)
                for (childNode in node.child) {
                    traverseTree(columnPanel, childNode)
                }
            }

            "Row" -> {
                val rowPanel = JPanel()
                rowPanel.layout = BoxLayout(rowPanel, BoxLayout.X_AXIS)
                rowPanel.background = Color(0x5500ff00)
                parent.add(rowPanel)
                for (childNode in node.child) {
                    traverseTree(rowPanel, childNode)
                }
            }

            "Text" -> {
                val textView = JLabel()
                textView.text = node.attributes["text"]
                val textSize =
                    if (node.attributes.containsKey("textSize")) node.attributes["textSize"]!!
                        .toFloat() else 16f
                textView.font = textView.font.deriveFont(textSize)
                parent.add(textView)
            }

            "Image" -> {
                val imageUrl = node.attributes["src"]
                if (imageUrl != null) {
                    try {
                        val url = URL(imageUrl)
                        val image = ImageIO.read(url)
                        val icon = ImageIcon(image)
                        val imageView = JLabel(icon)

                        val width = node.attributes["width"]?.toIntOrNull()
                        val height = node.attributes["height"]?.toIntOrNull()

                        if (width != null && height != null) {
                            if (width == -1 && height == -1) {
                                imageView.icon = ImageIcon(image) // match_parent
                            } else if (width == -2 && height == -2) {
                                imageView.icon = ImageIcon(image) // wrap_content
                            } else {
                                val scaledImage = image.getScaledInstance(
                                    if (width == -1 || width == -2) image.width else width,
                                    if (height == -1 || height == -2) image.height else height,
                                    Image.SCALE_SMOOTH
                                )
                                imageView.icon = ImageIcon(scaledImage)
                            }
                        }

                        parent.add(imageView)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}