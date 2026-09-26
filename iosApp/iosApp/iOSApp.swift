import SharedGymPlanner
import SwiftUI
import Swinject

@main
struct iOSApp: App {
    let container: Container = {
        let container = Container()
        let assembler = Assembler(
            [
                ViewModelAssembly(),
                RepositoryAssembly(),
            ],
            container: container
        )
        return container
    }()

    init() {
        KoinKt.doInitKoinIOS(
            baseUrl: "https://3954-86-45-28-173.ngrok-free.app",
            websocketBaseUrl: "wss://0fe5dce64a68.ngrok-free.app"
        )
    }

    var body: some Scene {
        WindowGroup {
            LoginView(container: container)
        }
    }
}
