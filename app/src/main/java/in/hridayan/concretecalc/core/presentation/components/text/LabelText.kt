package `in`.hridayan.concretecalc.core.presentation.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LabelText(text: String, modifier: Modifier = Modifier) {
    AutoResizeableText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
}
