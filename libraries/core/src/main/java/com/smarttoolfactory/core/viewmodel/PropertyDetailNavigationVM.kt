package com.smarttoolfactory.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.domain.model.PropertyItem

/**
 * ViewModel for navigating to detail  from different layers of the app. When the [PropertyItem]
 *
 * of this model is set from a list fragment specified can navigate to detail screen
 *
 * * Navigate from Main fragment in **App module**
 * that replaces the one that contains BottomNavigationView
 *
 * * Navigate from Home fragment in **Home module** that contains ViewPager2
 * * Navigate from individual property list fragments
 */
class PropertyDetailNavigationVM : ViewModel() {

    /**
     * LiveData to navigate Property detail from MainFragment in app module
     */
    val goToPropertyDetailFromMain = MutableLiveData<Event<PropertyItem?>>()

    /**
     * LiveData to navigate Property detail from HomeFragment in Home dynamic feature module
     */
    val goToPropertyDetailFromHome = MutableLiveData<Event<PropertyItem?>>()
}
