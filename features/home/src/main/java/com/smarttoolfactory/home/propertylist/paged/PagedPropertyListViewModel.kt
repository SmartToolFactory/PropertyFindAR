package com.smarttoolfactory.home.propertylist.paged

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.convertToFlowViewState
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.domain.ORDER_BY_NONE
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCasePaged
import com.smarttoolfactory.home.propertylist.AbstractPropertyListVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class PagedPropertyListViewModel @ViewModelInject constructor(
    private val coroutineScope: CoroutineScope,
    private val getPropertiesUseCase: GetPropertiesUseCasePaged
) : AbstractPropertyListVM() {

    private val _goToDetailScreen = MutableLiveData<Event<PropertyItem>>()

    override val goToDetailScreen: LiveData<Event<PropertyItem>>
        get() = _goToDetailScreen

    private val _propertyViewState = MutableLiveData<ViewState<List<PropertyItem>>>()

    override val propertyListViewState: LiveData<ViewState<List<PropertyItem>>>
        get() = _propertyViewState

    private var _orderByKey = ORDER_BY_NONE

    var orderKey = MutableLiveData<String>().apply { value = _orderByKey }

    private fun getOrderByKey(): Flow<String?> {
        return getPropertiesUseCase.getCurrentSortKey()
            .onEach {
                println("🍏 AbstractPropertyListVM init orderKey: $it")
                _orderByKey = it ?: _orderByKey
                orderKey.postValue(_orderByKey)
            }
            .catch {
                orderKey.postValue(_orderByKey)
                println("❌ AbstractPropertyListVM init error: $it")
            }
    }

    override fun getPropertyList() {

        getOrderByKey()
            .flatMapConcat {
                println("🔥 refreshPropertyList: $it")
                getPropertiesUseCase.getPagedOfflineLast(_orderByKey)
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

    override fun refreshPropertyList(orderBy: String?) {

        getOrderByKey()
            .flatMapConcat {
                println("🔥 refreshPropertyList: $it")
                getPropertiesUseCase.refreshData(orderBy ?: _orderByKey)
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

    override fun onClick(item: PropertyItem) {
        _goToDetailScreen.value = Event(item)
    }

    fun onLikeButtonClick(item: PropertyItem) {
        println("🔥 Like: $item")
    }
}
