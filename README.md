# HackerRank Data Structures — Learning App

An Android native app (Kotlin, Jetpack Compose) for learning data structures through structured educational content and MCQ quizzes, with full gamification to drive engagement.

## Features

- **15+ Data Structures** across 5 categories: Linear, Trees, Graphs, Hash-Based, and Other.
- **Educational Content** per structure: explanations, complexity tables, code examples, real-world use cases.
- **MCQ Quiz Engine** — timed quizzes with instant feedback, scoring, and explanations.
- **Full Gamification** — XP points, 50 levels, daily streaks, 13 achievement badges.
- **Progress Tracking** — per-structure mastery %, overall level, longest streak, and recent quiz history.
- **Local-Only Storage** — all data persisted on-device via Room database.
- **Material Design 3** with dynamic color theming (light + dark mode) and edge-to-edge transparency.

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
The codebase features exhaustive automated testing across all layers (157 tests total):
- **ViewModels & Use Cases**: Boundary conditions, edge-case states, and exception flows tested using MockK and Turbine.
- **Compose UI & Components**: Complete Robolectric UI tests validating screens, cards, checkmark overlays, and loading indicators.
- **Room DAOs**: In-memory SQLite testing verifying schema updates and DAO operations.
- **Integration Tests**: End-to-end integration tests validating quiz completion flows, streak counters, and XP persistence.

### 4. Accessibility (A11y) & UX Polish
- **Semantic Labels**: Added descriptive `contentDescription` resources to interactive icons, progress bars, and custom animations.
- **Font Scaling**: Utilized Material 3 typography tokens to support up to 200% system font resizing.
- **Edge-to-Edge Drawing**: Configured system status and navigation bars to draw transparently, automatically adapting system icon colors to active light/dark themes.

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
| Style Check | ktlint |
| Coverage | JaCoCo |

---

## Project Structure

```
app/
├── core/                   # Theme, navigation, constants
├── data/
│   ├── local/              # Room database, DAOs, entities
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
    ├── browse/             # Home screen with structure grid
    ├── components/         # Shared composables
    ├── detail/             # Structure detail view
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

### Local Quality Commands
Run these commands in terminal to check project quality locally:
- **Style Checking**: `./gradlew ktlintCheck`
- **Lint Check**: `./gradlew lintDebug`
- **Unit & Integration Tests**: `./gradlew testDebugUnitTest`
- **JaCoCo Test Coverage Report**: `./gradlew jacocoTestReport`

---

## CI/CD Pipeline

Automated checks are configured using GitHub Actions under `.github/workflows/`:
- **PR Check (`pr-check.yml`)**: Compiles debug target, runs ktlint, executes Android lint check, and runs unit/integration tests on every PR.
- **Release Build (`release.yml`)**: Automatically packages and drafts a signed release bundle on merge to master.

---

## License

MIT

