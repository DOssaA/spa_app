# AGENTS.md

This file defines how coding agents and engineers should work in this repository.

## Mission

Build MySpa as a Kotlin Multiplatform product with strong architectural boundaries, high testability, and maximal practical code sharing across Android, iOS, Web, and desktop where feasible.

## Non-negotiable principles

- Follow Clean Architecture boundaries.
- Keep SOLID visible in module, type, and dependency design.
- Use TDD for non-trivial behavior and regression-prone changes.
- Use SDD to keep work anchored to concrete user or business scenarios.
- Treat shared code as the default and platform code as the exception.
- Preserve Web compatibility and future desktop compatibility unless a platform-only tradeoff is explicit.

## Current repository layout

- `composeApp/`
  Shared Compose UI, shared resources, and multiplatform entry points.
- `core/`
  Shared architectural primitives. Currently contains the reducer contract and base MVI view model.
- `androidApp/`
  Android host application.
- `iosApp/`
  Native iOS host application and Xcode project.
- `config/`
  Shared static-analysis configuration.

## Architecture rules

Use these layers when adding or restructuring features:

1. Domain
   Pure business logic, entities, value objects, use cases, and repository contracts.
2. Data
   Repository implementations, DTOs, mappers, local and remote data sources, and infrastructure.
3. Presentation
   Screen state, reducers, intents/events, view models, UI models, and Compose screens.
4. Platform
   Android, iOS, Web, and desktop entry points plus platform adapters.

Dependency rules:

- Dependencies point inward.
- Domain must not depend on UI, Android, iOS, browser APIs, or infrastructure details.
- Data implements domain contracts; it does not define domain policy.
- Presentation can depend on domain, but should not directly own persistence or network logic.
- Platform code should compose and adapt shared code rather than own business behavior.

## Platform rules

- Android, iOS, and Web are current targets.
- Desktop is a compatibility goal, not a configured target yet.
- Do not document desktop as supported unless the Gradle target and host entry point exist.
- Shared code must avoid unnecessary platform assumptions so desktop can be added later.

## Source set rules

- Default to `commonMain` for production code.
- Default to `commonTest` for tests.
- Add platform-specific code only when a real platform API is required.
- Wrap platform APIs behind interfaces or dedicated adapters.
- Do not leak Android-specific or iOS-specific types into shared domain or presentation contracts.

## UI and state management rules

- Keep Composables focused on rendering and user interaction.
- Keep business rules and state transitions outside Composables.
- Prefer immutable UI state.
- Reducers must remain pure and deterministic.
- Use one-off effects only for ephemeral actions such as navigation, messages, or opening external resources.
- Do not mix rendering, navigation, and I/O in the same component unless the scope is trivial and temporary.

## Repository pattern guidance

- Define repository contracts in the domain layer.
- Implement repositories in the data layer.
- Keep mapping between transport/storage models and domain models explicit.
- Do not let UI or platform code talk directly to data sources when a repository boundary is expected.

## Testing expectations

- Add or update tests for every meaningful behavior change.
- Prefer shared unit tests over platform tests whenever possible.
- Test reducers and use cases before testing platform hosts.
- Use platform tests only for platform integration behavior.
- Replace placeholder tests as real features are added.

Minimum expectation for feature work:

1. tests for reducer or use-case behavior
2. tests for view model state and effect flow when applicable
3. manual verification notes for every impacted runtime target

## Dependency and library rules

- Prefer constructor injection.
- Depend on abstractions at boundaries.
- Keep third-party dependencies out of domain unless there is a clear reason.
- Before adding a shared dependency, verify it is suitable for multiplatform and does not block Web or future desktop support.

## Documentation rules

- Update `README.md` when setup, commands, supported targets, or responsibilities change.
- Keep examples aligned with real module names and tasks in this repository.
- Document architectural decisions when the tradeoff is not obvious from the code.
- Keep claims about platform support strictly aligned with actual configuration.

## Commands agents should prefer

Build Android app:

```bash
./gradlew :androidApp:assembleDebug
```

Run web targets:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
./gradlew :composeApp:jsBrowserDevelopmentRun
```

Run shared tests:

```bash
./gradlew :core:allTests :composeApp:allTests
```

Run static analysis:

```bash
./gradlew :composeApp:detekt
```

## Agent change policy

- Do not weaken boundaries to make a change faster.
- Do not move domain rules into Composables.
- Do not introduce platform-specific logic into shared code without an explicit abstraction point.
- Do not claim repository pattern, Clean Architecture, or desktop support are implemented in a feature unless the code actually reflects that.
- Prefer incremental, test-backed changes over broad speculative refactors.

## Feature workflow

1. Start from the scenario or use case.
2. Identify the proper layer boundary.
3. Add or update tests.
4. Implement shared logic first.
5. Add platform-specific adapters only where required.
6. Verify affected targets.
7. Update documentation if responsibilities or workflows changed.

## If code and docs conflict

Preserve correctness first. Then update the documentation in the same change set so the repository stays internally consistent.
