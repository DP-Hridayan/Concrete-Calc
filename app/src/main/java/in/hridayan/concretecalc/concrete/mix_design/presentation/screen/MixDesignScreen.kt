@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.mix_design.presentation.screen

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.concrete.data.model.CementGrades
import `in`.hridayan.concretecalc.concrete.data.model.ConcreteType
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcreteApplication
import `in`.hridayan.concretecalc.concrete.data.model.ZonesOfFineAggregate
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.states.MixDesignScreenState
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.button.BackButton
import `in`.hridayan.concretecalc.core.presentation.components.card.IconWithTextCard
import `in`.hridayan.concretecalc.core.presentation.components.card.PillShapedCard
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText
import `in`.hridayan.concretecalc.navigation.LocalNavController
import `in`.hridayan.concretecalc.navigation.NavRoutes
import kotlinx.coroutines.launch

@Composable
fun MixDesignScreen(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val weakHaptic = LocalWeakHaptic.current
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    val states by viewModel.states.collectAsState()
    val listState = rememberLazyListState()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(states) {
        val firstErrorIndex = getFirstErrorIndex(states = states)
        if (firstErrorIndex != null) {
            listState.animateScrollToItem(firstErrorIndex)
        }
    }

    Scaffold(
        modifier = modifier,
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
                        text = stringResource(R.string.mix_design),
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
            ExtendedFloatingActionButton(
                onClick = {
                    if (viewModel.checkEmptyFields()) {
                        coroutineScope.launch {
                            val firstErrorIndex = getFirstErrorIndex(states = states)
                            if (firstErrorIndex != null) {
                                listState.animateScrollToItem(firstErrorIndex)
                            }
                        }
                        return@ExtendedFloatingActionButton
                    }
                    viewModel.calculate()
                    navController.navigate(NavRoutes.ResultsScreen)
                    weakHaptic()
                },
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_calculate),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                AutoResizeableText(stringResource(R.string.calculate))
            }

        }) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = innerPadding
        ) {
            item {
                IconWithTextCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    text = stringResource(R.string.enter_data_mix_design),
                    icon = painterResource(R.drawable.ic_info)
                )
            }

            item {
                AutoResizeableText(
                    stringResource(R.string.project_and_strength_requirements),
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                )
            }

            item {
                ProjectNameInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                )
            }

            item {
                ConcreteGradeDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                VolumeOfConcreteInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                )
            }

            item {
                TypeOfConcreteDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                ExposureEnvironmentDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                AutoResizeableText(
                    stringResource(R.string.material_properties_cement_and_water),
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                )
            }

            item {
                GradeOfCementDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpGravityCementInput(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    SpGravityWaterInput(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }

            item {
                AutoResizeableText(
                    stringResource(R.string.material_properties_aggregate),
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                )
            }

            item {
                NominalAggregateSizeDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                ZoneOfFineAggregateDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                SlumpSizeInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpGravityCoarseAggregateInput(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    SpGravityFineAggregateInput(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }

            item {
                WaterReductionSwitch(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 10.dp),
                )
            }

            item {
                if (states.isWaterReductionSwitchChecked) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        WaterReductionPercentageInput(modifier = Modifier.fillMaxWidth())
                        SpGravityOfAdmixture(modifier = Modifier.fillMaxWidth())
                        DosageOfAdmixture(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            item {
                TypeOfConcreteApplication(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                )
            }
        }
    }
}

@Composable
fun ProjectNameInputField(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        modifier = modifier,
        value = states.projectName.fieldValue,
        onValueChange = { viewModel.setProjectNameField(it) },
        label = { Text(stringResource(R.string.project_name)) })
}

@Composable
fun ConcreteGradeDropdown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.grade_of_concrete),
) {
    val weakHaptic = LocalWeakHaptic.current
    val grades by viewModel.grades.collectAsState()
    val states by viewModel.states.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = states.gradeOfConcrete.fieldValue,
            onValueChange = {},
            readOnly = true,
            isError = states.gradeOfConcrete.isError,
            label = {
                Text(
                    text = if (states.gradeOfConcrete.isError) states.gradeOfConcrete.errorMessage else label
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            grades.forEach { grade ->
                DropdownMenuItem(
                    text = { Text(grade.gradeName) },
                    onClick = {
                        weakHaptic()
                        viewModel.setGradeOfConcreteField(TextFieldValue(grade.gradeName))
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun VolumeOfConcreteInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.volume_of_concrete)
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.volumeOfConcrete.fieldValue,
        onValueChange = { input ->
            if (input.text.all { it.isDigit() }) {
                viewModel.setVolumeOfConcreteField(TextFieldValue(input.text))
            }
        },
        isError = states.volumeOfConcrete.isError,
        label = {
            Text(
                text = if (states.volumeOfConcrete.isError) states.volumeOfConcrete.errorMessage else label
            )
        },
        trailingIcon = {
            Text(
                text = "m³",
                modifier = Modifier.padding(end = 10.dp)
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun ExposureEnvironmentDropdown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.exposure_condition),
) {
    val weakHaptic = LocalWeakHaptic.current
    val exposureConditions by viewModel.exposureConditions.collectAsState()
    val states by viewModel.states.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = states.exposureCondition.fieldValue,
            onValueChange = {},
            readOnly = true,
            isError = states.exposureCondition.isError,
            label = {
                Text(
                    text = if (states.exposureCondition.isError) states.exposureCondition.errorMessage else label
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            exposureConditions.forEach { condition ->
                val environment = condition.environment
                DropdownMenuItem(
                    text = { Text(environment.envName) },
                    onClick = {
                        weakHaptic()
                        viewModel.setExposureEnvironmentField(environment)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}


@Composable
fun SlumpSizeInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.slump_value_in_mm)
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.slumpValue.fieldValue,
        onValueChange = { input ->
            if (input.text.all { it.isDigit() }) {
                viewModel.setSlumpValueField(TextFieldValue(input.text))
            }
        },
        isError = states.slumpValue.isError,
        label = {
            Text(
                text = if (states.slumpValue.isError) states.slumpValue.errorMessage else label,
                modifier = Modifier.basicMarquee()
            )
        },
        trailingIcon = {
            Text(
                text = "mm",
                modifier = Modifier.padding(end = 10.dp)
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun NominalAggregateSizeDropdown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.nominal_max_aggregate_size),
) {
    val weakHaptic = LocalWeakHaptic.current
    val sizes = listOf("10 mm", "20 mm", "40 mm")
    val states by viewModel.states.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = states.maxAggregateSize.fieldValue,
            onValueChange = {},
            readOnly = true,
            isError = states.maxAggregateSize.isError,
            label = {
                Text(
                    text = if (states.maxAggregateSize.isError) states.maxAggregateSize.errorMessage else label,
                    modifier = Modifier.basicMarquee()
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sizes.forEach { size ->
                DropdownMenuItem(
                    text = { Text(size) },
                    onClick = {
                        weakHaptic()
                        viewModel.setNominalMaxAggregateSizeField(TextFieldValue(size))
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}


@Composable
fun ZoneOfFineAggregateDropdown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.zone_of_fine_aggregate),
) {
    val weakHaptic = LocalWeakHaptic.current
    val zones = ZonesOfFineAggregate.entries
    val states by viewModel.states.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = states.zoneOfFineAggregate.fieldValue,
            onValueChange = {},
            readOnly = true,
            isError = states.zoneOfFineAggregate.isError,
            label = {
                Text(
                    text = if (states.zoneOfFineAggregate.isError) states.zoneOfFineAggregate.errorMessage else label,
                    modifier = Modifier.basicMarquee()
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            zones.forEach { zone ->
                DropdownMenuItem(
                    text = { Text(zone.zone) },
                    onClick = {
                        weakHaptic()
                        viewModel.setZoneOfFineAggregateField(zone)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun TypeOfConcreteDropdown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.type_of_concrete),
) {
    val weakHaptic = LocalWeakHaptic.current
    val types = ConcreteType.entries
    val states by viewModel.states.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = states.typeOfConcrete.fieldValue,
            onValueChange = {},
            readOnly = true,
            isError = states.typeOfConcrete.isError,
            label = {
                Text(
                    text = if (states.typeOfConcrete.isError) states.typeOfConcrete.errorMessage else label,
                    modifier = Modifier.basicMarquee()
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.type) },
                    onClick = {
                        weakHaptic()
                        viewModel.setTypeOfConcreteField(type)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun GradeOfCementDropdown(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
    label: String = stringResource(R.string.grade_of_cement),
) {
    val weakHaptic = LocalWeakHaptic.current
    val grades = CementGrades.entries
    val states by viewModel.states.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = states.gradeOfCement.fieldValue,
            onValueChange = {},
            readOnly = true,
            isError = states.gradeOfCement.isError,
            label = {
                Text(
                    text = if (states.gradeOfCement.isError) states.gradeOfCement.errorMessage
                    else label,
                    modifier = Modifier.basicMarquee()
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            grades.forEach { grade ->
                DropdownMenuItem(
                    text = { Text(grade.gradeName) },
                    onClick = {
                        weakHaptic()
                        viewModel.setGradeOfCementField(grade)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun SpGravityCementInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.spGravityOfCement.fieldValue,
        onValueChange = { viewModel.setSpGravityOfCement(it) },
        isError = states.spGravityOfCement.isError,
        label = {
            Text(
                text = if (states.spGravityOfCement.isError) states.spGravityOfCement.errorMessage
                else stringResource(R.string.sp_gravity_of_cement),
                modifier = Modifier.basicMarquee()
            )
        },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun SpGravityWaterInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.spGravityOfWater.fieldValue,
        onValueChange = { viewModel.setSpGravityOfWater(it) },
        isError = states.spGravityOfWater.isError,
        label = {
            Text(
                text = if (states.spGravityOfWater.isError) states.spGravityOfWater.errorMessage
                else stringResource(R.string.sp_gravity_of_water),
                modifier = Modifier.basicMarquee()
            )
        },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun SpGravityFineAggregateInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.spGravityOfFineAggregate.fieldValue,
        onValueChange = { viewModel.setSpGravityOfFineAggregate(it) },
        isError = states.spGravityOfFineAggregate.isError,
        label = {
            Text(
                text = if (states.spGravityOfFineAggregate.isError) states.spGravityOfFineAggregate.errorMessage
                else stringResource(R.string.sp_gravity_of_fine_aggregate),
                modifier = Modifier.basicMarquee()
            )
        },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun SpGravityCoarseAggregateInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.spGravityOfCoarseAggregate.fieldValue,
        onValueChange = { viewModel.setSpGravityOfCoarseAggregate(it) },
        isError = states.spGravityOfCoarseAggregate.isError,
        label = {
            Text(
                text = if (states.spGravityOfCoarseAggregate.isError) states.spGravityOfCoarseAggregate.errorMessage
                else stringResource(R.string.sp_gravity_of_coarse_aggregate),
                modifier = Modifier.basicMarquee()
            )
        },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun TypeOfConcreteApplication(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val weakHaptic = LocalWeakHaptic.current
    val options = TypeOfConcreteApplication.entries
    val states by viewModel.states.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        AutoResizeableText(
            modifier = Modifier.padding(top = 5.dp),
            text = stringResource(R.string.type_of_concrete_application),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMediumEmphasized
        )

        Row(horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)) {
            options.forEach { option ->
                ToggleButton(
                    checked = states.typeOfConcreteApplication == option,
                    onCheckedChange = {
                        weakHaptic()
                        viewModel.setTypeOfConcreteApplication(option)
                    },
                    modifier = Modifier.weight(1f),
                    shapes = when (option) {
                        TypeOfConcreteApplication.PUMPABLE -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        TypeOfConcreteApplication.NOT_PUMPABLE -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    }
                ) {
                    Icon(
                        imageVector = when (option) {
                            TypeOfConcreteApplication.PUMPABLE -> Icons.Rounded.Check
                            TypeOfConcreteApplication.NOT_PUMPABLE -> Icons.Rounded.Close
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(ToggleButtonDefaults.IconSpacing))
                    AutoResizeableText(option.type)
                }
            }
        }
    }
}

@Composable
fun WaterReductionSwitch(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()
    val weakHaptic = LocalWeakHaptic.current

    PillShapedCard(
        modifier = modifier,
        clickable = true,
        onClick = {
            viewModel.toggleWaterReductionSwitch()
            weakHaptic()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.water_reduction_needed),
                style = MaterialTheme.typography.bodyLargeEmphasized,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Switch(
                checked = states.isWaterReductionSwitchChecked,
                onCheckedChange = {
                    viewModel.toggleWaterReductionSwitch()
                    weakHaptic()
                },
                thumbContent = {
                    Icon(
                        imageVector = if (states.isWaterReductionSwitchChecked) Icons.Rounded.Check else Icons.Rounded.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                })
        }
    }
}

@Composable
fun WaterReductionPercentageInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.waterReductionPercentage.fieldValue,
        onValueChange = { viewModel.setWaterReductionPercentage(it) },
        isError = states.waterReductionPercentage.isError,
        label = {
            Text(
                text = if (states.waterReductionPercentage.isError) states.waterReductionPercentage.errorMessage
                else stringResource(R.string.water_reduction_percentage),
                modifier = Modifier.basicMarquee()
            )
        },
        trailingIcon = { Text("%") },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun SpGravityOfAdmixture(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.spGravityOfAdmixture.fieldValue,
        onValueChange = { viewModel.setSpGravityOfAdmixture(it) },
        isError = states.spGravityOfAdmixture.isError,
        label = {
            Text(
                text = if (states.spGravityOfAdmixture.isError) states.spGravityOfAdmixture.errorMessage
                else stringResource(R.string.sp_gravity_of_admixture),
                modifier = Modifier.basicMarquee()
            )
        },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun DosageOfAdmixture(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()

    OutlinedTextField(
        value = states.dosageOfAdmixture.fieldValue,
        onValueChange = { viewModel.setDosageOfAdmixture(it) },
        isError = states.dosageOfAdmixture.isError,
        label = {
            Text(
                text = if (states.dosageOfAdmixture.isError) states.dosageOfAdmixture.errorMessage
                else stringResource(R.string.dosage_of_admixture),
                modifier = Modifier.basicMarquee()
            )
        },
        trailingIcon = { Text("%") },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

private fun getFirstErrorIndex(states: MixDesignScreenState): Int? {
    return when {
        states.gradeOfConcrete.isError -> 3
        states.volumeOfConcrete.isError -> 4
        states.typeOfConcrete.isError -> 5
        states.exposureCondition.isError -> 6
        states.gradeOfCement.isError -> 8
        states.spGravityOfCement.isError -> 9
        states.spGravityOfWater.isError -> 9
        states.maxAggregateSize.isError -> 11
        states.zoneOfFineAggregate.isError -> 12
        states.slumpValue.isError -> 13
        states.spGravityOfCoarseAggregate.isError -> 14
        states.spGravityOfFineAggregate.isError -> 14
        states.waterReductionPercentage.isError -> 15
        states.spGravityOfAdmixture.isError -> 16
        states.dosageOfAdmixture.isError -> 17
        else -> null
    }
}
