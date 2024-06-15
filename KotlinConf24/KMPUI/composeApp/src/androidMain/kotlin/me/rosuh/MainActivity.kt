package me.rosuh

import App
import HelloScreen
import HelloScreen2
import HelloScreen3
import HelloScreenDSL
import Node
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil.load

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = KMPUIViewDSL(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(rootView)
    }
}

/**
 * 第一个示例，只有一个文本组件
 */
class KMPUIView(context: Context) : FrameLayout(context) {
    init {
        // 获取 common 模块中的 HelloScreen 实例的控件树
        val node = HelloScreen().dumpTree()
        // 遍历控件树，生成 Android View
        when (node.nodeName) {
            "Text" -> {
                val textView = TextView(context)
                textView.text = node.attributes["text"]
                textView.textSize = node.attributes["textSize"]?.toFloat() ?: 16f
                addView(textView)
            }
        }
    }
}

class KMPUIView2 : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        val tree = HelloScreen2().dumpTree()
        traverseTree(this, tree)
    }
}

class KMPUIViewDSL : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        val tree = HelloScreenDSL().dumpTree()
        traverseTree(this, tree)
    }
}

fun View.traverseTree(parent: ViewGroup, node: Node) {
    when (node.nodeName) {
        "Column" -> {
            val linearLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(0x55ff0000)
            }
            val lp = ViewGroup.LayoutParams(
                node.width,
                node.height
            )
            parent.addView(linearLayout, lp)
            node.child.forEach {
                traverseTree(linearLayout, it)
            }
        }
        "Row" -> {
            val linearLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(0x5500ff00)
            }
            val lp = ViewGroup.LayoutParams(
                node.width,
                node.height
            )
            parent.addView(linearLayout, lp)
            node.child.forEach {
                traverseTree(linearLayout, it)
            }
        }

        "Text" -> {
            val textView = TextView(context).apply {
                text = node.attributes["text"]
                textSize = node.attributes["textSize"]?.toFloat() ?: 16f
            }
            val lp = ViewGroup.LayoutParams(
                node.width,
                node.height
            )
            parent.addView(textView, lp)
        }
        "Image" -> {
            val imageView = ImageView(context).apply {
                load(node.attributes["src"])
            }
            val lp = ViewGroup.LayoutParams(
                node.width,
                node.height
            )
            parent.addView(imageView, lp)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}