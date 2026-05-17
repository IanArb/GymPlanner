I would like to introduce gradle conventions plugin to the project. This plugin will help us maintain consistency across our Gradle build files and reduce boilerplate code.

The plugin will be applied to all modules in the project, and it will include common configurations such as:
- Setting the Java and Kotlin compatibility versions
- Configuring common dependencies
- Applying common plugins (e.g., `kotlin`, `application`, `library`)

To implement the Gradle conventions plugin, we will follow these steps:
1. Create a new Gradle plugin module in the project (e.g., `:build-logic`).
2. Define the plugin in the `build.gradle.kts` file of the new module,
3. Implement the plugin logic to apply the common configurations and dependencies.
4. Publish the plugin to the local Gradle repository.
5. Apply the plugin in the `build.gradle.kts` files of all modules in the
6. Verify that the plugin is working correctly by building the project and running tests.
7. Document the usage of the plugin in the project README or a dedicated documentation file.

Please ask any follow up questions you may have about the implementation details or specific configurations you would like to include in the plugin.