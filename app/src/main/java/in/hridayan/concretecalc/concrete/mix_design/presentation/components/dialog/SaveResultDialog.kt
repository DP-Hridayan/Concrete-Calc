@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.mix_design.presentation.components.dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.dialog.DialogContainer
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText

@Composable
fun SaveResultDialog(
    onDismiss: () -> Unit = {},
    onSave: (projectName: String) -> Unit = {},
    textInField: String = ""
) {
    val weakHaptic = LocalWeakHaptic.current
    val interactionSources = remember { List(2) { MutableInteractionSource() } }
    var text by remember { mutableStateOf(textInField) }

    DialogContainer(onDismiss = onDismiss) {
        AutoResizeableText(
            text = stringResource(R.string.save_result),
            style = MaterialTheme.typography.titleLargeEmphasized,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            isError = text.isEmpty(),
            label = {
                Text(
                    text = if (text.isEmpty()) stringResource(R.string.field_cannot_be_empty) else stringResource(
                        R.string.project_name
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        @Suppress("DEPRECATION")
        ButtonGroup(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = {
                    weakHaptic()
                    onDismiss()
                },
                shapes = ButtonDefaults.shapes(),
                modifier = Modifier
                    .weight(1f)
                    .animateWidth(interactionSources[0]),
                interactionSource = interactionSources[0],
            ) {
                AutoResizeableText(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Button(
                onClick = {
                    weakHaptic()
                    if (text.isEmpty()) return@Button

                    onSave(text)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .weight(1f)
                    .animateWidth(interactionSources[1]),
                interactionSource = interactionSources[1],
                shapes = ButtonDefaults.shapes(),
            ) {
                AutoResizeableText(
                    text = stringResource(R.string.save),
                )
            }
        }
    }
}