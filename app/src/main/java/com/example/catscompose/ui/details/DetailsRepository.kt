package com.example.catscompose.ui.details

import androidx.annotation.WorkerThread
import com.example.catscompose.network.CatService
import com.example.catscompose.persistence.BreedDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val breedDao: BreedDao
) {

    init {
        Timber.d(" Injecting detail repository")
    }

    @WorkerThread
    fun loadBreedDetails(
        breedId: String,
        onStart: () -> Unit,
        onCompletion: () -> Unit
    ) = flow {
        val breedDetails = breedDao.getBreedById(breedId)
        if (breedDetails == null) {
          return@flow
        } else {
            emit(breedDetails)
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
}