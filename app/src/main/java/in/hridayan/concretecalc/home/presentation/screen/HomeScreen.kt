@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.core.presentation.components.card.NavigationCard
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText
import `in`.hridayan.concretecalc.navigation.LocalNavController
import `in`.hridayan.concretecalc.navigation.NavRoutes

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = LocalNavController.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(25.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = innerPadding,
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp),
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
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                NavigationCard(
                    title = stringResource(R.string.cost_estimation),
                    description = stringResource(R.string.des_cost_estimation),
                    icon = painterResource(R.drawable.ic_calculate),
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}