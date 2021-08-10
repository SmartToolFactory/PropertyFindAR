package com.smarttoolfactory.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.domain.ORDER_BY_BEDS_ASCENDING
import com.smarttoolfactory.domain.ORDER_BY_DES_DESCENDING
import com.smarttoolfactory.domain.ORDER_BY_NONE
import com.smarttoolfactory.domain.ORDER_BY_PRICE_ASCENDING
import com.smarttoolfactory.domain.ORDER_BY_PRICE_DESCENDING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeToolbarVM @Inject constructor() : ViewModel() {

    var currentSortFilter = ORDER_BY_NONE

    val sortPropertyList = listOf(
        ORDER_BY_NONE,
        ORDER_BY_PRICE_ASCENDING,
        ORDER_BY_PRICE_DESCENDING,
        ORDER_BY_BEDS_ASCENDING,
        ORDER_BY_DES_DESCENDING
    )

    val sortFilterNames = listOf(
        "Featured",
        "Price Ascending",
        "Price Descending",
        "Beds Ascending",
        "Beds Descending"
    )

    val queryBySort = MutableLiveData<Event<String>>()
}
