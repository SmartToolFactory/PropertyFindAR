package com.smarttoolfactory.home.propertylist.rxjava

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.convertFromSingleToObservableViewStateWithLoading
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.domain.ORDER_BY_NONE
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCaseRxJava3
import com.smarttoolfactory.domain.usecase.property.SetPropertyStatsUseCaseRxJava3
import com.smarttoolfactory.home.propertylist.AbstractPropertyListVM
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModelRxJava3 @Inject constructor(
    private val getPropertiesUseCase: GetPropertiesUseCaseRxJava3,
    private val setPropertyStatsUseCase: SetPropertyStatsUseCaseRxJava3
) : AbstractPropertyListVM() {

    private val _goToDetailScreen = MutableLiveData<Event<PropertyItem>>()

    override val goToDetailScreen: LiveData<Event<PropertyItem>>
        get() = _goToDetailScreen

    private val _propertyListViewState = MutableLiveData<ViewState<List<PropertyItem>>>()

    override val propertyListViewState: LiveData<ViewState<List<PropertyItem>>>
        get() = _propertyListViewState

    private var _orderByKey = ORDER_BY_NONE

    var orderKey = MutableLiveData<String>().apply { value = _orderByKey }

    private fun getOrderByKey(): Single<String?> {
        return getPropertiesUseCase.getCurrentSortKey()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _orderByKey = it ?: _orderByKey
                orderKey.postValue(_orderByKey)
            }
            .onErrorResumeNext {
                Single.just(_orderByKey)
            }
    }

    override fun getPropertyList() {

        getOrderByKey()
            .flatMap {
                println("👻 GET RXJAVA PROPERTY LIST")
                getPropertiesUseCase.getPropertiesOfflineFirst(_orderByKey)
            }
            .flatMap {
                setPropertyStatsUseCase.getStatusOfPropertiesForUser(properties = it)
            }
            // Since we have multiple tabs with same data with same transition id
            // map it to something unique to this tab for shared transition to work
            .map {
                it.onEach { propertyItem ->
                    propertyItem.transitionName = "TabRxJava3${propertyItem.id}"
                }
            }
            .convertFromSingleToObservableViewStateWithLoading()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _propertyListViewState.value = it
                },
                {
                    _propertyListViewState.value = ViewState(status = Status.ERROR, error = it)
                }
            )
    }

    override fun refreshPropertyList(orderBy: String?) {

        getPropertiesUseCase.getPropertiesOfflineLast(orderBy ?: _orderByKey)
            .flatMap {
                setPropertyStatsUseCase.getStatusOfPropertiesForUser(properties = it)
            }
            // Since we have multiple tabs with same data with same transition id
            // map it to something unique to this tab for shared transition to work
            .map {
                it.onEach { propertyItem ->
                    propertyItem.transitionName = "TabRxJava3${propertyItem.id}"
                }
            }
            .convertFromSingleToObservableViewStateWithLoading()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _propertyListViewState.value = it
                },
                {
                    _propertyListViewState.value = ViewState(status = Status.ERROR, error = it)
                }
            )
    }

    override fun onClick(item: PropertyItem) {
//        _goToDetailScreen.value = Event(item)

        item.viewCount++
        setPropertyStatsUseCase
            .updatePropertyStatus(property = item)
            .subscribe(
                {
                },
                {
                }
            )
    }

    fun onLikeButtonClick(item: PropertyItem) {
        println("🔥 Like: $item")
    }
}
