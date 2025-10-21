package `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.model.GradeOfCement
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcreteApplication
import `in`.hridayan.concretecalc.concrete.domain.repository.ConcreteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MixDesignViewModel @Inject constructor(
    private val concreteRepository: ConcreteRepository
) : ViewModel() {

    private val _grades = MutableStateFlow<List<ConcreteGrade>>(emptyList())
    val grades = _grades.asStateFlow()
    private val _selectedGrade = MutableStateFlow("")
    val selectedGrade = _selectedGrade

    private val _exposureConditions = MutableStateFlow<List<ExposureCondition>>(emptyList())
    val exposureConditions = _exposureConditions.asStateFlow()

    private val _selectedExposureEnvironment = MutableStateFlow("")
    val selectedExposureEnvironment = _selectedExposureEnvironment

    private val _selectedSlumpValue = MutableStateFlow("")
    val selectedSlumpValue = _selectedSlumpValue

    private val _selectedNominalMaxAggregate = MutableStateFlow("")
    val selectedNominalMaxAggregate = _selectedNominalMaxAggregate

    private val _selectedZoneOfFineAggregate = MutableStateFlow("")
    val selectedZoneOfFineAggregate = _selectedZoneOfFineAggregate

    private val _selectedTypeOfApplication = MutableStateFlow(TypeOfConcreteApplication.PUMPABLE)
    val selectedTypeOfApplication = _selectedTypeOfApplication

    private val _selectedTypeOfConcrete = MutableStateFlow("")
    val selectedTypeOfConcrete = _selectedTypeOfConcrete

    private val _selectedGradeOfCement = MutableStateFlow("")
    val selectedGradeOfCement = _selectedGradeOfCement

    init {
        loadGrades()
    }

    fun setGrade(grade: String) {
        _selectedGrade.value = grade
    }

    fun setExposureEnvironment(environment: String) {
        _selectedExposureEnvironment.value = environment
    }

    fun setSlumpValue(slumpValueInMm: String) {
        _selectedSlumpValue.value = slumpValueInMm
    }

    fun setNominalMaxAggregateSize(size: String) {
        _selectedNominalMaxAggregate.value = size
    }

    fun setZoneOfFineAggregate(zone: String) {
        _selectedZoneOfFineAggregate.value = zone
    }

    fun setTypeOfConcrete(type: String) {
        _selectedTypeOfConcrete.value = type
    }
    fun setTypeOfConcreteApplication(type: TypeOfConcreteApplication) {
        _selectedTypeOfApplication.value = type
    }

    fun setGradeOfCement(grade: String) {
        _selectedGradeOfCement.value = grade
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