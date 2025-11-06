package `in`.hridayan.concretecalc.concrete.mix_design.data.model

data class MixDesignResult(
    val valueOfX: Double = 0.0,
    val standardDeviation: Double = 0.0,
    val targetStrength: Double = 0.00,
    val maxWaterCementRatio: Double = 0.00,
    val freeWaterCementRatio: Double = 0.0,
    val maxWaterContentForNominalSizeAnd50Slump: Int = 0,
    val changeInWaterContentPercentDueToSlump: Int = 0,
    val waterContentForGivenSlump: Double = 0.00,
    val waterContentAfterAdmixture: Double = 0.00,
    )