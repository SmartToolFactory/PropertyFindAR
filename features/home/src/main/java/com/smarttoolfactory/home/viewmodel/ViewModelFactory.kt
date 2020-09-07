package com.smarttoolfactory.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseRxJava3
import javax.inject.Inject

class ViewModelFactory

class PropertyListRxJava3ViewModelFactory @Inject constructor(
    private val getPropertiesUseCase: GetPropertiesUseCaseRxJava3
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass != PropertyListViewModelRxJava3::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return PropertyListViewModelRxJava3(getPropertiesUseCase) as T
    }
}
