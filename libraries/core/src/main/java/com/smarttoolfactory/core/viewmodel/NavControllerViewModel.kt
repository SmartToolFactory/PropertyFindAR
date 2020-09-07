package com.smarttoolfactory.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.smarttoolfactory.core.util.Event

class NavControllerViewModel : ViewModel() {
    val currentNavController = MutableLiveData<Event<NavController?>>()
}
