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
import com.smarttoolfactory.dashboard.model.ChartItemListModel
import com.smarttoolfactory.dashboard.model.GridPropertyListModel
import com.smarttoolfactory.dashboard.model.PropertyListModel
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

    companion object {

        const val KEY_FAVORITE_SCROLL = "favorite_scroll_state"
    }

    val favoriteScrollState = savedStateHandle.getLiveData<Parcelable?>(KEY_FAVORITE_SCROLL)

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

    /*
        Favorites Section
     */
    private val _propertyFavoriteViewState =
        MutableLiveData<ViewState<List<PropertyListModel>>>()

    val propertiesFavorite: LiveData<ViewState<List<PropertyListModel>>>
        get() = _propertyFavoriteViewState

    private val _chartFavoriteViewState =
        MutableLiveData<ViewState<List<ChartItemListModel>>>()

    val chartFavoriteViewState: LiveData<ViewState<List<ChartItemListModel>>>
        get() = _chartFavoriteViewState

    /*
        Most Viewed Section
     */
    private val _propertyMostViewedViewState =
        MutableLiveData<ViewState<List<PropertyListModel>>>()

    val propertiesMostViewed: LiveData<ViewState<List<PropertyListModel>>>
        get() = _propertyMostViewedViewState

    private val _chartMostViewedViewState =
        MutableLiveData<ViewState<List<ChartItemListModel>>>()

    val chartMostViewedViewState: LiveData<ViewState<List<ChartItemListModel>>>
        get() = _chartMostViewedViewState

    /*
        Recommendations Section
     */
    private val _propertyRecommendationViewState =
        MutableLiveData<ViewState<List<GridPropertyListModel>>>()

    val propertiesRecommended: LiveData<ViewState<List<GridPropertyListModel>>>
        get() = _propertyRecommendationViewState

    fun getDashboardDataCombined() {

        combine(
            dashboardStatsUseCase.getFavoriteProperties(),
            dashboardStatsUseCase.getFavoriteChartItems(),
            dashboardStatsUseCase.getMostViewedProperties(),
            dashboardStatsUseCase.getMostViewedChartItems(),
            dashboardStatsUseCase.getPropFlow()
        ) { propFavs, chartFavs, propViews, chartViews, props ->
            val array = arrayOfNulls<Any?>(4)

            array[0] = propFavs
            array[1] = chartViews
            array[2] = propViews
            array[3] = chartViews

            array
        }
            .convertToFlowViewState()
            .onStart {
                _propertyFavoriteViewState.value = ViewState(status = Status.LOADING)
//                delay(2000)
            }
            .onEach {
            }
            .launchIn(coroutineScope)
    }

    fun getFavoriteProperties() {
        dashboardStatsUseCase.getFavoriteProperties()
            .map {
                println(
                    "DashboardViewModel getFavoriteProperties() in " +
                        "thread: ${Thread.currentThread().name}"
                )
                listOf(PropertyListModel("Favorites", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyFavoriteViewState.value = ViewState(status = Status.LOADING)
//                delay(2000)
            }
            .onEach {
                _propertyFavoriteViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getFavoriteChartItems() {
        dashboardStatsUseCase.getFavoriteChartItems()
            .map {
                println(
                    "DashboardViewModel getFavoriteChartItems() in " +
                        "thread: ${Thread.currentThread().name}"
                )
                listOf(ChartItemListModel(it, "Favorites"))
            }
            .convertToFlowViewState()
            .onStart {
                _chartFavoriteViewState.value = ViewState(status = Status.LOADING)
//                delay(3000)
            }
            .onEach {
                _chartFavoriteViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getMostViewedProperties() {
        dashboardStatsUseCase.getMostViewedProperties()
            .map {
                println(
                    "DashboardViewModel getMostViewedProperties() in thread: " +
                        "${Thread.currentThread().name}"
                )
                listOf(PropertyListModel("Viewed Most", it))
            }
            .convertToFlowViewState()
            .onStart {
                _propertyMostViewedViewState.value = ViewState(status = Status.LOADING)
//                delay(4000)
            }
            .onEach {
                _propertyMostViewedViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getMostViewedChartItems() {
        dashboardStatsUseCase.getMostViewedChartItems()
            .map {
                println(
                    "DashboardViewModel getMostViewedChartItems() in thread: " +
                        "${Thread.currentThread().name}"
                )
                listOf(ChartItemListModel(it, "Viewed Most"))
            }
            .convertToFlowViewState()
            .onStart {
                _chartMostViewedViewState.value = ViewState(status = Status.LOADING)
//                delay(5000)
            }
            .onEach {
                _chartMostViewedViewState.value = it
            }
            .launchIn(coroutineScope)
    }

    fun getRecommendedProperties() {

        dashboardStatsUseCase.getPropFlow()
            .map {
                println(
                    "🚗🔥 DashboardViewModel getRecommendedProperties() -> map(): " +
                        "${Thread.currentThread().name}"
                )
                listOf(
                    GridPropertyListModel("Recommended For You", it)
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

    fun onSeeAllClick(propertyItemListModel: PropertyListModel) {
        _goToSeeAllScreen.value = Event(propertyItemListModel)
    }
}
