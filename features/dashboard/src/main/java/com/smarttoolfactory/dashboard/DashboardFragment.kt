package com.smarttoolfactory.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttoolfactory.core.di.CoreModuleDependencies
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemBinder
import com.smarttoolfactory.core.ui.recyclerview.adapter.ItemClazz
import com.smarttoolfactory.core.ui.recyclerview.adapter.SingleViewBinderListAdapter
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.dashboard.adapter.BarChartAdapter
import com.smarttoolfactory.dashboard.adapter.HorizontalSectionAdapter
import com.smarttoolfactory.dashboard.adapter.RecommendedSectionAdapter
import com.smarttoolfactory.dashboard.adapter.viewholder.BarChartViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.HorizontalSectionViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.LoadingIndicator
import com.smarttoolfactory.dashboard.adapter.viewholder.LoadingViewBinder
import com.smarttoolfactory.dashboard.adapter.viewholder.RecommendedSectionViewBinder
import com.smarttoolfactory.dashboard.databinding.FragmentDashboardBinding
import com.smarttoolfactory.dashboard.di.DaggerDashboardComponent
import com.smarttoolfactory.dashboard.di.withFactory
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardFragment :
    DynamicNavigationFragment<FragmentDashboardBinding>() {
    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    private val viewModel: DashboardViewModel
    by viewModels { withFactory(dashboardViewModelFactory) }

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    private lateinit var concatAdapter: ConcatAdapter

    val viewBinders = HashMap<ItemClazz, ItemBinder>()

    private lateinit var adapterFavoriteProperties: SingleViewBinderListAdapter
    private lateinit var adapterFavoriteChart: BarChartAdapter

    private lateinit var adapterMostViewedProperties: HorizontalSectionAdapter
    private lateinit var adapterMostViewedChart: BarChartAdapter

    private lateinit var adapterRecommendedProperties: RecommendedSectionAdapter

    private lateinit var favoriteViewBinder: HorizontalSectionViewBinder
    private lateinit var viewedMostViewBinder: HorizontalSectionViewBinder
    private lateinit var recommendedViewBinder: RecommendedSectionViewBinder

    private var fetchRecommendedItems = true

    override fun onCreate(savedInstanceState: Bundle?) {
        initCoreDependentInjection()
        super.onCreate(savedInstanceState)
        createAdapters(savedInstanceState)
    }

    /**
     * Creates adapters for outer and  inner recyclerViews, viewBinders to create
     * each type of ViewHolder depending on layout and data type
     */
    private fun createAdapters(savedInstanceState: Bundle?) {

        favoriteViewBinder = HorizontalSectionViewBinder(
            viewModel = viewModel,
            layoutManagerState = viewModel.scrollStateFavorites.value
        )

        viewedMostViewBinder = HorizontalSectionViewBinder(
            viewModel = viewModel,
            layoutManagerState = viewModel.scrollStateMostViewed.value
        )

        recommendedViewBinder = RecommendedSectionViewBinder(
            viewModel = viewModel,
            layoutManagerState = viewModel.scrollStateRecommended.value
        )

        /*
            Favorites
         */
        val scrollStateFavoritesFlow = MutableStateFlow(0)

        adapterFavoriteProperties =
            SingleViewBinderListAdapter(favoriteViewBinder as ItemBinder)

        val favoriteChartViewBinder = BarChartViewBinder { position ->
            scrollStateFavoritesFlow.value = position.toInt()
        }

        adapterFavoriteChart = BarChartAdapter(favoriteChartViewBinder)

        /*
            Most Viewed
         */

        val scrollStateMostViewedFlow = MutableStateFlow(0)

        adapterMostViewedProperties =
            HorizontalSectionAdapter(viewedMostViewBinder)

        val chartMostViewedClick: ((Float) -> Unit) = { position ->
            scrollStateMostViewedFlow.value = position.toInt()
        }

        val mostViewedChartViewBinder = BarChartViewBinder(chartMostViewedClick)
        adapterMostViewedChart = BarChartAdapter(mostViewedChartViewBinder)

        /*
            Recommended
         */
        adapterRecommendedProperties = RecommendedSectionAdapter(recommendedViewBinder)

        /*
            setIsolateViewTypes(false) lets nested adapters to use same RecycledViewPool

            🔥 Note: causing layout manager states to be overridden,
            saving state only one ViewHolder, others are returning NULL

         */
        val concatBuildConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()

        concatAdapter =
            ConcatAdapter(
//                concatBuildConfig,
                adapterFavoriteProperties,
                adapterFavoriteChart,
                adapterMostViewedProperties,
                adapterMostViewedChart,
                adapterRecommendedProperties
            )
    }

    override fun bindViews(view: View, savedInstanceState: Bundle?) {

        createAdapters(savedInstanceState)

        viewModel.getDashboardDataCombined()
        viewModel.combinedData.observe(
            viewLifecycleOwner,
            {

                when (it.status) {
                    Status.LOADING -> {
                        dataBinding.recyclerView.visibility = View.GONE
                        dataBinding.contentLoadingProgressBar.show()
                    }
                    Status.SUCCESS -> {
                        dataBinding.recyclerView.visibility = View.VISIBLE
                        dataBinding.contentLoadingProgressBar.hide()
                    }
                    else -> {
                        dataBinding.recyclerView.visibility = View.VISIBLE
                        dataBinding.contentLoadingProgressBar.hide()
                    }
                }
            }
        )

        // Check if RecyclerView has reached the bottom
        dataBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // Check if scrolled to bottom of RecyclerView and not fetched recommended data
                if (!recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE &&
                    fetchRecommendedItems
                ) {

                    fetchRecommendedItems = false
                }
            }
        })

        dataBinding.viewModel = viewModel

        dataBinding.recyclerView.apply {

            // Set Layout manager
            val linearLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            this.layoutManager = linearLayoutManager

            // Set RecyclerViewAdapter
            if (adapter == null) {
                this.adapter = concatAdapter
            }
        }

        subscribeFavorites(adapterFavoriteProperties, adapterFavoriteChart)
        subscribeMostViewed(adapterMostViewedProperties, adapterMostViewedChart)
        subscribeRecommendedProperties(adapterRecommendedProperties)
        subscribeEvents()
    }

    private fun subscribeFavorites(
        adapter: SingleViewBinderListAdapter,
        adapterChartAdapter: BarChartAdapter
    ) {
        viewModel.propertiesFavorite.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {

                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapter.submitList(it.data)
                    }
                }
                else -> {
                }
            }
        }

        viewModel.chartFavoriteViewState.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {

                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapterChartAdapter.submitList(it.data)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun subscribeMostViewed(
        adapter: HorizontalSectionAdapter,
        adapterChartAdapter: BarChartAdapter
    ) {
        viewModel.propertiesMostViewed.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {

                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapter.submitList(it.data)
                    }
                }
                else -> {
                }
            }
        }

        viewModel.chartMostViewedViewState.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {

                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapterChartAdapter.submitList(it.data)
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroyView() {

        if (::favoriteViewBinder.isInitialized) {
            viewModel.scrollStateFavorites.value = favoriteViewBinder.layoutManagerState
        }
        if (::viewedMostViewBinder.isInitialized) {
            viewModel.scrollStateMostViewed.value = viewedMostViewBinder.layoutManagerState
        }
        if (::recommendedViewBinder.isInitialized) {
            viewModel.scrollStateRecommended.value = recommendedViewBinder.layoutManagerState
        }

        super.onDestroyView()
    }

    private fun subscribeRecommendedProperties(adapter: RecommendedSectionAdapter) {

        viewModel.propertiesRecommended.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.LOADING -> {

                    val loadingAdapter =
                        SingleViewBinderListAdapter(LoadingViewBinder() as ItemBinder)
                            .apply { submitList(listOf(LoadingIndicator)) }

                    concatAdapter.addAdapter(loadingAdapter)
                    dataBinding.recyclerView
                        .smoothScrollToPosition(concatAdapter.adapters.size - 2)
                }
                Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        val adapters = concatAdapter.adapters
                        concatAdapter.removeAdapter(adapters[adapters.size - 1])
                        concatAdapter.addAdapter(adapter)
                        adapter.submitList(it.data)
                        dataBinding.recyclerView
                            .smoothScrollToPosition(concatAdapter.adapters.size - 1)
                    }
                }
                else -> {
                    val adapters = concatAdapter.adapters
                    concatAdapter.removeAdapter(adapters[adapters.size - 1])
                }
            }
        }
    }

    private fun subscribeEvents() {

        viewModel.goToDetailScreen.observe(
            viewLifecycleOwner
        ) {
            it.getContentIfNotHandled()?.let { propertyItem ->
                val bundle = bundleOf("property" to propertyItem)
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_nav_graph_property_detail,
                    bundle
                )
            }
        }

        viewModel.goToSeeAllListScreen.observe(
            viewLifecycleOwner
        ) {
            it.getContentIfNotHandled()?.let { propertyListModel ->

                val bundle = bundleOf(
                    "propertyListModel" to propertyListModel
                )

                findNavController().navigate(
                    R.id.action_dashboardFragment_to_dashboardSeeAllFragment,
                    bundle
                )
            }
        }
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
