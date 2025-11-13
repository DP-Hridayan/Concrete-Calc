@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.saved_calculation.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.button.BackButton
import `in`.hridayan.concretecalc.core.presentation.components.card.RoundedCornerCard
import `in`.hridayan.concretecalc.core.presentation.components.shape.getRoundedShape
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText
import `in`.hridayan.concretecalc.navigation.LocalNavController
import `in`.hridayan.concretecalc.navigation.NavRoutes

@Composable
fun SavedResultsScreen(
    mixDesignViewModel: MixDesignViewModel = hiltViewModel()
) {
    val weakHaptic = LocalWeakHaptic.current
    val navController = LocalNavController.current
    val allResults by mixDesignViewModel.allSavedResults.collectAsState()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            LargeTopAppBar(
                title = {
                    val collapsedFraction = scrollBehavior.state.collapsedFraction
                    val expandedFontSize = 33.sp
                    val collapsedFontSize = 20.sp

                    val fontSize = lerp(expandedFontSize, collapsedFontSize, collapsedFraction)
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = stringResource(R.string.saved_calculations),
                        maxLines = 1,
                        fontSize = fontSize,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.05.em
                    )
                },
                navigationIcon = { BackButton() },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = innerPadding
        ) {
            itemsIndexed(allResults) { i, result ->
                val shape = getRoundedShape(i, allResults.size)

                RoundedCornerCard(
                    modifier = Modifier.fillMaxWidth(),
                    roundedCornerShape = shape,
                    onClick = {
                        weakHaptic()
                        mixDesignViewModel.setResultDataFromSavedData(result)
                        mixDesignViewModel.setShowSaveButton(false)
                        navController.navigate(NavRoutes.ResultsScreen)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_calculate),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = result.projectName,
                                style = MaterialTheme.typography.titleMediumEmphasized
                            )

                            AutoResizeableText(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.saved_on) + " ${result.saveDate}",
                                style = MaterialTheme.typography.bodySmallEmphasized,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                            )
                        }

                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_right),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}