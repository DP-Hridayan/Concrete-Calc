@file:OptIn(ExperimentalSharedTransitionApi::class)

package `in`.hridayan.concretecalc.core.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import `in`.hridayan.concretecalc.navigation.Navigation

@Composable
fun AppUiEntry() {
    Surface {
        SharedTransitionLayout {
            Navigation(isFirstLaunch = false)
        }
    }
}