@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.mix_design.presentation.components.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.dialog.DialogContainer
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText

@Composable
fun CostEstimationWarningDialog(onDismiss: () -> Unit = {}) {
    val weakHaptic = LocalWeakHaptic.current

    DialogContainer(onDismiss = onDismiss) {
        AutoResizeableText(
            text = stringResource(R.string.warning),
            style = MaterialTheme.typography.titleLargeEmphasized,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = stringResource(R.string.cost_estimation_warning),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextButton(
            onClick = {
                weakHaptic()
                onDismiss()
            },
            modifier = Modifier.align(Alignment.End)
        ) { Text(stringResource(R.string.ok)) }
    }
}