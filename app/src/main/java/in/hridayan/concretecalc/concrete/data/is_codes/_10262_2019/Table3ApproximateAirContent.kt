package `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019

data class ApproxAirContent(
    val aggregateSize: Int,
    val entrappedAirPercentage: Double
)

object Table3ApproximateAirContent {

    val entries = listOf(
        ApproxAirContent(10, 1.5),
        ApproxAirContent(20, 1.0),
        ApproxAirContent(40, 0.8)
    )

    fun get(aggregateSize: Int): ApproxAirContent? =
      entries.find { it.aggregateSize == aggregateSize }
}