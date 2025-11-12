package `in`.hridayan.concretecalc.concrete.mix_design.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import `in`.hridayan.concretecalc.concrete.data.model.CementGrades
import `in`.hridayan.concretecalc.concrete.mix_design.data.converter.CementGradeConverter

@Entity(tableName = "mix_design_results")
@TypeConverters(CementGradeConverter::class)
data class MixDesignResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectName: String = "",
    val saveDate: String = "",
    val volumeOfConcrete: Double = 0.00,
    val concreteGrade: String = "",
    val cementGrade: CementGrades = CementGrades.OPC_33,
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
