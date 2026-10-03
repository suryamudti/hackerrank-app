# HackerRank Data Structures — Learning App

An Android native app (Kotlin, Jetpack Compose) for learning data structures through structured educational content and MCQ quizzes, with full gamification to drive engagement.

## Features

- **16 Data Structures** across 5 categories: Linear, Trees, Graphs, Hash-Based, and Other.
- **Educational Content** per structure: explanations, complexity tables, code examples, real-world use cases.
- **MCQ Quiz Engine** — timed quizzes with instant feedback, scoring, and explanations.
- **100 Algorithm Problems** — searchable, filterable by difficulty and category, each with approach notes and a Kotlin solution. Solvable problems award XP.
- **Full Gamification** — XP points, 50 levels, daily streaks, 13 achievement badges.
- **Progress Tracking** — per-structure mastery %, overall level, longest streak, and recent quiz history.
- **Local-Only Storage** — all data persisted on-device via Room database.
- **Bilingual UI** — English and Indonesian (`values-in`) string resources.
- **Material Design 3** with dynamic color theming (light + dark mode), themed adaptive launcher icon, and edge-to-edge transparency.

---

## Technical Architecture & Quality Gates

This project is built targeting a **production-grade architecture**, demonstrating clean separation of concerns, robust error handling, performance optimization, and strict CI/CD quality controls.

### 1. Clean Architecture & MVVM
- **Strict Layer Separation**: Decoupled ViewModels from repositories using single-responsibility Use Cases (`ObserveBrowseDataUseCase`, `ObserveProblemsUseCase`, `ObserveProgressOverviewUseCase`, `ObserveBadgesUseCase`, `GetDailyChallengeUseCase`, `FinishQuizUseCase`, etc.).
- **Interface Segregation**: Split repository concerns into focused contracts (`ProfileRepository`, `ProgressRepository`, `DailyChallengeRepository`).
- **Reactive Flows**: ViewModels observe UI state dynamically by collecting domain model state flows with lifecycle awareness.

### 2. Error Handling & Stability
- Every core screen implements structured, robust state management using sealed UI states: `Loading`, `Loaded`, `Empty`, and `Error`.
- **Home Screen & UI Component Stability**:
  - Safe gradient color index calculation using `Math.floorMod` prevents bounds crashes on structure cards.
  - Synchronized pull-to-refresh state machine prevents state collision exceptions on `BrowseScreen`.
  - Fail-safe Gson data mappers provide fallback empty objects to prevent `NullPointerException`s during JSON parsing.
  - Strict positional string formatting (`%1$d`, `%2$d`) ensures crash-free localized string rendering.

### 3. Comprehensive Test Coverage
The codebase features exhaustive automated testing across all layers (179 tests total, all passing):
- **ViewModels & Use Cases**: Boundary conditions, edge-case states, and exception flows tested using MockK and Turbine.
- **Compose UI & Components**: Complete Robolectric UI tests validating screens, cards, checkmark overlays, and loading indicators.
- **Room DAOs**: In-memory SQLite testing verifying schema updates and DAO operations.
- **Integration Tests**: End-to-end integration tests validating quiz completion flows, streak counters, and XP persistence.

### 4. Accessibility (A11y) & UX Polish
- **Semantic Labels**: Added descriptive `contentDescription` resources to interactive icons, progress bars, and custom animations.
- **Font Scaling**: Utilized Material 3 typography tokens to support up to 200% system font resizing.
- **Edge-to-Edge Drawing**: Configured system status and navigation bars to draw transparently, automatically adapting system icon colors to active light/dark themes.

### 5. Static Analysis & Style Gates
- **Zero-warning baseline**: `ktlintCheck`, `lintDebug`, and `lintRelease` all report *"No issues found"*.
- **Warnings are errors**: `warningsAsErrors = true` in `app/build.gradle.kts` turns every Android Lint warning into a build failure, so the gate cannot silently erode.
- **Both variants linted**: Release-only checks (e.g. `MonochromeLauncherIcon`) are exercised explicitly, not left to the debug-only `lint` aggregate task.
- **i18n correctness**: Quantity-bearing strings use `<plurals>` (e.g. *"Current: 1 day"* vs *"Current: 5 days"*) so translations stay grammatical; genuine false positives are suppressed with a scoped `tools:ignore` rather than a fake plural.
- **Correct launcher icon**: The manifest references the adaptive `@mipmap/ic_launcher` (with a themed `monochrome` layer) instead of a bare foreground drawable.

---

## Tech Stack

| Concern | Choice |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Database | Room (SQLite) |
| DI | Hilt |
| Async | Kotlin Coroutines + Flow |
| Architecture | Clean Architecture + MVVM |
| Style Check | ktlint (`ktlintCheck`) |
| Static Analysis | Android Lint (`warningsAsErrors = true`, debug + release) |
| Coverage | JaCoCo |

---

## Project Structure

```
app/
├── core/                   # Theme, navigation, constants
├── data/
│   ├── local/              # Room database, DAOs, entities
│   ├── remote/             # Daily challenge API client
│   ├── repository/         # Repository implementations
│   └── seed/               # Seed data loader
├── di/                     # Hilt modules
├── domain/
│   ├── gamification/       # XP, streak, badge engine
│   ├── model/              # Domain models
│   ├── repository/         # Repository interfaces
│   └── usecase/            # Business logic use cases
└── ui/
    ├── achievements/       # Badge gallery
    ├── badge/              # Badge detail screen
    ├── browse/             # Home screen with structure grid
    ├── components/         # Shared composables (design system)
    ├── detail/             # Structure detail view
    ├── problems/           # Algorithm problem list + detail
    ├── progress/           # Dashboard (XP, streaks, mastery)
    └── quiz/               # MCQ quiz engine
```

---

## Getting Started

1. Clone the repo: `git clone https://github.com/suryamudti/hackerrank-app.git`
2. Open in Android Studio.
3. Sync Gradle and run on emulator or device (API 26+).

### Git Pre-Commit Hook Setup
To enable automated pre-commit quality checks before every commit, run:
```bash
git config core.hooksPath .githooks
```
The hook runs `ktlintCheck` + `lintDebug`, so style problems surface before you push.

### Local Quality Commands
Run these commands in terminal to check project quality locally:
- **Style Checking**: `./gradlew ktlintCheck`
- **Lint Check**: `./gradlew lintDebug lintRelease` (see note below)
- **Unit & Integration Tests**: `./gradlew testDebugUnitTest`
- **Coverage Verification**: `./gradlew jacocoTestCoverageVerification`
- **JaCoCo Test Coverage Report**: `./gradlew jacocoTestReport`

> **Why not just `./gradlew lint`?** In AGP 8.7 the aggregate `lint` task only wires up the
> **debug** variant. Release-only checks such as `MonochromeLauncherIcon` are invisible to it,
> even though `checkReleaseBuilds = true` means they gate the release build. Naming both
> variants explicitly keeps local runs and CI aligned.

Reports are written to `app/build/reports/lint-results-debug.html` and
`app/build/reports/lint-results-release.html`.

---

## CI/CD Pipeline

Automated checks are configured using GitHub Actions under `.github/workflows/`:

- **PR Check (`pr-check.yml`)** — two parallel jobs on every pull request:
  - **`Lint`** — `ktlintCheck` plus Android Lint for the **debug and release** variants.
    Lint runs with `warningsAsErrors = true`, so *any* warning fails the job rather than
    scrolling past in the log. HTML reports are uploaded as artifacts for inspection.
  - **`Run Unit Tests`** — the full unit and integration suite (179 tests).
  - Concurrency is keyed on the PR number, so superseded runs are cancelled automatically.
- **Release Build (`release.yml`)**: Automatically packages and drafts a signed release bundle on merge to `main`/`master`.

Both `Lint` and `Run Unit Tests` should be selected as required status checks under
**Settings ➔ Branches ➔ Branch protection rules** so neither can be bypassed.

---

## License

MIT

