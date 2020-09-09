package com.smarttoolfactory.property_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.domain.usecase.SetPropertyStatusUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class PropertyDetailViewModel @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val setPropertyStatusUseCase: SetPropertyStatusUseCase
) : ViewModel()

class PropertyDetailViewModelFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val setPropertyStatusUseCase: SetPropertyStatusUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != PropertyDetailViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return PropertyDetailViewModel(
            coroutineScope,
            setPropertyStatusUseCase
        ) as T
    }
}
