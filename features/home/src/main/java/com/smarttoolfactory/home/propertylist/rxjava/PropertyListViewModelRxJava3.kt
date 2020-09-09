package com.smarttoolfactory.home.propertylist.rxjava

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.convertFromSingleToObservableViewStateWithLoading
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.domain.ORDER_BY_NONE
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseRxJava3
import com.smarttoolfactory.home.propertylist.AbstractPropertyListVM
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PropertyListViewModelRxJava3 @ViewModelInject constructor(
    private val getPropertiesUseCase: GetPropertiesUseCaseRxJava3
) : AbstractPropertyListVM() {

    private val _goToDetailScreen = MutableLiveData<Event<PropertyItem>>()

    override val goToDetailScreen: LiveData<Event<PropertyItem>>
        get() = _goToDetailScreen

    private val _propertyListViewState = MutableLiveData<ViewState<List<PropertyItem>>>()

    override val propertyListViewState: LiveData<ViewState<List<PropertyItem>>>
        get() = _propertyListViewState

    private var _orderByKey = ORDER_BY_NONE

    var orderKey = MutableLiveData<String>().apply { value = _orderByKey }

    init {
        updateOrderByKey()
    }

    private fun updateOrderByKey() {
        getPropertiesUseCase.getCurrentSortKey()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _orderByKey = it

                    orderKey.value = _orderByKey
                },
                {
                    println("PropertyListViewModelRxJava3 init error: $it")
                }
            )
    }

    override fun getPropertyList() {
        getPropertiesUseCase.getPropertiesOfflineFirst(_orderByKey)
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
        _goToDetailScreen.value = Event(item)
    }

    fun onLikeButtonClick(item: PropertyItem) {
        println("🔥 Like: $item")
    }
}
