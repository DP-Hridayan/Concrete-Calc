@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.mix_design.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignRecentResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResult
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignSavedResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.components.dialog.CostEstimationWarningDialog
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.components.dialog.SaveResultDialog
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.model.MaterialBreakDownData
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.model.MaterialCostEstimate
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.button.BackButton
import `in`.hridayan.concretecalc.core.presentation.components.card.RoundedCornerCard
import `in`.hridayan.concretecalc.core.presentation.components.chart.PieChart
import `in`.hridayan.concretecalc.core.presentation.components.shape.getRoundedShape
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText
import `in`.hridayan.concretecalc.core.presentation.model.PieChartData
import `in`.hridayan.concretecalc.core.presentation.utils.ToastUtils.makeToast
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ResultsScreen(viewModel: MixDesignViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val results by viewModel.mixResult.collectAsState(initial = MixDesignResult())
    val coroutineScope = rememberCoroutineScope()
    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

    val saveEntity = MixDesignSavedResultEntity(
        volumeOfConcrete = results?.volumeOfConcrete ?: 0.00,
        concreteGrade = results?.concreteGrade ?: "",
        cementGrade = results?.cementGrade ?: CementGrades.OPC_43,
        maxAggregateSize = results?.maxAggregateSize ?: 0,
        cementContentWithAdmixture = results?.cementContentWithAdmixture ?: 0.00,
        cementContentWithoutAdmixture = results?.cementContentWithoutAdmixture ?: 0.00,
        finalWaterInKg = results?.finalWaterInKg ?: 0.00,
        finalWaterVolume = results?.finalWaterVolume ?: 0.00,
        finalCementInKg = results?.finalCementInKg ?: 0.00,
        finalCementVolume = results?.finalCementVolume ?: 0.00,
        finalCoarseAggregateInKg = results?.finalCoarseAggregateInKg ?: 0.00,
        finalCoarseAggregateVolume = results?.finalCoarseAggregateVolume ?: 0.00,
        finalFineAggregateInKg = results?.finalFineAggregateInKg ?: 0.00,
        finalFineAggregateVolume = results?.finalFineAggregateVolume ?: 0.00,
        finalAdmixtureContent = results?.finalAdmixtureContent ?: 0.00,
        mixProportion = results?.mixProportion ?: "",
    )

    val weakHaptic = LocalWeakHaptic.current
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var showCostEstimationWarningDialog by rememberSaveable { mutableStateOf(false) }
    var showSaveResultDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!viewModel.getShowSaveButton()) return@LaunchedEffect
        Log.d("launch", "${viewModel.getShowSaveButton()}")

        val res = results
        if (res?.mixProportion?.isNotEmpty() == true) {
            val projectName = results?.projectName ?: ""

            val recentEntity = MixDesignRecentResultEntity(
                projectName = projectName.ifEmpty { "${results?.concreteGrade} - $currentTime" },
                volumeOfConcrete = results?.volumeOfConcrete ?: 0.00,
                concreteGrade = results?.concreteGrade ?: "",
                cementGrade = results?.cementGrade ?: CementGrades.OPC_43,
                maxAggregateSize = results?.maxAggregateSize ?: 0,
                cementContentWithAdmixture = results?.cementContentWithAdmixture ?: 0.00,
                cementContentWithoutAdmixture = results?.cementContentWithoutAdmixture ?: 0.00,
                finalWaterInKg = results?.finalWaterInKg ?: 0.00,
                finalWaterVolume = results?.finalWaterVolume ?: 0.00,
                finalCementInKg = results?.finalCementInKg ?: 0.00,
                finalCementVolume = results?.finalCementVolume ?: 0.00,
                finalCoarseAggregateInKg = results?.finalCoarseAggregateInKg ?: 0.00,
                finalCoarseAggregateVolume = results?.finalCoarseAggregateVolume ?: 0.00,
                finalFineAggregateInKg = results?.finalFineAggregateInKg ?: 0.00,
                finalFineAggregateVolume = results?.finalFineAggregateVolume ?: 0.00,
                finalAdmixtureContent = results?.finalAdmixtureContent ?: 0.00,
                mixProportion = results?.mixProportion ?: "",
            )

            coroutineScope.launch { viewModel.saveRecentMixDesignResult(recentEntity) }
        }
    }

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
        floatingActionButton = {
            if (viewModel.getShowSaveButton())
                ExtendedFloatingActionButton(
                    onClick = {
                        showSaveResultDialog = true
                        weakHaptic()
                    },
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_save),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AutoResizeableText(stringResource(R.string.save))
                }

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
                AutoResizeableText(
                    text = stringResource(R.string.material_breakdown),
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 15.dp)
                )
            }

            item {
                MaterialBreakDown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AutoResizeableText(
                        text = stringResource(R.string.cost_estimate),
                        style = MaterialTheme.typography.titleLargeEmphasized,
                        fontWeight = FontWeight.Bold,
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                enabled = true,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    weakHaptic()
                                    showCostEstimationWarningDialog = true
                                }),
                    )
                }
            }

            item {
                CostEstimate(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )
            }
        }
    }

    if (showCostEstimationWarningDialog) {
        CostEstimationWarningDialog(onDismiss = { showCostEstimationWarningDialog = false })
    }

    if (showSaveResultDialog) {
        SaveResultDialog(
            onDismiss = { showSaveResultDialog = false },
            onSave = { projectName ->
                coroutineScope.launch {
                    val saved =
                        viewModel.saveMixDesignResult(saveEntity.copy(projectName = projectName))
                    if (saved) makeToast(context, context.getString(R.string.success))
                    else makeToast(context, context.getString(R.string.failed))
                }
            },
            textInField = results?.projectName ?: ""
        )
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
            results?.finalCementInKg?.toFloat() ?: 2f,
            Color(0xFFA5A5A5)
        ),
        PieChartData(
            stringResource(R.string.fine),
            results?.finalFineAggregateInKg?.toFloat() ?: 3f,
            Color(0xFFEADDAF)
        ),
        PieChartData(
            stringResource(R.string.coarse),
            results?.finalCoarseAggregateInKg?.toFloat() ?: 4f,
            Color(0xFF8B7E74)
        ),
        PieChartData(
            stringResource(R.string.water),
            results?.finalWaterInKg?.toFloat() ?: 2f,
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
    val weakHaptic = LocalWeakHaptic.current
    val results by viewModel.mixResult.collectAsState(initial = MixDesignResult())
    val options = listOf(stringResource(R.string.volume), stringResource(R.string.weight))
    var checked by rememberSaveable { mutableStateOf(0) }

    val materials = results?.finalCementInKg?.toInt()?.let {
        listOf(
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_shopping_bag),
                iconBg = Color(0xFFA5A5A5),
                iconTint = Color(0xFFFFFFFF),
                title = stringResource(R.string.cement),
                subtitle = results?.cementGrade?.gradeName ?: CementGrades.OPC_33.gradeName,
                valueString = if (checked == 0) {
                    ((it.plus(49)) / 50).toString() + " bags"
                } else String.format("%.1f kg", results?.finalCementInKg ?: 0.0)
            ),
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_grain),
                iconBg = Color(0xFFEADDAF),
                iconTint = Color(0xFF5E5846),
                title = stringResource(R.string.sand),
                subtitle = stringResource(R.string.fine_aggregate),
                valueString = if (checked == 0) {
                    String.format("%.1f m³", results?.finalFineAggregateVolume ?: 0.0)
                } else {
                    String.format("%.1f kg", results?.finalFineAggregateInKg ?: 0.0)
                }
            ),
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_landscape),
                iconBg = Color(0xFF8B7E74),
                iconTint = Color(0xFFFFFFFF),
                title = stringResource(R.string.gravel),
                subtitle = stringResource(R.string.coarse_aggregate),
                valueString = if (checked == 0) {
                    String.format("%.1f m³", results?.finalCoarseAggregateVolume ?: 0.0)
                } else {
                    String.format("%.1f kg", results?.finalCoarseAggregateInKg ?: 0.0)
                }
            ),
            MaterialBreakDownData(
                icon = painterResource(R.drawable.ic_water_drop),
                iconBg = Color(0xFFAADAFF),
                iconTint = Color(0xFF4E6375),
                title = stringResource(R.string.water),
                subtitle = stringResource(R.string.clean_potable),
                valueString = if (checked == 0) {
                    String.format("%.1f litres", results?.finalWaterVolume ?: 0.0)
                } else {
                    String.format("%.1f kg", results?.finalWaterInKg ?: 0.0)
                }
            ),
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
        ) {
            options.forEachIndexed { i, option ->
                ToggleButton(
                    checked = i == checked,
                    onCheckedChange = {
                        weakHaptic()
                        checked = i
                    },
                    modifier = Modifier.weight(1f),
                    shapes = when (i) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        1 -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    }
                ) {
                    AutoResizeableText(option)
                }
            }
        }

        materials?.forEachIndexed { i, material ->
            val cornerShape = getRoundedShape(i, materials.size)

            RoundedCornerCard(
                modifier = Modifier.fillMaxWidth(),
                roundedCornerShape = cornerShape,
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

@Composable
fun CostEstimate(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val results by viewModel.mixResult.collectAsState(initial = MixDesignResult())

    val cementBagsNo = (results?.finalCementInKg?.plus(49))?.div(50) ?: 0.00
    val cementPrice =
        viewModel.calculateCementPrice(results?.cementGrade ?: CementGrades.OPC_33, cementBagsNo)

    val gravelVolume = results?.finalCoarseAggregateVolume ?: 0.00
    val size = results?.maxAggregateSize ?: 0
    val gravelPrice = viewModel.calculateGravelPrice(size = size, volume = gravelVolume)

    val sandPrice = results?.let { it.finalFineAggregateVolume * 2000.00 } ?: 0.00

    val totalEstimatedCost = cementPrice + sandPrice + gravelPrice

    val costs = listOf(
        MaterialCostEstimate(title = stringResource(R.string.cement_cost), cost = cementPrice),
        MaterialCostEstimate(title = stringResource(R.string.sand_cost), cost = sandPrice),
        MaterialCostEstimate(title = stringResource(R.string.gravel_cost), cost = gravelPrice),
        MaterialCostEstimate(
            title = stringResource(R.string.total_estimated_cost),
            cost = totalEstimatedCost
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        costs.forEachIndexed { i, cost ->
            val cornerShape = getRoundedShape(i, costs.size)

            val cardColors = if (i == costs.lastIndex) {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            } else {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            }

            RoundedCornerCard(
                modifier = Modifier.fillMaxWidth(),
                roundedCornerShape = cornerShape,
                colors = cardColors,
                paddingValues = PaddingValues(vertical = 1.dp, horizontal = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    AutoResizeableText(
                        text = cost.title,
                        style = MaterialTheme.typography.titleMediumEmphasized,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    AutoResizeableText(
                        text = "₹ %.2f".format(cost.cost),
                        style = MaterialTheme.typography.titleMediumEmphasized,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

