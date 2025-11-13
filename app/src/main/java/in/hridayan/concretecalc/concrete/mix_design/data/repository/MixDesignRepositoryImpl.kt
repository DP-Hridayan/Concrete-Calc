package `in`.hridayan.concretecalc.concrete.mix_design.data.repository

import `in`.hridayan.concretecalc.concrete.mix_design.data.database.MixDesignDao
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignRecentResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignSavedResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.domain.repository.MixDesignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixDesignRepositoryImpl @Inject constructor(
    private val dao: MixDesignDao
) : MixDesignRepository {

    override suspend fun saveResultInSave(result: MixDesignSavedResultEntity) {
        dao.insertResultInSave(result)
    }

    override suspend fun saveResultInRecent(result: MixDesignRecentResultEntity) {
        dao.insertResultInRecent(result)
    }

    override suspend fun deleteResult(result: MixDesignSavedResultEntity) {
        dao.deleteResult(result)
    }

    override suspend fun getResultById(id: Int): MixDesignSavedResultEntity? {
        return dao.getResultById(id)
    }

    override fun getAllSavedResults(): Flow<List<MixDesignSavedResultEntity>> {
        return dao.getAllSavedResults()
    }

    override fun getAllRecentResults(): Flow<List<MixDesignRecentResultEntity>> {
        return dao.getAllRecentResults()
    }
}
