import UIKit
import SwiftUI
import ComposeApp


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct NodeView: View {
    let node: Node

    var body: some View {
        switch node.nodeName {
        case "Text":
            Text(node.attributes["text"] ?? "")
                .font(.system(size: CGFloat((Float(node.attributes["textSize"] ?? "16") ?? 16.0))))
        case "Column":
            VStack {
                ForEach(node.child, id: \.nodeName) { childNode in
                    NodeView(node: childNode)
                }
            }
        case "Image" where node.attributes["src"] != nil:
            AsyncImage(url: URL(string: node.attributes["src"]!)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                    } placeholder: {
                        ProgressView() // 占位符
                    }
                    .frame(width: frameWidth, height: frameHeight)

        default:
            EmptyView()
        }
    }
    
    // 根据属性值计算宽度
        private var frameWidth: CGFloat? {
            if let width = node.attributes["width"], let widthValue = Float(width) {
                if widthValue == -1 { // match_parent
                    return nil
                } else if widthValue == -2 { // wrap_content
                    return nil // wrap_content 在 SwiftUI 中不需要特别设置宽度
                } else {
                    return CGFloat(widthValue)
                }
            }
            return nil
        }

        // 根据属性值计算高度
        private var frameHeight: CGFloat? {
            if let height = node.attributes["height"], let heightValue = Float(height) {
                if heightValue == -1 { // match_parent
                    return nil
                } else if heightValue == -2 { // wrap_content
                    return nil // wrap_content 在 SwiftUI 中不需要特别设置高度
                } else {
                    return CGFloat(heightValue)
                }
            }
            return nil
        }
}

struct ContentView: View {
    var body: some View {
        // Get the Node instance from HelloScreen2
        let node2 = HelloScreen2().dumpTree()
        let nodeDSL = HelloScreenDSL().dumpTree()

        // Use the Node instance to create a NodeView
        NodeView(node: nodeDSL)
            .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}

