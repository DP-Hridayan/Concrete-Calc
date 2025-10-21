package `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019

import `in`.hridayan.concretecalc.concrete.data.model.ZonesOfFineAggregate

data class CoarseAggregateRatio(
    val aggregateSize: Int,
    val zone: ZonesOfFineAggregate,
    val ratio: Double
)

object Table5CoarseAggregateRatio {

    val entries = listOf<CoarseAggregateRatio>(
        CoarseAggregateRatio(10, ZonesOfFineAggregate.ZONE_I, 0.48),
        CoarseAggregateRatio(10, ZonesOfFineAggregate.ZONE_II, 0.50),
        CoarseAggregateRatio(10, ZonesOfFineAggregate.ZONE_III, 0.52),
        CoarseAggregateRatio(10, ZonesOfFineAggregate.ZONE_IV, 0.54),
        CoarseAggregateRatio(20, ZonesOfFineAggregate.ZONE_I, 0.60),
        CoarseAggregateRatio(20, ZonesOfFineAggregate.ZONE_II, 0.62),
        CoarseAggregateRatio(20, ZonesOfFineAggregate.ZONE_III, 0.64),
        CoarseAggregateRatio(20, ZonesOfFineAggregate.ZONE_IV, 0.66),
        CoarseAggregateRatio(40, ZonesOfFineAggregate.ZONE_I, 0.69),
        CoarseAggregateRatio(40, ZonesOfFineAggregate.ZONE_II, 0.71),
        CoarseAggregateRatio(40, ZonesOfFineAggregate.ZONE_III, 0.72),
        CoarseAggregateRatio(40, ZonesOfFineAggregate.ZONE_IV, 0.73),
    )

    fun get(aggregateSize: Int, zone: ZonesOfFineAggregate): CoarseAggregateRatio? =
        entries.find { it.aggregateSize == aggregateSize && it.zone == zone }
}