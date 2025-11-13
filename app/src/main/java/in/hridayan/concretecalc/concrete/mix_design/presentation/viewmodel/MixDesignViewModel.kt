package `in`.hridayan.concretecalc.concrete.mix_design.presentation.viewmodel

import android.content.Context
import android.util.Log
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
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignRecentResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResult
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignSavedResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.domain.model.MixDesignResultHolder
import `in`.hridayan.concretecalc.concrete.mix_design.domain.repository.MixDesignRepository
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.states.MixDesignScreenState
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.states.SaveButtonVisibilityHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MixDesignViewModel @Inject constructor(
    private val concreteRepository: ConcreteRepository,
    private val mixDesignRepository: MixDesignRepository,
    @param:ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _grades = MutableStateFlow<List<ConcreteGrade>>(emptyList())
    val grades = _grades.asStateFlow()

    private val _exposureConditions = MutableStateFlow<List<ExposureCondition>>(emptyList())
    val exposureConditions = _exposureConditions.asStateFlow()

    private val _states = MutableStateFlow<MixDesignScreenState>(MixDesignScreenState())
    val states: StateFlow<MixDesignScreenState> = _states

    val mixResult = concreteRepository.mixDesignResult

    fun setShowSaveButton(value: Boolean) {
        SaveButtonVisibilityHolder.value = value
    }

    fun getShowSaveButton(): Boolean = SaveButtonVisibilityHolder.value

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
        viewModelScope.launch {
            mixDesignRepository.getAllSavedResults().collect { results ->
                Log.d("MixDesignViewModel", "All results from DB: $results")
            }
        }
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
        val errorMessage = appContext.getString(R.string.value_must_be_greater_than_one)
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue < 1

        _states.update {
            it.copy(
                volumeOfConcrete = it.volumeOfConcrete.copy(
                    fieldValue = value.copy(
                        selection = TextRange(value.text.length)
                    ),
                    isError = isError,
                    errorMessage = errorMessage
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
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "25", "300")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 25.00..300.00

        _states.update {
            it.copy(
                slumpValue = it.slumpValue.copy(
                    fieldValue = value.copy(selection = TextRange(value.text.length)),
                    isError = isError,
                    errorMessage = errorMessage
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
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "0.9", "1.1")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 0.90..1.10

        _states.update {
            it.copy(
                spGravityOfWater = it.spGravityOfWater.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
                )
            )
        }
    }

    fun setSpGravityOfCement(value: TextFieldValue) {
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "2.8", "3.3")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 2.80..3.30

        _states.update {
            it.copy(
                spGravityOfCement = it.spGravityOfCement.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
                )
            )
        }
    }

    fun setSpGravityOfFineAggregate(value: TextFieldValue) {
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "2", "3")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 2.00..3.00

        _states.update {
            it.copy(
                spGravityOfFineAggregate = it.spGravityOfFineAggregate.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
                )
            )
        }
    }

    fun setSpGravityOfCoarseAggregate(value: TextFieldValue) {
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "2", "3")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 2.00..3.00

        _states.update {
            it.copy(
                spGravityOfCoarseAggregate = it.spGravityOfCoarseAggregate.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
                )
            )
        }
    }

    fun setWaterReductionPercentage(value: TextFieldValue) {
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "0", "30")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 0.00..30.00

        _states.update {
            it.copy(
                waterReductionPercentage = it.waterReductionPercentage.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
                )
            )
        }
    }

    fun setSpGravityOfAdmixture(value: TextFieldValue) {
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "0.5", "2")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 0.5..2.0

        _states.update {
            it.copy(
                spGravityOfAdmixture = it.spGravityOfAdmixture.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
                )
            )
        }
    }

    fun setDosageOfAdmixture(value: TextFieldValue) {
        val errorMessage =
            appContext.getString(R.string.value_must_lie_between_a_and_b, "0.2", "2.5")
        val numericValue = value.text.toDoubleOrNull()
        val isError = if (numericValue == null) false else numericValue !in 0.2..2.5

        _states.update {
            it.copy(
                dosageOfAdmixture = it.dosageOfAdmixture.copy(
                    fieldValue = value,
                    isError = isError,
                    errorMessage = errorMessage
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

        if (this.volumeOfConcrete.isError) {
            return true
        }
        if (this.spGravityOfCement.isError) {
            return true
        }
        if (this.spGravityOfWater.isError) {
            return true
        }
        if (this.slumpValue.isError) {
            return true
        }
        if (this.spGravityOfCoarseAggregate.isError) {
            return true
        }
        if (this.spGravityOfFineAggregate.isError) {
            return true
        }
        if (isWaterReductionSwitchChecked && this.waterReductionPercentage.isError) {
            return true
        }
        if (isWaterReductionSwitchChecked && this.spGravityOfAdmixture.isError) {
            return true
        }
        if (isWaterReductionSwitchChecked && this.dosageOfAdmixture.isError) {
            return true
        }

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

    fun calculateCementPrice(grade: CementGrades, bags: Double): Double {
        val pricePerBag = when (grade) {
            CementGrades.OPC_33 -> 290
            CementGrades.OPC_43 -> 330
            CementGrades.OPC_53 -> 400
            CementGrades.PPC -> 370
            CementGrades.PSC -> 350
        }

        return pricePerBag * bags
    }

    fun calculateGravelPrice(size: Int, volume: Double): Double {
        val pricePerVolume = when (size) {
            10 -> 1450.00
            20 -> 1350.00
            40 -> 1200.00
            else -> 0.00
        }
        return pricePerVolume * volume
    }

    suspend fun saveMixDesignResult(result: MixDesignSavedResultEntity): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

                mixDesignRepository.saveResultInSave(result.copy(saveDate = currentDate))
                Log.d("MixDesign", "Saving result: $result")
            }
            true
        } catch (e: Exception) {
            Log.d("MixDesignViewModel", "saveMixDesignResult: ${e.message}")
            false
        }
    }

    suspend fun saveRecentMixDesignResult(result: MixDesignRecentResultEntity): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

                mixDesignRepository.saveResultInRecent(result.copy(saveDate = currentDate))
                Log.d("MixDesign", "Saving result: $result")
            }
            true
        } catch (e: Exception) {
            Log.d("MixDesignViewModel", "saveMixDesignResult: ${e.message}")
            false
        }
    }


    val allSavedResults: StateFlow<List<MixDesignSavedResultEntity>> = mixDesignRepository.getAllSavedResults()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allRecentResults: StateFlow<List<MixDesignRecentResultEntity>> = mixDesignRepository.getAllRecentResults()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setResultDataFromSavedData(entity: MixDesignSavedResultEntity?) {
        viewModelScope.launch {

            Log.d("MixDesignViewModel", "${allSavedResults.value}")

            val result = MixDesignResult(
                projectName = entity?.projectName ?: "",
                volumeOfConcrete = entity?.volumeOfConcrete ?: 0.00,
                concreteGrade = entity?.concreteGrade ?: "",
                cementGrade = entity?.cementGrade ?: CementGrades.OPC_43,
                maxAggregateSize = entity?.maxAggregateSize ?: 0,
                cementContentWithAdmixture = entity?.cementContentWithAdmixture ?: 0.00,
                cementContentWithoutAdmixture = entity?.cementContentWithoutAdmixture ?: 0.00,
                finalWaterInKg = entity?.finalWaterInKg ?: 0.00,
                finalWaterVolume = entity?.finalWaterVolume ?: 0.00,
                finalCementInKg = entity?.finalCementInKg ?: 0.00,
                finalCementVolume = entity?.finalCementVolume ?: 0.00,
                finalCoarseAggregateInKg = entity?.finalCoarseAggregateInKg ?: 0.00,
                finalCoarseAggregateVolume = entity?.finalCoarseAggregateVolume ?: 0.00,
                finalFineAggregateInKg = entity?.finalFineAggregateInKg ?: 0.00,
                finalFineAggregateVolume = entity?.finalFineAggregateVolume ?: 0.00,
                finalAdmixtureContent = entity?.finalAdmixtureContent ?: 0.00,
                mixProportion = entity?.mixProportion ?: ""
            )

            MixDesignResultHolder.setResult(result)
        }
    }

    fun setResultDataFromRecentData(entity: MixDesignRecentResultEntity?) {
        viewModelScope.launch {

            Log.d("MixDesignViewModel", "${allRecentResults.value}")

            val result = MixDesignResult(
                projectName = entity?.projectName ?: "",
                volumeOfConcrete = entity?.volumeOfConcrete ?: 0.00,
                concreteGrade = entity?.concreteGrade ?: "",
                cementGrade = entity?.cementGrade ?: CementGrades.OPC_43,
                maxAggregateSize = entity?.maxAggregateSize ?: 0,
                cementContentWithAdmixture = entity?.cementContentWithAdmixture ?: 0.00,
                cementContentWithoutAdmixture = entity?.cementContentWithoutAdmixture ?: 0.00,
                finalWaterInKg = entity?.finalWaterInKg ?: 0.00,
                finalWaterVolume = entity?.finalWaterVolume ?: 0.00,
                finalCementInKg = entity?.finalCementInKg ?: 0.00,
                finalCementVolume = entity?.finalCementVolume ?: 0.00,
                finalCoarseAggregateInKg = entity?.finalCoarseAggregateInKg ?: 0.00,
                finalCoarseAggregateVolume = entity?.finalCoarseAggregateVolume ?: 0.00,
                finalFineAggregateInKg = entity?.finalFineAggregateInKg ?: 0.00,
                finalFineAggregateVolume = entity?.finalFineAggregateVolume ?: 0.00,
                finalAdmixtureContent = entity?.finalAdmixtureContent ?: 0.00,
                mixProportion = entity?.mixProportion ?: ""
            )

            MixDesignResultHolder.setResult(result)
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