package `in`.hridayan.concretecalc.concrete.mix_design.data.repository

import `in`.hridayan.concretecalc.concrete.mix_design.data.database.MixDesignDao
import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResultEntity
import `in`.hridayan.concretecalc.concrete.mix_design.domain.repository.MixDesignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixDesignRepositoryImpl @Inject constructor(
    private val dao: MixDesignDao
) : MixDesignRepository {

    override suspend fun saveResult(result: MixDesignResultEntity) {
        dao.insertResult(result)
    }

    override suspend fun deleteResult(result: MixDesignResultEntity) {
        dao.deleteResult(result)
    }

    override suspend fun getResultById(id: Int): MixDesignResultEntity? {
        return dao.getResultById(id)
    }

    override fun getAllResults(): Flow<List<MixDesignResultEntity>> {
        return dao.getAllResults()
    }
}
