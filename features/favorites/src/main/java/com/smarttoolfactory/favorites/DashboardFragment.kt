package com.smarttoolfactory.favorites

import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.favorites.databinding.FragmentDashboardBinding
import javax.inject.Inject

class DashboardFragment : DynamicNavigationFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var dashboardViewModel: DashboardViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard
}
