package `in`.hridayan.concretecalc.concrete.mix_design.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import `in`.hridayan.concretecalc.concrete.mix_design.data.converter.CementGradeConverter
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignRecentResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignSavedResultEntity

@Database(
    entities = [
        MixDesignSavedResultEntity::class,
        MixDesignRecentResultEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(CementGradeConverter::class)
abstract class MixDesignDatabase : RoomDatabase() {
    abstract fun mixDesignDao(): MixDesignDao
}
