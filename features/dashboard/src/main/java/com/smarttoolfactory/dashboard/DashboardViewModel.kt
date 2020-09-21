package com.smarttoolfactory.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.convertToFlowViewState
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.dashboard.model.PropertyListWithTitleModel
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

    private val _propertyFavoriteViewState =
        MutableLiveData<ViewState<List<PropertyListWithTitleModel>>>()

    private val _propertyViewedViewState =
        MutableLiveData<ViewState<List<PropertyListWithTitleModel>>>()

    private val _propertyRecommendationViewState =
        MutableLiveData<ViewState<List<PropertyListWithTitleModel>>>()

    val propertiesFavorite: LiveData<ViewState<List<PropertyListWithTitleModel>>>
        get() = _propertyFavoriteViewState

    val propertiesMostViewed: LiveData<ViewState<List<PropertyListWithTitleModel>>>
        get() = _propertyViewedViewState

    val propertiesRecommended: LiveData<ViewState<List<PropertyListWithTitleModel>>>
        get() = _propertyRecommendationViewState

    fun getFavoriteProperties() {
        dashboardStatsUseCase.getFavoriteProperties()
            .map {
                listOf(PropertyListWithTitleModel("Favorites", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyFavoriteViewState.value = ViewState(status = Status.LOADING)
            }
            .onEach {
                _propertyFavoriteViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getMostViewedProperties() {
        dashboardStatsUseCase.getMostViewedProperties()
            .map {
                listOf(PropertyListWithTitleModel("Viewed Most", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyViewedViewState.value = ViewState(status = Status.LOADING)
            }
            .onEach {
                _propertyViewedViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getRecommendedItems() {

        dashboardStatsUseCase.getPropFlow()
            .map {
                listOf(PropertyListWithTitleModel("Recommended For You", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyRecommendationViewState.value = ViewState(status = Status.LOADING)
            }
            .onEach {
                _propertyRecommendationViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun onItemClick(propertyItem: PropertyItem) = Unit

    fun onFavoriteSeeAllClicked() = Unit

    fun onMostViewedSeeAllClicked() = Unit
}
