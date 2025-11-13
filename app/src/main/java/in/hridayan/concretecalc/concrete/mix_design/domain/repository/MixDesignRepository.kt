package `in`.hridayan.concretecalc.concrete.mix_design.domain.repository

import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignRecentResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignSavedResultEntity
import kotlinx.coroutines.flow.Flow

interface MixDesignRepository {
    suspend fun saveResultInSave(result: MixDesignSavedResultEntity)
    suspend fun saveResultInRecent(result: MixDesignRecentResultEntity)
    suspend fun deleteResult(result: MixDesignSavedResultEntity)
    suspend fun getResultById(id: Int): MixDesignSavedResultEntity?
    fun getAllSavedResults(): Flow<List<MixDesignSavedResultEntity>>
    fun getAllRecentResults(): Flow<List<MixDesignRecentResultEntity>>
}
