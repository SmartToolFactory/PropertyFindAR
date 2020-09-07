package com.smarttoolfactory.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseFlow
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseRxJava3
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class PropertyListFlowViewModelFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getPropertiesUseCase: GetPropertiesUseCaseFlow
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass != PropertyListViewModelFlow::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return PropertyListViewModelFlow(
            coroutineScope,
            getPropertiesUseCase
        ) as T
    }
}

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
