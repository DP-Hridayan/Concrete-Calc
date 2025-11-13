@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.card.NavigationCard
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText
import `in`.hridayan.concretecalc.core.presentation.utils.ToastUtils.makeToast
import `in`.hridayan.concretecalc.navigation.LocalNavController
import `in`.hridayan.concretecalc.navigation.NavRoutes

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mixDesignViewModel: MixDesignViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val weakHaptic = LocalWeakHaptic.current
    val navController = LocalNavController.current
    val allRecentResults by mixDesignViewModel.allRecentResults.collectAsState()
    val savedResults by mixDesignViewModel.allSavedResults.collectAsState()
    val latestResults = allRecentResults.take(3)

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = innerPadding,
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AutoResizeableText(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displaySmallEmphasized,
                        modifier = Modifier.alpha(0.95f)
                    )
                }
            }

            item {
                NavigationCard(
                    title = stringResource(R.string.mix_design),
                    description = stringResource(R.string.des_mix_design),
                    icon = painterResource(R.drawable.ic_science),
                    onClick = { navController.navigate(NavRoutes.MixDesignScreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                )
            }
            /**
             *  item {
             *                 NavigationCard(
             *                     title = stringResource(R.string.cost_estimation),
             *                     description = stringResource(R.string.des_cost_estimation),
             *                     icon = painterResource(R.drawable.ic_calculate),
             *                     onClick = { },
             *                     modifier = Modifier
             *                         .fillMaxWidth()
             *                         .padding(horizontal = 25.dp)
             *                 )
             *             }
             */


            item {
                NavigationCard(
                    title = stringResource(R.string.saved_calculations),
                    description = stringResource(R.string.access_your_saved_calculations),
                    icon = painterResource(R.drawable.ic_inventory),
                    onClick = {
                        if (savedResults.isEmpty()) {
                            makeToast(context, context.getString(R.string.no_saved_calculations))
                            return@NavigationCard
                        }
                        mixDesignViewModel.setShowSaveButton(false)
                        navController.navigate(NavRoutes.SavedResultsScreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                )
            }

            item {
                if (latestResults.isNotEmpty())
                    AutoResizeableText(
                        text = stringResource(R.string.recent_activity),
                        style = MaterialTheme.typography.titleLargeEmphasized,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 25.dp, end = 25.dp)
                    )
            }

            itemsIndexed(latestResults) { i, result ->
                if (latestResults.isNotEmpty())
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                enabled = true,
                                onClick = {
                                    weakHaptic()
                                    mixDesignViewModel.setResultDataFromRecentData(result)
                                    mixDesignViewModel.setShowSaveButton(false)
                                    navController.navigate(NavRoutes.ResultsScreen)
                                }
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp, vertical = 15.dp),
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
                                    painter = painterResource(R.drawable.ic_history),
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