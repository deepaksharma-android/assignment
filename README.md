## Assignment – Android App

### Overview
An Android app demonstrating a clean MVVM architecture with Paging 3, Hilt dependency injection, Retrofit/OkHttp networking, and robust connectivity handling. It displays a paged list of holdings and computes running totals as more pages load. The app is production-minded with resource externalization (strings/dimens), minify enabled for release, and comprehensive unit tests.

### Tech Stack
- **Architecture**: MVVM + Clean Architecture boundaries (domain, data, presentation)
- **DI**: Hilt
- **Async**: Kotlin Coroutines, Flows
- **Networking**: Retrofit + OkHttp (`HttpLoggingInterceptor`, timeouts)
- **Pagination**: Paging 3 (`HoldingsPagingSource`)
- **UI**: Fragments, RecyclerView, ViewBinding, Material
- **Connectivity**: `ConnectivityObserver` using `ConnectivityManager.NetworkCallback`
- **Testing**: JUnit4, Mockito, MockWebServer, Paging Testing, Coroutines Test
- **Build**: AGP 8.2.2, Kotlin 2.0.21, Java 11

### Notable Features
- Single source of truth in `HoldingsViewModel` for paging flow and computed totals.
- Immediate connectivity state emission on subscription; retries when network becomes available.
- Centralized resources: `strings.xml` and `dimens.xml` (no duplicates).
- Configurable base URL via `AppConstant.BASE_URL`.
- OkHttp client with connect/read/write timeouts and logging interceptor.

### Requirements
- Android Studio Giraffe/Koala+ (AGP 8.2.2 compatible)
- JDK 11
- Android SDK: compileSdk 35, targetSdk 35, minSdk 24

### Project Structure (high level)
- `app/src/main/java/com/example/assignment/`
  - `di/` – Hilt modules (`AppModule`, `DataModule`)
  - `data/` – Repository impls, Retrofit API, paging source
  - `domain/` – Models, repository interfaces, use cases
  - `presentation/` – ViewModels, Fragments, adapters, UI state/models
  - `util/` – Utilities like `ConnectivityObserver`, `UiFormat`, `AppConstant`
- `app/src/test/` – Unit tests (repository, use case, view model, utilities)
- `gradle/libs.versions.toml` – Central dependency versions and aliases

### Setup
1. Open the project in Android Studio.
2. Let Gradle sync. Ensure you have SDK 35 installed.
3. If you see a warning about compileSdk 35 with AGP 8.2.2, it’s informational. The project builds with AGP 8.2.2 and compile/target 35.

### Build & Run
- Debug build from Android Studio (Run/Debug) or via CLI:
```bash
./gradlew assembleDebug
```

### Testing
- Run all unit tests:
```bash
./gradlew testDebugUnitTest
```
- What’s covered:
  - `HoldingsRepositoryImpl` with MockWebServer (API mapping, error propagation)
  - `GetHoldingsUseCaseImpl` with Mockito (behavioral expectations)
  - `HoldingsViewModel` summary math (including direct tests of `calculateTotals` via reflection)
  - Utilities (formatting edge cases)

### Version Catalog (libs.versions.toml)
- Location: `gradle/libs.versions.toml`
- Purpose: Declares versions and library aliases once, used across modules for consistency and easy upgrades.
- Example usage in `build.gradle.kts`:
```kotlin
testImplementation(libs.okhttp.mockwebserver)
testImplementation(libs.mockito.kotlin)
```

### Connectivity Behavior
- On first subscription, `ConnectivityObserver.observe()` emits the current status so the UI can immediately show an offline toast if needed.
- When status becomes Available, the list retries via `holdingsAdapter.retry()`.

### Paging & Totals
- `HoldingsPagingSource` fetches pages using `GetHoldingsUseCase(page, pageSize)`.
- The fragment updates summary by passing the adapter snapshot to `HoldingsViewModel.updateHoldingSummaryFromSnapShot`, which computes totals safely (null-safe fields).

### Troubleshooting
- Suspend stubs in tests must be inside `runTest {}` blocks (Mockito).
- Locale-aware currency formatting can include non-breaking spaces; normalize strings in tests if asserting formatted output.
- If AGP warns about compileSdk 35, you may ignore or update AGP when convenient.

### License
This repository has no explicit license; use internally or add a license as needed.


