import kotlin.test.Test

class ComponentTest {

    @Test
    fun dumpTree() {
        val layout = Column {
            Row {
                Text {
                    text = "hello"
                    textSize = "20"
                }
                Text {
                    text = "world"
                }
            }
            Image {
                src = "https://picsum.photos/200/300"
                width = 200
                height = 300
            }
        }
        println(layout.dumpTree().prettyPrint())
    }
}