package `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000

import `in`.hridayan.concretecalc.concrete.data.model.GradeGroup

data class ConcreteGrade(
    val gradeName: String,
    val characteristicStrength: Int,
    val group: GradeGroup
)

object Table2GradesOfConcrete {
    val entries = listOf<ConcreteGrade>(
        ConcreteGrade("M10", 10, GradeGroup.ORDINARY_CONCRETE),
        ConcreteGrade("M15", 15, GradeGroup.ORDINARY_CONCRETE),
        ConcreteGrade("M20", 20, GradeGroup.ORDINARY_CONCRETE),

        ConcreteGrade("M25", 25, GradeGroup.STANDARD_CONCRETE),
        ConcreteGrade("M30", 30, GradeGroup.STANDARD_CONCRETE),
        ConcreteGrade("M35", 35, GradeGroup.STANDARD_CONCRETE),
        ConcreteGrade("M40", 40, GradeGroup.STANDARD_CONCRETE),

        ConcreteGrade("M45", 45, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M50", 50, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M55", 55, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M60", 60, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M65", 65, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M70", 70, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M75", 75, GradeGroup.HIGH_STRENGTH_CONCRETE),
        ConcreteGrade("M80", 80, GradeGroup.HIGH_STRENGTH_CONCRETE)
    )

    fun get(gradeName: String): ConcreteGrade? =
        entries.find { it.gradeName.equals(gradeName, ignoreCase = true) }
}