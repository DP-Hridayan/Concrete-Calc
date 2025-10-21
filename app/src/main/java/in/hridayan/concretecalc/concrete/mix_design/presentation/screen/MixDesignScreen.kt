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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.concrete.data.model.GradeOfCement
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcrete
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcreteApplication
import `in`.hridayan.concretecalc.concrete.data.model.ZonesOfFineAggregate
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel.MixDesignViewModel
import `in`.hridayan.concretecalc.core.common.LocalWeakHaptic
import `in`.hridayan.concretecalc.core.presentation.components.button.BackButton
import `in`.hridayan.concretecalc.core.presentation.components.card.IconWithTextCard
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText

@Composable
fun MixDesignScreen(modifier: Modifier = Modifier) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())


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
                        .padding(15.dp),
                    text = stringResource(R.string.enter_data_mix_design),
                    icon = painterResource(R.drawable.ic_info)
                )
            }

            item {
                ConcreteGradeDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                )
            }

            item {
                ExposureEnvironmentDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                )
            }

            item {
                SlumpSizeInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                )
            }

            item {
                NominalAggregateSizeDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                )
            }

            item {
                ZoneOfFineAggregateDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                )
            }

            item {
                TypeOfConcreteDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                )
            }

            item {
                GradeOfCementDropdown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
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
        }
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
    val selectedText by viewModel.selectedGrade.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
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
                        viewModel.setGrade(grade.gradeName)
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
    val selectedText by viewModel.selectedExposureEnvironment.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
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
                        viewModel.setExposureEnvironment(environment.envName)
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
    val slumpValue by viewModel.selectedSlumpValue.collectAsState()

    OutlinedTextField(
        value = slumpValue,
        onValueChange = { input ->
            if (input.all { it.isDigit() }) {
                viewModel.setSlumpValue(input)
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
    val selectedText by viewModel.selectedNominalMaxAggregate.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
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
                        viewModel.setNominalMaxAggregateSize(size)
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

    val selectedText by viewModel.selectedZoneOfFineAggregate.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
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
            zones.map { it.zone }.forEach { zone ->
                DropdownMenuItem(
                    text = { Text(zone) },
                    onClick = {
                        weakHaptic()
                        viewModel.setZoneOfFineAggregate(zone)
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
    val types = TypeOfConcrete.entries

    val selectedText by viewModel.selectedTypeOfConcrete.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
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
            types.map { it.type }.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        weakHaptic()
                        viewModel.setTypeOfConcrete(type)
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
    val grades = GradeOfCement.entries

    val selectedText by viewModel.selectedGradeOfCement.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
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
            grades.map { it.gradeName }.forEach { grade ->
                DropdownMenuItem(
                    text = { Text(grade) },
                    onClick = {
                        weakHaptic()
                        viewModel.setGradeOfCement(grade)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
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
