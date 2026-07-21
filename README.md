# HackerRank Data Structures — Learning App

An Android native app (Kotlin, Jetpack Compose) for learning data structures through structured educational content and MCQ quizzes, with full gamification to drive engagement.

## Features

- **15+ Data Structures** across 5 categories: Linear, Trees, Graphs, Hash-Based, and Other
- **Educational Content** per structure: explanations, complexity tables, code examples, real-world use cases
- **MCQ Quiz Engine** — timed quizzes with instant feedback, scoring, and explanations
- **Full Gamification** — XP points, 50 levels, daily streaks, 13 achievement badges
- **Progress Tracking** — per-structure mastery %, overall level, longest streak
- **Local-Only Storage** — all data persisted on-device via Room database
- **Material Design 3** with dynamic color theming (light + dark mode)

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

## Screenshots

_(Coming soon)_

## Getting Started

1. Clone the repo: `git clone https://github.com/suryamudti/hackerrank-app.git`
2. Open in Android Studio
3. Sync Gradle and run on emulator or device (API 26+)

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

## CI/CD & Code Quality

This project is configured with GitHub Actions workflows under `.github/workflows/` for automated verification:
- **Pull Request Check (`pr-check.yml`)**: Runs unit tests on every pull request.
- **Release Build (`release.yml`)**: Automatically packages and drafts a signed release on merge to main.

To run quality checks locally, use:
- **Lint Check**: `./gradlew lintDebug`
- **Unit & Integration Tests**: `./gradlew testDebugUnitTest`
- **Code Style Check**: `./gradlew ktlintCheck`
- **JaCoCo Test Coverage Report**: `./gradlew jacocoTestReport`

### Git Pre-Commit Hook Setup

To enable automated pre-commit quality checks before every commit, run:
```bash
git config core.hooksPath .githooks
```

## License

MIT
