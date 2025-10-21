package `in`.hridayan.concretecalc.concrete.domain.repository

import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition

interface ConcreteRepository {
    suspend fun getAllConcreteGrades() : List<ConcreteGrade>
    fun getGradeByName(gradeName : String) : ConcreteGrade?
    suspend fun getExposureConditions() : List<ExposureCondition>
}