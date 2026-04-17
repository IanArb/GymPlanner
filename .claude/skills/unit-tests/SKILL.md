---
name: unit-tests
description: Write unit tests for a given file or feature following project conventions
allowed-tools: Read Glob Grep Write Edit
argument-hint: "[file or feature name]"
---

Write unit tests for $ARGUMENTS following the conventions in this project.

## Shared module tests (commonTest)

Use fakes, never mocks. Create these files in `shared/src/commonTest/kotlin/com/ianarbuckle/gymplanner/<feature>/`:

1. `<Feature>TestDataProvider.kt` — all test data in a single `object`, grouped by sub-objects (DTOs, domain models, lists, exceptions)
2. `Fake<Feature>RemoteDataSource.kt` — implements the `RemoteDataSource` interface; has configurable responses, `shouldThrowException` flag, captured call lists, and a `reset()` method
3. `Fake<Feature>Repository.kt` — thin wrapper delegating to the fake data source, mirrors the `Default` implementation
4. `<Feature>RepositoryTest.kt` — uses `@BeforeTest`/`@AfterTest` with `fakeRemoteDataSource.reset()`; groups tests with `// ========== Section ==========` comments

Repository implementations return `Result<T>` using:
```kotlin
runCatching { ... }.onFailure { if (it is CancellationException) throw it }
```

Test structure per case:
```kotlin
@Test
fun `description of behaviour`() = runTest {
    // Given
    // When
    // Then
}
```

## Android module tests (androidApp)

- Use MockK for fakes, Turbine for StateFlow assertions
- Use `TestCoroutineRule` as `@get:Rule`
- Construct the ViewModel directly with mocked dependencies
- Test states: `Idle → Loading → Success | Failure`

## Web module tests (webApp/commonTest)

- Use Koin test module via `startKoin`/`stopKoin` in `@BeforeTest`/`@AfterTest`
- Use `StandardTestDispatcher` + `TestScope`
- Call `testDispatcher.scheduler.advanceUntilIdle()` after dispatching actions

## General rules

- Read the source file before writing tests
- Cover: success, empty, single item, multiple items, each error type, ordering/mapping, and repeated calls
- Never test implementation details — test observable behaviour
- Do not add tests for `CancellationException` propagation; just ensure it is not swallowed