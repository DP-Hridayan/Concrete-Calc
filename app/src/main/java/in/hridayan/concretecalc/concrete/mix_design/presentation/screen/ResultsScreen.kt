@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.mix_design.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.concrete.data.model.CementGrades
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResult
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.model.MaterialBreakDownData
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.button.BackButton
import `in`.hridayan.concretecalc.core.presentation.components.card.RoundedCornerCard
import `in`.hridayan.concretecalc.core.presentation.components.chart.PieChart
import `in`.hridayan.concretecalc.core.presentation.components.shape.getRoundedShape
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText
import `in`.hridayan.concretecalc.core.presentation.model.PieChartData

@Composable
fun ResultsScreen(viewModel: MixDesignViewModel = hiltViewModel()) {
    val results by viewModel.mixResult.collectAsState(initial = MixDesignResult())

    val weakHaptic = LocalWeakHaptic.current
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
                        text = stringResource(R.string.calculation_results),
                        maxLines = 1,
                        fontSize = fontSize,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.05.em
                    )
                },
                navigationIcon = { BackButton() },
                scrollBehavior = scrollBehavior,

                )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = innerPadding
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.volume_of_concrete_based_on_input),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                    AutoResizeableText(
                        text = "${results?.volumeOfConcrete} m³",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.displayMediumEmphasized,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                MixProportionPie(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp)
                )
            }

            item {
                MaterialBreakDown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp)
                )
            }
        }
    }
}


@Composable
fun MixProportionPie(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val results by viewModel.mixResult.collectAsState(initial = MixDesignResult())

    val chartData = listOf(
        PieChartData(
            stringResource(R.string.cement),
            results?.finalCementContent?.toFloat() ?: 2f,
            Color(0xFFA5A5A5)
        ),
        PieChartData(
            stringResource(R.string.fine),
            results?.finalFineAggregateContent?.toFloat() ?: 3f,
            Color(0xFFEADDAF)
        ),
        PieChartData(
            stringResource(R.string.coarse),
            results?.finalCoarseAggregateContent?.toFloat() ?: 4f,
            Color(0xFF8B7E74)
        ),
        PieChartData(
            stringResource(R.string.water),
            results?.finalWaterContent?.toFloat() ?: 2f,
            Color(0xFFAADAFF)
        )
    )

    val total = chartData.sumOf { it.value.toDouble() }.toFloat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PieChart(data = chartData, modifier = Modifier.size(200.dp))
            Box(
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    results?.concreteGrade?.let {
                        AutoResizeableText(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLargeEmphasized
                        )
                    }
                    results?.mixProportion?.let {
                        AutoResizeableText(
                            text = it,
                            style = MaterialTheme.typography.bodySmallEmphasized
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(25.dp),
            maxItemsInEachRow = 2,
            maxLines = 2,
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            chartData.forEach { item ->
                val percentage = (item.value / total) * 100

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        Modifier
                            .size(16.dp)
                            .background(item.color, CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    Text(
                        text = "${"%.1f".format(percentage)}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun MaterialBreakDown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val results by viewModel.mixResult.collectAsState(initial = MixDesignResult())

    val materials = results?.finalCementContent?.toInt()?.let {
        listOf(
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_shopping_bag),
                iconBg = Color(0xFFA5A5A5),
                iconTint = Color(0xFFFFFFFF),
                title = stringResource(R.string.cement),
                subtitle = results?.cementGrades?.gradeName ?: CementGrades.OPC_33.gradeName,
                valueString = ((it.plus(49)) / 50).toString() + " bags"
            ),
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_grain),
                iconBg = Color(0xFFEADDAF),
                iconTint = Color(0xFF5E5846),
                title = stringResource(R.string.sand),
                subtitle = stringResource(R.string.fine_aggregate),
                valueString = "${results?.finalFineAggregateContent?.toInt()} kg"
            ),
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_landscape),
                iconBg = Color(0xFF8B7E74),
                iconTint = Color(0xFFFFFFFF),
                title = stringResource(R.string.gravel),
                subtitle = stringResource(R.string.coarse_aggregate),
                valueString = "${results?.finalCoarseAggregateContent?.toInt()} kg"
            ),
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_water_drop),
                iconBg = Color(0xFFAADAFF),
                iconTint = Color(0xFF4E6375),
                title = stringResource(R.string.water),
                subtitle = stringResource(R.string.clean_potable),
                valueString = "${results?.finalWaterContent?.toInt()} litres"
            ),
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        materials?.forEachIndexed { i, material ->
            val cornerShape = getRoundedShape(i, materials.size)

            RoundedCornerCard(
                modifier = Modifier.fillMaxWidth(),
                roundedShape = cornerShape,
                paddingValues = PaddingValues(vertical = 1.dp, horizontal = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(material.iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = material.icon,
                            tint = material.iconTint,
                            contentDescription = null
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = material.title,
                            style = MaterialTheme.typography.titleMediumEmphasized
                        )
                        Text(
                            text = material.subtitle,
                            style = MaterialTheme.typography.labelSmallEmphasized,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                        )
                    }

                    AutoResizeableText(
                        text = material.valueString,
                        style = MaterialTheme.typography.titleLargeEmphasized,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

