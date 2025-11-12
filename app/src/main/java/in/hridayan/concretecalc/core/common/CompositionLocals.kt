package `in`.hridayan.concretecalc.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import `in`.hridayan.concretecalc.core.presentation.utils.HapticUtils.errorHaptic
import `in`.hridayan.concretecalc.core.presentation.utils.HapticUtils.strongHaptic
import `in`.hridayan.concretecalc.core.presentation.utils.HapticUtils.weakHaptic

val LocalWeakHaptic = staticCompositionLocalOf<() -> Unit> { {} }
val LocalStrongHaptic = staticCompositionLocalOf<() -> Unit> { {} }
val LocalErrorHaptic = staticCompositionLocalOf<() -> Unit> { {} }

@Composable
fun CompositionLocals(
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    val isHapticEnabled by rememberSaveable { mutableStateOf(true) }

    val weakHaptic = remember(isHapticEnabled, view) {
        {
            if (isHapticEnabled) {
                view.weakHaptic()
            }
        }
    }

    val strongHaptic = remember(isHapticEnabled, view) {
        {
            if (isHapticEnabled) {
                view.strongHaptic()
            }
        }
    }

    val errorHaptic = remember(isHapticEnabled, view) {
        {
            if (isHapticEnabled) {
                view.errorHaptic()
            }
        }
    }

    CompositionLocalProvider(
        LocalWeakHaptic provides weakHaptic,
        LocalStrongHaptic provides strongHaptic,
        LocalErrorHaptic provides errorHaptic
    ) {
        content()
    }
}