package com.hackerrank.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hackerrank.app.R
import com.hackerrank.app.core.LocaleManager
import com.hackerrank.app.core.ThemeManager
import com.hackerrank.app.ui.achievements.AchievementsScreen
import com.hackerrank.app.ui.browse.BrowseScreen
import com.hackerrank.app.ui.detail.DetailScreen
import com.hackerrank.app.ui.problems.ProblemDetailScreen
import com.hackerrank.app.ui.problems.ProblemsScreen
import com.hackerrank.app.ui.progress.ProgressScreen
import com.hackerrank.app.ui.quiz.QuizScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector) {
    data object Browse : Screen("browse", R.string.nav_structures, Icons.Default.Home)
    data object Problems : Screen("problems", R.string.nav_problems, Icons.Default.Code)
    data object Progress : Screen("progress", R.string.nav_progress, Icons.Default.Star)
    data object Achievements : Screen("achievements", R.string.nav_badges, Icons.Default.EmojiEvents)
}

sealed class DetailRoute(val route: String) {
    data object StructureDetail : DetailRoute("detail/{structureSlug}") {
        fun createRoute(slug: String) = "detail/$slug"
    }
    data object Quiz : DetailRoute("quiz/{structureSlug}") {
        fun createRoute(slug: String) = "quiz/$slug"
    }
    data object ProblemDetail : DetailRoute("problem/{problemId}") {
        fun createRoute(id: String) = "problem/$id"
    }
}

val bottomNavScreens = listOf(Screen.Browse, Screen.Problems, Screen.Progress, Screen.Achievements)

@Composable
fun NavGraph(
    themeManager: ThemeManager? = null,
    localeManager: LocaleManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                bottomNavScreens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.labelRes)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Browse.route
            ) {
                composable(Screen.Browse.route) {
                    BrowseScreen(
                        themeManager = themeManager,
                        localeManager = localeManager,
                        onStructureClick = { slug ->
                            navController.navigate(DetailRoute.StructureDetail.createRoute(slug))
                        },
                        onError = { msg ->
                            scope.launch { snackbarHostState.showSnackbar(msg) }
                        }
                    )
                }

                composable(Screen.Problems.route) {
                    ProblemsScreen(
                        onProblemClick = { id ->
                            navController.navigate(DetailRoute.ProblemDetail.createRoute(id))
                        },
                        onError = { msg ->
                            scope.launch { snackbarHostState.showSnackbar(msg) }
                        }
                    )
                }

                composable(Screen.Progress.route) {
                    ProgressScreen(
                        onError = { msg ->
                            scope.launch { snackbarHostState.showSnackbar(msg) }
                        }
                    )
                }

                composable(Screen.Achievements.route) {
                    AchievementsScreen(
                        onError = { msg ->
                            scope.launch { snackbarHostState.showSnackbar(msg) }
                        }
                    )
                }

                composable(
                    route = DetailRoute.StructureDetail.route,
                    arguments = listOf(navArgument("structureSlug") { type = NavType.StringType })
                ) { backStackEntry ->
                    val slug = backStackEntry.arguments?.getString("structureSlug") ?: return@composable
                    DetailScreen(
                        structureSlug = slug,
                        onBackClick = { navController.popBackStack() },
                        onQuizClick = { navController.navigate(DetailRoute.Quiz.createRoute(slug)) }
                    )
                }

                composable(
                    route = DetailRoute.Quiz.route,
                    arguments = listOf(navArgument("structureSlug") { type = NavType.StringType })
                ) { backStackEntry ->
                    val slug = backStackEntry.arguments?.getString("structureSlug") ?: return@composable
                    QuizScreen(
                        structureSlug = slug,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(
                    route = DetailRoute.ProblemDetail.route,
                    arguments = listOf(navArgument("problemId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val problemId = backStackEntry.arguments?.getString("problemId") ?: return@composable
                    ProblemDetailScreen(
                        problemId = problemId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
