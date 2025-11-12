package `in`.hridayan.concretecalc.concrete.mix_design.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MixDesignDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: MixDesignResultEntity)

    @Delete
    suspend fun deleteResult(result: MixDesignResultEntity)

    @Query("SELECT * FROM mix_design_results ORDER BY id DESC")
    fun getAllResults(): Flow<List<MixDesignResultEntity>>

    @Query("SELECT * FROM mix_design_results WHERE id = :id LIMIT 1")
    suspend fun getResultById(id: Int): MixDesignResultEntity?
}
