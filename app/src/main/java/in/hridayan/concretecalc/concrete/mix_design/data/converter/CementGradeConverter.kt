package `in`.hridayan.concretecalc.concrete.mix_design.data.converter

import androidx.room.TypeConverter
import `in`.hridayan.concretecalc.concrete.data.model.CementGrades

class CementGradeConverter {

    @TypeConverter
    fun fromCementGrades(grade: CementGrades): String {
        return grade.name
    }

    @TypeConverter
    fun toCementGrades(value: String): CementGrades {
        return CementGrades.valueOf(value)
    }
}
