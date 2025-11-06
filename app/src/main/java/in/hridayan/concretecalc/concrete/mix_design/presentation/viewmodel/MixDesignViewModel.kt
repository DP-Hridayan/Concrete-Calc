package `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val concreteRepository: ConcreteRepository
) : ViewModel() {

    private val _grades = MutableStateFlow<List<ConcreteGrade>>(emptyList())
    val grades = _grades.asStateFlow()

    private val _exposureConditions = MutableStateFlow<List<ExposureCondition>>(emptyList())
    val exposureConditions = _exposureConditions.asStateFlow()

    private val _selectedTypeOfApplication = MutableStateFlow(TypeOfConcreteApplication.PUMPABLE)
    val selectedTypeOfApplication = _selectedTypeOfApplication

    private val _states = MutableStateFlow<MixDesignScreenState>(MixDesignScreenState())
    val states: StateFlow<MixDesignScreenState> = _states


    val mixResult = concreteRepository.mixDesignResult

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

    fun setGradeOfConcreteField(value: TextFieldValue) {
        _states.update {
            it.copy(
                gradeOfConcrete = it.gradeOfConcrete.copy(fieldValue = value)
            )
        }
    }

    fun setExposureEnvironmentField(environment: ExposureEnvironment) {
        _states.update {
            it.copy(
                exposureCondition = it.exposureCondition.copy(
                    fieldValue = TextFieldValue(environment.envName),
                    environment = environment
                )
            )
        }
    }

    fun setSlumpValueField(value: TextFieldValue) {
        _states.update {
            it.copy(
                slumpValue = it.slumpValue.copy(fieldValue = value.copy(selection = TextRange(value.text.length)))
            )
        }
    }

    fun setNominalMaxAggregateSizeField(value: TextFieldValue) {
        _states.update {
            it.copy(
                maxAggregateSize = it.maxAggregateSize.copy(fieldValue = value)
            )
        }
    }

    fun setZoneOfFineAggregateField(zone: ZonesOfFineAggregate) {
        _states.update {
            it.copy(
                zoneOfFineAggregate = it.zoneOfFineAggregate.copy(
                    fieldValue = TextFieldValue(zone.zone),
                    zone = zone
                )
            )
        }
    }

    fun setTypeOfConcreteField(type: ConcreteType) {
        _states.update {
            it.copy(
                typeOfConcrete = it.typeOfConcrete.copy(
                    fieldValue = TextFieldValue(type.type),
                    type = type
                )
            )
        }
    }

    fun setTypeOfConcreteApplication(type: TypeOfConcreteApplication) {
        _selectedTypeOfApplication.value = type
    }

    fun setGradeOfCementField(grade: CementGrades) = with(_states.value) {
        _states.update {
            it.copy(
                gradeOfCement = it.gradeOfCement.copy(
                    fieldValue = TextFieldValue(
                        grade.name
                    ),
                    gradeOfCement = grade
                )
            )
        }
    }

    fun setSpGravityOfWater(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfWater = it.spGravityOfWater.copy(fieldValue = value)
            )
        }
    }

    fun setSpGravityOfCement(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfCement = it.spGravityOfCement.copy(fieldValue = value)
            )
        }
    }

    fun setSpGravityOfFineAggregate(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfFineAggregate = it.spGravityOfFineAggregate.copy(fieldValue = value)
            )
        }
    }

    fun setSpGravityOfCoarseAggregate(value: TextFieldValue) {
        _states.update {
            it.copy(
                spGravityOfCoarseAggregate = it.spGravityOfCoarseAggregate.copy(fieldValue = value)
            )
        }
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