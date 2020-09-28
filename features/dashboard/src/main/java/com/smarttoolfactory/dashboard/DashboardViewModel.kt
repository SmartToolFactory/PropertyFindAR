package com.smarttoolfactory.dashboard

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.convertToFlowViewState
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.dashboard.model.ChartSectionModel
import com.smarttoolfactory.dashboard.model.PropertyListModel
import com.smarttoolfactory.dashboard.model.RecommendedSectionModel
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.property.GetDashboardStatsUseCase
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class DashboardViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val coroutineScope: CoroutineScope,
    private val dashboardStatsUseCase: GetDashboardStatsUseCase,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCase
) : ViewModel() {

    /*
        Scroll states for inner horizontal and grid layout RecyclerViews
     */
    val scrollStateFavorites =
        savedStateHandle.getLiveData<Parcelable?>(KEY_FAVORITES_LAYOUT_MANAGER_STATE)

    val scrollStateMostViewed =
        savedStateHandle.getLiveData<Parcelable?>(KEY_MOST_VIEWED_LAYOUT_MANAGER_STATE)

    val scrollStateRecommended =
        savedStateHandle.getLiveData<Parcelable?>(KEY_RECOMMENDED_LAYOUT_MANAGER_STATE)

    /*
        Go to property item detail
     */
    private val _goToDetailScreen = MutableLiveData<Event<PropertyItem>>()

    val goToDetailScreen: LiveData<Event<PropertyItem>>
        get() = _goToDetailScreen

    /*
        Go to display all favorite or viewed items
     */
    private val _goToSeeAllScreen = MutableLiveData<Event<PropertyListModel>>()

    val goToSeeAllListScreen: LiveData<Event<PropertyListModel>>
        get() = _goToSeeAllScreen

    val combinedData = MutableLiveData<ViewState<Array<Any?>>>()

    /*
        Favorites Section
     */
    private val _propertyFavoriteViewState =
        MutableLiveData<ViewState<List<PropertyListModel>>>()

    val propertiesFavorite: LiveData<ViewState<List<PropertyListModel>>>
        get() = _propertyFavoriteViewState

    private val _chartFavoriteViewState =
        MutableLiveData<ViewState<List<ChartSectionModel>>>()

    val chartFavoriteViewState: LiveData<ViewState<List<ChartSectionModel>>>
        get() = _chartFavoriteViewState

    /*
        Most Viewed Section
     */
    private val _propertyMostViewedViewState =
        MutableLiveData<ViewState<List<PropertyListModel>>>()

    val propertiesMostViewed: LiveData<ViewState<List<PropertyListModel>>>
        get() = _propertyMostViewedViewState

    private val _chartMostViewedViewState =
        MutableLiveData<ViewState<List<ChartSectionModel>>>()

    val chartMostViewedViewState: LiveData<ViewState<List<ChartSectionModel>>>
        get() = _chartMostViewedViewState

    /*
        Recommendations Section
     */
    private val _propertyRecommendationViewState =
        MutableLiveData<ViewState<List<RecommendedSectionModel>>>()

    val propertiesRecommended: LiveData<ViewState<List<RecommendedSectionModel>>>
        get() = _propertyRecommendationViewState

    fun getDashboardDataCombined() {

        combine(
            dashboardStatsUseCase.getFavoriteProperties()
                .map { listOf(PropertyListModel("Favorites", it)) }
                .convertToFlowViewState()
                .onStart { _propertyFavoriteViewState.value = ViewState(status = Status.LOADING) }
                .onEach { _propertyFavoriteViewState.value = it },

            dashboardStatsUseCase.getFavoriteChartItems()
                .map { listOf(ChartSectionModel(it, "Favorites")) }
                .convertToFlowViewState()
                .onStart { _chartFavoriteViewState.value = ViewState(status = Status.LOADING) }
                .onEach { _chartFavoriteViewState.value = it },

            dashboardStatsUseCase.getMostViewedProperties()
                .map { listOf(PropertyListModel("Viewed Most", it)) }
                .convertToFlowViewState()
                .onStart { _propertyMostViewedViewState.value = ViewState(status = Status.LOADING) }
                .onEach { _propertyMostViewedViewState.value = it },

            dashboardStatsUseCase.getMostViewedChartItems()
                .map { listOf(ChartSectionModel(it, "Viewed Most")) }
                .convertToFlowViewState()
                .onStart { _chartMostViewedViewState.value = ViewState(status = Status.LOADING) }
                .onEach { _chartMostViewedViewState.value = it }

        ) { propFavs, chartFavs, propViews, chartViews ->
            val array = arrayOfNulls<Any?>(4)

            array[0] = propFavs
            array[1] = chartFavs
            array[2] = propViews
            array[3] = chartViews

            array
        }
            .onStart { combinedData.value = ViewState(status = Status.LOADING) }
            .onEach { combinedData.value = ViewState(status = Status.SUCCESS, data = it) }
            .launchIn(coroutineScope)
    }

    fun getRecommendedProperties() {
        dashboardStatsUseCase.getPropFlow()
            .map { listOf(RecommendedSectionModel("Recommended For You", it)) }
            .convertToFlowViewState()
            .onStart { _propertyRecommendationViewState.value = ViewState(status = Status.LOADING) }
            .onEach { _propertyRecommendationViewState.value = it }
            .launchIn(coroutineScope)
    }

    fun onItemClick(propertyItem: PropertyItem) {

        _goToDetailScreen.value = Event(propertyItem)
        propertyItem.viewCount++
        setPropertyStatsUseCase.updatePropertyStatus(property = propertyItem)
            .launchIn(coroutineScope)
    }

    fun onSeeAllClick(propertyItemListModel: PropertyListModel) {
        _goToSeeAllScreen.value = Event(propertyItemListModel)
    }
}
