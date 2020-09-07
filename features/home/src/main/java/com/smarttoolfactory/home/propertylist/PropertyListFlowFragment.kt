package com.smarttoolfactory.home.propertylist

import android.os.Bundle
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.home.R
import com.smarttoolfactory.home.databinding.FragmentPropertyListBinding
import com.smarttoolfactory.home.di.DaggerHomeComponent
import com.smarttoolfactory.home.viewmodel.PropertyListViewModelFlow
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PropertyListFlowFragment : DynamicNavigationFragment<FragmentPropertyListBinding>() {

    @Inject
    lateinit var viewModel: PropertyListViewModelFlow

    override fun getLayoutRes(): Int = R.layout.fragment_property_list

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        viewModel.getPropertyList()
    }

    override fun bindViews() {
        super.bindViews()
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerHomeComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }
}
