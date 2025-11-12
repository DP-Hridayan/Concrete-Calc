package `in`.hridayan.concretecalc.concrete.mix_design.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import `in`.hridayan.concretecalc.concrete.mix_design.data.converter.CementGradeConverter
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResultEntity

@Database(
    entities = [MixDesignResultEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CementGradeConverter::class)
abstract class MixDesignDatabase : RoomDatabase() {
    abstract fun mixDesignDao(): MixDesignDao
}
