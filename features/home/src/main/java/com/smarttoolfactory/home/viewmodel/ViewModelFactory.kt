package com.smarttoolfactory.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCaseFlow
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCasePaged
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCaseRxJava3
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCase
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCaseRxJava3
import com.smarttoolfactory.home.propertylist.flow.PropertyListViewModel
import com.smarttoolfactory.home.propertylist.paged.PagedPropertyListViewModel
import com.smarttoolfactory.home.propertylist.rxjava.PropertyListViewModelRxJava3
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class PropertyListFlowViewModelFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getPropertiesUseCase: GetPropertiesUseCaseFlow,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass != PropertyListViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return PropertyListViewModel(
            coroutineScope,
            getPropertiesUseCase,
            setPropertyStatsUseCase
        ) as T
    }
}

class PagedPropertyListViewModelFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getPropertiesUseCase: GetPropertiesUseCasePaged,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCase

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass != PagedPropertyListViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return PagedPropertyListViewModel(
            coroutineScope,
            getPropertiesUseCase,
            setPropertyStatsUseCase
        ) as T
    }
}

class PropertyListRxJava3ViewModelFactory @Inject constructor(
    private val getPropertiesUseCase: GetPropertiesUseCaseRxJava3,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCaseRxJava3
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass != PropertyListViewModelRxJava3::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return PropertyListViewModelRxJava3(getPropertiesUseCase, setPropertyStatsUseCase) as T
    }
}
