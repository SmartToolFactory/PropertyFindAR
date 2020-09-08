package com.smarttoolfactory.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.viewstate.ViewState
import com.smarttoolfactory.domain.model.PropertyItem

/**
 * Common class for multiple [ViewModel]s for [PropertyItem]s with Flow, RxJava3, and Pagination
 */
abstract class AbstractPropertyListVM : ViewModel() {

    companion object {
        const val PROPERTY_LIST = "PROPERTY_LIST"
        const val PROPERTY_DETAIL = "PROPERTY_DETAIL"
    }

    abstract val goToDetailScreen: LiveData<Event<PropertyItem>>

    abstract val propertyListViewState: LiveData<ViewState<List<PropertyItem>>>

    abstract fun getPropertyList()

    abstract fun refreshPropertyList()

    abstract fun onClick(propertyItem: PropertyItem)
}
