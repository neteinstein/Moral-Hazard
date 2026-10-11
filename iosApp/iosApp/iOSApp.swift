import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Kotlin/Native exports `initKoin()` as `doInitKoin()` (Obj-C forbids methods starting with "init").
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
