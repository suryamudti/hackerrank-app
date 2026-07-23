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

This project has been optimized to target a **production-grade portfolio showcase**, demonstrating robust architectural integrity, error handling, performance optimization, and CI/CD quality controls.

### 1. Clean Architecture & MVVM
- **Strict Layer Separation**: Eliminates repository-in-viewmodel injection by abstracting all logic into single-responsibility Use Cases.
- **Interface Segregation (ISP)**: Split `ProgressRepository` into focused repository concerns (`ProfileRepository`, `ProgressRepository`, `DailyChallengeRepository`).
- **Reactive Flows**: ViewModels observe UI state dynamically by collecting and mapping domain model state flows with lifecycle awareness.

### 2. Error Handling & State Consistency
- Every core screen implements structured, robust state management using sealed UI states: `Loading`, `Loaded`, `Empty`, and `Error`.
- Repository execution blocks catch exceptions gracefully and propagate localized messages to UI layers without crashes.
- **Home Screen & UI Component Stability**:
  - Safe gradient color calculation using `Math.floorMod` prevents bounds crashes on structure cards.
  - Synchronized pull-to-refresh state machine prevents state collision exceptions on `BrowseScreen`.
  - Fail-safe Gson data mappers provide fallback empty objects to prevent `NullPointerException`s during JSON parsing.
  - Strict positional string formatting (`%1$d`, `%2$d`) ensures crash-free localized string rendering.

### 3. Testing Overhaul
The codebase features exhaustive test coverage across all layers (157 tests total):
- **ViewModels & Use Cases**: Boundary conditions, edge-case states, and exception flows tested using MockK and Turbine.
- **Compose UI & Components**: Complete Robolectric UI tests validating screens, cards, checkmark overlays, and loading indicators.
- **Room DAOs**: In-memory database testing verifying schema updates and DAO operations.
- **Integration Tests**: End-to-end integration tests validating quiz completion flows, streak counters, and XP persistence.

### 4. Performance Optimizations
- Replaced database table-scanning `ORDER BY RANDOM()` with an indexed `row_index` database model paired with Kotlin shuffles.
- Created Room database indexes on foreign keys (`structure_id`, `category`, `problem_id`) to accelerate query executions.
- Transformed list collections into reactive single-emission Flow mappings.

### 5. Accessibility (A11y) & UX Polish
- **Semantic Labels**: Added descriptive `contentDescription` resources to all interactive icons, progress bars, and custom animations.
- **Font Scaling**: Eliminated hardcoded text heights, utilizing Material 3 typography tokens to support 200% system font resizing.
- **Edge-to-Edge Drawing**: Configured system status and navigation bars to draw transparently, automatically adapting system icon colors to active light/dark themes.
- **Micro-Interactions**: Features custom confetti celebration overlays and smooth `Animatable` XP counter bounces.

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
| Style Check | ktlint |
| Coverage | JaCoCo |

---

## Getting Started

1. Clone the repo: `git clone https://github.com/suryamudti/hackerrank-app.git`
2. Open in Android Studio.
3. Sync Gradle and run on emulator or device (API 26+).

### Git Pre-Commit Hook Setup
To configure automated pre-commit lint and test checks before every local commit, run:
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
- **PR Check (`pr-check.yml`)**: Compiles the debug target, runs ktlint, executes Android lint check, and runs all unit/integration tests on every PR.
- **Release Build (`release.yml`)**: Builds, signs, and packages release APK/AAB bundles to draft a GitHub Release automatically on branch merge to main.

---

## License

MIT
