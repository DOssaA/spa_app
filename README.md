# MySpa

MySpa is a Kotlin Multiplatform application designed to grow under TDD, SDD, SOLID, Clean Architecture, and the repository pattern. The codebase is intended to maximize shared business and presentation logic across Android, iOS, Web, and desktop where feasible.

The repository currently includes active Android, iOS, and Web targets. Desktop is not configured yet, but shared code should continue to be written so a desktop target can be added with minimal refactoring.

## Goals

- Keep business rules platform-agnostic.
- Share as much code as possible across targets.
- Prefer explicit boundaries over convenience coupling.
- Drive non-trivial behavior through tests and scenarios.
- Preserve Web compatibility and future desktop viability when writing shared code.

## Tech direction

- Kotlin Multiplatform
- Compose Multiplatform
- Clean Architecture
- Repository pattern
- MVI-style presentation primitives
- TDD and SDD
- SOLID design principles
- Static analysis with Detekt

## Current repository structure

- `composeApp/`
  Shared Compose UI, shared presentation code, Compose resources, and Web/iOS entry points.
- `core/`
  Shared architectural primitives. Today it contains the base reducer contract and base MVI view model.
- `androidApp/`
  Android host application that boots the shared Compose UI.
- `iosApp/`
  Native iOS host application and Xcode project that embeds the shared Compose UI.
- `config/`
  Shared repository configuration, currently including Detekt rules.
- `gradle/`
  Gradle wrapper and version catalog.

## Current implementation status

The project is still early-stage. At the moment the repository contains:

- a shared onboarding-style Compose screen
- a reusable MVI foundation in `core`
- Android and iOS host shells
- Web support through JS and Wasm browser targets

The repository does not yet include:

- feature-specific domain modules
- repository implementations
- remote or local data sources
- dependency injection wiring
- navigation flows
- a configured desktop target

## Architecture expectations

Feature work should evolve toward these conceptual layers:

1. `domain`
   Pure business logic, entities, value objects, use cases, and repository contracts.
2. `data`
   Repository implementations, DTOs, mappers, persistence, networking, and infrastructure concerns.
3. `presentation`
   Reducers, UI state, intents/events, view models, UI models, and Compose screens.
4. `platform`
   Android, iOS, Web, and desktop bootstrapping plus platform-specific adapters.

Dependency direction must point inward. Domain must not depend on UI or platform code. Presentation must not own infrastructure. Platform code should adapt shared code, not replace it.

## Platform support

- Android
  First-class target through `androidApp`.
- iOS
  First-class target through `iosApp` and the shared `ComposeApp` framework.
- Web
  Supported through JS and Wasm browser targets in `composeApp`.
- Desktop
  Not configured yet. Shared code should remain desktop-friendly where practical.

Desktop support should be described as a design goal, not as a shipping target, until the Gradle target and host app are actually added.

## Source set guidance

- Use `commonMain` by default for shared logic.
- Use `commonTest` for shared tests.
- Add platform-specific code only when a platform API is required.
- Hide platform dependencies behind interfaces or small adapters.
- Avoid Android-only assumptions in shared UI or presentation code.

## Development principles

- Prefer feature code that is easy to test in shared modules.
- Keep reducers pure and deterministic.
- Keep Composables focused on rendering and user interaction.
- Move state transitions and business decisions out of Composables.
- Prefer constructor injection and boundary abstractions.
- Do not introduce dependencies into shared code unless they are compatible with multiplatform targets.

## Prerequisites

- JDK 17 or newer compatible with the Android Gradle Plugin used by this repository
- Android Studio or IntelliJ IDEA with Kotlin Multiplatform support
- Android SDK installed and available through `local.properties`
- Xcode on macOS for iOS builds

If Gradle fails locally, verify that `JAVA_HOME` points to a real JDK installation and not to the Android SDK directory.

## Common commands

### Android

Build the Android app:

```bash
./gradlew :androidApp:assembleDebug
```

Install the Android debug build:

```bash
./gradlew :androidApp:installDebug
```

### Web

Run the Wasm web target:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Run the JS web target:

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### iOS

Open the iOS host project in Xcode:

```bash
open iosApp/iosApp.xcodeproj
```

The Xcode project invokes:

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

### Tests and analysis

Run shared tests:

```bash
./gradlew :core:allTests :composeApp:allTests
```

Run Android device tests:

```bash
./gradlew :core:connectedAndroidTest
```

Run Detekt:

```bash
./gradlew :composeApp:detekt
```

## Recommended workflow

1. Define the scenario and expected behavior.
2. Add or update tests before or alongside the implementation.
3. Implement domain and presentation behavior in shared code where possible.
4. Add platform adapters only when a real platform concern exists.
5. Run tests and static analysis before merging.
6. Update documentation when responsibilities, commands, or supported targets change.

## Contribution guidelines

- Keep code compatible with Android, iOS, Web, and future desktop support unless the requirement is intentionally platform-specific.
- Prefer small feature-oriented packages or modules over generic utility growth.
- Keep domain names explicit and business-oriented.
- Replace placeholder tests as real features are introduced.
- Update this file and `AGENTS.md` when architectural rules or setup expectations change.
