@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package `in`.hridayan.concretecalc.concrete.mix_design.presentation.screen

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import `in`.hridayan.concretecalc.concrete.data.model.SpGravities
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcreteApplication
import `in`.hridayan.concretecalc.concrete.data.model.ZonesOfFineAggregate
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.button.BackButton
import `in`.hridayan.concretecalc.core.presentation.components.card.IconWithTextCard
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText

@Composable
fun MixDesignScreen(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val weakHaptic = LocalWeakHaptic.current
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var shouldShowResults by rememberSaveable() { mutableStateOf(false) }

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
                        modifier = modifier.basicMarquee(),
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
                    viewModel.calculate()
                    shouldShowResults = true
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
                ConcreteGradeDropdown(
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
                SlumpSizeInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
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
                TypeOfConcreteDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
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
                SpGravityInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                )
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
                if (shouldShowResults) {
                    Results(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                    )
                }
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
fun Results(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val result by viewModel.mixResult.collectAsState(null)

    result?.let {
        Text(
            text = "Result: ${it.valueOfX}" +
                    "Result2: ${it.standardDeviation}" +
                    "Result3: ${it.targetStrength}" +
                    "Result4: ${it.maxWaterCementRatio}" +
                    "Result5: ${it.freeWaterCementRatio}" +
                    "Result6: ${it.maxWaterContentForNominalSizeAnd50Slump}" +
                    "Result7: ${it.changeInWaterContentPercentDueToSlump}" +
                    "Result8: ${it.waterContentForGivenSlump}",
            modifier = modifier
        )
    }

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
            label = { Text(label) },
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
            label = { Text(label) },
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
        label = {
            Text(
                text = label,
                modifier = Modifier.basicMarquee()
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
            label = {
                Text(
                    text = label,
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
            label = {
                Text(
                    text = label,
                    modifier = Modifier.basicMarquee()
                )
            },
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_info),
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        enabled = true,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            weakHaptic()
                        })
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
            label = {
                Text(
                    text = label,
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
            label = {
                Text(
                    text = label,
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
fun SpGravityInput(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel(),
) {
    val labels = listOf(
        stringResource(R.string.sp_gravity_of_water),
        stringResource(R.string.sp_gravity_of_cement),
        stringResource(R.string.sp_gravity_of_fine_aggregate),
        stringResource(R.string.sp_gravity_of_coarse_aggregate)
    )

    val list = SpGravities.entries

    val states by viewModel.states.collectAsState()



    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AutoResizeableText(
            modifier = Modifier
                .padding(start = 5.dp, top = 10.dp)
                .offset(y = 10.dp),
            text = stringResource(R.string.specific_gravity),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMediumEmphasized
        )

        list.forEachIndexed { index, gravity ->
            val fieldValue = when (gravity) {
                SpGravities.WATER -> states.spGravityOfWater.fieldValue
                SpGravities.CEMENT -> states.spGravityOfCement.fieldValue
                SpGravities.FINE_AGGREGATE -> states.spGravityOfFineAggregate.fieldValue
                SpGravities.COARSE_AGGREGATE -> states.spGravityOfCoarseAggregate.fieldValue
            }

            val updateValues: (fieldValue: TextFieldValue) -> Unit = {
                when (gravity) {
                    SpGravities.WATER -> viewModel.setSpGravityOfWater(it)
                    SpGravities.CEMENT -> viewModel.setSpGravityOfCement(it)
                    SpGravities.FINE_AGGREGATE -> viewModel.setSpGravityOfFineAggregate(it)
                    SpGravities.COARSE_AGGREGATE -> viewModel.setSpGravityOfCoarseAggregate(it)
                }
            }

            OutlinedTextField(
                value = fieldValue,
                onValueChange = { input ->
                    if (input.text.isEmpty() || input.text.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        updateValues(input)
                    }
                },
                label = {
                    Text(
                        text = labels.getOrNull(index) ?: "Specific Gravity",
                        modifier = Modifier.basicMarquee()
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

        }
    }
}

@Composable
fun TypeOfConcreteApplication(
    modifier: Modifier = Modifier,
    viewModel: MixDesignViewModel = hiltViewModel()
) {
    val weakHaptic = LocalWeakHaptic.current
    val options = TypeOfConcreteApplication.entries
    val selected by viewModel.selectedTypeOfApplication.collectAsState()

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
                    checked = selected == option,
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
