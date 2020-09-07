package com.smarttoolfactory.core.util

import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.NavHostFragment
import com.smarttoolfactory.core.ui.fragment.navhost.FieldProperty
import com.smarttoolfactory.core.viewmodel.NavControllerViewModel

var DynamicNavHostFragment.viewModel: NavControllerViewModel by FieldProperty {
    NavControllerViewModel()
}

var NavHostFragment.viewModel: NavControllerViewModel by FieldProperty {
    NavControllerViewModel()
}
