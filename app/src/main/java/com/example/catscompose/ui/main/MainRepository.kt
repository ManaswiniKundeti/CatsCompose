package com.example.catscompose.ui.main

import androidx.annotation.WorkerThread
import com.example.catscompose.model.Breed
import com.example.catscompose.network.CatService
import com.example.catscompose.persistence.BreedDao
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val catService: CatService,
    private val breedDao: BreedDao
) {

    init {
        Timber.d("Injeccting Main Repo")
    }

    @WorkerThread
    fun loadCatsBreedData(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val breedsList: List<Breed> = breedDao.getBreedList()
        if (breedsList.isEmpty()) {
            //Network
            catService.fetchCatBreedsList()
            //if reesponse is success
                .suspendOnSuccess {
                    breedDao.insertBreedList(data)
                    emit(data)
                }
            // if fails
                .onFailure { onError(message()) }
        } else {
            emit(breedsList)
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
}