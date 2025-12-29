import SwiftUI
import SharedGymPlanner
import Swinject

@main
struct iOSApp: App {
    
    let container: Container = {
        let container = Container()
        let assembler = Assembler(
            [
                ViewModelAssembly(),
                RepositoryAssembly()
            ],
            container: container
        )
        return container
    }()
    
    init() {
        KoinKt.doInitKoinIOS(baseUrl: "https://ae15f80bb69d.ngrok-free.app", websocketBaseUrl: "wss://0fe5dce64a68.ngrok-free.app")
    }
    
	var body: some Scene {
        WindowGroup {
        LoginView(container: container)
      }
	}
}
