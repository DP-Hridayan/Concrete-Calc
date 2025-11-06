package `in`.hridayan.concretecalc.concrete.data.repository

import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table1ValueOfX
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table2AssumedStandardDeviation
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table4WaterContent
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table2GradesOfConcrete
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table3ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table5ExposureRequirements
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.getWCratioForTargetStrength
import `in`.hridayan.concretecalc.concrete.domain.repository.ConcreteRepository
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResult
import `in`.hridayan.concretecalc.concrete.mix_design.domain.model.MixDesignResultHolder
import `in`.hridayan.concretecalc.concrete.mix_design.presentation.states.MixDesignScreenState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.math.max

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

    override val mixDesignResult: Flow<MixDesignResult?>
        get() = MixDesignResultHolder.result

    override suspend fun calculateMixDesign(input: MixDesignScreenState) {
        val gradeOfConcrete = input.gradeOfConcrete.fieldValue.text
        val exposureEnvironment = input.exposureCondition.environment
        val slumpValue = input.slumpValue.fieldValue.text.toDoubleOrNull() ?: throw IllegalArgumentException("Slump value is missing")
        val maxAggregateSize = input.maxAggregateSize.fieldValue.text.removeSuffix(" mm").toIntOrNull() ?: throw IllegalArgumentException("Aggregate size missing")
        val zoneOfFineAggregate = input.zoneOfFineAggregate.zone
        val typeOfConcrete = input.typeOfConcrete.type
        val gradeOfCement = input.gradeOfCement.gradeOfCement
        val spGravityOfWater = input.spGravityOfWater.fieldValue.text.toDoubleOrNull() ?: throw IllegalArgumentException("Specific gravity of water missing")
        val spGravityOfCement = input.spGravityOfCement.fieldValue.text.toDoubleOrNull() ?: throw IllegalArgumentException("Specific gravity of cement missing")
        val spGravityOfFineAggregate = input.spGravityOfFineAggregate.fieldValue.text.toDoubleOrNull() ?: throw IllegalArgumentException("Specific gravity of fine aggregate missing")
        val spGravityOfCoarseAggregate = input.spGravityOfCoarseAggregate.fieldValue.text.toDoubleOrNull() ?: throw IllegalArgumentException("Specific gravity of coarse aggregate missing")

        val valueOfX = Table1ValueOfX.get(gradeOfConcrete)?.valueX ?: 0.00
        val standardDeviation = Table2AssumedStandardDeviation.get(gradeOfConcrete)?.valueSD ?: 0.00
        val compressiveStrength = gradeOfConcrete.removePrefix("M").toDouble()
        val targetStrength =
            max(compressiveStrength + 1.65 * standardDeviation, compressiveStrength + valueOfX)

        val maxWaterCementRatio =
            Table5ExposureRequirements.get(exposureEnvironment)?.maxWCRatio ?: 0.00
        val freeWaterCementRatio =
            getWCratioForTargetStrength(gradeOfCement, targetStrength) ?: 0.00
        val maxWaterContent = Table4WaterContent.get(maxAggregateSize)?.waterContent ?: 0
        val percentChangeInWaterContent = ((slumpValue - 50) / 25).toInt() * 3
        val waterContentForGivenSlump =
            maxWaterContent.toDouble() + (percentChangeInWaterContent.toDouble() * maxWaterContent.toDouble() / 100)


        MixDesignResultHolder.setResult(
            MixDesignResult(
                valueOfX = valueOfX,
                standardDeviation = standardDeviation,
                targetStrength = targetStrength,
                maxWaterCementRatio = maxWaterCementRatio,
                freeWaterCementRatio = freeWaterCementRatio,
                maxWaterContentForNominalSizeAnd50Slump = maxWaterContent,
                changeInWaterContentPercentDueToSlump = percentChangeInWaterContent,
                waterContentForGivenSlump = waterContentForGivenSlump
            )
        )
    }
}