package `in`.hridayan.concretecalc.concrete.mix_design.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class MaterialBreakDownData(
    val icon: Painter,
    val iconTint : Color,
    val iconBg : Color,
    val title: String,
    val subtitle: String,
    val valueString: String
)