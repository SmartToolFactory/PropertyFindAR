package com.smarttoolfactory.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.convertToFlowViewState
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.dashboard.model.HorizontalTitleListModel
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.property.GetDashboardStatsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class DashboardViewModel @ViewModelInject constructor(
    private val coroutineScope: CoroutineScope,
    private val dashboardStatsUseCase: GetDashboardStatsUseCase
) : ViewModel() {

    private val _propertyViewState = MutableLiveData<ViewState<List<HorizontalTitleListModel>>>()

    val propertyListViewState: LiveData<ViewState<List<HorizontalTitleListModel>>>
        get() = _propertyViewState

    fun getFavoriteProperties() {
        dashboardStatsUseCase.getFavoriteProperties()
            .map {
                listOf(HorizontalTitleListModel("Favorites", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyViewState.value = ViewState(status = Status.LOADING)
            }
            .onEach {
                _propertyViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun onClick(propertyItem: PropertyItem) {
        println()
    }
}
