package `in`.hridayan.concretecalc.concrete.data.is_codes._10262_2019

data class ValueOfXEntry(
    val grade: String,
    val valueX: Double
)

object Table1ValueOfX {

    val entries = listOf(
        ValueOfXEntry("M10", 5.0),
        ValueOfXEntry("M15", 5.0),
        ValueOfXEntry("M20", 5.5),
        ValueOfXEntry("M25", 5.5),
        ValueOfXEntry("M30", 6.5),
        ValueOfXEntry("M35", 6.5),
        ValueOfXEntry("M40", 6.5),
        ValueOfXEntry("M45", 6.5),
        ValueOfXEntry("M50", 6.5),
        ValueOfXEntry("M55", 6.5),
        ValueOfXEntry("M60", 6.5),
        ValueOfXEntry("M65", 8.0),
        ValueOfXEntry("M70", 8.0),
        ValueOfXEntry("M75", 8.0),
        ValueOfXEntry("M80", 8.0)
    )

    fun get(grade: String): ValueOfXEntry? =
        entries.find { it.grade.equals(grade, ignoreCase = true) }
}