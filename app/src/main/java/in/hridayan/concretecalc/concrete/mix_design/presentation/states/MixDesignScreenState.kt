package `in`.hridayan.concretecalc.concrete.mix_design.presentation.states

data class MixDesignScreenState(
    val projectName: InputFieldStates.ProjectName = InputFieldStates.ProjectName(),
    val gradeOfConcrete: InputFieldStates.GradeOfConcrete = InputFieldStates.GradeOfConcrete(),
    val exposureCondition: InputFieldStates.ExposureCondition = InputFieldStates.ExposureCondition(),
    val slumpValue: InputFieldStates.SlumpValue = InputFieldStates.SlumpValue(),
    val maxAggregateSize: InputFieldStates.MaxAggregateSize = InputFieldStates.MaxAggregateSize(),
    val zoneOfFineAggregate: InputFieldStates.ZoneOfFineAggregate = InputFieldStates.ZoneOfFineAggregate(),
    val typeOfConcrete: InputFieldStates.TypeOfConcrete = InputFieldStates.TypeOfConcrete(),
    val gradeOfCement: InputFieldStates.GradeOfCement = InputFieldStates.GradeOfCement(),
    val spGravityOfWater: InputFieldStates.SpGravityOfWater = InputFieldStates.SpGravityOfWater(),
    val spGravityOfCement: InputFieldStates.SpGravityOfCement = InputFieldStates.SpGravityOfCement(),
    val spGravityOfFineAggregate: InputFieldStates.SpGravityOfFineAggregate = InputFieldStates.SpGravityOfFineAggregate(),
    val spGravityOfCoarseAggregate: InputFieldStates.SpGravityOfCoarseAggregate = InputFieldStates.SpGravityOfCoarseAggregate(),
    val spGravityOfAggregate: InputFieldStates.SpGravityOfAggregate = InputFieldStates.SpGravityOfAggregate(),
    val waterReductionPercentage: InputFieldStates.WaterReductionPercentage = InputFieldStates.WaterReductionPercentage(),
    val spGravityOfAdmixture: InputFieldStates.SpGravityOfAdmixture = InputFieldStates.SpGravityOfAdmixture(),
    val dosageOfAdmixture: InputFieldStates.DosageOfAdmixture = InputFieldStates.DosageOfAdmixture()
)
