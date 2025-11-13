package `in`.hridayan.concretecalc.concrete.mix_design.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignRecentResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignSavedResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MixDesignDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultInSave(result: MixDesignSavedResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultInRecent(result: MixDesignRecentResultEntity)

    @Delete
    suspend fun deleteResult(result: MixDesignSavedResultEntity)

    @Query("SELECT * FROM mix_design_saved_results ORDER BY id DESC")
    fun getAllSavedResults(): Flow<List<MixDesignSavedResultEntity>>

    @Query("SELECT * FROM mix_design_recent_results ORDER BY id DESC")
    fun getAllRecentResults(): Flow<List<MixDesignRecentResultEntity>>

    @Query("SELECT * FROM mix_design_saved_results WHERE id = :id LIMIT 1")
    suspend fun getResultById(id: Int): MixDesignSavedResultEntity?
}
