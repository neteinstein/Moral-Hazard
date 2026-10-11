import SwiftUI
import UIKit
import ComposeApp

/// Hosts the shared Compose UI.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        // Edge-to-edge: Compose draws behind the status bar/home indicator and applies insets itself.
        ComposeView()
            .ignoresSafeArea()
    }
}
