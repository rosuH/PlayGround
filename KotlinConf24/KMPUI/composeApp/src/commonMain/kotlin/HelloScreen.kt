class HelloScreen : Component() {

    override fun dumpTree(): Node {
        return Node(
            "Text",
            mapOf("text" to "hello", "textSize" to "20"),
            0,
            0,
            100,
            100
        )
    }
}

class HelloScreen2 : Component() {
    override fun dumpTree(): Node {
        val child = listOf(
            Node("Text", mapOf("text" to "hello", "textSize" to "20")),
            Node("Text", mapOf("text" to "world")),
        )
        return Node("Column", emptyMap(), child = child)
    }
}

class HelloScreen3 : Component() {
    override fun dumpTree(): Node {
        val child = listOf(
            Node("Text", mapOf("text" to "hello", "textSize" to "20")),
            Node("Text", mapOf("text" to "world")),
        )
        return Node("Row", emptyMap(), child = child)
    }
}

class HelloScreenDSL : Component() {
    override fun dumpTree(): Node {
        val nodeTree = Column {
            Text {
                text = "hello"
                textSize = "20"
            }
            Text {
                text = "world"
            }
            Image {
                src = "https://kotlinlang.org/_next/static/chunks/images/hero-cover@2x-0095f955d809bb1d716b7ad41889166b.png"
            }
        }.dumpTree()
        return nodeTree
    }
}