package com.example.catscompose.ui.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.catscompose.model.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    detailsRepository: DetailsRepository
): ViewModel() {

    init {
        Timber.d("Injecting details view model")
    }

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val breedIdSharedFlow: MutableSharedFlow<String> = MutableSharedFlow(replay = 1)

    val breedDetailFlow: Flow<Breed> = breedIdSharedFlow.flatMapLatest { breedId ->
        detailsRepository.loadBreedDetails(
            breedId,
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false }
        )
    }

    fun loadBreedById(id: String) = breedIdSharedFlow.tryEmit(id)
}