package `in`.hridayan.concretecalc.concrete.mix_design.data.model

import `in`.hridayan.concretecalc.concrete.data.model.CementGrades

data class MixDesignResult(
    val projectName: String = "",
    val volumeOfConcrete: Double = 0.00,
    val concreteGrade: String = "",
    val cementGrade: CementGrades = CementGrades.OPC_33,
    val maxCementContent: Double = 0.00,
    val minCementContent: Double = 0.00,
    val cementContentWithoutAdmixture: Double = 0.00,
    val cementContentWithAdmixture: Double = 0.00,
    val maxAggregateSize: Int = 0,
    val finalCementInKg: Double = 0.00,
    val finalCementVolume: Double = 0.00,
    val finalWaterInKg: Double = 0.00,
    val finalWaterVolume: Double = 0.00,
    val finalCoarseAggregateInKg: Double = 0.00,
    val finalCoarseAggregateVolume: Double = 0.00,
    val finalFineAggregateInKg: Double = 0.00,
    val finalFineAggregateVolume: Double = 0.00,
    val finalAdmixtureContent: Double = 0.00,
    val mixProportion: String = ""
)