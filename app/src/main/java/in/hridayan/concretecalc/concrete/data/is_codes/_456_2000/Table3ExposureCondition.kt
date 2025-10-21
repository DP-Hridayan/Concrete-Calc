package `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000

import `in`.hridayan.concretecalc.concrete.data.model.ExposureEnvironment

data class ExposureCondition(
    val environment: ExposureEnvironment,
    val exposureCondition: String

)

object Table3ExposureCondition {
    val entries = listOf<ExposureCondition>(
        ExposureCondition(
            ExposureEnvironment.MILD,
            "Concrete surfaces protected against weather or aggressive conditions, except those situated in coastal area"
        ),
        ExposureCondition(
            ExposureEnvironment.MODERATE,
            "Concrete surfaces sheltered from severe rain or freezing whilst wet\nConcrete exposed to condensation and rain\nConcrete continuously under water\nConcrete in contact or buried under non aggressive soil/ground water\nConcrete surfaces sheltered from saturated salt air in coastal area"
        ),
        ExposureCondition(
            ExposureEnvironment.SEVERE,
            "Concrete surfaces exposed to severe rain, alternate wetting and drying or occasional freezing whilst wet or severe condensation\nConcrete completely immersed in sea water\nConcrete exposed to coastal environment"
        ),
        ExposureCondition(
            ExposureEnvironment.VERY_SEVERE,
            "Concrete surfaces exposed to sea water spray, corrosive fumes or severe freezing conditions whilst wet\nConcrete in contact with or buried under aggressive sub-soil/ground water"
        ),
        ExposureCondition(
            ExposureEnvironment.EXTREME,
            "Surface of members in tidal zone\nMembers in direct contact with liquid/solid aggressive chemicals"
        )
    )

    fun get(exposureEnvironment: ExposureEnvironment): ExposureCondition? =
        entries.find { it.environment == exposureEnvironment }
}