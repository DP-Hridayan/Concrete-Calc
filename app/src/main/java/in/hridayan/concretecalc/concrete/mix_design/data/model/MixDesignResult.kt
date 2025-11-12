package `in`.hridayan.concretecalc.concrete.mix_design.data.model

import `in`.hridayan.concretecalc.concrete.data.model.CementGrades

data class MixDesignResult(
    val volumeOfConcrete: Double = 0.00,
    val concreteGrade: String = "",
    val cementGrades: CementGrades = CementGrades.OPC_33,
    val maxCementContent: Double = 0.00,
    val minCementContent: Double = 0.00,
    val cementContentWithoutAdmixture: Double = 0.00,
    val cementContentWithAdmixture: Double = 0.00,
    val finalCementContent: Double = 0.00,
    val finalWaterContent: Double = 0.00,
    val finalCoarseAggregateContent: Double = 0.00,
    val finalFineAggregateContent: Double = 0.00,
    val finalAdmixtureContent: Double = 0.00,
    val mixProportion: String = ""
)