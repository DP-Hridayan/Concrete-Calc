package `in`.hridayan.concretecalc.concrete.domain.repository

import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MaterialCost
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResult
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.states.MixDesignScreenState
import kotlinx.coroutines.flow.Flow

interface ConcreteRepository {
    suspend fun getAllConcreteGrades(): List<ConcreteGrade>
    fun getGradeByName(gradeName: String): ConcreteGrade?
    suspend fun getExposureConditions(): List<ExposureCondition>
    val mixDesignResult: Flow<MixDesignResult?>
    val materialCosts : Flow<MaterialCost?>
    suspend fun calculateMixDesign(input: MixDesignScreenState)
}