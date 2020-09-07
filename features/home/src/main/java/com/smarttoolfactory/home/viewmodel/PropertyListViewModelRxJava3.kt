package com.smarttoolfactory.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.util.convertFromSingleToObservableViewStateWithLoading
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseRxJava3
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class PropertyListViewModelRxJava3 @ViewModelInject constructor(
    private val getPropertiesUseCase: GetPropertiesUseCaseRxJava3
) : AbstractPropertyListVM() {

    private val _goToDetailScreen = MutableLiveData<Event<PropertyItem>>()

    override val goToDetailScreen: LiveData<Event<PropertyItem>>
        get() = _goToDetailScreen

    private val _propertyListViewState = MutableLiveData<ViewState<List<PropertyItem>>>()

    override val propertyListViewState: LiveData<ViewState<List<PropertyItem>>>
        get() = _propertyListViewState

    override fun getPropertyList() {
        getPropertiesUseCase.getPropertiesOfflineFirst(ORDER_BY_NONE)
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

    override fun refreshPropertyList() {
        getPropertiesUseCase.getPropertiesOfflineLast(ORDER_BY_NONE)
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
}
