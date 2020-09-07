package com.smarttoolfactory.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.Event

class HomeToolbarVM @ViewModelInject constructor() : ViewModel() {

    val setFilter = MutableLiveData<Event<String>>()
}
