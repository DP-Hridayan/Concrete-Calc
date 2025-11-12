package `in`.hridayan.concretecalc.concrete.data.repository

import android.annotation.SuppressLint
import android.util.Log
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
        val volumeOfConcrete = input.volumeOfConcrete.fieldValue.text.toDoubleOrNull() ?: 1.00
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
            input.waterReductionPercentage.fieldValue.text.toDoubleOrNull() ?: 0.00

        val typeOfConcreteApplication = input.typeOfConcreteApplication
        val spGravityOfAdmixture =
            input.spGravityOfAdmixture.fieldValue.text.toDoubleOrNull() ?: 0.00
        val dosageOfAdmixture = input.dosageOfAdmixture.fieldValue.text.toDoubleOrNull() ?: 0.00

        val valueOfX = Table1ValueOfX.get(gradeOfConcrete)?.valueX ?: 0.00
        val standardDeviation = Table2AssumedStandardDeviation.get(gradeOfConcrete)?.valueSD ?: 0.00
        val compressiveStrength = gradeOfConcrete.removePrefix("M").toDoubleOrNull() ?: 0.00
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
            if (isWaterReductionSwitchChecked) cementContentWithAdmixture else cementContentWithoutAdmixture

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

        val volumeOfCement = finalCementContent / (spGravityOfCement * 1000)
        val volumeOfWater = finalWaterContent / (spGravityOfWater * 1000)
        val volumeOfAdmixture =
            ((dosageOfAdmixture / 100) * finalCementContent) / (spGravityOfAdmixture * 1000)

        val entrappedAirPercentage =
            Table3ApproximateAirContent.get(maxAggregateSize)?.entrappedAirPercentage ?: 0.00
        val volumeOfAir = entrappedAirPercentage / 100

        val volumeOfTotalAggregate =
            1 - (volumeOfWater + volumeOfCement + volumeOfAir + if (isWaterReductionSwitchChecked) volumeOfAdmixture else 0.0)
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
                volumeOfConcrete = volumeOfConcrete,
                concreteGrade = gradeOfConcrete,
                cementGrade = gradeOfCement,
                maxCementContent = maxCementContent.toDouble(),
                minCementContent = minCementContent.toDouble(),
                maxAggregateSize = maxAggregateSize,
                cementContentWithAdmixture = cementContentWithAdmixture,
                cementContentWithoutAdmixture = cementContentWithoutAdmixture,
                finalWaterInKg = finalWaterContent * volumeOfConcrete,
                finalWaterVolume = volumeOfWater * volumeOfConcrete,
                finalCementInKg = finalCementContent * volumeOfConcrete,
                finalCementVolume = volumeOfCement * volumeOfConcrete,
                finalCoarseAggregateInKg = finalCoarseAggregateContent * volumeOfConcrete,
                finalCoarseAggregateVolume = volumeOfCoarseAggregate * volumeOfConcrete,
                finalFineAggregateInKg = finalFineAggregateContent * volumeOfConcrete,
                finalFineAggregateVolume = volumeOfFineAggregate * volumeOfConcrete,
                finalAdmixtureContent = finalAdmixtureContent * volumeOfConcrete,
                mixProportion = mixProportion,
            )
        )

        Log.d("MixDesignCalc", "gradeOfConcrete = $gradeOfConcrete")
        Log.d("MixDesignCalc", "exposureEnvironment = $exposureEnvironment")
        Log.d("MixDesignCalc", "slumpValue = $slumpValue")
        Log.d("MixDesignCalc", "maxAggregateSize = $maxAggregateSize")
        Log.d("MixDesignCalc", "zoneOfFineAggregate = $zoneOfFineAggregate")
        Log.d("MixDesignCalc", "typeOfConcrete = $typeOfConcrete")
        Log.d("MixDesignCalc", "gradeOfCement = $gradeOfCement")
        Log.d("MixDesignCalc", "spGravityOfWater = $spGravityOfWater")
        Log.d("MixDesignCalc", "spGravityOfCement = $spGravityOfCement")
        Log.d("MixDesignCalc", "spGravityOfFineAggregate = $spGravityOfFineAggregate")
        Log.d("MixDesignCalc", "spGravityOfCoarseAggregate = $spGravityOfCoarseAggregate")
        Log.d("MixDesignCalc", "isWaterReductionSwitchChecked = $isWaterReductionSwitchChecked")
        Log.d("MixDesignCalc", "waterReductionPercentage = $waterReductionPercentage")
        Log.d("MixDesignCalc", "typeOfConcreteApplication = $typeOfConcreteApplication")
        Log.d("MixDesignCalc", "spGravityOfAdmixture = $spGravityOfAdmixture")
        Log.d("MixDesignCalc", "dosageOfAdmixture = $dosageOfAdmixture")

        Log.d("MixDesignCalc", "valueOfX = $valueOfX")
        Log.d("MixDesignCalc", "standardDeviation = $standardDeviation")
        Log.d("MixDesignCalc", "compressiveStrength = $compressiveStrength")
        Log.d("MixDesignCalc", "targetStrength = $targetStrength")

        Log.d("MixDesignCalc", "maxWaterCementRatio = $maxWaterCementRatio")
        Log.d("MixDesignCalc", "freeWaterCementRatio = $freeWaterCementRatio")
        Log.d("MixDesignCalc", "maxWaterContent = $maxWaterContent")
        Log.d("MixDesignCalc", "percentChangeInWaterContent = $percentChangeInWaterContent")
        Log.d("MixDesignCalc", "waterContentForGivenSlump = $waterContentForGivenSlump")
        Log.d("MixDesignCalc", "finalWaterContent = $finalWaterContent")

        Log.d("MixDesignCalc", "minCementContent = $minCementContent")
        Log.d("MixDesignCalc", "maxCementContent = $maxCementContent")
        Log.d("MixDesignCalc", "cementContentWithoutAdmixture = $cementContentWithoutAdmixture")
        Log.d("MixDesignCalc", "cementContentWithAdmixture = $cementContentWithAdmixture")
        Log.d("MixDesignCalc", "finalCementContent = $finalCementContent")

        Log.d(
            "MixDesignCalc",
            "initialCoarseAggregateProportion = $initialCoarseAggregateProportion"
        )
        Log.d(
            "MixDesignCalc",
            "adjustedCoarseAggregateProportion = $adjustedCoarseAggregateProportion"
        )
        Log.d("MixDesignCalc", "finalCoarseAggregateProportion = $finalCoarseAggregateProportion")
        Log.d("MixDesignCalc", "fineAggregateProportion = $fineAggregateProportion")

        Log.d("MixDesignCalc", "volumeOfConcrete = $volumeOfConcrete")
        Log.d("MixDesignCalc", "volumeOfCement = $volumeOfCement")
        Log.d("MixDesignCalc", "volumeOfWater = $volumeOfWater")
        Log.d("MixDesignCalc", "volumeOfAdmixture = $volumeOfAdmixture")

        Log.d("MixDesignCalc", "entrappedAirPercentage = $entrappedAirPercentage")
        Log.d("MixDesignCalc", "volumeOfAir = $volumeOfAir")

        Log.d("MixDesignCalc", "volumeOfTotalAggregate = $volumeOfTotalAggregate")
        Log.d("MixDesignCalc", "volumeOfCoarseAggregate = $volumeOfCoarseAggregate")
        Log.d("MixDesignCalc", "volumeOfFineAggregate = $volumeOfFineAggregate")

        Log.d("MixDesignCalc", "finalCoarseAggregateContent = $finalCoarseAggregateContent")
        Log.d("MixDesignCalc", "finalFineAggregateContent = $finalFineAggregateContent")
        Log.d("MixDesignCalc", "finalAdmixtureContent = $finalAdmixtureContent")

        Log.d("MixDesignCalc", "mixProportion = $mixProportion")

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

