package `in`.hridayan.concretecalc.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.hridayan.concretecalc.concrete.mix_design.data.database.MixDesignDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMixDesignDatabase(
        @ApplicationContext context: Context
    ): MixDesignDatabase {
        return Room.databaseBuilder(
            context,
            MixDesignDatabase::class.java,
            "mix_design_db"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideMixDesignDao(db: MixDesignDatabase) = db.mixDesignDao()
}
