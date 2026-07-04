# HackerRank Code Practice App — Implementation Plan

## Overview

An Android native app (Kotlin) for learning data structures through structured educational content and MCQ quizzes, with full gamification to drive engagement. Users browse 15+ data structures organized by category, read explanations with examples, test their knowledge via quizzes, and track progress through XP, levels, streaks, and badges — all stored locally on-device.

---

## Current State Analysis

**The project directory `C:\Flutter\hackerrank-app` was initialized as Flutter but pivoted to Android Kotlin native** — this is a greenfield build with Android SDK. All implementation will be from scratch.

---

## Desired End State

- **15+ data structures** covered across 5 categories (Linear, Trees, Graphs, Hash-based, Other)
- **Educational content** per structure: explanation, complexity table, visual diagrams, code examples, real-world use cases
- **MCQ quiz engine** — 5–10 questions per structure with instant feedback and scoring
- **Full gamification** — XP, 50 levels, daily streaks, 12+ achievement badges
- **Progress tracking** — per-structure mastery %, overall level, longest streak
- **Local-only persistence** — all data stored in Hive, no backend required
- **Material Design 3** UI with dynamic color theming

### Navigation Structure (final)

```
AppShell (BottomNavigationBar)
├── Tab 1: Home / Browse (grid of data structure cards by category)
├── Tab 2: Progress Dashboard (XP bar, level, streaks, today's activity)
└── Tab 3: Achievements (badge gallery)

Push routes:
├── StructureDetailScreen (explanation, example, when-to-use)
│   └── QuizScreen (MCQ flow, timer, results)
└── BadgeDetailScreen (badge unlock criteria, progress)
```

---

## What We're NOT Doing

| Out of Scope | Rationale |
|---|---|
| Code execution / IDE | Requirement is "Reference + MCQ quizzes" only |
| Cloud sync | Local-only per spec |
| User accounts / auth | No backend needed |
| iOS release | Android-only per spec |
| Video content | Text + diagrams only |
| Community features / leaderboards | Out of scope for simplicity |
| Custom animations engine | Use flutter_animate / lottie where needed |
| Real-time multiplayer quizzing | Local single-player only |

---

## Implementation Approach

### Architecture: Clean Architecture with MVVM

```
app/
├── core/                    # Shared utilities, theme, routing, constants
│   ├── theme/
│   ├── navigation/
│   └── extensions/
├── data/                    # Data layer
│   ├── local/               # Room DAOs, database
│   ├── model/               # Room entities, DTOs
│   └── repository/          # Repository implementations
├── domain/                  # Domain layer
│   ├── model/               # Domain entities
│   ├── repository/          # Abstract repository interfaces
│   └── usecase/             # Business logic use cases
├── ui/                      # Presentation layer
│   ├── browse/              # Home screen, data structure grid
│   ├── detail/              # Structure detail view
│   ├── quiz/                # MCQ quiz engine
│   ├── progress/            # Dashboard, XP, streaks
│   └── achievements/        # Badge gallery
└── HackerRankApp.kt         # Application class
```

### Tech Stack Details

| Concern | Choice | Version |
|---|---|---|
| Language | Kotlin | 2.0.x |
| UI Framework | Jetpack Compose (Material 3) | BOM 2024.x |
| Navigation | Jetpack Navigation Compose | 2.8.x |
| Local Database | Room | 2.6.x |
| DI | Hilt | 2.51.x |
| Async | Kotlin Coroutines + Flow | 1.8.x |
| Image Loading | Coil Compose | 2.6.x |
| Animations | Compose Animation + Lottie | built-in |
| Architecture | Clean Architecture + MVVM | — |
| Gamification | Custom engine (Kotlin sealed classes) | — |

### Why Custom Gamification Engine (not quest_gamification)

The existing gamification libraries target generic use cases. Our domain (learning data structures) has different badge conditions, XP triggers, and progression rules. Building a custom engine gives us:

- Full control over badge logic (per-structure mastery, perfect quiz scores, etc.)
- Tight integration with Room and Hilt without adapter layers
- No dependency on a library with unknown long-term maintenance
- Smaller APK size

---

## Phases

### Phase 1: Project Scaffold & Foundation

**Goal:** Android Kotlin project with Compose, Room, Hilt, Navigation Compile, and folder structure.

**Changes Required:**

1. Clear Flutter artifacts, create Android project structure from scratch
2. Root `build.gradle.kts` + `settings.gradle.kts` — configure project
3. `app/build.gradle.kts` — add dependencies:
   ```kotlin
   plugins {
       id("com.android.application")
       id("org.jetbrains.kotlin.android")
       id("org.jetbrains.kotlin.plugin.compose")
       id("com.google.devtools.ksp")
       id("com.google.dagger.hilt.android")
       id("kotlin-parcelize")
   }

   dependencies {
       // Compose BOM
       implementation(platform("androidx.compose:compose-bom:2024.06.00"))
       implementation("androidx.compose.material3:material3")
       implementation("androidx.compose.ui:ui")
       implementation("androidx.compose.ui:ui-tooling-preview")
       implementation("androidx.activity:activity-compose:1.9.0")
       implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
       implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

       // Navigation
       implementation("androidx.navigation:navigation-compose:2.8.0")

       // Room
       implementation("androidx.room:room-runtime:2.6.1")
       implementation("androidx.room:room-ktx:2.6.1")
       ksp("androidx.room:room-compiler:2.6.1")

       // Hilt
       implementation("com.google.dagger:hilt-android:2.51.1")
       ksp("com.google.dagger:hilt-android-compiler:2.51.1")
       implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

       // Coil
       implementation("io.coil-kt:coil-compose:2.6.0")

       // Gson
       implementation("com.google.code.gson:gson:2.10.1")

       // Testing
       testImplementation("junit:junit:4.13.2")
       androidTestImplementation("androidx.test.ext:junit:1.1.5")
   }
   ```
4. Create `app/src/main/java/com/hackerrank/app/` package structure
5. Create `core/theme/Theme.kt` — Material 3 Compose theme with dynamic color, light + dark schemes
6. Create `core/navigation/NavGraph.kt` — Navigation Compose with initial route table (placeholder composables)
7. Create `core/Constants.kt` — app-wide constants (level XP thresholds, badge definitions, category order)
8. Create `data/local/HackerRankDatabase.kt` — Room database with DAOs
9. Create `HackerRankApp.kt` — Application class with `@HiltAndroidApp`
10. Create `MainActivity.kt` — `@AndroidEntryPoint` with Compose `setContent` + `HackerRankTheme` + `NavGraph`
11. Create folder structure as outlined above

**Success Criteria:**
- Project compiles with `./gradlew assembleDebug`
- App launches on emulator with M3-themed blank shell
- Navigation between placeholder screens works
- Room database initializes without errors

---

### Phase 2: Data Models & Content Storage

**Goal:** All data models defined, Room entities set up, seed content loaded via Room.

**Changes Required:**

1. **Domain Models** (`app/.../domain/model/`):
   - `DataStructure` — data class `{ id, name, slug, category, explanation, complexityTable, whenToUse, diagramRes, codeExample, quizIds }`
   - `QuizQuestion` — data class `{ id, structureId, question, options, correctIndex, explanation }`
   - `UserProgress` — data class `{ structureId, quizzesCompleted, totalCorrect, bestScore, masteryLevel }`
   - `UserProfile` — data class `{ totalXp, level, currentStreak, longestStreak, lastActiveDate, earnedBadgeIds }`

2. **Room Entities** (`data/local/entity/`):
   - `DataStructureEntity` — Room entity with `@PrimaryKey`, type converters for `Map<String, String>` (complexity), `List<String>` (whenToUse, quizIds)
   - `QuizQuestionEntity` — Room entity with `@PrimaryKey`, `@ForeignKey` to DataStructure
   - `UserProgressEntity` — Room entity with `@PrimaryKey`
   - `UserProfileEntity` — single-row entity (always id=1)

3. **Room DAOs** (`data/local/dao/`):
   - `DataStructureDao` — `@Query` for all, by category, by id/slug
   - `QuizQuestionDao` — `@Query` by structureId, random order
   - `ProgressDao` — `@Upsert`, `@Query` for progress by structureId, all progress
   - `ProfileDao` — `@Upsert`, `@Query` for single profile row

4. **Room Database** (`HackerRankDatabase.kt`) — version 1, all entities, all DAOs

5. **Seed Data** (`data/seed/`):
   - Pre-populate Room database via `RoomDatabase.Callback` + `createFromAsset()` if using a prepopulated DB, or insert on first launch
   - Hardcoded list of all 15+ data structures with educational content
   - 5–10 MCQ questions per structure
   - Badge definitions with unlock conditions
   - Loaded via `database.writeBatch{}` in `onCreate` callback

6. **Data Structure Taxonomy** (5 categories × 15+ structures):

   **Linear (4)**
   - Array / List
   - Linked List (Singly, Doubly, Circular)
   - Stack
   - Queue (Simple, Circular, Priority, Deque)

   **Trees (5)**
   - Binary Tree
   - Binary Search Tree (BST)
   - AVL Tree / Self-Balancing Trees
   - Heap (Min-Heap, Max-Heap)
   - Trie (Prefix Tree)

   **Graphs (3)**
   - Graph (Directed, Undirected)
   - Weighted Graph
   - Graph Algorithms (BFS, DFS, Dijkstra)

   **Hash-Based (2)**
   - Hash Table / HashMap
   - Hash Set

   **Other (2+)**
   - Disjoint Set (Union-Find)
   - Segment Tree

7. **Repository Layer** (`data/repository/`):
   - `ContentRepositoryImpl` — reads `DataStructure` objects via DAO, maps to domain models
   - `QuizRepositoryImpl` — reads `QuizQuestion` by `structureId` via DAO
   - `ProgressRepositoryImpl` — reads/writes `UserProgress` and `UserProfile` via DAOs

8. **Domain Repository Interfaces** (`domain/repository/`):
   - `ContentRepository` — interface
   - `QuizRepository` — interface
   - `ProgressRepository` — interface

**Success Criteria:**
- All entities compile with Room/KSP annotations
- `./gradlew assembleDebug` succeeds
- Seed data loads into Room database on first launch
- DAO queries return correct data for every structure

---

### Phase 3: Browse & Detail Screens

**Goal:** Users can browse all data structures by category and view detailed content.

**Changes Required:**

1. **BrowseScreen** (`ui/browse/`):
   - `BrowseViewModel` — exposes `StateFlow<List<DataStructureCategory>>` with grouped structures
   - `BrowseScreen` composable — `LazyVerticalGrid` (2 columns) with category section headers
   - Each card shows: icon, name, difficulty indicator, mastery % ring (`CircularProgressIndicator`)
   - Pull-to-refresh via `pullRefresh` modifier
   - Animated list entrance via `AnimatedVisibility` + `animateItemPlacement()`

2. **DetailScreen** (`ui/detail/`):
   - `DetailViewModel` — loads structure by slug via `ContentRepository`
   - `DetailScreen` composable — `LazyColumn` with sections:
     - **Header**: structure name, category badge, complexity overview
     - **Explanation**: `Text` with bullet points (`AnnotatedString`)
     - **Complexity Table**: `Table` composable with O-notation rows
     - **Code Example**: syntax-highlighted code in a `Card` with copy button
     - **When to Use**: `LazyColumn` items with real-world scenarios + icons
     - **Mastery Section**: `LinearProgressIndicator`, "Take Quiz" `Button`
   - All sections separated by `Divider` / section headers

3. **DataStructureCategory** — sealed class/enum with display name and icon resource

**Success Criteria:**
- Browse screen shows all 15+ structures grouped by 5 categories
- Tapping a card navigates to DetailScreen via Navigation Compose
- Detail screen renders all sections (explanation, complexity, code, use-cases)
- Mastery progress ring reflects persisted Room data

---

### Phase 4: Quiz Engine

**Goal:** Users can take MCQ quizzes per structure with timer, scoring, and feedback.

**Changes Required:**

1. **QuizScreen** (`ui/quiz/`):
   - `QuizViewModel` — manages quiz state machine: `Idle -> Loading -> Ready -> Answering -> Reviewing -> Completed`
   - `QuizScreen` composable — state-driven UI with `StateFlow<QuizState>`
   - Question card with question text + 4 option `Button`s
   - Selected answer highlights immediately (green if correct, red if wrong via `Animatable`)
   - Explanation shown after each answer in `AnimatedVisibility`
   - Progress indicator (question 3/8) via `LinearProgressIndicator`
   - Per-quiz timer using `LaunchedEffect` + `System.nanoTime()` (Stopwatch equivalent)
   - Results screen at end: score, time, XP earned, badges earned

2. **Quiz Logic** (`domain/usecase/`):
   - `QuizSession` — data class managing:
     - Current question index
     - Selected answers map
     - Score tally
     - Start/end time nanos
     - List of answered question IDs
   - `SubmitAnswerUseCase` — records answer, updates score, determines correctness
   - `CalculateXpUseCase` — formula: `base(50) + streakBonus(10) + speedBonus(upTo(20))`

3. **Quiz Results**:
   - Score card (X/10, percentage) via `Card` composable
   - Time taken formatted
   - XP earned (with animated counter using `Animatable`)
   - "Review Answers" button (shows all questions with correct/incorrect marks)
   - "Retry" and "Back to Structure" buttons

**Success Criteria:**
- 5–10 MCQs load per structure
- Answer selection works with instant visual feedback
- Score + XP calculated correctly at end
- Progress persists to Room after quiz completion
- XP animation counter runs on results screen

---

### Phase 5: Gamification Engine

**Goal:** Full XP/level/streak/badge system driving engagement.

**Changes Required:**

1. **Gamification Core** (`domain/gamification/`):

   **XP & Levels:**
   - Formula: `level = floor(sqrt(totalXP / 100))` — each level requires slightly more XP
   - XP sources:
     - Complete a quiz: 50 XP base
     - Perfect score (100%): +50 XP bonus
     - Daily login: +10 XP
     - Daily quiz completion: +20 XP
     - Streak milestone (3/7/14/30 days): +100–500 XP

   **Streaks:**
   - Tracked via `lastActiveDate` (date only, no time)
   - Checked on app startup: if lastActive == yesterday, increment streak; if lastActive < yesterday, reset to 0
   - `longestStreak` tracks all-time best

   **Badges** (12+ defined):
   | Badge | Condition |
   |---|---|
   | First Steps | Complete first quiz |
   | Quick Learner | Perfect score on any quiz |
   | Speed Demon | Complete quiz in under 60s |
   | Streak Novice | 3-day streak |
   | Streak Master | 7-day streak |
   | Streak Legend | 30-day streak |
   | Array Ace | Master all array quizzes |
   | Tree Whisperer | Master all tree quizzes |
   | Graph Guru | Master all graph quizzes |
   | Completionist | Master every data structure |
   | Level 10 | Reach level 10 |
   | Level 25 | Reach level 25 |
   | Level 50 | Reach level 50 |

2. **Gamification Use Cases** (`domain/usecase/`):
   - `RecordEventUseCase` — single entry point:
     - Awards XP via `ProgressRepository`
     - Checks streak via `ProgressRepository`
     - Evaluates badge conditions via `BadgeEvaluator`
     - Returns `GamificationResult { xpAwarded, newLevel, newBadges[], streakInfo }`
   - `GetStreakUseCase` — exposes current streak count
   - `GetLevelUseCase` — exposes current level + XP progress to next level
   - `GetBadgesUseCase` — exposes earned vs locked badges

3. **BadgeEvaluator** (`domain/gamification/BadgeEvaluator.kt`):
   - Evaluates all badge conditions against current state
   - Returns list of newly earned badges

4. **UI Toast / Animation** — when badges/levels are earned, show overlay `Dialog` with confetti-like animation (`Lottie` or Compose `Canvas` drawing + haptic feedback via `HapticFeedback`)

**Success Criteria:**
- XP is correctly awarded and accumulated across quiz completions
- Level-ups trigger at correct thresholds
- Streaks track accurately across app restarts
- All 12+ badges can be earned with correct conditions
- Gamification result (XP + level + badge) displays as animated toast

---

### Phase 6: Progress Dashboard & Achievements

**Goal:** Users see their overall progress and badge collection.

**Changes Required:**

1. **DashboardScreen** (`ui/progress/`):
   - `DashboardViewModel` — exposes `StateFlow<DashboardState>` aggregating all stats
   - **Top section**: Avatar/name placeholder, Level badge with XP progress ring (`Canvas` drawn), total stats (quizzes taken, structures mastered)
   - **Streak section**: Fire emoji + current streak count + longest streak record
   - **Category mastery grid**: 5 category cards showing progress bars (% of quizzes completed in each category)
   - **Recent activity**: last 5 quiz results with scores and timestamps

2. **AchievementsScreen** (`ui/achievements/`):
   - `AchievementsViewModel` — exposes earned vs locked badges
   - Grid of badge cards (3 columns via `LazyVerticalGrid`)
   - Earned badges: full color, unlocked animation (`animateContentSize`)
   - Locked badges: grayscale `ColorFilter` + "?" overlay + unlock condition text
   - Progress toward locked badges shown (e.g., "3/7 days")

3. **Dashboard Use Cases** (`domain/usecase/`):
   - `GetDashboardStatsUseCase` — aggregates all stats from various repositories
   - `GetRecentActivityUseCase` — last 5 quiz session records via DAO
   - `GetBadgesUseCase` — all badges with earned status

**Success Criteria:**
- Dashboard shows accurate XP, level, streak, and category progress
- Badge gallery renders earned vs locked with correct conditions
- All values update after completing a quiz (ViewModel survives config change)
- Progress ring animates smoothly on load

---

### Phase 7: UI Polish & Performance

**Goal:** Polished M3 experience with smooth transitions and edge-case handling.

**Changes Required:**

1. **M3 Theming** — final pass on `MaterialTheme`, typography, shape scheme
2. **Dark Mode** — automatic system dark mode via `isSystemInDarkTheme()`, manual toggle via `ThemeModeState`
3. **Transitions & Animations**:
   - Shared element transitions between browse cards and detail screen
   - Staggered list animations on browse screen (`AnimatedVisibility` with `staggeredDelay`)
   - Confetti particle burst on level-up and badge earn (`Canvas` + `LaunchedEffect`)
   - Progress ring animations (`animateFloatAsState`)
   - Micro-interactions: button press scaling via `Modifier.composedGestures`
4. **Empty States** — no-structures-loaded, no-quizzes-taken, no-badges-earned placeholder composables
5. **Error Handling** — `snackbar` for DB init failure, graceful degradation
6. **Accessibility** — `contentDescription` on all icons, sufficient contrast ratios, large text support

**Success Criteria:**
- Light + dark mode both render correctly
- Transitions feel smooth (60fps on mid-range devices)
- Empty states never crash — show helpful messages
- Content descriptions present on all interactive elements

---

## Testing Strategy

### Unit Tests (domain layer)
- **Gamification engine**: test XP calculation, level thresholds, streak logic, badge conditions (`RecordEventUseCaseTest`)
- **Quiz scoring**: test score calculation, perfect score bonus, time bonus (`CalculateXpUseCaseTest`)
- **Mastery calculation**: test percentage computation, mastery level boundaries

### UI Tests (presentation layer with Compose)
- BrowseScreen renders correct number of structure cards
- DetailScreen displays all sections
- Quiz flow: answer selection, next question, results display
- Dashboard renders accurate stats from mocked ViewModel
- Badge gallery shows correct earned/locked states

### Instrumentation Tests
- Full quiz flow: browse → open structure → take quiz → view results → verify progress updated in Room
- Streak persistence: manipulate `lastActiveDate` in Room, verify streak counter

### Testing Tools & Approach
- `JUnit 5` for unit tests
- `Turbine` for testing Kotlin Flow emissions
- `MockK` or `Mockito` for mocking repositories
- `Compose UI Test` (`createComposeRule()`) for UI tests
- Room's in-memory database for database tests

---

## Performance Considerations

| Concern | Mitigation |
|---|---|
| **Room database size** | Content is read-heavy with ~100KB total. Pre-populated DB shipped with APK |
| **Cold start** | Seed data check is part of Room `onCreate` callback |
| **Quiz timer** | Use `System.nanoTime()` (nanosecond precision), not `Handler.postDelayed` |
| **List scrolling** | `LazyVerticalGrid` + `LazyColumn` (no eager loading of items) |
| **Animation jank** | Compose animation layer runs on render thread; avoid recomposition during animation |
| **Memory** | Content loaded via Room DAOs with `Flow` — only what's observed |
| **Build time** | KSP Room processing runs incrementally; no full rebuilds needed |
| **APK size** | ~8MB release APK with ProGuard/R8 enabled |
