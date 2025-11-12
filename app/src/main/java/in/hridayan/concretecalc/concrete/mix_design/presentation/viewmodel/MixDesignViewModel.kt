package `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel

import android.content.Context
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.hridayan.concretecalc.R
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.model.CementGrades
import `in`.hridayan.concretecalc.concrete.data.model.ConcreteType
import `in`.hridayan.concretecalc.concrete.data.model.ExposureEnvironment
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcreteApplication
import `in`.hridayan.concretecalc.concrete.data.model.ZonesOfFineAggregate
import `in`.hridayan.concretecalc.concrete.domain.repository.ConcreteRepository
import `in`.hridayan.concretecalc.concrete.mix_design.domain.model.MixDesignResultHolder
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.states.MixDesignScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MixDesignViewModel @Inject constructor(
    private val concreteRepository: ConcreteRepository,
    @param:ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _grades = MutableStateFlow<List<ConcreteGrade>>(emptyList())
    val grades = _grades.asStateFlow()

    private val _exposureConditions = MutableStateFlow<List<ExposureCondition>>(emptyList())
    val exposureConditions = _exposureConditions.asStateFlow()

    private val _states = MutableStateFlow<MixDesignScreenState>(MixDesignScreenState())
    val states: StateFlow<MixDesignScreenState> = _states

    val mixResult = concreteRepository.mixDesignResult

    val materialCosts = concreteRepository.materialCosts

    fun calculate() {
        viewModelScope.launch {
            concreteRepository.calculateMixDesign(_states.value)
        }
    }

    fun clearResult() {
        MixDesignResultHolder.clear()
    }

    init {
        loadGrades()
    }

    fun setProjectNameField(value: TextFieldValue) {
        _states.update {
            it.copy(
                projectName = it.projectName.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setGradeOfConcreteField(value: TextFieldValue) {
        _states.update {
            it.copy(
                gradeOfConcrete = it.gradeOfConcrete.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setVolumeOfConcreteField(value: TextFieldValue) {
        _states.update {
            it.copy(
                volumeOfConcrete = it.volumeOfConcrete.copy(
                    fieldValue = value.copy(
                        selection = TextRange(value.text.length)
                    ),
                    isError = false
                )
            )
        }
    }

    fun setExposureEnvironmentField(environment: ExposureEnvironment) {
        _states.update {
            it.copy(
                exposureCondition = it.exposureCondition.copy(
                    fieldValue = TextFieldValue(environment.envName),
                    environment = environment,
                    isError = false
                )
            )
        }
    }

    fun setSlumpValueField(value: TextFieldValue) {
        _states.update {
            it.copy(
                slumpValue = it.slumpValue.copy(
                    fieldValue = value.copy(selection = TextRange(value.text.length)),
                    isError = false
                )
            )
        }
    }

    fun setNominalMaxAggregateSizeField(value: TextFieldValue) {
        _states.update {
            it.copy(
                maxAggregateSize = it.maxAggregateSize.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setZoneOfFineAggregateField(zone: ZonesOfFineAggregate) {
        _states.update {
            it.copy(
                zoneOfFineAggregate = it.zoneOfFineAggregate.copy(
                    fieldValue = TextFieldValue(zone.zone),
                    zone = zone,
                    isError = false
                )
            )
        }
    }

    fun setTypeOfConcreteField(type: ConcreteType) {
        _states.update {
            it.copy(
                typeOfConcrete = it.typeOfConcrete.copy(
                    fieldValue = TextFieldValue(type.type),
                    type = type,
                    isError = false
                )
            )
        }
    }

    fun setTypeOfConcreteApplication(type: TypeOfConcreteApplication) {
        _states.update { it.copy(typeOfConcreteApplication = type) }
    }

    fun setGradeOfCementField(grade: CementGrades) = with(_states.value) {
        _states.update {
            it.copy(
                gradeOfCement = it.gradeOfCement.copy(
                    fieldValue = TextFieldValue(
                        grade.gradeName
                    ),
                    gradeOfCement = grade,
                    isError = false
                )
            )
        }
    }

    fun setSpGravityOfWater(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfWater = it.spGravityOfWater.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setSpGravityOfCement(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfCement = it.spGravityOfCement.copy(
                    fieldValue = value, isError = false
                )
            )
        }
    }

    fun setSpGravityOfFineAggregate(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfFineAggregate = it.spGravityOfFineAggregate.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setSpGravityOfCoarseAggregate(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfCoarseAggregate = it.spGravityOfCoarseAggregate.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setWaterReductionPercentage(value: TextFieldValue) {
        _states.update {
            it.copy(
                waterReductionPercentage = it.waterReductionPercentage.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setSpGravityOfAdmixture(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfAdmixture = it.spGravityOfAdmixture.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun setDosageOfAdmixture(value: TextFieldValue) {
        _states.update {
            it.copy(
                dosageOfAdmixture = it.dosageOfAdmixture.copy(
                    fieldValue = value,
                    isError = false
                )
            )
        }
    }

    fun toggleWaterReductionSwitch() {
        _states.update {
            it.copy(isWaterReductionSwitchChecked = !it.isWaterReductionSwitchChecked)
        }
    }

    fun checkEmptyFields(): Boolean = with(_states.value) {
        val fieldBlankErrorMessage = appContext.getString(R.string.field_cannot_be_empty)

        if (this.gradeOfConcrete.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                gradeOfConcrete = this.gradeOfConcrete.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.volumeOfConcrete.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                volumeOfConcrete = this.volumeOfConcrete.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.typeOfConcrete.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                typeOfConcrete = this.typeOfConcrete.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.exposureCondition.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                exposureCondition = this.exposureCondition.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.gradeOfCement.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                gradeOfCement = this.gradeOfCement.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.spGravityOfCement.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                spGravityOfCement = this.spGravityOfCement.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.spGravityOfWater.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                spGravityOfWater = this.spGravityOfWater.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.maxAggregateSize.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                maxAggregateSize = this.maxAggregateSize.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.zoneOfFineAggregate.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                zoneOfFineAggregate = this.zoneOfFineAggregate.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.slumpValue.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                slumpValue = this.slumpValue.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.spGravityOfCoarseAggregate.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                spGravityOfCoarseAggregate = this.spGravityOfCoarseAggregate.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.spGravityOfFineAggregate.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                spGravityOfFineAggregate = this.spGravityOfFineAggregate.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.isWaterReductionSwitchChecked && this.waterReductionPercentage.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                waterReductionPercentage = this.waterReductionPercentage.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.isWaterReductionSwitchChecked && this.spGravityOfAdmixture.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                spGravityOfAdmixture = this.spGravityOfAdmixture.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else if (this.isWaterReductionSwitchChecked && this.dosageOfAdmixture.fieldValue.text.isEmpty()) {
            _states.value = this.copy(
                dosageOfAdmixture = this.dosageOfAdmixture.copy(
                    isError = true,
                    errorMessage = fieldBlankErrorMessage
                )
            )
            return true
        } else false
    }

    fun loadGrades() {
        viewModelScope.launch {
            val gradeList = concreteRepository.getAllConcreteGrades()
            _grades.value = gradeList

            val exposureConditionList = concreteRepository.getExposureConditions()
            _exposureConditions.value = exposureConditionList
        }
    }
}