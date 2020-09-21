package com.smarttoolfactory.dashboard

import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.dashboard.adapter.FooterAdapter
import com.smarttoolfactory.dashboard.adapter.HorizontalPropertySectionAdapter
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class DashboardFragment : DynamicNavigationFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var viewModel: DashboardViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    private val footerAdapter = FooterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        viewModel.getFavoriteProperties()
    }

    override fun bindViews() {

        dataBinding!!.viewModel = viewModel

        val adapterHorizontalTop = HorizontalPropertySectionAdapter()

        val adapterHorizontalBottom = HorizontalPropertySectionAdapter()

        val concatAdapter =
            ConcatAdapter(adapterHorizontalTop, footerAdapter, adapterHorizontalBottom)

        dataBinding!!.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            // Set RecyclerViewAdapter

            this.adapter = concatAdapter
        }

        viewModel.propertyListViewState.observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        adapterHorizontalTop.submitList(it.data)
                        adapterHorizontalBottom.submitList(it.data)
                    }
                    else -> {
                    }
                }
            }
        )
    }

    private fun initCoreDependentInjection() {

        val coreModuleDependencies = EntryPointAccessors.fromApplication(
            requireActivity().applicationContext,
            CoreModuleDependencies::class.java
        )

        DaggerDashboardComponent.factory().create(
            dependentModule = coreModuleDependencies,
            fragment = this
        )
            .inject(this)
    }
}
