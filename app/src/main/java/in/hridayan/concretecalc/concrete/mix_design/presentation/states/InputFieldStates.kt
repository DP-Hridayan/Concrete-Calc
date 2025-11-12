package `in`.hridayan.concretecalc.concrete.mix_design.presentation.states

import androidx.compose.ui.text.input.TextFieldValue
import `in`.hridayan.concretecalc.concrete.data.model.CementGrades
import `in`.hridayan.concretecalc.concrete.data.model.ConcreteType
import `in`.hridayan.concretecalc.concrete.data.model.ExposureEnvironment
import `in`.hridayan.concretecalc.concrete.data.model.ZonesOfFineAggregate

sealed class InputFieldStates() {

    data class ProjectName(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class GradeOfConcrete(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class VolumeOfConcrete(
        val fieldValue: TextFieldValue = TextFieldValue("1"),
        val isError: Boolean = false,
        val errorMessage: String = ""
    )
    data class ExposureCondition(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = "",
        val environment: ExposureEnvironment = ExposureEnvironment.MODERATE
    ) : InputFieldStates()

    data class SlumpValue(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class MaxAggregateSize(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class ZoneOfFineAggregate(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = "",
        val zone: ZonesOfFineAggregate = ZonesOfFineAggregate.ZONE_III
    ) : InputFieldStates()

    data class TypeOfConcrete(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = "",
        val type : ConcreteType = ConcreteType.REINFORCED_CONCRETE
    ) : InputFieldStates()

    data class GradeOfCement(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = "",
        val gradeOfCement: CementGrades = CementGrades.OPC_43
    ) : InputFieldStates()

    data class SpGravityOfWater(
        val fieldValue: TextFieldValue = TextFieldValue("1"),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class SpGravityOfCement(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class SpGravityOfFineAggregate(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class SpGravityOfCoarseAggregate(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class SpGravityOfAggregate(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class WaterReductionPercentage(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class SpGravityOfAdmixture(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

    data class DosageOfAdmixture(
        val fieldValue: TextFieldValue = TextFieldValue(""),
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) : InputFieldStates()

}