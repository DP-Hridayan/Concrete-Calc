package `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019

data class AssumedStandardDeviationEntry(
    val grade: String,
    val valueSD: Double 
)
object Table2AssumedStandardDeviation {
    val entries = listOf(
        AssumedStandardDeviationEntry("M10", 3.5),
        AssumedStandardDeviationEntry("M15", 3.5),
        AssumedStandardDeviationEntry("M20", 4.0),
        AssumedStandardDeviationEntry("M25", 4.0),
        AssumedStandardDeviationEntry("M30", 5.0),
        AssumedStandardDeviationEntry("M35", 5.0),
        AssumedStandardDeviationEntry("M40", 5.0),
        AssumedStandardDeviationEntry("M45", 5.0),
        AssumedStandardDeviationEntry("M50", 5.0),
        AssumedStandardDeviationEntry("M55", 5.0),
        AssumedStandardDeviationEntry("M60", 5.0),
        AssumedStandardDeviationEntry("M65", 6.0),
        AssumedStandardDeviationEntry("M70", 6.0),
        AssumedStandardDeviationEntry("M75", 6.0),
        AssumedStandardDeviationEntry("M80", 6.0)
    )

    fun get(grade: String): AssumedStandardDeviationEntry? =
        entries.find { it.grade.equals(grade, ignoreCase = true) }
}