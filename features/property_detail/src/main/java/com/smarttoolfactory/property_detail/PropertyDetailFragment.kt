package com.smarttoolfactory.property_detail

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.viewmodel.NavControllerViewModel
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.property_detail.databinding.FragmentPropertyDetailBinding
import com.smarttoolfactory.property_detail.di.DaggerPropertyDetailComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class PropertyDetailFragment : DynamicNavigationFragment<FragmentPropertyDetailBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_property_detail

    @Inject
    lateinit var viewModel: PropertyDetailViewModel

    /**
     * ViwModel for getting [NavController] for setting Toolbar navigation
     */
    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    override fun bindViews() {
        // Get Post from navigation component arguments
        val property = arguments?.get("property") as PropertyItem
        dataBinding!!.item = property
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerPropertyDetailComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }
}
