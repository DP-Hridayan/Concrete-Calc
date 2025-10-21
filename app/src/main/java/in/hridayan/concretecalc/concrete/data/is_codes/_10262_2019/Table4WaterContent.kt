package `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019

data class WaterContent(
    val aggregateSize: Int,
    val waterContent: Int
)

object Table4WaterContent {

    val entries = listOf<WaterContent>(
        WaterContent(10, 208),
        WaterContent(20, 186),
        WaterContent(40, 165),
    )

    fun get(aggregateSize: Int): WaterContent? =
        entries.find { it.aggregateSize == aggregateSize }

}