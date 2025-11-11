package `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000

import `in`.hridayan.concretecalc.concrete.data.model.ConcreteType
import `in`.hridayan.concretecalc.concrete.data.model.ExposureEnvironment

data class ExposureRequirements(
    val exposureEnvironment: ExposureEnvironment,
    val concreteType: ConcreteType,
    val minCementContent: Int,
    val maxWCRatio: Double,
    val minConcreteGrade: String
)

object Table5ExposureRequirements {

    val entries = listOf<ExposureRequirements>(
        ExposureRequirements(
            ExposureEnvironment.MILD,
            ConcreteType.PLAIN_CONCRETE,
            220,
            0.60,
            "unspecified"
        ),
        ExposureRequirements(
            ExposureEnvironment.MILD,
            ConcreteType.REINFORCED_CONCRETE,
            300,
            0.55,
            "M20"
        ),
        ExposureRequirements(
            ExposureEnvironment.MODERATE,
            ConcreteType.PLAIN_CONCRETE,
            240,
            0.60,
            "M15"
        ),
        ExposureRequirements(
            ExposureEnvironment.MODERATE,
            ConcreteType.REINFORCED_CONCRETE,
            300,
            0.50,
            "M25"
        ),
        ExposureRequirements(
            ExposureEnvironment.SEVERE,
            ConcreteType.PLAIN_CONCRETE,
            250,
            0.50,
            "M20"
        ),
        ExposureRequirements(
            ExposureEnvironment.SEVERE,
            ConcreteType.REINFORCED_CONCRETE,
            320,
            0.45,
            "M30"
        ),
        ExposureRequirements(
            ExposureEnvironment.VERY_SEVERE,
            ConcreteType.PLAIN_CONCRETE,
            260,
            0.45,
            "M20"
        ),
        ExposureRequirements(
            ExposureEnvironment.VERY_SEVERE,
            ConcreteType.REINFORCED_CONCRETE,
            340,
            0.45,
            "M35"
        ),
        ExposureRequirements(
            ExposureEnvironment.EXTREME,
            ConcreteType.PLAIN_CONCRETE,
            280,
            0.40,
            "M25"
        ),
        ExposureRequirements(
            ExposureEnvironment.EXTREME,
            ConcreteType.REINFORCED_CONCRETE,
            360,
            0.40,
            "M40"
        ),
    )

    fun get(
        exposureEnvironment: ExposureEnvironment,
        concreteType: ConcreteType
    ): ExposureRequirements? =
        entries.find { it.exposureEnvironment == exposureEnvironment && it.concreteType == concreteType }
}