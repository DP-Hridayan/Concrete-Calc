package `in`.hridayan.concretecalc.concrete.data.repository

import android.annotation.SuppressLint
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table1ValueOfX
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table2AssumedStandardDeviation
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table3ApproximateAirContent
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table4WaterContent
import `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019.Table5CoarseAggregateRatio
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ConcreteGrade
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table2GradesOfConcrete
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table3ExposureCondition
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.Table5ExposureRequirements
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.getWCratioForTargetStrength
import `in`.hridayan.concretecalc.concrete.data.model.TypeOfConcreteApplication
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
        val slumpValue = input.slumpValue.fieldValue.text.toDoubleOrNull()
            ?: throw IllegalArgumentException("Slump value is missing")
        val maxAggregateSize =
            input.maxAggregateSize.fieldValue.text.removeSuffix(" mm").toIntOrNull()
                ?: throw IllegalArgumentException("Aggregate size missing")
        val zoneOfFineAggregate = input.zoneOfFineAggregate.zone
        val typeOfConcrete = input.typeOfConcrete.type
        val gradeOfCement = input.gradeOfCement.gradeOfCement
        val spGravityOfWater = input.spGravityOfWater.fieldValue.text.toDoubleOrNull()
            ?: throw IllegalArgumentException("Specific gravity of water missing")
        val spGravityOfCement = input.spGravityOfCement.fieldValue.text.toDoubleOrNull()
            ?: throw IllegalArgumentException("Specific gravity of cement missing")
        val spGravityOfFineAggregate =
            input.spGravityOfFineAggregate.fieldValue.text.toDoubleOrNull()
                ?: throw IllegalArgumentException("Specific gravity of fine aggregate missing")
        val spGravityOfCoarseAggregate =
            input.spGravityOfCoarseAggregate.fieldValue.text.toDoubleOrNull()
                ?: throw IllegalArgumentException("Specific gravity of coarse aggregate missing")
        val isWaterReductionSwitchChecked = input.isWaterReductionSwitchChecked
        val waterReductionPercentage =
            input.waterReductionPercentage.fieldValue.text.toDouble()

        val typeOfConcreteApplication = input.typeOfConcreteApplication
        val spGravityOfAdmixture = input.spGravityOfAdmixture.fieldValue.text.toDouble()
        val dosageOfAdmixture = input.dosageOfAdmixture.fieldValue.text.toDouble()

        val valueOfX = Table1ValueOfX.get(gradeOfConcrete)?.valueX ?: 0.00
        val standardDeviation = Table2AssumedStandardDeviation.get(gradeOfConcrete)?.valueSD ?: 0.00
        val compressiveStrength = gradeOfConcrete.removePrefix("M").toDouble()
        val targetStrength =
            max(compressiveStrength + 1.65 * standardDeviation, compressiveStrength + valueOfX)

        val maxWaterCementRatio =
            Table5ExposureRequirements.get(exposureEnvironment, typeOfConcrete)?.maxWCRatio ?: 0.00
        val freeWaterCementRatio =
            getWCratioForTargetStrength(gradeOfCement, targetStrength) ?: 0.00
        val maxWaterContent = Table4WaterContent.get(maxAggregateSize)?.waterContent ?: 0
        val percentChangeInWaterContent = ((slumpValue - 50) / 25).toInt() * 3
        val waterContentForGivenSlump =
            maxWaterContent.toDouble() + (percentChangeInWaterContent.toDouble() * maxWaterContent.toDouble() / 100)
        val finalWaterContent =
            if (isWaterReductionSwitchChecked) {
                waterContentForGivenSlump - (waterReductionPercentage * waterContentForGivenSlump / 100)
            } else waterContentForGivenSlump


        val minCementContent = Table5ExposureRequirements.get(
            exposureEnvironment = exposureEnvironment,
            concreteType = typeOfConcrete
        )?.minCementContent
            ?: throw IllegalArgumentException("Values mismatch")
        val maxCementContent = 450 //kg

        val cementContentWithoutAdmixture = waterContentForGivenSlump / freeWaterCementRatio
        val cementContentWithAdmixture = finalWaterContent / freeWaterCementRatio
        val finalCementContent =
            if (isWaterReductionSwitchChecked) cementContentWithoutAdmixture else cementContentWithAdmixture

        val initialCoarseAggregateProportion = Table5CoarseAggregateRatio.get(
            aggregateSize = maxAggregateSize,
            zone = zoneOfFineAggregate
        )?.ratio ?: 0.00
        val adjustedCoarseAggregateProportion = adjustCoarseAggregateProportion(
            waterCementRatio = freeWaterCementRatio,
            coarseAggregateProportion = initialCoarseAggregateProportion
        )
        val finalCoarseAggregateProportion =
            if (typeOfConcreteApplication == TypeOfConcreteApplication.PUMPABLE) {
                adjustedCoarseAggregateProportion - (0.1 * adjustedCoarseAggregateProportion)
            } else adjustedCoarseAggregateProportion

        val fineAggregateProportion = 1 - finalCoarseAggregateProportion

        val volumeOfConcrete = 1 //m³
        val volumeOfCement = finalCementContent / (spGravityOfCement * 1000)
        val volumeOfWater = finalWaterContent / (spGravityOfWater * 1000)
        val volumeOfAdmixture =
            ((dosageOfAdmixture / 100) * finalCementContent) / (spGravityOfAdmixture * 1000)

        val entrappedAirPercentage =
            Table3ApproximateAirContent.get(maxAggregateSize)?.entrappedAirPercentage ?: 0.00
        val volumeOfAir = entrappedAirPercentage / 100

        val volumeOfTotalAggregate =
            volumeOfConcrete - (volumeOfWater + volumeOfCement + volumeOfAir + volumeOfAdmixture)
        val volumeOfCoarseAggregate = volumeOfTotalAggregate * finalCoarseAggregateProportion
        val volumeOfFineAggregate = volumeOfTotalAggregate * fineAggregateProportion

        val finalCoarseAggregateContent =
            volumeOfCoarseAggregate * spGravityOfCoarseAggregate * 1000
        val finalFineAggregateContent = volumeOfFineAggregate * spGravityOfFineAggregate * 1000
        val finalAdmixtureContent = volumeOfAdmixture * spGravityOfAdmixture * 1000

        val mixProportion = getMixProportion(
            cementContent = finalCementContent,
            fineAggregateContent = finalFineAggregateContent,
            coarseAggregateContent = finalCoarseAggregateContent
        )

        MixDesignResultHolder.setResult(
            MixDesignResult(
                maxCementContent = maxCementContent.toDouble(),
                minCementContent = minCementContent.toDouble(),
                cementContentWithAdmixture = cementContentWithAdmixture,
                cementContentWithoutAdmixture = cementContentWithoutAdmixture,
                finalCementContent = finalCementContent,
                finalCoarseAggregateContent = finalCoarseAggregateContent,
                finalFineAggregateContent = finalFineAggregateContent,
                finalAdmixtureContent = finalAdmixtureContent,
                mixProportion = mixProportion,
            )
        )
    }

    private fun adjustCoarseAggregateProportion(
        waterCementRatio: Double,
        coarseAggregateProportion: Double
    ): Double {
        val difference = waterCementRatio - 0.5
        val steps = difference / 0.05

        val adjustment = -0.01 * steps

        return coarseAggregateProportion + adjustment
    }
}

@SuppressLint("DefaultLocale")
private fun getMixProportion(
    cementContent: Double,
    fineAggregateContent: Double,
    coarseAggregateContent: Double
): String {
    val cementPart = 1
    val fineAggregatePart = fineAggregateContent / cementContent
    val coarseAggregatePart = coarseAggregateContent / cementContent

    val proportion = "$cementPart : " + String.format(
        "%.2f",
        fineAggregatePart
    ) + " : " + String.format("%.2f", coarseAggregatePart)

    return proportion
}

