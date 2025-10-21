package `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000

data class WorkabilityInfo(
    val placingCondition: String,
    val degreeOfWorkability: String,
    val minSlumpMm: Int? = null,
    val maxSlumpMm: Int? = null,
    val clause: Clause? = null
)

object WorkabilityOfConcrete {
    val entries = listOf(
        WorkabilityInfo(
            placingCondition = "Blinding concrete; Shallow sections; Pavements using pavers",
            degreeOfWorkability = "Very low",
            clause = Clause(
                name = "7.1.1",
                details = "In the 'very low' category of workability where strict control is necessary, for example pavement quality concrete, measurement of workability by determination of compacting factor will be more appropriate than slump (see IS 1199) and a value of compacting factor of 0.75 to 0.80 is suggested"
            )
        ),
        WorkabilityInfo(
            placingCondition = "Mass concrete; Lightly reinforced sections in slabs, beams, walls, columns; Floors; Hand placed pavements; Canal lining; Strip footings",
            degreeOfWorkability = "Low",
            minSlumpMm = 25,
            maxSlumpMm = 75
        ),
        WorkabilityInfo(
            placingCondition = "Heavily reinforced sections in slabs, beams, walls, columns; Slipform work; Pumped concrete",
            degreeOfWorkability = "Medium",
            minSlumpMm = 50,
            maxSlumpMm = 100
        ),
        WorkabilityInfo(
            placingCondition = "Trench fill; In-situ piling",
            degreeOfWorkability = "High",
            minSlumpMm = 100,
            maxSlumpMm = 150
        ),
        WorkabilityInfo(
            placingCondition = "Tremie concrete",
            degreeOfWorkability = "Very high",
            clause = Clause(
                name = "7.1.2",
                details = "In the 'very high' category of workability, measurement of workability by determination of flow will be appropriate (see IS 9103)"
            )
        )
    )
}

