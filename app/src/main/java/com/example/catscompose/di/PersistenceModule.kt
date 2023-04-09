package com.example.catscompose.di

import android.app.Application
import androidx.room.Room
import com.example.catscompose.persistence.BreedDao
import com.example.catscompose.persistence.CatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideCatDatabase(application: Application): CatDatabase {
        return Room
            .databaseBuilder(
                application,
                CatDatabase::class.java,
                "CatCompose.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCatDao(catDatabase: CatDatabase): BreedDao {
        return catDatabase.breedDao()
    }
}