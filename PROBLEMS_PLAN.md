# Problems Feature — Implementation Plan

## Overview
Add a 4th bottom nav tab "Problems" with 100 algorithm problems, Kotlin solutions, approach explanations, difficulty info, and gamification (XP awards).

## Current State Analysis
- 3 bottom nav tabs: Browse, Progress, Badges
- Room DB with 4 entities (`data_structures`, `quiz_questions`, `user_progress`, `user_profile`)
- Seed data for 16 structures + quiz questions
- `GamificationEngine` awards XP for quizzes and login streaks
- Navigation uses sealed `Screen` and `DetailRoute` classes
- `HackerRankDatabase` version 1 with `fallbackToDestructiveMigration()`

## Desired End State
- New bottom nav tab "Problems" with a `Code` icon
- `ProblemsScreen` listing 100 problems, filterable by difficulty/category
- `ProblemDetailScreen` showing problem → approach → solution code (toggleable)
- "Mark as Solved" button awarding XP (Easy=10, Medium=25, Hard=50)
- Problems stored in Room, seeded on first launch alongside existing data
- Solved state persisted across sessions

## What We're NOT Doing
- No code execution in-app (static solutions only, like existing code examples)
- No tying problems to specific data structures (general algorithm problems)
- No new badge types for problem-solving (reuses existing streak/level badges)
- No search bar (filter chips only to keep scope manageable)
- No migration script (using `fallbackToDestructiveMigration()` — fine in development)

## Implementation Approach

### Phase 1 — Data Layer

1. **Create** `data/local/entity/ProblemEntity.kt`
   - `id` (String, PK), `title`, `description`, `solutionCode`, `approachExplanation`, `difficulty` (String), `category` (String), `orderIndex` (Int)

2. **Create** `data/local/entity/SolvedProblemEntity.kt`
   - `problemId` (String, PK), `solvedAt` (Long timestamp)

3. **Create** `data/local/dao/ProblemDao.kt`
   - `getAllProblems(): Flow<List<ProblemEntity>>`
   - `getProblemsByCategory(category): Flow<List<ProblemEntity>>`
   - `getProblemById(id): Flow<ProblemEntity?>`
   - `insertAll(problems): suspend`
   - `count(): Flow<Int>`

4. **Create** `data/local/dao/SolvedProblemDao.kt`
   - `getSolvedIds(): Flow<Set<String>>`
   - `isSolved(problemId): Flow<Boolean>`
   - `insert(solved): suspend`

5. **Edit** `data/local/HackerRankDatabase.kt`
   - Add `ProblemEntity` and `SolvedProblemEntity` to entities list
   - Add abstract DAO methods for both
   - Bump version to 2

6. **Edit** `data/local/DatabaseModule.kt`
   - Add `@Provides` functions for both new DAOs

7. **Create** `domain/model/Problem.kt`
   - `Problem` data class with same fields + `Difficulty` enum + `ProblemCategory` enum
   - `ProblemCategory` enum: `ARRAYS`, `STRINGS`, `LINKED_LISTS`, `STACKS_QUEUES`, `TREES`, `GRAPHS`, `DYNAMIC_PROGRAMMING`, `SORTING_SEARCHING`, `HASH_BASED`, `RECURSION_MATH`, `BIT_MANIPULATION`, `MISCELLANEOUS`

8. **Create** `domain/repository/ProblemRepository.kt` (interface)
   - `getAllProblems(): Flow<List<Problem>>`
   - `getProblemById(id): Flow<Problem?>`
   - `getSolvedIds(): Flow<Set<String>>`
   - `markAsSolved(problemId): suspend`

9. **Create** `data/repository/ProblemRepositoryImpl.kt`
   - Maps `ProblemEntity` → `Problem` domain model
   - Wraps `ProblemDao` + `SolvedProblemDao`

10. **Edit** `di/RepositoryModule.kt`
    - Add `@Binds` for `ProblemRepository` → `ProblemRepositoryImpl`

11. **Create** `data/seed/ProblemSeedData.kt`
    - 100 problems across 12 categories:
      - Arrays (12), Strings (12), Linked Lists (8), Stacks & Queues (8)
      - Trees (10), Graphs (8), Dynamic Programming (10), Sorting & Searching (8)
      - Hash-Based (6), Recursion & Math (8), Bit Manipulation (5), Miscellaneous (5)

12. **Edit** `data/seed/SeedInitializer.kt`
    - After seeding structures, also seed problems if `problemDao.count()` == 0

### Phase 2 — Domain & Gamification

1. **Edit** `core/Constants.kt`
   - Add: `EASY_PROBLEM_XP = 10`, `MEDIUM_PROBLEM_XP = 25`, `HARD_PROBLEM_XP = 50`

2. **Edit** `domain/gamification/GamificationEngine.kt`
   - Add `recordProblemSolved(difficulty: String): GamificationResult`
   - Awards XP by difficulty, updates streak, evaluates level-up badges
   - Returns `GamificationResult` with XP info

3. **Create** `domain/usecase/RecordProblemSolveUseCase.kt`
   - Calls `problemRepository.markAsSolved()`
   - Calls `gamificationEngine.recordProblemSolved()`
   - Updates `UserProgress` in `progressRepository`

### Phase 3 — UI Layer

1. **Create** `ui/problems/ProblemsScreen.kt`
   - Filter chips row (All, Easy, Medium, Hard + horizontal category chips)
   - `LazyColumn` of problem cards
   - Each card: title, difficulty badge (color-coded), category chip, solved checkmark (green)
   - Card click navigates to `problem/{problemId}`

2. **Create** `ui/problems/ProblemsViewModel.kt`
   - Injects `ProblemRepository`
   - State: `allProblems`, `filteredProblems`, `selectedDifficulty`, `selectedCategory`, `solvedIds`, `isLoading`
   - Filter logic: combine difficulty + category selection
   - Solved check powered by `solvedIds` flow

3. **Create** `ui/problems/ProblemDetailScreen.kt`
   - Top bar with back arrow + problem title
   - Difficulty badge + category chip
   - "Problem" section: description text
   - "Approach" section: algorithm explanation with bullet points or paragraphs
   - "Solution" section: code block styled text, with copy-to-clipboard button
   - "Mark as Solved" button (primary color, disabled if already solved)
   - Shows snackbar with XP awarded on solve

4. **Create** `ui/problems/ProblemDetailViewModel.kt`
   - Injects `ProblemRepository` + `RecordProblemSolveUseCase`
   - Loads problem by ID, observes solved status
   - `solve()` calls use case, emits result for snackbar

5. **Edit** `core/navigation/NavGraph.kt`
   - Add `Screen.Problems` to sealed class: `"problems"`, label `"Problems"`, icon `Icons.Default.Code`
   - Add `DetailRoute.ProblemDetail`: `"problem/{problemId}"`
   - Add to `bottomNavScreens` (now 4 tabs)
   - Add composable for `Screen.Problems.route` → `ProblemsScreen`
   - Add composable for `DetailRoute.ProblemDetail.route` → `ProblemDetailScreen`

## Phases Summary

| Phase | What | Files |
|-------|------|-------|
| 1 | Data layer (entities, DAOs, DB, repository, seed) | 10 create, 4 edit |
| 2 | Domain + gamification (use case, engine update) | 1 create, 2 edit |
| 3 | UI (screens, viewmodels, navigation) | 4 create, 1 edit |
| 4 | Content (100 problems in seed data) | embedded in Phase 1 |

## Files Changed (Complete List)

### Create (15 files)
- `data/local/entity/ProblemEntity.kt`
- `data/local/entity/SolvedProblemEntity.kt`
- `data/local/dao/ProblemDao.kt`
- `data/local/dao/SolvedProblemDao.kt`
- `domain/model/Problem.kt`
- `domain/repository/ProblemRepository.kt`
- `data/repository/ProblemRepositoryImpl.kt`
- `domain/usecase/RecordProblemSolveUseCase.kt`
- `data/seed/ProblemSeedData.kt` (100 problems + solutions)
- `ui/problems/ProblemsScreen.kt`
- `ui/problems/ProblemsViewModel.kt`
- `ui/problems/ProblemDetailScreen.kt`
- `ui/problems/ProblemDetailViewModel.kt`

### Edit (7 files)
- `data/local/HackerRankDatabase.kt` — add entities, DAOs, version 2
- `data/local/DatabaseModule.kt` — add DAO provides
- `di/RepositoryModule.kt` — bind ProblemRepository
- `data/seed/SeedInitializer.kt` — seed problems
- `core/Constants.kt` — problem XP constants
- `domain/gamification/GamificationEngine.kt` — recordProblemSolved()
- `core/navigation/NavGraph.kt` — add tab + detail route

## Testing Strategy
1. `assembleDebug` — must succeed with zero warnings
2. Install on emulator — verify 4th "Problems" tab appears
3. Tap Problems tab — 100 problems load, filter chips work
4. Tap a problem — detail screen shows description, approach, solution
5. Tap "Mark as Solved" — button disables, snackbar shows XP awarded
6. Navigate to Progress tab — verify XP increased
7. Kill and reopen app — solved state persists, button stays disabled

## Performance Considerations
- 100 rows in Room is negligible — no indexing needed
- `LazyColumn` handles list efficiently
- Filtering done client-side in ViewModel (instant, no re-query)
- No network calls — fully offline
- Single `insert` per problem solved — minimal write overhead
