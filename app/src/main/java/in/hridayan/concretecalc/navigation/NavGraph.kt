@file:OptIn(ExperimentalSharedTransitionApi::class)

package `in`.hridayan.concretecalc.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.screen.MixDesignScreen
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.screen.ResultsScreen
import `in`.hridayan.concretecalc.concrete.saved_calculation.presentation.screen.SavedResultsScreen
import `in`.hridayan.concretecalc.home.presentation.screen.HomeScreen

@Composable
fun SharedTransitionScope.Navigation(isFirstLaunch: Boolean = false) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HomeScreen
        ) {
            composable<NavRoutes.HomeScreen>(
                enterTransition = { slideFadeInFromRight() },
                exitTransition = { slideFadeOutToLeft() },
                popEnterTransition = { slideFadeInFromLeft() }
            ) {
                HomeScreen()
            }

            composable<NavRoutes.MixDesignScreen>(
                enterTransition = { slideFadeInFromRight() },
                exitTransition = { slideFadeOutToLeft() },
                popEnterTransition = { slideFadeInFromLeft() },
                popExitTransition = { slideFadeOutToRight() }
            ) {
                MixDesignScreen()
            }

            composable<NavRoutes.ResultsScreen>(
                enterTransition = { slideFadeInFromRight() },
                exitTransition = { slideFadeOutToLeft() },
                popEnterTransition = { slideFadeInFromLeft() },
                popExitTransition = { slideFadeOutToRight() }
            ) {
                ResultsScreen()
            }

            composable<NavRoutes.SavedResultsScreen>(
                enterTransition = { slideFadeInFromRight() },
                exitTransition = { slideFadeOutToLeft() },
                popEnterTransition = { slideFadeInFromLeft() },
                popExitTransition = { slideFadeOutToRight() }
            ) {
                SavedResultsScreen()
            }
        }
    }
}