package `in`.hridayan.concretecalc.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.hridayan.concretecalc.core.common.CompositionLocals
import `in`.hridayan.concretecalc.core.presentation.AppUiEntry
import `in`.hridayan.concretecalc.core.presentation.ui.theme.ConcreteCalcTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CompositionLocals {
                ConcreteCalcTheme {
                    AppUiEntry()
                }
            }
        }
    }
}