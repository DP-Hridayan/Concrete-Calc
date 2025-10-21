package `in`.hridayan.concretecalc.concrete.data.repository

import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table2GradesOfConcrete
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table3ExposureCondition
import `in`.hridayan.concretecalc.concrete.domain.repository.ConcreteRepository
import javax.inject.Inject

class ConcreteRepositoryImpl @Inject constructor() : ConcreteRepository {
    override suspend fun getAllConcreteGrades(): List<ConcreteGrade> {
        return Table2GradesOfConcrete.entries
    }

    override fun getGradeByName(gradeName: String): ConcreteGrade? {
        return Table2GradesOfConcrete.get(gradeName)
    }

    override suspend fun getExposureConditions(): List<ExposureCondition> {
        return Table3ExposureCondition.entries
    }
}