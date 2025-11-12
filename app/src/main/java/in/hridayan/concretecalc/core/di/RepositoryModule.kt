package `in`.hridayan.concretecalc.core.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.hridayan.concretecalc.concrete.data.repository.ConcreteRepositoryImpl
import `in`.hridayan.concretecalc.concrete.domain.repository.ConcreteRepository
import `in`.hridayan.concretecalc.concrete.mix_design.data.database.MixDesignDao
import `in`.hridayan.concretecalc.concrete.mix_design.data.repository.MixDesignRepositoryImpl
import `in`.hridayan.concretecalc.concrete.mix_design.domain.repository.MixDesignRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindConcreteRepository(
        concreteRepositoryImpl: ConcreteRepositoryImpl
    ): ConcreteRepository


    @Binds
    @Singleton
    abstract fun bindMixDesignRepository(
        impl: MixDesignRepositoryImpl
    ): MixDesignRepository
}