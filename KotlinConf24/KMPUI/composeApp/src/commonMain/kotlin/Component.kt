/**
 * 布局组件：最小单元
 */
abstract class Component {
    /**
     * 生成平台无关的组件树，类似于 DOM
     */
    abstract fun dumpTree(): Node
}

/**
 * 容器组件：包含多个子组件
 */
abstract class Container : Component() {
    abstract fun addView(component: Component)
}


/**
 * 组件节点信息
 */
data class Node(
    val nodeName: String,
    val attributes: Map<String, String>,
    val left: Int = 0, // margin left
    val top: Int = 0, // margin top
    val right: Int = 0, // margin right
    val bottom: Int = 0, // margin bottom
    val width: Int = -2, // wrap_content
    val height: Int = -2, // wrap_content
    val child: List<Node> = emptyList()
) {
    fun prettyPrint(indent: Int = 0): String {
        val indentStr = " ".repeat(indent)
        val childStr = child.joinToString(",\n") { it.prettyPrint(indent + 2) }
        return """
            |$indentStr$nodeName(${attributes.entries.joinToString(", ") { "${it.key}=${it.value}" }}) {
            |$childStr
            |$indentStr}
        """.trimMargin()
    }

}


/**
 * 文本组件
 */
class Text(
    private val parent: Container?,
    private val init: TextAttributes.() -> Unit
) : Component() {
    class TextAttributes {
        var text: String = ""
        var textSize: String = "16"
        var width = -2
        var height = -2
    }

    override fun dumpTree(): Node {
        val attributes = TextAttributes().apply(init)
        return Node(
            "Text",
            mapOf("text" to attributes.text, "textSize" to attributes.textSize),
            width = attributes.width,
            height = attributes.height
        )
    }
}

/**
 * 图片组件
 */
class Image(
    private val parent: Container,
    private val init: ImageAttributes.() -> Unit
) : Component() {
    class ImageAttributes {
        var src: String = ""
        var width: Int = -2
        var height: Int = -2
    }

    override fun dumpTree(): Node {
        val attributes = ImageAttributes().apply(init)
        return Node(
            "Image",
            mapOf("src" to attributes.src),
            width = attributes.width,
            height = attributes.height
        )
    }
}

/**
 * 垂直布局组件
 */
class Column(
    val parent: Container?,
    init: Column.() -> Unit
) : Container() {
    private val children = mutableListOf<Component>()

    init {
        init()
    }

    override fun addView(component: Component) {
        children.add(component)
    }

    override fun dumpTree(): Node {
        val child = children.map { it.dumpTree() }
        return Node("Column", emptyMap(), child = child)
    }
}

class Row (
    val parent: Container?,
    init: Row.() -> Unit
) : Container() {
    private val children = mutableListOf<Component>()

    init {
        init()
    }

    override fun addView(component: Component) {
        children.add(component)
    }

    override fun dumpTree(): Node {
        val child = children.map { it.dumpTree() }
        return Node("Row", emptyMap(), child = child)
    }
}


fun Container.Text(init: Text.TextAttributes.() -> Unit) {
    addView(Text(this, init))
}

fun Container.Image(init: Image.ImageAttributes.() -> Unit) {
    addView(Image(this, init))
}

fun Column(init: Column.() -> Unit): Column {
    return Column(null, init)
}

fun Row(init: Row.() -> Unit): Row {
    return Row(null, init)
}

fun Container.Column(init: Column.() -> Unit) {
    addView(Column(this, init))
}

fun Container.Row(init: Row.() -> Unit) {
    addView(Row(this, init))
}