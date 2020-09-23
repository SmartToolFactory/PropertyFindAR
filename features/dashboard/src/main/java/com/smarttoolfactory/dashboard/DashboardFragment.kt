package com.smarttoolfactory.dashboard

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.util.EndlessScrollListener
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.dashboard.adapter.BarChartAdapter
import com.smarttoolfactory.dashboard.adapter.GridListWithTitleAdapter
import com.smarttoolfactory.dashboard.adapter.HorizontalListWithTitleAdapter
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardFragment :
    DynamicNavigationFragment<FragmentDashboardBinding>(),
    EndlessScrollListener.ScrollToBottomListener {

    @Inject
    lateinit var viewModel: DashboardViewModel

    private var isRecommendedAdded = false

    private lateinit var concatAdapter: ConcatAdapter

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
    }

    override fun bindViews() {

        viewModel.getFavoriteProperties()
        viewModel.getFavoriteChartItems()
        viewModel.getMostViewedProperties()

        dataBinding.viewModel = viewModel

        val scrollStateFavoritesFlow = MutableStateFlow(0)

        val adapterFavoriteProperties =
            HorizontalListWithTitleAdapter(
                onItemClick = viewModel::onItemClick,
                onSeeAllClick = viewModel::onSeeAllClick,
                coroutineScope = viewLifecycleOwner.lifecycleScope,
                scrollPositionStateFlow = scrollStateFavoritesFlow
            )

        val adapterMostViewedProperties = HorizontalListWithTitleAdapter(
            onItemClick = viewModel::onItemClick,
            onSeeAllClick = viewModel::onSeeAllClick,
            coroutineScope = viewLifecycleOwner.lifecycleScope
        )

        val chartItemClickLambda: ((Float) -> Unit) = { position ->
            scrollStateFavoritesFlow.value = position.toInt()
        }

        val adapterChart =
            BarChartAdapter(chartItemClickLambda)

        concatAdapter =
            ConcatAdapter(
                adapterFavoriteProperties,
                adapterChart,
                adapterMostViewedProperties
            )

        dataBinding.recyclerView.apply {

            // Set Layout manager
            val linearLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            this.layoutManager = linearLayoutManager

            // Set RecyclerViewAdapter

            this.adapter = concatAdapter

            val endlessScrollListener =
                EndlessScrollListener(linearLayoutManager, this@DashboardFragment)

            this.addOnScrollListener(endlessScrollListener)
        }

        subscribeFavoriteProperties(adapterFavoriteProperties)

        subscribeMostViewedProperties(adapterMostViewedProperties)

        subscribeFavoriteChartItems(adapterChart)

        subscribeGoToPropertyDetailsScreen()

        subscribeGoToSeeAllScreen()
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

    private fun subscribeRecommendedProperties(adapter: GridListWithTitleAdapter) {
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

    private fun subscribeFavoriteChartItems(adapter: BarChartAdapter) {
        viewModel.chartFavoriteViewState.observe(
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

    private fun subscribeGoToPropertyDetailsScreen() {
        viewModel.goToDetailScreen.observe(
            viewLifecycleOwner,
            {
                it.getContentIfNotHandled()?.let { propertyItem ->
                    val bundle = bundleOf("property" to propertyItem)
                    findNavController().navigate(
                        R.id.action_dashboardFragment_to_nav_graph_property_detail,
                        bundle
                    )
                }
            }
        )
    }

    private fun subscribeGoToSeeAllScreen() {

        viewModel.goToSeeAllListScreen.observe(
            viewLifecycleOwner,
            {
                it.getContentIfNotHandled()?.let { propertyItemListModel ->

                    val bundle = bundleOf(
                        "propertyItemListModel" to propertyItemListModel
                    )

                    findNavController().navigate(
                        R.id.action_dashboardFragment_to_dashboardSeeAllFragment,
                        bundle
                    )
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

    override fun onScrollToBottom() {

//        if (!isRecommendedAdded) {
//
//            isRecommendedAdded = true
//
//            // Recommended Properties
//            val adapterRecommendedProperties = GridListWithTitleAdapter(viewModel::onItemClick)
//
//            concatAdapter.addAdapter(adapterRecommendedProperties)
//
//            subscribeRecommendedProperties(adapterRecommendedProperties)
//
//        }
    }
}
