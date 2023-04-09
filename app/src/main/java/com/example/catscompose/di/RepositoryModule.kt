package com.example.catscompose.di

import com.example.catscompose.network.CatService
import com.example.catscompose.persistence.BreedDao
import com.example.catscompose.ui.main.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        catService: CatService,
        breedDao: BreedDao
    ): MainRepository {
        return MainRepository(catService, breedDao)
    }
}