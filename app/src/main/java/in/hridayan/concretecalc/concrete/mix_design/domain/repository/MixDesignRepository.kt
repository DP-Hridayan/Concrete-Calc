package `in`.hridayan.concretecalc.concrete.mix_design.domain.repository

import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResultEntity
import kotlinx.coroutines.flow.Flow

interface MixDesignRepository {
    suspend fun saveResult(result: MixDesignResultEntity)
    suspend fun deleteResult(result: MixDesignResultEntity)
    suspend fun getResultById(id: Int): MixDesignResultEntity?
    fun getAllResults(): Flow<List<MixDesignResultEntity>>
}
