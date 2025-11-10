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

@Composable
fun SharedTransitionScope.Navigation(isFirstLaunch: Boolean = false) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.MixDesignScreen
        ) {
            composable<NavRoutes.MixDesignScreen>(
                enterTransition = { slideFadeInFromRight() },
                popExitTransition = { slideFadeOutToRight() }
            ) {
                MixDesignScreen()
            }

            composable<NavRoutes.ResultsScreen>(
                enterTransition = { slideFadeInFromRight() },
                popExitTransition = { slideFadeOutToRight() }
            ) {
                ResultsScreen()
            }
        }
    }
}