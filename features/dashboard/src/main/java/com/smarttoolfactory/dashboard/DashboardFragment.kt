package com.smarttoolfactory.dashboard

import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.dashboard.adapter.ChartContainerAdapter
import com.smarttoolfactory.dashboard.adapter.HorizontalListWithTitleAdapter
import com.smarttoolfactory.dashboard.adapter.VerticalListWithTitleAdapter
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import com.smarttoolfactory.dashboard.model.ChartItemModel
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class DashboardFragment : DynamicNavigationFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var viewModel: DashboardViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        viewModel.getFavoriteProperties()
        viewModel.getMostViewedProperties()
        viewModel.getRecommendedItems()
    }

    override fun bindViews() {

        dataBinding!!.viewModel = viewModel

        val adapterFavoriteProperties = HorizontalListWithTitleAdapter()
        val adapterMostViewedProperties = HorizontalListWithTitleAdapter()
        val adapterRecommendedProperties = VerticalListWithTitleAdapter()

        val adapterChart = ChartContainerAdapter().apply {

            val chartItemModel =
                listOf(
                    ChartItemModel(
                        listOf(
                            1_100_000,
                            650_000,
                            700_000,
                            3_100_000,
                            1_250_000,
                            1_300_000,
                            2_200_000,
                            5_000_000,
                            2_100_000,
                            1_750_000
                        )
                    )
                )
            submitList(chartItemModel)
        }

        val concatAdapter =
            ConcatAdapter(
                adapterFavoriteProperties,
                adapterChart,
                adapterMostViewedProperties,
                adapterRecommendedProperties
            )

        dataBinding!!.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            // Set RecyclerViewAdapter

            this.adapter = concatAdapter
        }

        subscribeFavoriteProperties(adapterFavoriteProperties)

        subscribeMostViewedProperties(adapterMostViewedProperties)

        subscribeRecommendedProperties(adapterRecommendedProperties)
    }

    private fun subscribeFavoriteProperties(
        adapter: HorizontalListWithTitleAdapter
    ) {
        viewModel.propertiesFavorite.observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (!it.data.isNullOrEmpty()) {
                            adapter.submitList(it.data)
                        }
                    }
                    else -> {
                    }
                }
            }
        )
    }

    private fun subscribeMostViewedProperties(adapter: HorizontalListWithTitleAdapter) {
        viewModel.propertiesMostViewed.observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (!it.data.isNullOrEmpty()) {
                            adapter.submitList(it.data)
                        }
                    }
                    else -> {
                    }
                }
            }
        )
    }

    private fun subscribeRecommendedProperties(adapter: VerticalListWithTitleAdapter) {
        viewModel.propertiesRecommended.observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (!it.data.isNullOrEmpty()) {
                            adapter.submitList(it.data)
                        }
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
