package com.smarttoolfactory.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.convertToFlowViewState
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.dashboard.model.ChartItemListModel
import com.smarttoolfactory.dashboard.model.PropertyItemListModel
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.property.GetDashboardStatsUseCase
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class DashboardViewModel @ViewModelInject constructor(
    private val coroutineScope: CoroutineScope,
    private val dashboardStatsUseCase: GetDashboardStatsUseCase,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCase
) : ViewModel() {

    /*
        Go to property item detail
     */
    private val _goToDetailScreen = MutableLiveData<Event<PropertyItem>>()

    val goToDetailScreen: LiveData<Event<PropertyItem>>
        get() = _goToDetailScreen

    /*
        Go to display all favorite or viewed items
     */
    private val _goToSeeAllScreen = MutableLiveData<Event<PropertyItemListModel>>()

    val goToSeeAllListScreen: LiveData<Event<PropertyItemListModel>>
        get() = _goToSeeAllScreen

    /*
        Favorites Section
     */
    private val _propertyFavoriteViewState =
        MutableLiveData<ViewState<List<PropertyItemListModel>>>()

    val propertiesFavorite: LiveData<ViewState<List<PropertyItemListModel>>>
        get() = _propertyFavoriteViewState

    private val _chartFavoriteViewState =
        MutableLiveData<ViewState<List<ChartItemListModel>>>()

    val chartFavoriteViewState: LiveData<ViewState<List<ChartItemListModel>>>
        get() = _chartFavoriteViewState

    /*
        Most Viewed Section
     */
    private val _propertyViewedViewState =
        MutableLiveData<ViewState<List<PropertyItemListModel>>>()

    val propertiesMostViewed: LiveData<ViewState<List<PropertyItemListModel>>>
        get() = _propertyViewedViewState

    private val _chartMostViewedViewState =
        MutableLiveData<ViewState<List<ChartItemListModel>>>()

    val chartMostViewedViewState: LiveData<ViewState<List<ChartItemListModel>>>
        get() = _chartMostViewedViewState

    /*
        Recommendations Section
     */
    private val _propertyRecommendationViewState =
        MutableLiveData<ViewState<List<PropertyItemListModel>>>()

    val propertiesRecommended: LiveData<ViewState<List<PropertyItemListModel>>>
        get() = _propertyRecommendationViewState

    fun getFavoriteProperties() {
        dashboardStatsUseCase.getFavoriteProperties()
            .map {
                listOf(PropertyItemListModel("Favorites", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyFavoriteViewState.value = ViewState(status = Status.LOADING)
//                delay(300)
            }
            .onEach {
                _propertyFavoriteViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getFavoriteChartItems() {
        dashboardStatsUseCase.getFavoriteChartItems()
            .map {
                listOf(ChartItemListModel(it, "Favorites"))
            }
            .convertToFlowViewState()
            .onStart {
                _chartFavoriteViewState.value = ViewState(status = Status.LOADING)
//                delay(700)
            }
            .onEach {
                _chartFavoriteViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getMostViewedProperties() {
        dashboardStatsUseCase.getMostViewedProperties()
            .map {
                listOf(PropertyItemListModel("Viewed Most", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyViewedViewState.value = ViewState(status = Status.LOADING)
//                delay(1000)
            }
            .onEach {
                _propertyViewedViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getMostViewedChartItems() {
        dashboardStatsUseCase.getMostViewedChartItems()
            .map {
                listOf(ChartItemListModel(it, "Viewed Most"))
            }
            .convertToFlowViewState()
            .onStart {
                _chartFavoriteViewState.value = ViewState(status = Status.LOADING)
            }
            .onEach {
                _chartFavoriteViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getRecommendedProperties() {

        dashboardStatsUseCase.getPropFlow()
            .map {
                listOf(
                    PropertyItemListModel("Recommended For You", it)
                        .apply { seeAll = false }
                )
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

    fun onItemClick(propertyItem: PropertyItem) {

        _goToDetailScreen.value = Event(propertyItem)
        propertyItem.viewCount++
        setPropertyStatsUseCase.updatePropertyStatus(property = propertyItem)
            .launchIn(coroutineScope)
    }

    fun onSeeAllClick(propertyItemListModel: PropertyItemListModel) {
        _goToSeeAllScreen.value = Event(propertyItemListModel)
    }
}
